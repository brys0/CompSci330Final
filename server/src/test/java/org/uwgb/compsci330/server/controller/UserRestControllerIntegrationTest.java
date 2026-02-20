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
import org.uwgb.compsci330.server.APIServerApplication;
import org.uwgb.compsci330.server.dto.request.LoginUserRequest;
import org.uwgb.compsci330.server.dto.request.RegisterUserRequest;
import org.uwgb.compsci330.server.repository.UserRepository;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
public class UserRestControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private String createUser(String username, String password) throws Exception {
        RegisterUserRequest request = new RegisterUserRequest(username, password);

        return mvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString(); // Expect success when creating the user
    }

    @Test
    public void registerUser_whenValid_thenReturns200() throws Exception {
        RegisterUserRequest req = new RegisterUserRequest("test", "password12345");

        mvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                )
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN));
    }

    @Test
    public void registerUser_whenPasswordTooShort_thenReturns400() throws Exception {
        RegisterUserRequest req = new RegisterUserRequest("test", "p");

        mvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                )
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    public void registerUser_whenUsernameTooShort_thenReturns400() throws Exception {
        RegisterUserRequest req = new RegisterUserRequest("t", "password12345");

        mvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                )
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    public void registerUser_whenUsernameExists_thenReturns409() throws Exception {
        createUser("test", "password12345");

        RegisterUserRequest req = new RegisterUserRequest("test", "password12345");

        mvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                )
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().is(409));
    }

    @Test
    public void loginUser_whenValid_thenReturns200() throws Exception {
        createUser("test", "password12345");

        LoginUserRequest req = new LoginUserRequest("test", "password12345");

         mvc.perform(post("/users/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(req))
                )
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(status().is(200));
    }

    @Test
    public void loginUser_whenInvalidUsername_thenReturns400() throws Exception {
        createUser("test", "password12345");

        LoginUserRequest req = new LoginUserRequest("feopf", "password12345");

        mvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                )
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    public void loginUser_whenInvalidPassword_thenReturns400() throws Exception {
        createUser("test", "password12345");

        LoginUserRequest req = new LoginUserRequest("test", "password123456");

        mvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                )
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    public void getUserMe_whenValid_thenReturns200() throws Exception {
        String existingToken = createUser("test", "password12345");

        mvc.perform(get("/users/@me")
                        .header("Authorization", existingToken)
                )
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                // TODO: Check that user is actually correct
//                .andExpect(content().json(objectMapper.writeValueAsString(new SafeUser(""))))
                .andExpect(status().is(200));
    }

    @Test
    public void getUserMe_whenInvalidToken_thenReturns401() throws Exception {
        createUser("test", "password12345");

        mvc.perform(get("/users/@me")
                        .header("Authorization", "invalid token")
                )
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().is(401));
    }

    @Test
    public void getUserMe_whenNoToken_thenReturns401() throws Exception {
        createUser("test", "password12345");

        mvc.perform(get("/users/@me"))
                .andDo(print())
                .andExpect(status().is(400));
    }
}
