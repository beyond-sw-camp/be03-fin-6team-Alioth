<template>
  <AppSidebar></AppSidebar>
  <v-main>
    <AppHeader></AppHeader>
    <v-container>
      <v-card class="mt-5" outlined>
        <v-card-title class="headline">{{ board.title }}</v-card-title>
        <v-card-subtitle>
          <span> 작성자 사번 {{ board.salesMemberCode }}</span>
          <span class="grey--text"> | 작성일 {{ board.created_at }}</span>
        </v-card-subtitle>
        <v-card-text v-html="board.content"></v-card-text>

        <div class="answers" v-if="board.boardType === 'SUGGESTION'">
          <div v-for="answer in answers" :key="answer.answer_id" class="answer">
            <v-divider></v-divider>

            <h3>답변</h3>
            <v-divider></v-divider>
            <v-card-subtitle>
              <span> 작성자 사번 {{ answer.salesMemberCode }}</span>
              <p><span> 작성시간 {{ answer.created_at }}</span></p>
            </v-card-subtitle>
            <div v-html="answer.content"></div>
            <v-btn small class="small-btn" @click="openEditModal(answer)" style="margin-bottom: 10px; margin-left: 10px;">답변 수정</v-btn>


          </div>
          <v-btn v-if="answers.length === 0 && !showModal" @click="showModal = true">답글 작성</v-btn>
          <v-dialog v-model="showModal" persistent max-width="600px">
            <v-card>
              <v-card-title>
                답글 작성
                <v-spacer></v-spacer>
                <v-btn icon @click="showModal = false"><v-icon>mdi-close</v-icon></v-btn>
              </v-card-title>
              <v-card-text>
                <Editor @update:content="updateContent" :initialContent="newAnswer"/>
              </v-card-text>
              <v-card-actions>
                <v-btn color="primary" @click="submitAnswer">답글 등록</v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>

          <div v-if="showSuccess">
            <v-alert class="success-alert" dense>

              <template #prepend>
                <v-icon large>mdi-check-circle</v-icon>
              </template>
              답변이 완료된 게시글입니다.
            </v-alert>
          </div>
        </div>

        <v-dialog v-model="editModalVisible" persistent max-width="600px">
          <v-card>
            <v-card-title>
              답글 수정
              <v-spacer></v-spacer>
              <v-btn icon @click="closeEditModal"><v-icon>mdi-close</v-icon></v-btn>
            </v-card-title>
            <v-card-text>
              <Editor @update:content="updateEditContent" :initialContent="editContent"/>
            </v-card-text>
            <v-card-actions>
              <v-btn color="primary" @click="confirmEdit">수정 완료</v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>

        <v-card-actions>
          <v-btn text color="primary" @click="goBack">목록으로</v-btn>
          <v-btn text color="secondary" @click="editBoard">수정</v-btn>
          <v-btn text color="red" @click="deleteBoard">삭제</v-btn>
        </v-card-actions>

      </v-card>
    </v-container>
  </v-main>
</template>

<script>
import AppSidebar from "@/layouts/AppSidebar.vue";
import AppHeader from "@/layouts/AppHeader.vue";
import axiosInstance from '@/plugins/loginaxios'
import Editor from "@/layouts/Editor.vue";



