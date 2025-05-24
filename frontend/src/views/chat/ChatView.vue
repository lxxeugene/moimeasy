<template>
  <div class="chat-container">
    <div class="chat-messages" ref="chatContainer">
      <div
        v-for="(message, index) in messages"
        :key="message.id"
        :class="[
          'message',
          message.senderId === loggedInUserId ? 'user' : 'bot',
        ]"
      >
        <Avatar
          :icon="'pi pi-user'"
          :style="{ backgroundColor: getAvatarColor(message.senderId) }"
          shape="circle"
        />
        <div class="message-content">
          <strong>{{ message.senderNickname }}</strong>

          <p v-if="message.messageType === 'TEXT' || !message.messageType">
            {{ message.content }}
          </p>

          <img
            v-else-if="message.messageType === 'IMAGE'"
            :src="message.fileUrl || message.content"
            alt="Image"
            class="chat-media"
          />

          <video
            v-else-if="message.messageType === 'VIDEO'"
            :src="message.fileUrl || message.content"
            controls
            class="chat-media"
          ></video>

          <p v-else>Unsupported message type: {{ message.messageType }}</p>
          <small>{{ formatTimestamp(message.timestamp) }}</small>
        </div>
      </div>
    </div>
    <div class="chat-input">
      <input type="file" ref="fileInput" @change="handleFileSelect" style="display: none" />
      <Button icon="pi pi-plus" class="btn-add-file" @click="triggerFileUpload" />
      <InputText
        v-model="newMessage"
        placeholder="메세지를 작성하세요"
        aria-label="Message Input"
        @keyup.enter="sendMessage"
      />
      <Button icon="pi pi-send" aria-label="Send Message" @click="sendMessage" />
      <Button aria-label="Open Emoji Picker" class="emoji-button" @click="toggleEmojiPicker">😉</Button>
      <div v-if="showEmojiPicker" class="emoji-picker-container">
        <EmojiPicker :native="true" @select="onSelectEmoji" />
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, onBeforeUnmount, watch, nextTick } from 'vue';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import axios from 'axios';
import Avatar from 'primevue/avatar';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import EmojiPicker from 'vue3-emoji-picker';
import { useAuthStore } from '@/stores/auth';
import 'vue3-emoji-picker/css';
import {
  ref as firebaseRef,
  uploadBytes,
  getDownloadURL,
} from 'firebase/storage';
import { firebaseStorage } from '@/firebase/firebaseConfig';

