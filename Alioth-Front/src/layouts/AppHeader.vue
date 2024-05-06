<template>
  <link href='//spoqa.github.io/spoqa-han-sans/css/SpoqaHanSansNeo.css' rel='stylesheet' type='text/css'>
  <v-toolbar color="white">
    <v-app-bar-title class="ms-auto custom-font">{{ pageName }}</v-app-bar-title>
    <template v-slot:append>
      <span class="ms-2 custom-font">{{ currentDateTime }}</span>
      <v-menu offset-y open-on-hover>
        <template v-slot:activator="{ props, on }">
          <v-btn icon v-bind="props" v-on="{ ...on }">
            <v-icon color="black" >mdi-bell-outline</v-icon>
            <v-badge color="red" dot v-if="hasNewNotifications"></v-badge>
          </v-btn>
        </template>
        <v-list dense v-if="notifications && notifications.length > 0">
          <v-list-item
            v-for="(notification, index) in notifications"
            :key="index"
            @click="handleNotificationClick(index)"
          >
            <div>
              <div class="text-h6">{{ notification.title }}</div>
              <div class="text-subtitle-1">{{ notification.body }}</div>
            </div>
          </v-list-item>
        </v-list>
      </v-menu>
    </template>
  </v-toolbar>
  <v-divider :thickness="2" color="#0D47A1" class="border-opacity-50"></v-divider>
</template>

<script>
import { computed, onBeforeUnmount, watch } from 'vue';
import { useNotificationStore } from '@/stores/notificationStore';
import { useRoute, useRouter } from 'vue-router';

export default {
  setup() {
    const notificationStore = useNotificationStore();
    const router = useRouter();
    const route = useRoute();

    const pageName = computed(() => route.meta.title || 'Not Found');

    watch(() => notificationStore.notifications, (newNotifications) => {
      console.log('알림이 업데이트되었습니다:', newNotifications);
    }, {deep: true, immediate: true});

    const handleNotificationClick = (index) => {
      console.log('알림 클릭 처리 중, 인덱스:', index);
      if (Array.isArray(notificationStore.notifications) && notificationStore.notifications.length > index) {
        const notification = notificationStore.notifications[index];
        console.log('선택된 알림:', notification);

        if (notification && notification.url) {
          notificationStore.removeNotification(index);
          console.log('Navigating to:', notification.url);
          router.push(notification.url);
          if (notificationStore.notifications.length === 0) {
            notificationStore.hasNewNotifications = false;
            console.log('모든 알림이 지워졌습니다.');
          }
        } else {
          console.error('url누락:', notification);
        }
      } else {
        console.error('Index out of bounds or notifications array is undefined or empty.');
      }
    };

    onBeforeUnmount(() => {
      console.log('Component is being destroyed, current notifications:', notificationStore.notifications);
    });

    return {
      hasNewNotifications: computed(() => notificationStore.hasNewNotifications),
      notifications: computed(() => notificationStore.notifications),
      handleNotificationClick,
      pageName
    };
  },
  data() {
    return {
      currentDateTime: '',
    };
  },
  mounted() {
    const updateDateTime = () => {
      const now = new Date();
      const addZero = (num) => (num < 10 ? '0' + num : num); // 한 자리 숫자에 0을 추가하는 함수
      const year = now.getFullYear();
      const month = addZero(now.getMonth() + 1);
      const date = addZero(now.getDate());
      const day = ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'][now.getDay()];
      const hours = addZero(now.getHours());
      const minutes = addZero(now.getMinutes());
      const seconds = addZero(now.getSeconds());
      this.currentDateTime = `${year}년 ${month}월 ${date}일 ${day} ${hours}:${minutes}:${seconds}`;
    };
    updateDateTime();
    setInterval(updateDateTime, 1000);
  }
}
</script>

<style scoped>
.custom-font {
  font-family: "Spoqa Han Sans Neo", sans-serif; /* 여기에 원하는 폰트 패밀리를 넣어주세요 */
}
</style>
