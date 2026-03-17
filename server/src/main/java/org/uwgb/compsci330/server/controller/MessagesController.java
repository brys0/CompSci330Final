package org.uwgb.compsci330.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.uwgb.compsci330.common.exception.*;
import org.uwgb.compsci330.common.model.response.ErrorResponse;
import org.uwgb.compsci330.common.model.response.message.SafeMessage;
import org.uwgb.compsci330.server.dto.request.CreateMessageRequest;
import org.uwgb.compsci330.server.service.ConversationService;

import java.util.List;

@RestController
public class MessagesController {
    @Autowired
    private ConversationService conversationService;

    @GetMapping("/{friendId}/messages")
    @ResponseStatus(HttpStatus.OK)
    public List<SafeMessage> getMessages(
            @PathVariable String friendId,
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(name = "after", required = false) String afterId,
            @RequestParam(name = "before", required = false) String beforeId,
            Authentication auth
    ) {
        return conversationService.getMessages(auth.getName(), friendId, afterId, beforeId, limit);
    }

    @PostMapping("/{friendId}/messages")
    @ResponseStatus(HttpStatus.CREATED)
    public SafeMessage createMessage(
            @PathVariable String friendId,
            @RequestBody(required = true) CreateMessageRequest body,
            Authentication auth
    ) {
        return conversationService.createMessage(auth.getName(), friendId, body);
    }

    @DeleteMapping("/{friendId}/messages")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createMessage(
            @PathVariable String friendId,
            @RequestParam(name = "message", required = true) String messageId,
            Authentication auth
    ) {
        conversationService.deleteMessage(auth.getName(), friendId, messageId);
    }

    @ExceptionHandler(value = MessageLimitZeroOrLessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMessageLimitZeroOrLess(MessageLimitZeroOrLessException ex) {
        return new ErrorResponse(ex);
    }

    @ExceptionHandler(value = MessageLimitExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMessageLimitExceeded(MessageLimitExceededException ex) {
        return new ErrorResponse(ex);
    }

    @ExceptionHandler(value = InvalidConversationFetchOptions.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidConversationFetchOptions(InvalidConversationFetchOptions ex) {
        return new ErrorResponse(ex);
    }

    @ExceptionHandler(value = InvalidConversationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidConversation(InvalidConversationException ex) {
        return new ErrorResponse(ex);
    }

    @ExceptionHandler(value = InvalidMessageReferenceId.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidMessageReferenceId(InvalidMessageReferenceId ex) {
        return new ErrorResponse(ex);
    }

    @ExceptionHandler(value = MaxMessageLengthExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMaxMessageLengthExceeded(MaxMessageLengthExceededException ex) {
        return new ErrorResponse(ex);
    }

    @ExceptionHandler(value = InvalidMessageLengthException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidMessageLength(InvalidMessageLengthException ex) {
        return new ErrorResponse(ex);
    }

    @ExceptionHandler(value = MessageNotOwnedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleMessageNotOwned(MessageNotOwnedException ex) {
        return new ErrorResponse(ex);
    }
}
