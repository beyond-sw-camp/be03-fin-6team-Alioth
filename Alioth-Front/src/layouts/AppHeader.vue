<template>

  <v-toolbar color="white">
    <v-app-bar-title class="ms-auto">{{ pageName }}</v-app-bar-title>
    <template v-slot:append>
      <span class="ms-2">{{ currentDateTime }}</span>
      <v-menu offset-y open-on-hover>
        <template v-slot:activator="{ props, on }">
          <v-btn icon v-bind="props" v-on="{ ...on }">
            <v-icon :color="hasNewNotifications ? 'red' : 'black'">mdi-bell</v-icon>
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
  <v-divider :thickness="4" class="border-opacity-50"></v-divider>
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
    }, { deep: true, immediate: true });

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
      this.currentDateTime = `${now.getFullYear()}년 ${now.getMonth() + 1}월 ${now.getDate()}일 ${['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'][now.getDay()]} ${now.getHours()}:${now.getMinutes()}:${now.getSeconds()}`;
    };
    updateDateTime();
    setInterval(updateDateTime, 1000);
  }
}
</script>

<style scoped>
</style>
