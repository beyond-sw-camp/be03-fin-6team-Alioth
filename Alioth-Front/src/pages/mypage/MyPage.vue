<template>
  <AppSidebar></AppSidebar>
  <v-main>
    <AppHeader></AppHeader>
    <v-container fluid>
      <div>
        <h1>마이페이지</h1>
      </div>
    </v-container>
  </v-main>
</template>


<script>
import AppSidebar from "@/layouts/AppSidebar.vue";
import AppHeader from "@/layouts/AppHeader.vue";

export default {
  components: {AppHeader, AppSidebar},
  // metaInfo: {
  //   title: '마이페이지'
  // },
  setup() {
    return {}
  }
}
</script>

<style scoped>

</style>
