<template>
  <AppSidebar></AppSidebar>
  <v-main>
    <AppHeader></AppHeader>
    <v-container fluid>
      <div>
        <p>계약 상세 조회 페이지</p>
        <p><button onclick="location.href=`/ContractList/AddModify`">수정</button></p>
        <p><button onclick="location.href=`/ContractList/Cancel`">해약</button></p>
      </div>
    </v-container>
  </v-main>
</template>

<script>
import AppSidebar from "@/layouts/AppSidebar.vue";
import AppHeader from "@/layouts/AppHeader.vue";

export default {
  components: {AppHeader, AppSidebar},
  setup() {


    return {}
  }
}
</script>

<style scoped>

</style>