export default {
  components: {AppHeader, AppSidebar,Editor},
  data() {
    return {
      board: {},
      answers: [],
      newAnswer: '',
      currentUser: localStorage.getItem('userId'),
      baseUrl: import.meta.env.VITE_API_SERVER_BASE_URL || 'http://localhost:8080',
      showSuccess: false,
      showInput: false,
      showModal: false,
      editModalVisible: false,
      editContent: '',
      currentEditingId: null,
      editTitle: '',
      submitting: false
    };
  },
  computed: {
    isAuthor() {
      return this.board.memberId === this.currentUser;
    }
  },
  methods: {

    openEditModal(answer) {
      this.editTitle = answer.title;
      this.editContent = answer.content;
      this.currentEditingId = answer.answer_id;
      this.editModalVisible = true;
    },
    closeEditModal() {
      this.editModalVisible = false;
    },
    updateEditContent(htmlContent) {
      this.editContent = htmlContent;
    },
    stripTags(html) {
      const doc = new DOMParser().parseFromString(html, 'text/html');
      return doc.body.textContent || "";
    },
    updateContent(htmlContent) {
      this.newAnswer = htmlContent;
    },
    fetchBoardDetail() {
      const boardId = this.$route.params.boardId;
      axiosInstance.get(`${this.baseUrl}/api/board/detail/${boardId}`)
      .then(response => {
        this.board = response.data.result;
        this.fetchAnswers(boardId);
        tihs.showSuccess = false;
      }).catch(error => {
        console.error('Error fetching board details:', error);
      });
    },
    showInputField() {
      this.showInput = true;
      console.log('Input field should be showing now');
    },
    fetchAnswers(boardId) {
      axiosInstance.get(`${this.baseUrl}/api/answer/list/${boardId}`)
      .then(response => {
          this.answers = response.data.result || [];
        }).catch(error => {
          if (error.response && error.response.status === 404) {
          this.answers = [];
        } else {
          console.error('Error fetching answers:', error);
        }
      });
    },
    editBoard() {
      this.$router.push(`/BoardList/Modify/${this.board.boardId}`);
    },
    deleteBoard() {
      const boardId = this.board.boardId;
      if (confirm("게시글을 정말 삭제하시겠습니까?")) {
        axiosInstance.delete(`${this.baseUrl}/api/board/delete/${boardId}`)
        .then(response => {
          alert('게시글이 삭제되었습니다.');
          this.$router.push('/BoardList');
        }).catch(error => {
          alert('게시글 삭제 실패: ' + error.message);
        });
      }
    },
    submitAnswer() {
      if (this.submitting) {
        alert("처리 중입니다. 잠시만 기다려주세요.");
        return;
      }
      if (!this.newAnswer.trim()) {
        alert("답글을 입력해주세요.");
        return;
      }

      this.submitting = true;
      const boardId = this.$route.params.boardId;

      axiosInstance.post(`${this.baseUrl}/api/answer/${boardId}/create`, {
        title: 'Response',
        content: this.newAnswer
      })
      .then(() => {
        this.newAnswer = '';
        this.showSuccess = true;
        this.fetchAnswers(boardId);
        // this.showInput = false;
        this.showModal = false;
        // localStorage.setItem('showSuccess', 'true');
        // this.showModal = false;
      }).catch(error => {
        console.error('Error submitting answer:', error);
        // this.showSuccess = false;

        alert('답글 등록 실패: ' + (error.message  && error.response.data.message ? error.response.data.message : '서버 에러'));
      }).finally(() => {
        this.submitting = false;
      });
    },
    confirmEdit() {
      const requestData = {
    content: this.editContent,
    title: this.editTitle
  };

    axiosInstance.patch(`${this.baseUrl}/api/answer/update/${this.currentEditingId}`, {
      content: this.editContent
    })
    .then(() => {
      alert('답글이 수정되었습니다.');
      this.fetchAnswers(this.board.boardId);
      this.editModalVisible = false;
    })
    .catch(error => {
      console.error('답글 수정 실패:', error);
      alert('답글 수정 실패: ' + error.message);
    });
  },
    deleteAnswer(answer) {
      const answerId = answer.answer_id;
      if (confirm("이 답글을 정말 삭제하시겠습니까?")) {
        axiosInstance.delete(`${this.baseUrl}/api/answer/delete/${answerId}`)
          .then(() => {
            alert('답글이 삭제되었습니다.');
            this.answers = this.answers.filter(a => a.answer_id !== answerId);
            this.showInput = true;
            this.showSuccess = false;
            this.newAnswer = '';
          }).catch(error => {
            console.error('답글 삭제 실패:', error);
            alert('답글 삭제 실패: ' + error.message);
          });
      }
    },

    goBack() {
  // 건의사항 게시판에서 '뒤로가기'를 클릭했을 때
      if (this.board.boardType === 'SUGGESTION') {
        this.$router.push({ path: '/BoardList', query: { type: 'Suggestion' } });
      } else {
        this.$router.push({ path: '/BoardList', query: { type: 'Announcement' } });
      }
    }
  },
  mounted() {
    // this.showSuccess = localStorage.getItem("showSuccess") === "true";

    this.fetchBoardDetail();
  }
}
</script>

<style scoped>
.headline {
  font-size: 24px;
  font-weight: bold;
}
.answers {
  margin-top: 20px;
}
.answer {
  margin-bottom: 10px;
}
.small-btn {
  padding: 4px 8px;
  font-size: 0.75rem;
  min-width: auto;
}
.success-alert {
  border-left: 5px solid green;
  color: green;
}
</style>
