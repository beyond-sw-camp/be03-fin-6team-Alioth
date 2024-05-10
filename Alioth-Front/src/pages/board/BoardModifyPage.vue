<template>
  <AppSidebar></AppSidebar>
  <v-container fluid>
    <v-main>
      <AppHeader></AppHeader>
      <v-card>
        <v-card-text>
          <v-form>
            <v-text-field v-model="board.title" label="제목" outlined dense></v-text-field>
            <Editor :initialContent="board.content" @update:content="updateContent"/>
          </v-form>
        </v-card-text>
        <v-col class="text-right">
            <v-btn variant="tonal" color="#2979FF" style="margin-right: 0.5vw;" @click="updateBoard">저장</v-btn>
            <v-btn variant="tonal" color="#2C3E50" @click="goBack">닫기</v-btn>
        </v-col>
      </v-card>
    </v-main>
  </v-container>
</template>

<script>
import {ref} from 'vue';
import AppSidebar from "@/layouts/AppSidebar.vue";
import AppHeader from "@/layouts/AppHeader.vue";
import { useRouter} from 'vue-router';
import axiosInstance from '@/plugins/loginaxios';
import Editor from "@/layouts/Editor.vue"; // Editor 컴포넌트를 import

export default {
  components: {AppHeader, AppSidebar, Editor},
  props:["boardId"],
  setup(props) {
    const board = ref({title: '', content: ''});
    const router = useRouter();
    const baseUrl = import.meta.env.VITE_API_SERVER_BASE_URL || 'http://localhost:8080';

    const fetchBoardDetails = () => {
      axiosInstance.get(`${baseUrl}/api/board/detail/${props.boardId}`)
        .then(response => {
          console.log(response.data);
          board.value = response.data.result;
        }).catch(error => {
        console.error('게시판 상세 정보를 가져오는데 실패했습니다:', error);
        // Handle error or redirect
      });
    };

    const updateBoard = () => {
      axiosInstance.patch(`${baseUrl}/api/board/update/${props.boardId}`, {
        title: board.value.title,
        content: board.value.content
      })

        .then(response => {
          alert(response.data.message);
          router.push('/BoardList');
        }).catch(error => {
        console.error('게시판 수정 실패:', error);
        alert('수정 실패: ' + (error.response ? error.response.data.message : '서버 응답 없음'));
      });
    };

    const updateContent = (newContent) => {
      board.value.content = newContent;
    };

    const goBack = () => {
      router.go(-1);
    };

    fetchBoardDetails();

    return {board, updateBoard, goBack, updateContent};
  }
}
</script>

<style scoped>
.headline {
  font-size: 24px;
  font-weight: bold;
}
</style>
