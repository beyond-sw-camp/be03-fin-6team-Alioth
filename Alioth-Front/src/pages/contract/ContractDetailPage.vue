<template>
  <AppSidebar></AppSidebar>
  <v-main>
    <AppHeader></AppHeader>
    <v-container fluid>
      <v-card>
        <v-card-title>계약 상세 정보</v-card-title>
        <v-card-text>
          <div v-if="contract">
            <p>계약 번호: {{ contract.contractId }}</p>
            <p>계약 코드: {{ contract.contractCode }}</p>
            <p>보험 상품: {{ contract.insuranceProductName }}</p>
            <p>고객 이름: {{ contract.customName }}</p>
            <p>계약 일자: {{ formatDate(contract.contractDate) }}</p>
            <p>계약 만료 일자: {{ formatDate(contract.contractExpireDate) }}</p>
            <p>계약 기간: {{ contract.contractPeriod }}</p>
            <p>총 계약 금액: {{ contract.contractTotalPrice }}</p>
            <p>결제 금액: {{ contract.contractPaymentAmount }}</p>
            <p>결제 빈도: {{ contract.contractPaymentFrequency }}</p>
            <p>결제 회차: {{ contract.contractPaymentMaturityInstallment }}</p>
            <p>계약 수: {{ contract.contractCount }}</p>
            <p>결제 방법: {{ contract.contractPaymentMethod }}</p>
            <p>지불자: {{ contract.contractPayer }}</p>
            <p>상담 내용: {{ contract.contractConsultation }}</p>
            <p>계약 상태: {{ contract.contractStatus }}</p>
            <p>계약 담당자: {{ contract.contractMemberName }}</p>
            <p>영업 담당자 ID: {{ contract.salesMemberId }}</p>
            <p>영업 담당자 이름: {{ contract.salesMemberName }}</p>
          </div>
          <div v-else>
            <p>계약 정보를 불러오는 중...</p>
          </div>
          <v-btn color="primary" @click="navigateToModify">수정</v-btn>
          <v-btn color="error" @click="navigateToCancel">해약</v-btn>
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
import axios from 'axios';
import { format, parseISO } from 'date-fns';
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

    const fetchContractDetail = () => {
      const contractId = route.params.id;
      

      axiosInstance.get(`http://localhost:8080/api/contract/detail/${contractId}`)
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
</style>
