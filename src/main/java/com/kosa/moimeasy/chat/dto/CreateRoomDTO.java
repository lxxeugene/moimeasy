package com.kosa.moimeasy.chat.dto;

import com.kosa.moimeasy.chat.entity.ChatRoom.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoomDTO {
    private String roomName;   // 채팅방 이름
    private RoomType roomType; // 채팅방 유형 (PRIVATE or GROUP)
}
