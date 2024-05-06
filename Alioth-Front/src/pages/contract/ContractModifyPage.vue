<template>
  <AppSidebar></AppSidebar>
  <v-main>
    <AppHeader></AppHeader>
    <v-container fluid>
      <v-card>
        <!-- <v-card-title>계약 수정</v-card-title> -->
        <v-card-text>
          <v-form v-model="valid">
            <v-text-field label="계약 기간(년)" v-model="contract.contractPeriod" :rules="nameRules" required></v-text-field>
            <v-select label="결제 빈도" v-model="contract.contractPaymentFrequency" :items="paymentFrequencies" :rules="nameRules" required></v-select>
            <v-text-field label="지불자(이름)" v-model="contract.contractPayer" :rules="nameRules" required></v-text-field>
            <v-text-field label="결제 방법" v-model="contract.contractPaymentMethod" :rules="nameRules" required></v-text-field>
            <v-select label="계약 상태" v-model="contract.contractStatus" :items="contractStatuses" :rules="nameRules" required></v-select>
            <v-textarea
                label="상담 내용"
                v-model="contract.contractConsultation"
                rows="5"
                auto-grow
              ></v-textarea>
            <v-btn color="#42A5F5" rounded="pill" :disabled="!valid"  @click="submit" >저장</v-btn>
            <v-btn color="#42A5F5" rounded="pill" @click="cancel">취소</v-btn>
          </v-form>
        </v-card-text>
      </v-card>
    </v-container>
  </v-main>
</template>


<script>
import { ref, onMounted } from 'vue';
import AppSidebar from "@/layouts/AppSidebar.vue";
import AppHeader from "@/layouts/AppHeader.vue";
import { useRouter, useRoute } from 'vue-router';
import axiosInstance from '@/plugins/loginaxios';

export default {
  components: { AppHeader, AppSidebar },
  setup() {
    const router = useRouter();
    const route = useRoute();
    const valid = ref(false);
    const baseUrl = import.meta.env.VITE_API_SERVER_BASE_URL || 'http://localhost:8080';
    const contract = ref({
      contractPeriod: '',
      contractPaymentFrequency: '',
      contractPayer: '',
      contractPaymentMethod: '',
      contractConsultation: '',
      contractStatus: ''
    });
    const nameRules = [v => !!v || '필수 입력 사항입니다'];
    const paymentFrequencies = ['Monthly', 'Quarter', 'Yearly'];
    const contractStatuses = ['New', 'Renewal', 'Cancellation'];


    const fetchContractDetail = () => {
      const contractId = route.params.id;
      if (!contractId) {
        console.error("No contract ID found in the route parameters.");
        return;
      }

      axiosInstance.get(`${baseUrl}/api/contract/detail/${contractId}`)
      .then(response => {
        const data = response.data.result;
        contract.value.contractPeriod = data.contractPeriod;
        contract.value.contractPaymentFrequency = data.contractPaymentFrequency;
        contract.value.contractPayer = data.contractPayer;
        contract.value.contractPaymentMethod = data.contractPaymentMethod;
        contract.value.contractConsultation = data.contractConsultation;
      }).catch(error => {
        console.error("Failed to load contract details:", error);
      });
    };

    onMounted(fetchContractDetail);

    const submit = () => {
      if (!valid.value) {
        alert("모든 칸은 입력해주세요.");
        return;
      }

      axiosInstance.patch(`${baseUrl}/api/contract/update/${route.params.id}`, contract.value)
      .then(() => {
        alert('계약 정보가 성공적으로 변경되었습니다.');
        router.push('/ContractList');
      }).catch(error => {
        console.error("Error updating contract:", error);
      });
    };

    const cancel = () => {
      router.back();
    };

    return { contract, valid, nameRules, paymentFrequencies, contractStatuses,submit, cancel };
  }
}
</script>

<style scoped>
.v-card {
  box-shadow: 0 4px 8px rgba(0,0,0,0.1); /* 그림자 효과 추가 */
  border-radius: 12px; /* 모서리 둥글게 처리 */
  overflow: hidden; /* 내부 요소가 카드 밖으로 나가지 않도록 처리 */
}
.v-card-text {
  border-radius: 10px;
}
</style>
