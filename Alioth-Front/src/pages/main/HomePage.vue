<template>
  <AppSidebar></AppSidebar>
  <v-main>
    <AppHeader></AppHeader>
    <v-container fluid>
      <div>
        <h1>메인</h1>
      </div>
    </v-container>
  </v-main>
</template>

<script>
import AppHeader from "@/layouts/AppHeader.vue";
import AppSidebar from "@/layouts/AppSidebar.vue";

export default {
  components: {AppSidebar, AppHeader},
  setup() {


    return {}
  }
}
</script>

<style scoped>

</style>

