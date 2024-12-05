// package com.kosa.moimeasy.chat.controller;

// import com.kosa.moimeasy.chat.dto.CreateRoomDTO;
// import com.kosa.moimeasy.chat.dto.SendMessageDTO;
// import com.kosa.moimeasy.chat.entity.ChatRoom;
// import com.kosa.moimeasy.chat.entity.ChatMessage;
// import com.kosa.moimeasy.chat.service.ChatService;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// @RestController
// @RequestMapping("/api/v1/chat")
// public class ChatController {

//     @Autowired
//     private ChatService chatService;

//     @PostMapping("/room")
//     public ResponseEntity<ChatRoom> createRoom(@RequestBody CreateRoomDTO request) {
//         ChatRoom chatRoom = chatService.createRoom(request.getRoomName(), request.getRoomType());
//         return ResponseEntity.ok(chatRoom);
//     }

//     @PostMapping("/message")
//     public ResponseEntity<ChatMessage> sendMessage(@RequestBody SendMessageDTO request) {
//         ChatMessage chatMessage = chatService.sendMessage(
//                 request.getChatRoomId(), request.getSenderId(), request.getContent(), request.getMessageType());
//         return ResponseEntity.ok(chatMessage);
//     }

//     @PutMapping("/message/read/{messageId}")
//     public ResponseEntity<String> updateReadStatus(@PathVariable Long messageId) {
//         chatService.updateReadStatus(messageId);
//         return ResponseEntity.ok("읽음 상태가 업데이트되었습니다.");
//     }
// }

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

    @MessageMapping("/app/chat.sendMessage")
@SendTo("/topic/messages")
public ChatMessage sendMessage(SendMessageDTO request) {
    // 메시지를 전송하기 전에 데이터베이스에 저장
    ChatMessage chatMessage = chatService.sendMessage(
            request.getChatRoomId(), request.getSenderId(), request.getContent(), request.getMessageType());
    return chatMessage;
}

}
