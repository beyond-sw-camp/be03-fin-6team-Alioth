<template>
  <AppSidebar></AppSidebar>
  <v-main>
    <AppHeader></AppHeader>
    <v-container fluid>

      <div v-if="isModalOpen" class="modal-container" @click="handleModalClick">
        <div class="modal-content" @click.stop>
          <div class="modal-inner">
            <ModalMypage @closeMyPageModal="closeMyPageModal"/>
          </div>
        </div>
      </div>

      

      <v-row>
        <!-- v-col를 이용하면 안에 들어가는 열을 만들수있다 -->
        <!-- cols=6작성시 전체 반차지 12넣으면 전체차지 기본값12이여서 넘으면 아래로내려옴-->
        <v-col cols="12" class="myimage mt-12 ml-12">
          <image></image>
        </v-col>
      </v-row>
        <!-- cols을 이용하여 차지하는 열의 갯수를 정할수 있다 -->

      <v-row class="ml-20">
        <v-col cols="4" class="border_style"></v-col>
        <v-col cols="2" class="myprofilebox mt-12 ml-12">
          <div>
            <h5>이름</h5>
            <h3>{{ this.memberName }}</h3>
          </div>
        </v-col>
        <v-col cols="2" class="myprofilebox mt-12 ml-12">
          <div>
            <h5>소속</h5>
            <h3>{{ this.teamName }}</h3>
          </div>
        </v-col>
        <v-col cols="2" class="myprofilebox mt-12 ml-12">
          <div>
            <h5>사번</h5>
            <h3>{{ this.memberCode }}</h3>
          </div>
        </v-col>


        <v-col cols="4" class="border_style"></v-col>
        <v-col cols="2" class="myprofilebox mt-3 ml-12">
          <div>
            <h5>직급</h5>
            <h3>상무</h3>
          </div>
        </v-col>
        <v-col cols="2" class="myprofilebox mt-3 ml-12">
          <div>
            <h5>직책</h5>
            <h3>CTO</h3>
          </div>
        </v-col>
        <v-col cols="2" class="myprofilebox mt-3 ml-12">
          <div>
            <h5>직무</h5>
            <h3>개발총괄</h3>
          </div>
        </v-col>

        <!--위의 6에서 다음남은칸채운다음 줄바꿈함  -->
        <v-col cols="4" class="border_style"></v-col>
        <v-col cols="6" class="mt-3 ml-10">
          <div class="myprofiledetail">
            <div class="d-flex">
              <h3 class="ma-2 pa-2">상세 설명</h3>
              <div class="flex-grow-1 mr-3"/> <!-- Flex item 2를 오른쪽에 정렬하기 위한 빈 div -->
              <button class="detail-text ma-2 pa-2" @click="openDetailModal">정보수정</button>
              <button class="detail-text ma-2 pa-2 mr-8" @click="">비밀번호 변경</button>
              
              <!-- <h3 class="ma-2 pa-2">정보수정</h3>
              <h3 class="ma-2 pa-2 mr-8">비밀번호 변경</h3> -->
            </div>       
            <div class="divider ml-2"></div>

            <div class="d-flex">
              <h3 class="ma-2 pa-2">회사 주소</h3>
              <div class="flex-grow-1"/> <!-- Flex item 2를 오른쪽에 정렬하기 위한 빈 div -->
              <h4 class="ma-2 pa-2">{{ this.officeAddress }}</h4>
              <h4 class="ma-2 pa-2"></h4>
            </div>

            <div class="d-flex">
              <h3 class="ma-2 pa-2">집 주소</h3>
              <div class="flex-grow-1"/> <!-- Flex item 2를 오른쪽에 정렬하기 위한 빈 div -->
              <h4 class="ma-2 pa-2">{{ this.address }}</h4>
              <h4 class="ma-2 pa-2"></h4>
            </div>

            <div class="d-flex">
              <h3 class="ma-2 pa-2">이메일</h3>
              <div class="flex-grow-1"/> <!-- Flex item 2를 오른쪽에 정렬하기 위한 빈 div -->
              <h4 class="ma-2 pa-2">{{ this.email }}</h4>
              <h4 class="ma-2 pa-2"></h4>
            </div>

            <div class="d-flex">
              <h3 class="ma-2 pa-2">연락처</h3>
              <div class="flex-grow-1"/> <!-- Flex item 2를 오른쪽에 정렬하기 위한 빈 div -->
              <h4 class="ma-2 pa-2">{{ this.phoneNumber }}</h4>
              <h4 class="ma-2 pa-2"></h4>
            </div>

            <div class="d-flex">
              <h3 class="ma-2 pa-2">내선번호</h3>
              <div class="flex-grow-1"/> <!-- Flex item 2를 오른쪽에 정렬하기 위한 빈 div -->
              <h4 class="ma-2 pa-2">{{ this.extensionNumber }}</h4>
              <h4 class="ma-2 pa-2"></h4>
            </div>

            <div class="d-flex">
              <h3 class="ma-2 pa-2">생년월일</h3>
              <div class="flex-grow-1"/> <!-- Flex item 2를 오른쪽에 정렬하기 위한 빈 div -->
              <h4 class="ma-2 pa-2">{{ this.birthDay }}</h4>
              <h4 class="ma-2 pa-2"></h4>
            </div>

          </div>

        </v-col>
      </v-row>
      
    </v-container>
  </v-main>
