<template>
  <AppSidebar></AppSidebar>
  <v-main>
    <AppHeader></AppHeader>
    <v-container fluid>
      <v-row justify="center">
        <v-col cols="12" md="8">
          <v-card>
            <v-card-text>
              <v-form @submit.prevent="submitForm">
              <span>사진</span>
              <v-img v-if="form.imageUrl" :width="300" aspect-ratio="16/9" cover :src="form.imageUrl"
                     alt="Selected Image"></v-img>
              <input type="file" @change="handleFileUpload">
              <v-spacer></v-spacer>
              <span>이름</span>
              <v-text-field v-model="form.name" label="이름을 입력하세요" required></v-text-field>
              <span>비밀번호</span>
              <v-text-field v-model="form.password" label="비밀번호를 입력하세요" type="password" required></v-text-field>
              <span>이메일</span>
              <v-text-field v-model="form.email" label="이메일을 입력하세요" required></v-text-field>
              <span>직급</span>
              <v-combobox v-model="form.rank" label="직급을 입력하세요" :items="rank" required></v-combobox>
              <span>휴대폰번호</span>
              <v-text-field v-model="form.phone" label="휴대폰 번호를 입력하세요" required></v-text-field>
              <span>생년월일</span>
              <v-text-field v-model="form.birthDay" label="생년월일" type="date" required></v-text-field>
              <span>주소</span>
              <v-spacer></v-spacer>
              <span>우편번호</span>
              <v-spacer></v-spacer>
              <v-text-field type="text" v-model="zoneCode" placeholder="우편번호" readonly/>
              <v-btn id="postcode" type="button" @click="openPostCode" value="우편번호 찾기">우편번호 찾기</v-btn>
              <v-text-field type="text" v-model="roadAddress" placeholder="도로명주소" readonly/>
              <span id="guide" style="color:#999;display:none"></span>
              <v-text-field type="text" v-model="detailAddress" placeholder="상세주소"/>
              <v-spacer></v-spacer>
              <v-btn color="primary" type="submit">사원 추가</v-btn>
              </v-form>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </v-main>
</template>
<script>

import {ref} from 'vue';
import AppSidebar from "@/layouts/AppSidebar.vue";
import AppHeader from "@/layouts/AppHeader.vue";
import axiosInstance from "@/plugins/loginaxios";
import router from "@/router";

export default {
  components: {AppHeader, AppSidebar},
  setup() {
    const form = ref({
      name: '',
      email: '',
      phone: '',
      password: 'a1234567!',
      birthDay: '',
      address: '',
      imageUrl: '',
      rank: 'FP'
    });

    const rank = ['FP', 'MANAGER', 'HQ']
    const zoneCode = ref('')
    const roadAddress = ref('')
    const jibunAddress = ref('')
    const detailAddress = ref('')
    const formatDateTime = (date) => {
      return `${date}T00:00:00`;
    };


    const handleFileUpload = (event) => {
      const file = event.target.files[0];
      form.value.imageUrl = URL.createObjectURL(file);
    };

    function openPostCode() {
      new window.daum.Postcode({
        oncomplete: (data) => {
          zoneCode.value = data.zonecode
          roadAddress.value = data.roadAddress
          jibunAddress.value = data.jibunAddress
        },
      }).open();
    }

    const submitForm = () => {
      if (form.value) {
        const formData = {
          ...form.value,
          birthDay: formatDateTime(form.value.birthDay),
          address : roadAddress.value + detailAddress.value + zoneCode.value // address
        };
        const baseUrl = process.env.VUE_APP_API_BASE_URL || 'http://localhost:8080';
        axiosInstance.post(`${baseUrl}/api/members/create`, formData)
          .then(response => {
            // alert('계약이 성공적으로 생성되었습니다.');
            alert(response.data.message)
            router.push('/SalesMembersList');
          }).catch(error => {
          console.error(error.response.data.message);
          alert(error.response.data.message)
        });
      }
    };


    return {
      zoneCode,
      roadAddress,
      jibunAddress,
      detailAddress,
      form,
      rank,
      handleFileUpload,
      submitForm,
      openPostCode
    };
  }
}
</script>

<style scoped>

</style>
