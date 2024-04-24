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
      <div class="d-flex">
        <SalesPagePieChart></SalesPagePieChart>
        <SalesPageCountPieChart></SalesPageCountPieChart>
      </div>
      


    </v-container>
  </v-main>
</template>

<script>
import AppSidebar from "@/layouts/AppSidebar.vue";
import AppHeader from "@/layouts/AppHeader.vue";
import SalesPagePieChart from "@/pages/sales/charts/SalesPagePieChart"
import SalesPageCountPieChart from "@/pages/sales/charts/SalesPageCountPieChart"

export default {
  components: {AppHeader, AppSidebar, SalesPagePieChart, SalesPageCountPieChart},
  setup() {

    return {}
  }
}
</script>

<style scoped>

</style>
