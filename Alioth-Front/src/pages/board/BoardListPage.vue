<template>
  <AppSidebar></AppSidebar>
  <v-container fluid>
    <v-main>
      <AppHeader></AppHeader>
      <v-divider></v-divider>
      <v-toolbar flat>
        <v-switch
          v-model="model"
          :label="model === 'Announcement' ? '공지사항' : '건의사항'"
          :color="model === 'Announcement' ? 'success' : 'info'"
          false-value="Announcement"
          true-value="Suggestion"
          hide-details
          @change="fetchData"
          class="flex-grow-1"
        ></v-switch>
        <v-btn color="primary" @click="navigateToAddPage">
          글쓰기
        </v-btn>
      </v-toolbar>
      <ListComponent
        :columns="headers"
        :rows="formattedItems.slice((currentPage-1)*10, currentPage*10)"
        @click:row="handleRowClick"
      />
      <v-pagination
        v-model="currentPage"
        :length="pageCount"
        class="pt-2"
      ></v-pagination>
    </v-main>
  </v-container>
</template>
<script>
import { useRouter } from 'vue-router';
import AppSidebar from "@/layouts/AppSidebar.vue";
import AppHeader from "@/layouts/AppHeader.vue";
import ListComponent from "@/layouts/ListComponent.vue";
import axiosInstance from '@/plugins/loginaxios';

export default {

  components: {
    AppHeader,
    AppSidebar,
    ListComponent
  },
  setup() {
    const router = useRouter();
    return { router };
  },
  data() {
    return {
      model: 'Announcement',
      items: [],
      currentPage: 1,
      pageCount: 0,
      headers: [
        { title: 'No', key: 'boardId' },
        { title: '제목', key: 'title' },
        { title: '작성자', key: 'salesMemberCode' },
        { title: '글내용', key: 'content' },
        { title: '작성일자', key: 'created_at' },
        { title: '수정일자', key: 'updated_at' },
      ],
    };
  },
  computed: {
    pageCount() {
    return Math.ceil(this.items.length / 10);
  },
    formattedItems() {
      return this.items.map(item => ({
        ...item,
        memberId: item.memberId ? item.memberId.toString() : 'N/A',
        content: item.content ? this.stripHtml(item.content) : '', // HTML 태그 제거
        created_at: item.created_at ? new Date(item.created_at).toLocaleString() : 'N/A',
        updated_at: item.updated_at ? new Date(item.updated_at).toLocaleString() : 'N/A',
      }));
    }
  },
  methods: {
    stripHtml(html) {
    const doc = new DOMParser().parseFromString(html, 'text/html');
    return doc.body.textContent || "";
    },
    handleRowClick(event, { item }) {
      console.log('상세 게시물 데이터:', item);
      if (!item || !item.boardId) {
        console.error('잘못된 항목 또는 boardId가 누락됨:', item);
        return;
      }
      const path = `/BoardList/Detail/${item.boardId}`;
      this.router.push(path);
    },
    fetchData() {
      const baseUrl = process.env.VUE_APP_API_BASE_URL || 'http://localhost:8080';
      const apiEndpoint = this.model === 'Announcement' ? 'list' : 'suggestions-list';
      const apiURL = `${baseUrl}/api/board/${apiEndpoint}`;

      axiosInstance.get(apiURL)
      .then(response => {
        this.items = response.data.result || [];
      }).catch(error => {
        console.error('HTTP 데이터를 가져오는 중 에러 발생:', error);
        alert('데이터를 가져오는 중 문제가 발생했습니다.');
        this.items = [];
      });
    },
    navigateToAddPage() {
      const path = this.model === 'Announcement' ? '/BoardList/Add' : '/BoardList/SuggestionBoardAdd';
      this.router.push(path);
    },
  },
  mounted() {
    this.fetchData();
  }
};
</script>



<style scoped>
</style>
