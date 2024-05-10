<template>
  <AppSidebar></AppSidebar>
  <v-main>
    <div>
      <v-divider></v-divider>
      <h2>건의사항 상세조회</h2>
      <v-divider></v-divider>
      <v-btn variant="tonal" color="#2979FF" onclick="location.href=`/BoardList/SuggestionBoardModify`">건의사항 수정</v-btn>
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
