import { createRouter, createWebHistory } from 'vue-router';
import Home from '@/views/HomeView.vue';
import Login from '@/views/login/LoginView.vue';
import Signup from '@/views/login/SignupView.vue';
import MoeimSelect from '@/views/moeim/MoeimSelectView.vue';
import CreateMoeim from '@/views/moeim/CreateMoeimView.vue';
import EnterMoeim from '@/views/moeim/EnterMoeimView.vue';
import Schedule from '@/views/schedule/Schedule.vue';
import UserManage from '@/views/user/UserManageView.vue';
import UserProfile from '@/views/user/UserProfileView.vue';
import InviteUser from '@/views/user/InviteUserView.vue';
import InvitationList from '@/views/user/InvitationListView.vue'; // 초대 목록 추가
import PayList from '@/views/PayList.vue';
import WebSocket from '@/views/WebSocketView.vue';

const routes = [
  { path: '/', name: 'Home', component: Home },
  { path: '/login', name: 'Login', component: Login },
  { path: '/signup', name: 'Signup', component: Signup },
  { path: '/moeim-select', name: 'MoeimSelect', component: MoeimSelect },
  { path: '/create-moeim', name: 'CreateMoeim', component: CreateMoeim },
  { path: '/enter-moeim', name: 'EnterMoeim', component: EnterMoeim },
  { path: '/schedule', name: 'Schedule', component: Schedule },
  { path: '/user-manage', name: 'UserManage', component: UserManage },
  {
    path: '/user-profile/:userId',
    name: 'UserProfile',
    component: UserProfile,
  },
  { path: '/invite-user', name: 'InviteUser', component: InviteUser },
  {
    path: '/invitation-list',
    name: 'InvitationList',
    component: InvitationList,
  }, // 초대 목록 추가
  { path: '/pay-list', name: 'PayList', component: PayList },
  { path: '/chat', name: 'WebSocket', component: WebSocket },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
