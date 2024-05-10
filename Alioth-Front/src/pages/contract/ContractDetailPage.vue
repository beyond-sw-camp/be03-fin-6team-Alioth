<template>
  <AppSidebar></AppSidebar>
  <v-container fluid>
    <v-main>
      <AppHeader></AppHeader>
      <v-card>
        <v-card-text>
          <div v-if="contract">
            <p><strong>계약 번호:</strong> {{ contract.contractId }}</p>
            <v-divider></v-divider>
            <p><strong>계약 코드:</strong> {{ contract.contractCode }}</p>
            <v-divider></v-divider>
            <p><strong>보험 상품:</strong> {{ contract.insuranceProductName }}</p>
            <v-divider></v-divider>
            <p><strong>고객 이름:</strong> {{ contract.customName }}</p>
            <v-divider></v-divider>
            <p><strong>계약 일자:</strong> {{ formatDate(contract.contractDate) }}</p>
            <v-divider></v-divider>
            <p><strong>계약 만료 일자:</strong> {{ formatDate(contract.contractExpireDate) }}</p>
            <v-divider></v-divider>
            <p><strong>계약 기간(년):</strong> {{ contract.contractPeriod }}</p>
            <v-divider></v-divider>
            <p><strong>총 계약 금액(원):</strong> {{ contract.contractTotalPrice }}</p>
            <v-divider></v-divider>
            <p><strong>결제 금액(원):</strong> {{ contract.contractPaymentAmount }}</p>
            <v-divider></v-divider>
            <p><strong>결제 빈도:</strong> {{ contract.contractPaymentFrequency }}</p>
            <v-divider></v-divider>
            <p><strong>결제 회차:</strong> {{ contract.contractPaymentMaturityInstallment }}</p>
            <v-divider></v-divider>
            <p><strong>계약 수:</strong> {{ contract.contractCount }}</p>
            <v-divider></v-divider>
            <p><strong>결제 방법:</strong> {{ contract.contractPaymentMethod }}</p>
            <v-divider></v-divider>
            <p><strong>지불자:</strong> {{ contract.contractPayer }}</p>
            <v-divider></v-divider>
            <p><strong>상담 내용:</strong> {{ contract.contractConsultation }}</p>
            <v-divider></v-divider>
            <p><strong>계약 상태:</strong> {{ contract.contractStatus }}</p>
            <v-divider></v-divider>
            <p><strong>계약 담당자:</strong> {{ contract.contractMemberName }}</p>
            <v-divider></v-divider>
            <p><strong>영업 사원 사번:</strong> {{ contract.salesMemberCode }}</p>
            <v-divider></v-divider>
            <p><strong>영업 담당자 이름:</strong> {{ contract.salesMemberName }}</p>

          </div>
          <div v-else>
            <p>계약 정보를 불러오는 중...</p>
          </div>
          <v-btn variant="tonal" color="#2979FF" @click="navigateToModify">수정</v-btn>
          <v-btn variant="tonal" color="primary" @click="navigateToCancel">해약</v-btn>
        </v-card-text>
      </v-card>
    </v-main>
  </v-container>
</template>

<script>
import {ref, onMounted} from 'vue';
import AppSidebar from "@/layouts/AppSidebar.vue";
import AppHeader from "@/layouts/AppHeader.vue";
import {useRouter, useRoute} from 'vue-router';
import {format, parseISO} from 'date-fns';
import axiosInstance from '@/plugins/loginaxios';

export default {
  components: {
    AppHeader,
    AppSidebar
  },
  setup() {
    const router = useRouter();
    const route = useRoute();
    const contract = ref(null);
    const baseUrl = import.meta.env.VITE_API_SERVER_BASE_URL
    const fetchContractDetail = () => {
      const contractId = route.params.id;
      axiosInstance.get(`${baseUrl}/api/contract/detail/${contractId}`)
        .then(response => {
          contract.value = response.data.result;
        })
        .catch(error => {
          console.error("Failed to fetch contract details:", error);
        });
    };

    const formatDate = (dateString) => {
      if (!dateString) return '';
      return format(parseISO(dateString), 'yyyy-MM-dd');
    };

    onMounted(fetchContractDetail);

    return {
      contract,
      navigateToModify: () => router.push(`/ContractList/Modify/${route.params.id}`),
      navigateToCancel: () => router.push(`/ContractList/Cancel/${route.params.id}`),
      formatDate
    }
  }
}
</script>

<style scoped>
.v-container {
  max-width: 1160px; /* 최대 너비 조정 */
  margin: auto; /* 가운데 정렬 */
}

.v-card {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 그림자 효과 추가 */
  border-radius: 13px; /* 모서리 둥글게 처리 */
  overflow: hidden; /* 내부 요소가 카드 밖으로 나가지 않도록 처리 */
}

.v-card-title {
  background-color: #42A5F5; /* 타이틀 배경 색상 변경 */
  color: white; /* 타이틀 텍스트 색상 변경 */
  padding: 16px 24px; /* 타이틀 패딩 조정 */
  font-size: 1.5rem; /* 타이틀 폰트 크기 조정 */
}

.v-card-text {
  padding: 20px; /* 텍스트 패딩 조정 */
  color: #424242; /* 텍스트 색상 조정 */
}

p {
  margin-bottom: 10px; /* 문단 간격 조정 */
  line-height: 1.6; /* 줄간격 조정 */
  font-size: 1rem; /* 폰트 크기 조정 */
}

.v-btn {
  margin-right: 10px; /* 버튼 간격 조정 */
}

</style>
