<template>
  <link href='//spoqa.github.io/spoqa-han-sans/css/SpoqaHanSansNeo.css' rel='stylesheet' type='text/css'>
  <AppSidebar></AppSidebar>
  <v-container fluid>
    <v-main>
      <AppHeader></AppHeader>
      <v-card class="mt-5" outlined>
        <v-row>
          <v-card-title class="headline" style="margin-top: 1vw; margin-left:1vw; margin-bottom:1vw; font-family: 'Spoqa Han Sans Neo'">{{board.title }}
          </v-card-title>
          <v-col class="text-right" style="margin-bottom:1vw" >
            <v-btn v-if="loginStore.memberCode===board.salesMemberCode " small class="small-btn" variant="tonal" color="#2979FF" style="margin-right: 0.5vw; margin-top: 0.5vw; margin-bottom: 0.75vw" @click="editBoard">수정</v-btn>
            <v-btn v-if="loginStore.memberCode===board.salesMemberCode " small class="small-btn" variant="tonal" color="primary" style="margin-right: 1vw; margin-top: 0.5vw; margin-bottom: 0.75vw" @click="deleteBoard">삭제</v-btn>
          </v-col>
        </v-row>
        <v-divider></v-divider>
        <v-col class="text-right">
          <v-card-subtitle style="margin-top: 0.5vw;">
            <span> 작성자 {{ board.writerName}}</span>
            <span class="grey--text"> | 작성일자 {{ formatDate(board.created_at) }}</span>
          </v-card-subtitle>
        </v-col>
        <v-card-text v-html="board.content"></v-card-text>
      </v-card>

        <v-card style="margin-top:1vw" v-if="board.boardType === 'SUGGESTION'">
          <div v-for="answer in answers" :key="answer.answer_id" class="answer">
            <v-card-title style="font-family: 'Spoqa Han Sans Neo'">RE:</v-card-title>
            <v-divider></v-divider>
            <v-card-subtitle class="text-right" style="margin-top: 0.5vw">
              <span> 작성자 {{ answer.answer_name }}</span>
              <span> | 작성일자 {{ formatDate(answer.created_at) }}</span>
            </v-card-subtitle>
            <v-card-text v-html="answer.content"></v-card-text>
            <v-col class="text-right" v-if="loginStore.memberRank==='HQ' || answer.salesMemberCode === loginStore.getMemberCode">
              <v-btn small class="small-btn" variant="tonal" color="#2979FF" @click="openEditModal(answer)"
                     style="margin-top: 0.5vw; margin-right: 0.5vw"> 답변 수정

              </v-btn>
              <v-btn small class="small-btn" variant="tonal" color="primary" v-if="loginStore.memberRank==='HQ' || answer.salesMemberCode === loginStore.getMemberCode" style="margin-top: 0.5vw;" @click="deleteAnswer(answer)">답변 삭제
              </v-btn>
            </v-col>

          </div>
          <v-dialog v-model="showModal" persistent max-width="600px">
            <v-card>
              <v-row>
                <v-col>
                  <v-card-title class="text-left" style="font-family: 'Spoqa Han Sans Neo'"> 답글 작성</v-card-title>
                </v-col>
                <v-col class="text-right">
                  <v-btn variant="flat" @click="showModal = false">
                    <v-icon>mdi-close</v-icon>
                  </v-btn>
                </v-col>
              </v-row>
              <v-card-text>
                <Editor @update:content="updateContent" :initialContent="newAnswer"/>
              </v-card-text>
              <v-card-actions>
                <v-col class="text-right">
                  <v-btn variant="tonal" color="#2979FF" @click="submitAnswer">답글 등록</v-btn>
                </v-col>
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
        </v-card>
      <v-dialog v-model="editModalVisible" persistent max-width="600px">
        <v-card>
          <v-row>
            <v-col>
              <v-card-title style="font-family: 'Spoqa Han Sans Neo'">
                답글 수정
              </v-card-title>
            </v-col>

            <v-col class="text-right">
              <v-btn variant="flat" @click="closeEditModal">
                <v-icon>mdi-close</v-icon>
              </v-btn>
            </v-col>
          </v-row>

          <v-card-text>
            <Editor @update:content="updateEditContent" :initialContent="editContent"/>
          </v-card-text>
          <v-card-actions>
            <v-col class="text-right">
              <v-btn variant="tonal" color="#2979FF" @click="confirmEdit">수정</v-btn>
            </v-col>
          </v-card-actions>
        </v-card>
      </v-dialog>
      <v-row>
        <v-col>
          <v-btn color="#424242" variant="tonal" style="margin-top:1vw" @click="goBack">목록으로</v-btn>
        </v-col>
        <v-col class="text-right">
          <v-btn variant="tonal" color="#1A237E" style="margin-top:1vw" v-if="answers.length === 0 && !showModal && this.board.boardType === 'SUGGESTION' && this.board.salesMemberCode !== loginStore.getMemberCode " @click="showModal = true">답글
            작성
          </v-btn>
        </v-col>
      </v-row>
    </v-main>
  </v-container>
</template>

<script>
import AppSidebar from "@/layouts/AppSidebar.vue";
import AppHeader from "@/layouts/AppHeader.vue";
import axiosInstance from '@/plugins/loginaxios'
import Editor from "@/layouts/Editor.vue";
import {parseISO, format} from "date-fns";
import {useLoginInfoStore} from "@/stores/loginInfo";

export default {
  components: { AppHeader, AppSidebar, Editor },
  data() {
    return {
      board: {},
      answers: {},
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
      submitting: false,
      loginStore: useLoginInfoStore()
    };
  },
  computed: {
    isAuthor() {
      return this.board.memberId === this.currentUser;
    }
  },
  methods: {
    formatDate(dateString) {
      if (!dateString) return '';
      return format(parseISO(dateString), 'yyyy-MM-dd HH:mm:ss');
    },
    openEditModal(answer) {
      console.log(answer.answer_id)
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
          this.showSuccess = false;
        }).catch(error => {
        console.error('Error fetching board details:', error);
      });
    },
    showInputField() {
      this.showInput = true;
      console.log('Input field should be showing now');
    },
    fetchAnswers() {
      const boardId = this.$route.params.boardId;
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
          .then(() => {
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

        alert('답글 등록 실패: ' + (error.message && error.response.data.message ? error.response.data.message : '서버 에러'));
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
          alert('답글 수정 실패: ' + error.response.data.message);
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
          alert('답글 삭제 실패: ' + error.error.response.data.message);
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
