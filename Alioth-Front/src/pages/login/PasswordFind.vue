<template>
  <div class="d-flex justify-center align-center" style="height: 100vh;">
    <v-container fluid>
      <v-row class="fill-height" no-gutters>
        <v-col cols="6" class="d-flex align-center justify-center">
          <img src="@/assets/2024-03-18_2.22.312.png" alt="Logo" height="400">
        </v-col>
        <v-col cols="5" class="d-flex align-center justify-center">
          <v-card class="pa-4 elevation-12" width="500" style="border-radius: 15px;">
            <v-card-title class="text-h5 blue-grey lighten-2 white--text" style="text-align: center;">
              비밀번호 변경
            </v-card-title>
            <v-divider></v-divider>
            <v-card-text>
              <v-form @submit.prevent="submit">
                <v-text-field v-model="employeeNumber" label="사원번호" prepend-icon="mdi-account" type="text" placeholder="사원번호를 입력해주세요" required></v-text-field>
                <v-row>
                  <v-col cols="8">
                    <v-text-field v-model="phone" label="연락처" prepend-icon="mdi-phone" type="text" :rules="[phoneRules]" placeholder="예: 010-1234-5678" required @input="clearVerification"></v-text-field>
                  </v-col>
                  <v-col cols="4">
                    <v-btn :disabled="!phonePatternTest" color="primary" @click="sendVerificationCode">인증번호 발송</v-btn>
                  </v-col>
                </v-row>
                <v-row v-if="verificationVisible">
                  <v-col cols="8">
                    <v-text-field v-model="verificationCode" :class="{'error--text': verificationError, 'success--text': verificationSuccess}" label="인증번호 확인" prepend-icon="mdi-checkbox-marked-circle-outline" type="text" placeholder="인증번호를 입력해주세요" required></v-text-field>
                  </v-col>
                  <v-col cols="4">
                    <v-btn :disabled="!verificationCode" color="success" @click="verifyCode">확인</v-btn>
                  </v-col>
                </v-row>
                <transition name="slide-fade">
                  <div v-if="verified">
                    <v-text-field v-model="newPassword" label="새로운 비밀번호" prepend-icon="mdi-lock" type="password" placeholder="새로운 비밀번호를 입력해주세요" required></v-text-field>
                    <v-text-field v-model="newPasswordConfirmation" label="새로운 비밀번호 확인" prepend-icon="mdi-lock-check" type="password" placeholder="새로운 비밀번호를 다시 입력해주세요" required></v-text-field>
                    <v-btn block large color="blue" class="white--text my-4" type="submit">확인</v-btn>
                  </div>
                </transition>
              </v-form>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import axios from 'axios';
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';

export default {
  setup() {
    const employeeNumber = ref('');
    const phone = ref('');
    const verificationCode = ref('');
    const verificationVisible = ref(false);
    const verificationError = ref(false);
    const newPassword = ref('');
    const newPasswordConfirmation = ref('');
    const verified = ref(false);
    const verificationSuccess = ref(false);
    const router = useRouter();

    const baseUrl = import.meta.env.VUE_APP_API_BASE_URL || 'http://localhost:8080';

    const phoneRules = [
      v => !!v || '연락처 입력은 필수입니다.',
      v => (/^\d{3}-\d{4}-\d{4}$/).test(v) || '핸드폰 번호의 양식과 맞지 않습니다. 예: 010-1234-5678'
    ];

    const phonePatternTest = computed(() => /^\d{3}-\d{4}-\d{4}$/.test(phone.value));
    const formattedPhoneNumber = computed(() => phone.value.replace(/-/g, '').replace(/^010/, '+82010'));

    const sendVerificationCode = async () => {
      if (!phonePatternTest.value) {
        alert('연락처 형식을 확인해주세요.');
        return;
      }
      await axios.post(`${baseUrl}/api/send-verification`, { phone: formattedPhoneNumber.value })
        .then(response => {
          console.log(response.data);
          alert(response.data.message);
          verificationVisible.value = true;
        })
        .catch(error => {
          console.error('Error sending verification code:', error);
          alert('Failed to send verification code.');
        });
    };

    const verifyCode = async () => {
      await axios.post(`${baseUrl}/api/verify-code`, { phone: formattedPhoneNumber.value, code: verificationCode.value })
        .then(response => {
          if (response.data.message === "인증에 성공하였습니다.") {
            verificationError.value = false;
            verificationSuccess.value = true;
            verified.value = true;
          } else {
            verificationError.value = true;
            verificationSuccess.value = false;
          }
        })
        .catch(error => {
          verificationError.value = true;
          verificationSuccess.value = false;
        });
    };

    const clearVerification = () => {
      verificationVisible.value = false;
      verificationError.value = false;
      verificationSuccess.value = false;
      verified.value = false;
      verificationCode.value = '';
    };

    const submit = async () => {
      if (newPassword.value !== newPasswordConfirmation.value) {
        alert('새로운 비밀번호가 일치하지 않습니다.');
        return;
      }
      await axios.patch(`${baseUrl}/api/members/${employeeNumber.value}/password`, { password: newPassword.value })
        .then(response => {
          alert(response.data.message);
          router.push("/Login");
        })
        .catch(error => {
          console.error('비밀번호 변경 오류:', error.response || error.message);
          alert('비밀번호 변경 실패.');
        });
    };

    return {
      employeeNumber,
      phone,
      verificationCode,
      verificationVisible,
      verificationError,
      verificationSuccess,
      newPassword,
      newPasswordConfirmation,
      verified,
      sendVerificationCode,
      verifyCode,
      submit,
      phoneRules,
      phonePatternTest,
      clearVerification
    };
  }
};
</script>

<style scoped>

</style>