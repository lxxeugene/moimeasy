package com.kosa.moimeasy.chat.controller;

import com.kosa.moimeasy.chat.dto.CreateRoomDTO;
import com.kosa.moimeasy.chat.dto.SendMessageDTO;
import com.kosa.moimeasy.chat.entity.ChatRoom;
import com.kosa.moimeasy.chat.entity.ChatMessage;
import com.kosa.moimeasy.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/chat")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/room")
    public ResponseEntity<ChatRoom> createRoom(@RequestBody CreateRoomDTO request, @RequestParam Long userId) {
        ChatRoom chatRoom = chatService.createRoom(request, userId);
        return ResponseEntity.ok(chatRoom);
    }

    @PostMapping("/message")
    public ResponseEntity<ChatMessage> sendMessage(@RequestBody SendMessageDTO request) {
        ChatMessage message = chatService.sendMessage(request);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/rooms/{roomId}/poll-messages")
    public ResponseEntity<List<ChatMessage>> pollMessages(
            @PathVariable Long roomId,
            @RequestParam Long lastMessageId) {
        List<ChatMessage> messages = chatService.getMessagesSince(roomId, lastMessageId);
        return ResponseEntity.ok(messages);
    }


    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoom>> getAllRooms(@RequestParam Long userId) {
        return ResponseEntity.ok(chatService.getAllRooms(userId));
    }

//     @PostMapping("/upload")
// public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
//     try {
//         String filePath = fileService.uploadFile(file);
//         return ResponseEntity.ok(filePath);
//     } catch (Exception e) {
//         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패");
//     }
// }
}



