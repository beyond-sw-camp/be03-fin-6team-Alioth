<template>
  <AppSidebar></AppSidebar>
  <v-main>
    <AppHeader></AppHeader>
    <v-container fluid>
      <div class="d-flex justify-space-between align-center">
        <h2>{{ contract.insuranceProductName }}</h2>
        <v-btn color="red" @click="cancelContract">해약 완료</v-btn>
      </div>
      <v-divider></v-divider>
      <div class="d-flex justify-space-between align-center my-4">
        <h3>{{ contract.customName }}</h3>
        <div>
          <h3>담당 직원: {{ contract.salesMemberName }}</h3>
        </div>
      </div>
      <v-divider></v-divider>
      <v-card class="mt-4">
        <v-card-title>계약 상세 정보</v-card-title>
        <v-card-text>
          <v-container>
            <v-row>
              <v-col cols="12" md="6">
                <v-text-field
                  label="청구일"
                  v-model="billingDate"
                  readonly
                ></v-text-field>
                <v-text-field
                  label="지급일"
                  v-model="paymentDate"
                  readonly
                ></v-text-field>
              </v-col>
              <v-col cols="12" md="6">
                <v-textarea
                  label="해약 사유"
                  v-model="cancellationReason"
                  rows="5"
                  auto-grow
                ></v-textarea>
              </v-col>
            </v-row>
            <v-btn @click="fetchRefundInfo">조회</v-btn>
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
    </v-container>
  </v-main>
</template>
<script>
import AppSidebar from "@/layouts/AppSidebar.vue";
import AppHeader from "@/layouts/AppHeader.vue";
import axios from 'axios';
import { ref, onMounted } from "vue";
import { useRouter, useRoute } from 'vue-router';
import axiosInstance from '@/plugins/loginaxios';

export default {
  components: { AppHeader, AppSidebar },
  setup() {
    const route = useRoute();
    const router = useRouter();
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
      axiosInstance.get(`http://localhost:8080/api/contract/detail/${contractId}`)
        .then(response => {
        contract.value = response.data.result;
        refundDetails.value.monthlyPremium = parseInt(contract.value.contractPaymentAmount).toLocaleString() + "원";
        calculateRefundDetails(new Date(contract.value.contractDate), new Date());
      }).catch(error => {
        console.error('Failed to fetch contract details:', error);
      });
    };

    const cancelContract = () => {
      axiosInstance.post(`http://localhost:8080/api/contract/cancel/${contractId}`), {
        reason: cancellationReason.value
      }
      .then(() => {
        alert("해약이 완료되었습니다.");
        router.push('/ContractList');
      }).catch(error => {
        console.error('Failed to cancel contract:', error);
        alert("해약 처리에 실패했습니다.");
      });
    };

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
      showRefundDetails,
      refundDetails
    };
  }
};
</script>

<style scoped>
</style>
