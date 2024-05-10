<template>
  <AppSidebar></AppSidebar>
  <v-container fluid>
    <v-main>
      <AppHeader></AppHeader>
      <div class="text-center">
        <v-form ref="form" v-model="valid" lazy-validation>
          <v-text-field
            v-model="announcement.title"
            :rules="titleRules"
            label="제목"
            required
          ></v-text-field>
          <Editor @update:content="updateContent" :initialContent="announcement.content"/>
          <v-btn variant="tonal" color="#2979FF" :disabled="!valid" @click="submitAnnouncement">
            공지사항 추가
          </v-btn>
        </v-form>
      </div>
    </v-main>
  </v-container>
</template>

<script>
import Editor from "@/layouts/Editor.vue";
import AppSidebar from "@/layouts/AppSidebar.vue";
import AppHeader from "@/layouts/AppHeader.vue";
import axiosInstance from '@/plugins/loginaxios';

export default {
  components: {
    AppHeader, AppSidebar, Editor
  },
  data() {
    return {
      valid: true,
      baseUrl: import.meta.env.VITE_API_SERVER_BASE_URL || 'http://localhost:8080',
      announcement: {
        title: '',
        content: ''
      },
      titleRules: [
        v => !!v || '제목은 필수 입력 사항입니다.',
        v => (v && v.length <= 100) || '제목은 100자 이내여야 합니다.'
      ]
    };
  },
  methods: {
    updateContent(htmlContent) {
      // console.log("Received content from editor:", htmlContent);
      this.announcement.content = htmlContent;
    },
    submitAnnouncement() {

      if (!this.announcement.content) {
        alert('공지사항 내용을 입력해주세요.');
        return;
      }
      if (this.$refs.form.validate()) {

        const apiUrl = `${this.baseUrl}/api/board/create`;

        const payload = {
          title: this.announcement.title,
          content: this.announcement.content,
          boardType: 'ANNOUNCEMENT'
        };
        axiosInstance.post(apiUrl, payload)
          .then(() => {
            alert('공지사항이 추가되었습니다.');
            this.resetForm();
          })
          .catch(error => {
            console.error('공지사항 추가 중 오류가 발생했습니다', error);
            alert('오류가 발생했습니다.');
          });
      }
    },
    resetForm() {
      this.announcement.title = '';
      this.announcement.content = '';
      this.$refs.form.resetValidation();
      this.$router.push('/BoardList');
    }
  }
}
</script>

<style scoped>
.text-center {
  text-align: center;
}
</style>