</template>


<script>
import AppSidebar from "@/layouts/AppSidebar.vue";
import AppHeader from "@/layouts/AppHeader.vue";
import ModalMypage from "@/pages/mypage/ModalMyPage.vue"
import axiosInstance from "@/plugins/loginaxios";
import { useLoginInfoStore } from '@/stores/loginInfo';
import { useMyPageUpdateStore } from '@/stores/myPageUpdate';


export default {
  components: { AppHeader, AppSidebar, ModalMypage },
  setup() {

    return {

    }
  },
  mounted() {
    this.getMyPage();
  },
  data() {
    return {
      isModalOpen: false,
      isDetailModalOpen: false,
      loginMemberCode: "",
      loginStore: useLoginInfoStore(),
      myPageUpdateStore: useMyPageUpdateStore(),
      /* 출력정보들 수정시 이야기좀해주세요 by.JunStiN */ 
      prefileImage: null,
      memberName: "",
      memberCode: "",
      teamName: "",
      // 직급, 직책, 직무 추가해야함
      officeAddress: "",
      address: "",
      email: "",
      phoneNumber: "",
      extensionNumber: "",
      birthDay: "",
    };
  },
  methods: {
    handleModalClick(event) {
      // 모달 배경 클릭 시 모달 닫기
      if (event.target === this.$refs.modalContainer) {
        this.closeModal();
      }
    },
    openDetailModal() {
      this.isModalOpen = true;
    },
    closeMyPageModal() {
      this.isModalOpen = false;
      window.location.reload(true);
    },
    getMyPage() {
      console.log("시작화면 겟 요청테스트");
      this.loginMemberCode = this.loginStore.getMemberCode;
      console.log("api/members/details/" + this.loginMemberCode);
      axiosInstance.get("api/members/details/" + this.loginMemberCode)
        .then(response => {
          // get 요청으로 받은 데이터를 화면에 출력
          const findMember = response.data.result;
          this.myPageUpdateStore.memberInfo = findMember;
          this.memberName = findMember.name;
          this.memberCode = findMember.salesMemberCode;
          this.teamName = findMember.teamName;
          this.address = findMember.address;
          this.officeAddress = findMember.officeAddress;
          this.email = findMember.email;
          this.phoneNumber = findMember.phone;
          this.extensionNumber = findMember.extensionNumber;
          this.birthDay = findMember.birthDay;
          // // 직급, 직책, 직무 추가해야함

        })
        .catch(error => {
          console.error("회원정보를 요청할 수 없습니다. : ", error);
          alert("회원정보를 요청할 수 없습니다.");
        });
    },
  },
}
</script>

<style scoped>
.divider {
  background-color:rgba(0, 0, 0, 0.5);
  height:4px;
  width:770px;
}

.myimage {
  height: 630px;
  width: 380px;
  filter:drop-shadow(0px 4px 4px rgba(0,0,0,0.25));
  border-radius:30px;
  border:1px solid #000000;
  position:absolute;
}

.myprofilebox {
  height:80px;
  width:150px;
  filter:drop-shadow(0px 4px 4px rgba(0,0,0,0.25));
  border-radius:30px;
  border:1px solid #000000;
}

.myprofiledetail {
  height: 435px;
  width: 800px;
  border-radius: 30px;
  border:1px solid #000000;
}

.mydetailinfo {
  display: flex;
}


.detail-text {
  color: #2911e3;
  cursor: pointer;
  background: none;
  border: none;
  font-size: small;
  margin-left: 5px;
}




/* 모달 CSS */
.modal-container {
  position: fixed;
  z-index: 1000;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
}

.modal-inner {
  /* 추가된 부분 */
  width: 100%;
  height: 100%;
  overflow: auto;
}
/* 모달 CSS */

</style>
