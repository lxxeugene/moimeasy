package com.kosa.moimeasy.chat.controller;

import com.kosa.moimeasy.chat.dto.CreateRoomDTO;
import com.kosa.moimeasy.chat.dto.SendMessageDTO;
import com.kosa.moimeasy.chat.entity.ChatRoom;
import com.kosa.moimeasy.chat.entity.ChatMessage;
import com.kosa.moimeasy.chat.service.ChatService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/api/v1/chat/room")
    @ResponseBody
    public ChatRoom createRoom(@RequestBody CreateRoomDTO request) {
        return chatService.createRoom(request.getRoomName(), request.getRoomType());
    }

    @MessageMapping("/publish/chat.sendMessage")
    @SendTo("/subscribe/chat")
    public ChatMessage sendMessage(SendMessageDTO request) {
        ChatMessage chatMessage = chatService.sendMessage(
                request.getChatRoomId(), request.getSenderId(), request.getContent(), request.getMessageType());
        return chatMessage;
    }
}
