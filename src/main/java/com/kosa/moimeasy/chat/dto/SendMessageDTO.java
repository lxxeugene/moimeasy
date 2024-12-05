package com.kosa.moimeasy.chat.dto;

import com.kosa.moimeasy.chat.entity.ChatMessage.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageDTO {
    private Long chatRoomId;   // 채팅방 ID
    private Long senderId;     // 메시지 발신자 ID
    private String content;    // 메시지 내용
    private MessageType messageType; // 메시지 유형 (TEXT, IMAGE, FILE)
}
