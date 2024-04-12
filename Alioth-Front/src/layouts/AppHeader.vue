<template>
  <v-app-bar>
    <span class="ml-3"></span>
    <v-app-bar-title class="ms-auto">{{ pageName }}</v-app-bar-title>
    <template v-slot:append>
      <span class="ms-2">{{ currentDateTime }}</span>
      <span class="ml-5"> </span>
        <v-icon v-if="hasUnreadAlarm" color="">mdi-bell-alert</v-icon>
        <v-icon v-else color="" >mdi-bell</v-icon>
        <v-badge color="red" dot v-if="hasUnreadAlarm"></v-badge>
    </template>

  </v-app-bar>
  <v-divider :thickness="4" class="border-opacity-50"></v-divider>

</template>
<script>
export default {
  data() {
    return {
      currentDateTime: '',
      pageName: 'Not Found',
      hasUnreadAlarm: false // 기본값, 로딩 문제 발생시 표시됩니다.
    };
  },
  mounted() {
    this.updateDateTime();
    this.updatePageName(); // 페이지 이름 업데이트 메소드 호출 추가
    setInterval(() => {
      this.updateDateTime();
    }, 1000);
  },
  methods: {
    updateDateTime() {
      const now = new Date();
      const year = now.getFullYear();
      const month = String(now.getMonth() + 1).padStart(2, '0');
      const day = String(now.getDate()).padStart(2, '0');
      const hours = String(now.getHours()).padStart(2, '0');
      const minutes = String(now.getMinutes()).padStart(2, '0');
      const seconds = String(now.getSeconds()).padStart(2, '0');
      const dayOfWeek = ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'][now.getDay()];
      this.currentDateTime = `${year}년 ${month}월 ${day}일 ${dayOfWeek} ${hours}:${minutes}:${seconds}`;
    },
    updatePageName() {
      if (this.$route.meta.title) {
        this.pageName = this.$route.meta.title;
      }
    }
  }
}
</script>

<style scoped>

</style>
