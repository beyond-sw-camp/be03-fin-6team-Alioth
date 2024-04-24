<template>
  <AppSidebar></AppSidebar>
  <v-main>
    <AppHeader></AppHeader>
    <v-container fluid>
      <h2>전사매출조회</h2>
      <v-divider></v-divider>
      <v-btn-toggle v-model="selectedPeriod" mandatory>
          <v-btn @click="changePeriod('월')" :class="{ 'grey': selectedPeriod === '월' }">월</v-btn>
          <v-btn @click="changePeriod('반기')" :class="{ 'grey': selectedPeriod === '반기' }">반기</v-btn>
          <v-btn @click="changePeriod('년')" :class="{ 'grey': selectedPeriod === '년' }">년</v-btn>
      </v-btn-toggle>
      <SalesTotalTableChart></SalesTotalTableChart>

    </v-container>
  </v-main>
</template>

<script>
import AppSidebar from "@/layouts/AppSidebar.vue";
import AppHeader from "@/layouts/AppHeader.vue";
import SalesTotalTableChart from "@/pages/sales/charts/SalesTotalTableChart"
import { useSalesStore } from '@/stores/SalesStore';

export default {
  components: {AppHeader, AppSidebar, SalesTotalTableChart},
  setup() {


    return {

    }
  },
  data() {
    return {
      selectedPeriod: '월', // 초기 선택값은 월로 설정
      salesStore: useSalesStore(),
    };
  },
  methods: {
    changePeriod(period) {
      this.salesStore.salesHQ = period;
    },
  },
}
</script>

<style scoped>
  .grey {
    background-color: grey !important; /* 회색 배경색 */
    color: white !important; /* 흰색 텍스트색 */
  }
</style>
