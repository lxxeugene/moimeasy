package com.kosa.moimeasy.chat.controller;

import com.kosa.moimeasy.chat.dto.SendMessageDTO;
import com.kosa.moimeasy.chat.entity.ChatMessage;
import com.kosa.moimeasy.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send") // /app/chat.send 로 들어온 메시지 처리
    public void sendMessage(SendMessageDTO messageDTO) {
        ChatMessage saved = chatService.sendMessage(messageDTO);

        // 구독자들에게 전송 (예: /topic/room.3)
        messagingTemplate.convertAndSend("/topic/room." + messageDTO.getChatRoomId(), saved);
    }
}
