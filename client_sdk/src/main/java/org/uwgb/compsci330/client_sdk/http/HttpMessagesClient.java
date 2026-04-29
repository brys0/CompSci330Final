package org.uwgb.compsci330.client_sdk.http;

import lombok.Getter;
import okhttp3.Request;
import org.jetbrains.annotations.Nullable;
import org.uwgb.compsci330.client_sdk.Client;
import org.uwgb.compsci330.client_sdk.ClientConfig;
import org.uwgb.compsci330.client_sdk.entity.Entity;
import org.uwgb.compsci330.common.model.request.message.CreateMessageRequest;
import org.uwgb.compsci330.common.model.response.message.SafeMessage;

import java.io.IOException;
import java.util.List;

public class HttpMessagesClient extends HttpClient {
    public HttpMessagesClient(Client client) {
        super(client);
    }


    public List<SafeMessage> getMessages(
            String friendId,
            int limit,
            @Nullable String afterId,
            @Nullable String beforeId
    ) throws IOException {
        StringBuilder requestString = new StringBuilder(String.format("/%s/messages?limit=%d", friendId, limit));

        if (afterId != null) {
            requestString
                    .append("&after=")
                    .append(afterId);
        }
        if (beforeId != null) {
            requestString
                    .append("&before=")
                    .append(beforeId);
        }

        final Request request = this
                .createRequestWithAuthorization(requestString.toString())
                .get()
                .build();

        return this.executeForList(request, SafeMessage.class);
    }

    public List<SafeMessage> getLatestMessages(String friendId, int limit) throws IOException {
        return getMessages(friendId, limit, null, null);
    }

    public List<SafeMessage> getMessagesAfter(String friendId, int limit, String afterId) throws IOException {
        return getMessages(friendId, limit, afterId, null);
    }

    public List<SafeMessage> getMessagesBefore(String friendId, int limit, String beforeId) throws IOException {
        return getMessages(friendId, limit, null, beforeId);
    }

    public SafeMessage createMessage(String friendId, CreateMessageRequest messageRequest) throws IOException {
        final Request request = this
                .createRequestWithAuthorization(String.format("/%s/messsages", friendId))
                .post(this.createBody(messageRequest))
                .build();

        return this.execute(request, SafeMessage.class);
    }

    public void deleteMessage(String friendId, String messageId) throws IOException {
        final Request request = this
                .createRequestWithAuthorization(String.format("/%s/messsages?message=%s", friendId, messageId))
                .delete()
                .build();

        this.execute(request);
    }
}
