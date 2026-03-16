package org.uwgb.compsci330.server.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.uwgb.compsci330.common.model.response.relationship.SafeRelationship;
import org.uwgb.compsci330.common.model.response.user.SafeUser;
import org.uwgb.compsci330.server.APIServerApplication;
import org.uwgb.compsci330.server.ServerConfiguration;
import org.uwgb.compsci330.server.dto.request.RegisterUserRequest;
import org.uwgb.compsci330.server.exception.*;
import org.uwgb.compsci330.server.security.JwtUtil;
import org.uwgb.compsci330.server.service.RelationshipService;
import org.uwgb.compsci330.server.service.UserService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = APIServerApplication.class
)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties"
)
@ContextConfiguration(classes = APIServerApplication.class)
@Transactional
public class RelationshipRestControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserService userService;

    @Autowired
    private RelationshipService relationshipService;

    private String createTestUser(String username) {
        return userService.register(new RegisterUserRequest(username, "password12345"));
    }

    private SafeUser getTestUser(String token) {
        return userService.getMe(token);
    }

    @SuppressWarnings("UnusedReturnValue")
    private SafeRelationship createRelationship(String requester, String requestee) {
        return relationshipService.createRelationship(requester, requestee);
    }

    private List<SafeRelationship> getRelationships(String userID) {
        return relationshipService.getRelationships(userID);
    }

    @Test
    public void createRelationship_whenValid_thenReturns201() throws Exception {
        String requester = createTestUser("alice");
        String requestee = createTestUser("bob");

        String requesteeId = JwtUtil.getUserIdFromToken(requestee);
        String requesterId = JwtUtil.getUserIdFromToken(requester);

        mvc.perform(post(String.format("/users/@me/relationships/%s", getTestUser(requestee).getUsername()))
                        .header("Authorization", requester)
                )

                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.requestee.id").value(requesteeId))
                .andExpect(jsonPath("$.requester.id").value(requesterId))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void createRelationship_whenAlreadyPendingIncoming_thenReturns201() throws Exception {
        final String requester = createTestUser("alice");
        final String requestee = createTestUser("bob");
        final String requesteeUsername = getTestUser(requestee).getUsername();
        final String requesterUsername = getTestUser(requester).getUsername();

        String requesterId = JwtUtil.getUserIdFromToken(requester);

        createRelationship(requesterId, requesteeUsername);


        mvc.perform(post(String.format("/users/@me/relationships/%s", requesterUsername))
                        .header("Authorization", requestee)
                )

                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("ACCEPTED"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void createRelationship_whenInvalidUser_thenReturns400() throws Exception {
        final String requester = createTestUser("alice");
        final String requestee = createTestUser("bob");
        final String requesteeUsername = getTestUser(requestee).getUsername();

        mvc.perform(post(String.format("/users/@me/relationships/%s", requesteeUsername+"bleh"))
                        .header("Authorization", requester)
                )

                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(new InvalidFriendRequestException(requesteeUsername+"bleh").getMessage()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void createRelationship_whenFriendingSelf_thenReturns400() throws Exception {
        final String requester = createTestUser("alice");
        final String requesterUsername = getTestUser(requester).getUsername();

        mvc.perform(post(String.format("/users/@me/relationships/%s", requesterUsername))
                        .header("Authorization", requester)
                )

                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(new SelfFriendException().getMessage()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void createRelationship_whenSystemUser_thenReturns400() throws Exception {
        final String requester = createTestUser("alice");

        mvc.perform(post(String.format("/users/@me/relationships/%s", ServerConfiguration.SYSTEM_USER))
                        .header("Authorization", requester)
                )

                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(new ReservedUsernameException().getMessage()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void createRelationship_whenAlreadyExisting_thenReturns400() throws Exception {
        final String requester = createTestUser("alice");
        final String requestee = createTestUser("bob");
        final String requesteeUsername = getTestUser(requestee).getUsername();
        final String requesterUsername = getTestUser(requester).getUsername();

        String requesterId = JwtUtil.getUserIdFromToken(requester);
        String requesteeId = JwtUtil.getUserIdFromToken(requestee);

        createRelationship(requesterId, requesteeUsername);
        createRelationship(requesteeId, requesterUsername);

        mvc.perform(post(String.format("/users/@me/relationships/%s", requesterUsername))
                        .header("Authorization", requestee)
                )

                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(new ExistingRelationshipException(requesterUsername).getMessage()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }


    @Test
    public void createRelationship_whenTokenInvalid_thenReturns400() throws Exception {
        String requester = createTestUser("alice");
        String requestee = createTestUser("bob");

        mvc.perform(post(String.format("/users/@me/relationships/%s", getTestUser(requestee).getUsername()))
                        .header("Authorization", requester+"bleh")
                )

                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void createRelationship_whenAlreadyPendingOutgoing_thenReturns400() throws Exception {
        String requester = createTestUser("alice");
        String requestee = createTestUser("bob");
        String requesteeUsername = getTestUser(requestee).getUsername();

        String requesterId = JwtUtil.getUserIdFromToken(requester);

        createRelationship(requesterId, requesteeUsername);


        mvc.perform(post(String.format("/users/@me/relationships/%s", requesteeUsername))
                        .header("Authorization", requester)
                )

                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(new OutgoingRequestAlreadyExistsException(requesteeUsername).getMessage()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }


    // TODO: Finish /user/@me/relationships DELETE tests

    @Test
    public void deleteExistingRelationship_whenValid_thenReturns204() throws Exception {
        String requester = createTestUser("alice");
        String requestee = createTestUser("bob");

        String requesterUsername = getTestUser(requester).getUsername();
        String requesteeUsername = getTestUser(requestee).getUsername();

        String requesterId = JwtUtil.getUserIdFromToken(requester);
        String requesteeId = JwtUtil.getUserIdFromToken(requestee);

        createRelationship(requesterId, requesteeUsername);
        createRelationship(requesteeId, requesterUsername);

        mvc.perform(
                delete(String.format("/users/@me/relationships/%s", requesterUsername))
                        .header("Authorization", requestee)
        )
                .andDo(print())
                .andExpect(status().isNoContent());

        assert getRelationships(requesterId).isEmpty();
    }

    @Test
    public void deletePendingIncomingRelationship_whenValid_thenReturns204() throws Exception {
        String requester = createTestUser("alice");
        String requestee = createTestUser("bob");

        String requesterUsername = getTestUser(requester).getUsername();
        String requesteeUsername = getTestUser(requestee).getUsername();

        String requesterId = JwtUtil.getUserIdFromToken(requester);

        createRelationship(requesterId, requesteeUsername);

        mvc.perform(
                        delete(String.format("/users/@me/relationships/%s", requesterUsername))
                                .header("Authorization", requestee)
                )
                .andDo(print())
                .andExpect(status().isNoContent());

        assert getRelationships(requesterId).isEmpty();
    }

    @Test
    public void deletePendingOutgoingRelationship_whenValid_thenReturns204() throws Exception {
        String requester = createTestUser("alice");
        String requestee = createTestUser("bob");

        String requesteeUsername = getTestUser(requestee).getUsername();

        String requesterId = JwtUtil.getUserIdFromToken(requester);

        createRelationship(requesterId, requesteeUsername);

        mvc.perform(
                        delete(String.format("/users/@me/relationships/%s", requesteeUsername))
                                .header("Authorization", requester)
                )
                .andDo(print())
                .andExpect(status().isNoContent());

        assert getRelationships(requesterId).isEmpty();
    }

    @Test
    public void deleteExistingRelationship_whenInvalidRelationship_thenReturns400() throws Exception {
        String requester = createTestUser("alice");
        String requestee = createTestUser("bob");

        String requesterUsername = getTestUser(requester).getUsername();
        String requesteeUsername = getTestUser(requestee).getUsername();

        String requesterId = JwtUtil.getUserIdFromToken(requester);
        String requesteeId = JwtUtil.getUserIdFromToken(requestee);

        createRelationship(requesterId, requesteeUsername);
        createRelationship(requesteeId, requesterUsername);

        mvc.perform(
                        delete(String.format("/users/@me/relationships/%s", requesterUsername+"bleh"))
                                .header("Authorization", requestee)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());

        assert !getRelationships(requesterId).isEmpty();
    }
}
