<template>
  <AppSidebar></AppSidebar>
  <v-container fluid>
    <v-main>
      <AppHeader></AppHeader>
      <v-card flat style="margin-bottom: 1vw">
        <v-row>
          <v-col cols="4" class="d-flex align-center">
            <v-card-title class="mr-2 custom-font">상품명</v-card-title>
            <div class="custom-font" >{{ contract.insuranceProductName }}</div>
            </v-col>
          <v-col cols="4" class="d-flex align-center">
            <v-card-title class="mr-2 custom-font">고객</v-card-title>
            <div class="custom-font">{{ contract.customName }}</div>
          </v-col>
          <v-col cols="4" class="d-flex align-center">
            <v-card-title class="mr-2 custom-font">담당 직원</v-card-title>
            <div class="custom-font">{{ contract.salesMemberName }}</div>
          </v-col>
        </v-row>
      </v-card>
      <v-divider></v-divider>
      <v-card class="mt-4">
        <v-row>
          <v-col>
            <v-card-title class="customed-font">계약 상세 정보</v-card-title>
          </v-col>
        </v-row>
        <v-card-text>
          <v-container>
            <v-row>
              <v-col cols="12" md="6">
                <v-text-field label="청구일" v-model="billingDate" readonly></v-text-field>
                <span> </span>
                <v-text-field label="지급일" v-model="paymentDate" readonly ></v-text-field>
              </v-col>
              <v-col cols="12" md="6">
                <v-textarea label="해약 사유" v-model="cancellationReason" rows="3.6" auto-grow></v-textarea>
              </v-col>
            </v-row>
            <v-row>
              <v-col>
                <v-btn color="#1A237E" variant="tonal" @click="fetchRefundInfo">환급 정보 조회</v-btn>
              </v-col>
              <v-col class="text-right">
                <v-btn color="primary" variant="tonal" style="margin-bottom: 0.25vw" @click="cancelContract">해약 완료</v-btn>
              </v-col>
            </v-row>
            <v-expand-transition>
              <div v-show="showRefundDetails">
                <v-card class="mt-4">
                  <v-card-title>환급 정보</v-card-title>
                  <v-card-text>
                    <p>해약 환급금: {{ refundDetails.refundAmount }}</p>
                    <p>월 보험료: {{ refundDetails.monthlyPremium }}</p>
                    <p>총 납입 보험료: {{ refundDetails.totalPremium }}</p>
                    <p>실지급액: {{ refundDetails.actualPayment }}</p>
                  </v-card-text>
                </v-card>
              </div>
            </v-expand-transition>
          </v-container>
        </v-card-text>
      </v-card>
      <v-btn color="#2C3E50" variant="tonal" style="margin-top: 1vw" @click="goBack">목록으로</v-btn>
    </v-main>
  </v-container>
</template>
<script>
import AppSidebar from "@/layouts/AppSidebar.vue";
import AppHeader from "@/layouts/AppHeader.vue";
import { ref, onMounted } from "vue";
import { useRouter, useRoute } from 'vue-router';
import axiosInstance from '@/plugins/loginaxios';

export default {
  components: { AppHeader, AppSidebar },
  setup() {
    const route = useRoute();
    const router = useRouter();
    const baseUrl = import.meta.env.VITE_API_SERVER_BASE_URL
    const contractId = route.params.id;
    const contract = ref({});
    const billingDate = ref(new Date().toISOString().substr(0, 10));
    const paymentDate = ref(new Date().toISOString().substr(0, 10));
    const cancellationReason = ref("");
    const showRefundDetails = ref(false);
    const refundDetails = ref({
      refundAmount: "0원",
      monthlyPremium: "0원",
      totalPremium: "0원",
      actualPayment: "0원"
    });


    const loadContractDetails = () => {
      axiosInstance.get(`${baseUrl}/api/contract/detail/${contractId}`)
        .then(response => {
          contract.value = response.data.result;
          refundDetails.value.monthlyPremium = parseInt(contract.value.contractPaymentAmount).toLocaleString() + "원";
          calculateRefundDetails(new Date(contract.value.contractDate), new Date());
        }).catch(error => {
        console.error('Failed to fetch contract details:', error);
      });
    };

    const cancelContract = () => {
      axiosInstance.post(`${baseUrl}/api/contract/cancel/${contractId}`, { reason: cancellationReason.value })
        .then(() => {
          alert("해약이 완료되었습니다.");
          router.push('/ContractList');
        }).catch(error => {
        console.error('Failed to cancel contract:', error);
        alert("해약 처리에 실패했습니다.");
      });
    };

    function goBack() {
      router.push('/ContractList');
    }

    const fetchRefundInfo = () => {
      showRefundDetails.value = true;
    };

    const calculateRefundDetails = (startDate, endDate) => {
      const monthDifference = ((endDate.getFullYear() - startDate.getFullYear()) * 12) + (endDate.getMonth() - startDate.getMonth());
      const monthlyPremium = parseInt(contract.value.contractPaymentAmount);
      const totalPremium = monthlyPremium * monthDifference;
      refundDetails.value.totalPremium = `${totalPremium.toLocaleString()}원`;
      refundDetails.value.refundAmount = `${totalPremium.toLocaleString()}원`;
      refundDetails.value.actualPayment = `${totalPremium.toLocaleString()}원`;
    };

    onMounted(loadContractDetails);

    return {
      contract,
      billingDate,
      paymentDate,
      cancellationReason,
      cancelContract,
      fetchRefundInfo,
      goBack,
      showRefundDetails,
      refundDetails
    };
  }
};
</script>


<style scoped>
.customed-font {
  font-family: "Spoqa Han Sans Neo", sans-serif;
}
</style>
