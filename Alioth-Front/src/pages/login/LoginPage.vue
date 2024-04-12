<template>
  <div class="d-flex justify-center align-center" style="height: 100vh;">
    <v-container fluid>
      <v-row class="fill-height" no-gutters>
        <v-col cols="6" class="d-flex align-center justify-center">
          <img src="@/assets/2024-03-18_2.22.312.png" alt="Alioth Logo" height="400">
        </v-col>
        <v-col cols="5" class="d-flex align-mid justify-center">
          <v-card class="pa-4 elevation-12" width="500" style="border-radius: 15px;">
            <v-card-title class="text-h5 blue-grey lighten-2 white--text" style="text-align: center;">
              WELCOME!
            </v-card-title>
            <v-divider></v-divider>
            <v-card-text>
              <v-form @submit.prevent="login">
                <v-text-field v-model="memberCode" label="사원번호" prepend-icon="mdi-account" type="text" placeholder="사원번호를 입력해주세요" required></v-text-field>
                <v-text-field v-model="password" label="비밀번호" prepend-icon="mdi-lock" type="password" placeholder="비밀번호를 입력해주세요" required></v-text-field>
                <v-btn block large color="blue" class="white--text my-4" type="submit">로그인</v-btn>
              </v-form>
            </v-card-text>

            <v-card-actions>
              <v-btn text color="blue" @click="this.goToFindPassword">비밀번호를 잊어버렸나요?</v-btn>
            </v-card-actions>

          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>
<script>
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';// 수정된 import 
import { errorMessages } from 'vue/compiler-sfc';

export default {
  data() {
    return {
      memberCode: '',
      password: '',
      fcmToken: 'OPTIONAL_FCM_TOKEN' // 필요한 경우 사용
    };
  },
  methods: {
    async login() {
      // 수정된 URL 참조 방식
      const baseUrl = import.meta.env.VUE_APP_API_BASE_URL || 'http://localhost:8080';
      
        const loginData = { memberCode: this.memberCode, password: this.password }
        // 경로가 /api/login이 되도록 수정
        await axios.post(`${baseUrl}/api/login`, loginData)
        .then(response => {
          const data = response.data.result
          localStorage.setItem('accessToken', data.accessToken);
          localStorage.setItem('refreshToken', data.refreshToken);
          alert("성공적으로 로그인 되었습니다.")
          this.$router.push("/")
        })
        .catch(error => {
          alert(error.response.data.message)
        });
      },
    
    goToFindPassword() {
      this.$router.push("/Login/PasswordFind");
    },
  }
}
</script>

<style scoped>
.rounded-input input {
  border-radius: 8px;
}
</style>
