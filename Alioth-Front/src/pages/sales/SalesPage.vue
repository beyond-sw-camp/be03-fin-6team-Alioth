<template>
  <AppSidebar></AppSidebar>
  <v-main>
    <AppHeader></AppHeader>
    <v-container fluid>
      <div>
        <v-divider></v-divider>
        <h2>매출 메인 화면</h2>
        <v-divider></v-divider>
        <p><button onclick="location.href=`/Sales/Ranking`">매출순위</button></p>
      </div>

      <v-btn-toggle v-model="selectedPeriod" mandatory>
        <v-btn :class="{ 'grey': selectedPeriod === '월' }">월</v-btn>
        <v-btn :class="{ 'grey': selectedPeriod === '반기' }">반기</v-btn>
        <v-btn :class="{ 'grey': selectedPeriod === '년' }">년</v-btn>
      </v-btn-toggle>

      <v-row class="mt-4">
        <v-col cols="12" md="5">
          <v-card class="p-20">
            <SalesPageTargetChart style="width: 80%;"></SalesPageTargetChart>
          </v-card>
        </v-col>


        <v-col cols="12" md="5">
          <v-card>
            <SalesTransChart></SalesTransChart>
          </v-card>
        </v-col>
      </v-row>

      <!-- <v-row>
        <v-col cols="12" md="5">
          <v-card>
            <SalesPagePieChart :loaded_PricePie="loaded_PricePie"></SalesPagePieChart>
          </v-card>
        </v-col>

        <v-col cols="12" md="5">
          <v-card>
            <SalesPageCountPieChart :loaded_CountPie="loaded_CountPie"></SalesPageCountPieChart>
          </v-card>
        </v-col>
      </v-row> -->

      <v-row>
        <v-col cols="12" md="10"> <!-- Changed md="5" to md="7" -->
          <SalesUserTable></SalesUserTable>
        </v-col>
      </v-row>



    </v-container>
  </v-main>
</template>

<script>
import AppSidebar from "@/layouts/AppSidebar.vue";
import AppHeader from "@/layouts/AppHeader.vue";
import SalesPagePieChart from "@/pages/sales/charts/SalesPagePieChart"
import SalesPageCountPieChart from "@/pages/sales/charts/SalesPageCountPieChart"
import SalesPageTargetChart from "@/pages/sales/charts/SalesPageTarget"
import SalesTransChart from "@/pages/sales/charts/SalesAnalytics.vue"
import SalesUserTable from "@/pages/sales/charts/AnalyticsUserTable.vue"
import ListComponent from "@/layouts/ListComponent.vue";

export default {
  components: {
    AppHeader, AppSidebar, SalesPagePieChart, SalesPageCountPieChart, SalesPageTargetChart,
    SalesTransChart, SalesUserTable, ListComponent
  },
  setup() {
    const loaded_CountPie = ref(false);
    const loaded_PricePie = ref(false);

    // 데이터 로딩 후 loaded 상태 변경
    setTimeout(() => {
      loaded_CountPie.value = true;
    }, 1000); // 예시로 setTimeout 사용, 실제 데이터 로딩 후 변경하는 로직으로 변경

    setTimeout(() => {
      loaded_PricePie.value = true;
    }, 2000);


    return {
      loaded_CountPie,
      loaded_PricePie
    }
  },
  data() {
    return {
      selectedPeriod: '월', // 초기 선택값은 월로 설정
    };
  },
  methods: {

  },
}
</script>

<style scoped>
  .grey {
    background-color: grey !important; /* 회색 배경색 */
    color: white !important; /* 흰색 텍스트색 */
  }
</style>
