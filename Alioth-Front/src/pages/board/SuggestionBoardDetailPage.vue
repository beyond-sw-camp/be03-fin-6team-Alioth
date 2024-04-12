<template>
  <AppSidebar></AppSidebar>
  <v-main>
    <div>
      <v-divider></v-divider>
      <h2>건의사항 상세조회</h2>
      <v-divider></v-divider>
      <p><button onclick="location.href=`/BoardList/SuggestionBoardModify`">건의사항 수정</button></p>
    </div>
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