export default {
  props: {
    roomId: { type: [String, Number], required: true },
  },
  setup(props) {
    const authStore = useAuthStore();
    const loggedInUserId = authStore.user?.userId;

    const messages = ref([]);
    const newMessage = ref('');
    const showEmojiPicker = ref(false);
    const chatContainer = ref(null);
    const fileInput = ref(null);
    const selectedFile = ref(null);
    const stompClient = ref(null);
    const socket = ref(null);

    const connectWebSocket = () => {
      stompClient.value = Stomp.over(() => new SockJS('http://localhost:8088/ws-chat'));

      const token = authStore.accessToken;

      stompClient.value.connect(
        { Authorization: `Bearer ${token}` }, // ✅ 토큰을 헤더로 전달
        () => {
          console.log('WebSocket 연결 성공');
          stompClient.value.subscribe(`/topic/room.${props.roomId}`, (message) => {
            const received = JSON.parse(message.body);
            messages.value.push(received);
            scrollToBottom();
          });
        },
        (error) => {
          console.error('WebSocket 연결 실패:', error);
        }
      );

    };


    const disconnectWebSocket = () => {
      if (stompClient.value?.connected) {
        stompClient.value.disconnect(() => {
          console.log('WebSocket 연결 종료됨');
        });
      }
    };

    const sendMessage = () => {
      if (!newMessage.value.trim()) return;

      const payload = {
        chatRoomId: props.roomId,
        senderId: loggedInUserId,
        content: newMessage.value,
        messageType: 'TEXT',
      };

      if (stompClient.value && stompClient.value.connected) {
        stompClient.value.send('/app/chat.send', {}, JSON.stringify(payload));
      } else {
        console.warn('STOMP 연결되지 않았습니다. 메시지 전송 취소');
      }
      newMessage.value = '';
    };

    const sendFileMessage = async (fileUrl, messageType) => {
      const payload = {
        chatRoomId: props.roomId,
        senderId: loggedInUserId,
        content: fileUrl,
        fileUrl: fileUrl,
        messageType,
      };

      stompClient.value.send('/app/chat.send', {}, JSON.stringify(payload));
    };

    const triggerFileUpload = () => fileInput.value.click();

    const handleFileSelect = async (event) => {
      selectedFile.value = event.target.files[0];
      if (!selectedFile.value) return;

      const maxFileSize = 1 * 1024 * 1024;
      if (selectedFile.value.size > maxFileSize) {
        alert('파일 크기는 1MB를 초과할 수 없습니다.');
        return;
      }

      const file = selectedFile.value;
      const storagePath = `chatFiles/${file.name}`;
      const storageRef = firebaseRef(firebaseStorage, storagePath);

      try {
        await uploadBytes(storageRef, file);
        const fileUrl = await getDownloadURL(storageRef);
        await sendFileMessage(fileUrl, file.type.includes('video') ? 'VIDEO' : 'IMAGE');
      } catch (error) {
        console.error('파일 업로드 실패:', error);
      }
    };

    const toggleEmojiPicker = () => {
      showEmojiPicker.value = !showEmojiPicker.value;
    };

    const onSelectEmoji = (emoji) => {
      newMessage.value += emoji.i;
      showEmojiPicker.value = false;
    };

    const scrollToBottom = () => {
      nextTick(() => {
        if (chatContainer.value) {
          chatContainer.value.scrollTop = chatContainer.value.scrollHeight;
        }
      });
    };

    const getAvatarColor = (userId) => {
      const colors = ['#6b48c6', '#c3aff8', '#7350cd', '#b9a0f4', '#7f56d9'];
      const index = Math.abs(hashCode(userId)) % colors.length;
      return colors[index];
    };

    const hashCode = (str) => {
      let hash = 0;
      const s = String(str);
      for (let i = 0; i < s.length; i++) {
        hash = (hash << 5) - hash + s.charCodeAt(i);
        hash |= 0;
      }
      return hash;
    };

    const formatTimestamp = (timestamp) => {
      const date = new Date(timestamp);
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      return `${month}.${day} ${hours}:${minutes}`;
    };

    onMounted(() => {
      connectWebSocket();
    });

    onBeforeUnmount(() => {
      disconnectWebSocket();
    });

    // 방 전환 시 WebSocket 재연결
    watch(() => props.roomId, (newRoomId, oldRoomId) => {
      if (stompClient.value?.connected) {
        stompClient.value.disconnect(() => {
          console.log('채팅방 변경됨, 재연결 중...');
          messages.value = [];
          connectWebSocket();
        });
      } else {
        connectWebSocket();
      }
    });

    return {
      messages,
      newMessage,
      sendMessage,
      showEmojiPicker,
      toggleEmojiPicker,
      onSelectEmoji,
      getAvatarColor,
      formatTimestamp,
      chatContainer,
      triggerFileUpload,
      handleFileSelect,
      fileInput,
      loggedInUserId,
    };
  },
};
</script>

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  height: 600px;
  width: 90%;
  margin: 15px;
  border: 1px solid #ccc;
  border-radius: 8px;
  overflow: hidden;
  background-color: #f9f9f9;
}
.chat-messages {
  flex: 1;
  overflow: auto;
  padding: 10px;
  background-color: #f0f0f0;
}
.message {
  display: flex;
  margin-bottom: 15px;
  align-items: flex-start;
}
.message.user {
  flex-direction: row-reverse;
}
.message-content {
  max-width: 70%;
  padding: 10px;
  border-radius: 12px;
  background-color: #fff;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
  margin: 0 10px;
}
.user .message-content {
  background-color: #e3f2fd;
}
.message-content p {
  margin: 0 0 5px 0;
}
.message-content small {
  font-size: 0.8em;
  color: #888;
}
.chat-input {
  display: flex;
  align-items: center;
  padding: 10px;
  background-color: #fff;
  border-top: 1px solid #ccc;
}
.chat-input .p-inputtext {
  flex: 1;
  margin-right: 10px;
}
.p-avatar {
  width: 32px;
  height: 32px;
  color: #ccc;
}
.btn-add-file {
  margin-right: 10px;
}
.emoji-button {
  margin-left: 10px;
}
.emoji-picker-container {
  position: absolute;
  bottom: 60px;
  right: 10px;
  z-index: 10;
  background-color: white;
  box-shadow: 0px 2px 8px rgba(0, 0, 0, 0.15);
  border-radius: 10px;
  padding: 10px;
}
.chat-media {
  max-width: 300px;
  border-radius: 8px;
  margin-top: 5px;
}
</style>
