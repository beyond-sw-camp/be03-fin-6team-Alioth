<!-- ModalComponent.vue -->
<template>
  <div class="modal-container">
    <div class="modal-content">
      <div class="back-button">
        <button class="back-button-content" @click="closeMyPageModal">
          ⬅︎ BACK
        </button>
      </div>
        <div class="login-window">
        <div class="title">정보 수정</div>
        <div class="form-field">
          <label for="email">회사주소</label>
          <input @keypress.enter="doLogin" type="email" v-model="officeAddress" placeholder="회사주소를 입력해주세요." />
        </div>
        <div class="form-field">
          <label for="text">집주소</label>
          <input @keypress.enter="doLogin" type="tel" v-model="address" placeholder="집주소를 입력해주세요." />
        </div>
        <div class="form-field">
          <label for="text">이메일</label>
          <input @keypress.enter="doLogin" type="tel" v-model="email" placeholder="이메일을 입력해주세요." />
        </div>
        <div class="form-field">
          <label for="text">연락처</label>
          <input @keypress.enter="doLogin" type="tel" v-model="phone" placeholder="연락처를 입력해주세요." />
        </div>
        <div class="form-field">
          <label for="text">내선번호</label>
          <input @keypress.enter="doLogin" type="tel" v-model="extensionNumber" placeholder="내선번호를 입력해주세요." />
        </div>
        <div class="form-field">
          <label for="text">생년월일</label>
          <input @keypress.enter="doLogin" type="tel" v-model="birthDay" placeholder="생년월일을 입력해주세요." />
        </div>
        
        <div class="non-author">
        <button class="signup-text" @click="updateMypage">수정하기</button>

        <!-- <ModalComponent ref="modalContainer"></ModalComponent> -->

        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axiosInstance from "@/plugins/loginaxios";
import { useMyPageUpdateStore } from '@/stores/myPageUpdate';

export default {
  props:[],
  data() {
    return {
      myPageUpdateStore: useMyPageUpdateStore(),
      officeAddress: "",
      address: "",
      email: "",
      phoneNumber: "",
      extensionNumber: "",
      birthDay: "",
      name: "",
      profileImage: "",
    };
  },
  mounted() {
    this.getModalMyPage();
  },
  methods: {
    openSignupModal() {
      
    },
    closeMyPageModal() {
      console.log("모달 마이페이지 닫기 실행 로그");
      this.$emit('closeMyPageModal');
    },
    getModalMyPage() {
      this.address = this.myPageUpdateStore.memberInfo.address;
      this.officeAddress = this.myPageUpdateStore.memberInfo.officeAddress;
      this.email = this.myPageUpdateStore.memberInfo.email;
      this.phone = this.myPageUpdateStore.memberInfo.phone;
      this.extensionNumber = this.myPageUpdateStore.memberInfo.extensionNumber;
      this.birthDay = this.myPageUpdateStore.memberInfo.birthDay;
      this.name = this.myPageUpdateStore.memberInfo.name;
      this.profileImage = this.myPageUpdateStore.memberInfo.profileImage;
    },
    updateMypage() {
      console.log("회원정보 업데이트 요청");
      const data = {
        "email": this.email,
        "phone": this.phone,
        "birthDay": this.birthDay,
        "address": this.address,
        "officeAddress": this.officeAddress,
        "extensionNumber": this.extensionNumber,
        "name": this.name,
        "profileImage": this.profileImage,
      };
      console.log(data);
      axiosInstance.patch("api/members/details/update", data, {
          headers: {
            'Content-Type': 'application/json',
          },
        })
        .then(response => {
          console.error("res결과: " +response);
          this.closeMyPageModal();
        })
        .catch(error => {
          console.error("회원정보를 수정할 수 없습니다. : ", error);
          alert("회원정보를 수정할 수 없습니다.");
        });


    },
    handleModalClick(event) {
      // 모달 배경 클릭 시 모달 닫기
      if (event.target === this.$el) {
        this.$emit('closeModal');
      }
    },
  },
};
</script>

<style scoped>

.modal-container {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background: rgba(0, 0, 0, 0.5);
}

.modal-content {
  width: 30%;
  background: white;
  padding: 20px;
  border-radius: 8px;
}

.back-button {
  margin-bottom: 10px;
}

.back-button-content {
  color: #8692A6;
  font-size: 16px;
  font-family: Inter;
  font-weight: 600;
  word-wrap: break-word;
  border: none;
  background: none;
  padding: 0;
  cursor: pointer;
}
</style>
