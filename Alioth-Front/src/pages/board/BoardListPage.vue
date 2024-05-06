<template>
  <AppSidebar></AppSidebar>
  <v-container fluid>
    <v-main>
      <AppHeader></AppHeader>
      <v-divider></v-divider>

      <v-card style="margin-top: 10px;">
          <v-tabs
            v-model="model"
            align-tabs="center"
            color="deep-purple-accent-4"
          >
            <v-tab :value="1">공지사항</v-tab>
            <v-tab :value="2">건의사항</v-tab>
          </v-tabs>


      <v-toolbar flat color="white">
          v-model="model"
          :label="model === 'Announcement' ? '공지사항' : '건의사항'"
          :color="model === 'Announcement' ? 'success' : 'info'"
          false-value="Announcement"
          true-value="Suggestion"
          hide-details
          @change="fetchData"
          class="flex-grow-1"
        <v-btn variant="tonal" color="#2979FF" @click="navigateToAddPage" v-if="shouldShowWriteButton">
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
    </v-card>
    </v-main>
  </v-container>
</template>

<script>
import { useRouter } from 'vue-router';
import AppSidebar from "@/layouts/AppSidebar.vue";
import AppHeader from "@/layouts/AppHeader.vue";
import ListComponent from "@/layouts/ListComponent.vue";
import axiosInstance from '@/plugins/loginaxios';
import { useLoginInfoStore } from '@/stores/loginInfo.js';
import { ref, computed,watchEffect } from 'vue';
import { useBoardTypeStore } from '@/stores/boardTypeStore.js';


export default {

  components: {
    AppHeader,
    AppSidebar,
    ListComponent
  },
  setup() {
  const router = useRouter();
  const loginInfoStore = useLoginInfoStore();
  const salesMemberRank = ref(loginInfoStore.getMemberRank);
  const boardTypeStore = useBoardTypeStore();
  const model = ref('Announcement');

  // onBeforeRouteEnter((to, from, next) => {
  //     // URL에서 type 쿼리를 읽어 상태를 업데이트
  //     model.value = to.query.type || 'Announcement';
  //     next();
  //   });

  //   onBeforeRouteUpdate((to, from, next) => {
  //     // URL에서 type 쿼리를 읽어 상태를 업데이트
  //     model.value = to.query.type || 'Announcement';
  //     next();
  //   });

    watchEffect(() => {
      const type = router.currentRoute.value.query.type || 'Announcement';
      model.value = type;
      boardTypeStore.setBoardType(type);
    });



  const shouldShowWriteButton = computed(() => {
  // 'Suggestion' 상태일 때는 항상 true를 반환하여 글쓰기 버튼이 보이도록 함
  if (model.value === 'Suggestion') {
    return true;
  }
  // 'Announcement' 상태이고 등급이 'FP'일 때만 false를 반환하여 글쓰기 버튼을 숨김
  return !(model.value === 'Announcement' && salesMemberRank.value === 'FP');
});

  // 함수와 반응형 참조들을 반환
  return { router, salesMemberRank, model, shouldShowWriteButton };
},

  data() {
    return {
      // model: 'Announcement',
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
      const baseUrl = import.meta.env.VITE_API_SERVER_BASE_URL || 'http://localhost:8080';
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
    // const type = this.$route.query.type || 'Announcement';  // URL에서 type 쿼리 파라미터를 읽음
    // this.model = type;  // model 상태를 업데이트하여 알맞은 목록을 표시
    this.fetchData();
  }
};
</script>



<style scoped>
</style>
