<template>
  <div class="chat-room-list">
    <div class="header">
      <h2>채팅방 목록</h2>
      <button @click="openCreateRoomModal" class="btn-primary">
        채팅방 생성
      </button>
    </div>
    <ul>
      <li
        v-for="room in rooms"
        :key="room.id"
        @click="enterRoom(room.id)"
        class="room-item"
      >
        <span class="room-name">{{ room.name }}</span>
        <hr />
        <small class="room-users">
          참여자: {{ room.users.map((user) => user.nickname).join(', ') }}
        </small>
      </li>
    </ul>
    <!-- 채팅방 생성 모달 -->
    <div v-if="isCreateRoomModalOpen" class="modal">
      <div class="modal-content">
        <h3>채팅방 생성</h3>
        <label for="roomName">채팅방 이름:</label>
        <input v-model="newRoomName" id="roomName" placeholder="채팅방 이름" />

        <h4>참여자 선택</h4>
        <div class="members-list">
          <ul>
            <li v-for="user in members" :key="user.id">
              <label>
                <input
                  type="checkbox"
                  :value="user.id"
                  v-model="selectedMembers"
                />
                {{ user.nickname }}
              </label>
            </li>
          </ul>
        </div>

        <div class="selected-members">
          <h4>선택된 회원</h4>
          <ul>
            <li v-for="id in selectedMembers" :key="id">
              {{
                members.find((member) => member.id === id)?.nickname ||
                '알 수 없는 사용자'
              }}
            </li>
          </ul>
        </div>

        <div class="modal-buttons">
          <button @click="createRoom" class="btn-primary">생성</button>
          <button @click="closeCreateRoomModal" class="btn-secondary">
            취소
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      rooms: [],
      members: [],
      isCreateRoomModalOpen: false,
      newRoomName: '',
      selectedMembers: [],
    };
  },
  methods: {
    async fetchRooms() {
      try {
        const response = await axios.get('/api/v1/chat/rooms', {
          params: { userId: 2 }, // 현재 사용자 ID
        });
        this.rooms = response.data;
      } catch (error) {
        console.error('Error fetching chat rooms:', error);
      }
    },
    async fetchMembers() {
      try {
        const response = await axios.get('/api/v1/users', {
          params: { moeimId: 3 },
        });
        this.members = response.data;
      } catch (error) {
        console.error('Error fetching members:', error);
      }
    },
    openCreateRoomModal() {
      this.isCreateRoomModalOpen = true;
    },
    closeCreateRoomModal() {
      this.isCreateRoomModalOpen = false;
      this.newRoomName = '';
      this.selectedMembers = [];
    },
    async createRoom() {
      try {
        const payload = {
          roomName: this.newRoomName || '',
          createdBy: 2, // 현재 사용자 ID
          memberIds: this.selectedMembers,
        };
        const response = await axios.post('/api/v1/chat/room', payload, {
          params: { userId: 2 }, // 현재 사용자 ID
        });
        this.rooms.push(response.data);
        this.closeCreateRoomModal();
      } catch (error) {
        console.error('Error creating chat room:', error);
      }
    },
    enterRoom(roomId) {
      this.$router.push({ name: 'ChatView', params: { roomId } });
    },
  },
  mounted() {
    this.fetchRooms();
    this.fetchMembers();
  },
};
</script>

<style scoped>
.chat-room-list {
  max-width: 700px;
  margin: 50px auto;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  color: #414651;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

h2 {
  font-size: 24px;
  font-weight: bold;
}

ul {
  list-style-type: none;
  padding: 0;
  margin: 0;
}

.room-item {
  padding: 10px;
  margin-bottom: 10px;
  border-radius: 5px;
  cursor: pointer;
  background-color: #f9f9f9;
  text-align: left;
  border: 1px solid #ddd;
}

.room-item:hover {
  background-color: #f1f1f1;
}

.room-name {
  font-size: 18px;
  font-weight: bold;
  color: #414651;
}

.room-users {
  font-size: 14px;
  color: #777;
}

hr {
  border: none;
  border-top: 1px solid #ddd;
  margin: 8px 0;
}

.btn-primary {
  background-color: #7f56d9;
  color: white;
  padding: 10px;
  border-radius: 5px;
  font-size: 14px;
  font-weight: bold;
  cursor: pointer;
  border: none;
}

.btn-primary:hover {
  background-color: rgba(127, 86, 217, 0.8);
}

.btn-secondary {
  background-color: #ddd;
  color: #333;
  padding: 10px;
  border-radius: 5px;
  font-size: 14px;
  font-weight: bold;
  cursor: pointer;
  border: none;
}

.btn-secondary:hover {
  background-color: #bbb;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 10px;
  max-width: 500px;
  width: 100%;
}

.members-list {
  max-height: 150px;
  overflow-y: scroll;
  border: 1px solid #ddd;
  padding: 10px;
  border-radius: 5px;
}

.modal-buttons {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}
</style>
