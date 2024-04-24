<template>
  <AppSidebar></AppSidebar>
  <v-main>
    <AppHeader></AppHeader>
    <v-container fluid>
      <div>
        <h1>개인매출조회</h1>
        <v-btn-toggle v-model="selectedPeriod" mandatory>
          <v-btn @click="changePeriod('월')" :class="{ 'grey': selectedPeriod === '월' }">월</v-btn>
          <v-btn @click="changePeriod('반기')" :class="{ 'grey': selectedPeriod === '반기' }">반기</v-btn>
          <v-btn @click="changePeriod('년')" :class="{ 'grey': selectedPeriod === '년' }">년</v-btn>
        </v-btn-toggle>
      </div>
      <SalesPersonalTableChart></SalesPersonalTableChart>

    </v-container>
  </v-main>
</template>

<script>
import AppSidebar from "@/layouts/AppSidebar.vue";
import AppHeader from "@/layouts/AppHeader.vue";
import SalesPersonalTableChart from "@/pages/sales/charts/SalesPersonalTableChart"
import { useSalesStore } from '@/stores/SalesStore';


export default {
  components: {AppHeader, AppSidebar, SalesPersonalTableChart},
  setup() {


    return {}
  },
  data() {
    return {
      selectedPeriod: '월', // 초기 선택값은 월로 설정
      salesStore: useSalesStore(),
    };
  },
  methods: {
    changePeriod(period) {
      this.salesStore.salesPersonal = period; // 선택된 기간을 업데이트
    }
  },
}
</script>

<style scoped>
  .grey {
    background-color: grey !important; /* 회색 배경색 */
    color: white !important; /* 흰색 텍스트색 */
  }
</style>
