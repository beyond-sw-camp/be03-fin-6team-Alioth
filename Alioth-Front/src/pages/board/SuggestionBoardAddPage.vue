<template>
  <AppSidebar></AppSidebar>
  <v-main>
    <AppHeader></AppHeader>
    <v-container fluid>
      <div class="text-center">
        <v-form ref="form" v-model="valid" lazy-validation>
          <v-text-field
            v-model="suggestion.title"
            :rules="titleRules"
            label="제목"
            required
          ></v-text-field>
          <!-- Editor 컴포넌트 사용 -->
          <Editor :initialContent="suggestion.content" @update:content="updateContent" />
          <v-btn :disabled="!valid" @click="submitSuggestion">
            건의사항 추가
          </v-btn>
        </v-form>
      </div>
    </v-container>
  </v-main>
</template>

<script>

import AppSidebar from "@/layouts/AppSidebar.vue";
import AppHeader from "@/layouts/AppHeader.vue";
import { useRouter } from 'vue-router';
import axiosInstance from '@/plugins/loginaxios';
import Editor from "@/layouts/Editor.vue";


export default {
  components: {AppHeader, AppSidebar, Editor},
  setup() {
    const router = useRouter(); // 여기로 이동
    return { router };
  },

  data() {
    return {
      valid: true,
      suggestion: {
        title: '',
        content: ''
      },
      titleRules: [
        v => !!v || '제목은 필수 입력 사항입니다.',
        v => (v && v.length <= 100) || '제목은 100자 이내여야 합니다.'
      ],
      contentRules: [
        v => !!v || '내용은 필수 입력 사항입니다.'
      ]
    };
  },
  methods: {
    updateContent(content) {
      this.suggestion.content = content; // Editor에서 내용이 업데이트 될 때 실행
    },
    submitSuggestion() {
      if (this.$refs.form.validate()) {
        const baseUrl = import.meta.env.VUE_APP_API_BASE_URL || 'http://localhost:8080';
        const apiUrl = `${baseUrl}/api/board/create`;
        
        
        const payload = {
          title: this.suggestion.title,
          content: this.suggestion.content,
          boardType: 'SUGGESTION'
        };

        axiosInstance.post(apiUrl, payload)
        .then(() => {
          alert('건의사항이 추가되었습니다.');
          this.resetForm();
          this.router.push('/BoardList'); // this.router 사용
        }).catch(error => {
          console.error('건의사항 추가에 실패했습니다:', error);
          alert('오류가 발생했습니다: ' + (error.response && error.response.data.message ? error.response.data.message : '서버 응답 없음'));
        });
      }
    },
    resetForm() {
      this.suggestion.title = '';
      this.suggestion.content = '';
      this.$refs.form.resetValidation();
    }
  }
}
</script>

<style scoped>
.text-center {
  text-align: center;
}
</style>
