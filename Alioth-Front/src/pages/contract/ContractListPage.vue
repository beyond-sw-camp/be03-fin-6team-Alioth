<template>
  <AppSidebar></AppSidebar>
  <v-main>
    <AppHeader></AppHeader>
      <v-divider></v-divider>
      <h2>계약목록조회</h2>
      <v-divider></v-divider>
      <p><button @click="navigateToAddModify">계약 추가</button></p>
      <v-data-table
        :headers="headers"
        :items="contracts"
        class="elevation-1"
        @click:row="navigateToDetail"
      >
      </v-data-table>
  </v-main>
</template>

<script>
import AppSidebar from "@/layouts/AppSidebar.vue";
import AppHeader from "@/layouts/AppHeader.vue";
import { useRouter } from 'vue-router';

export default {
  components: { AppHeader, AppSidebar },
  setup() {
    const router = useRouter();

    function navigateToAddModify() {
      router.push('/ContractList/AddModify');
    }

    function navigateToDetail(item) {
      router.push({ path: '/ContractList/Detail', query: { id: item.id }});
    }

    return {
      navigateToAddModify,
      navigateToDetail
    }
  },
  data() {
    return {
      headers: [
        { text: '보험상품', value: 'insuranceProduct' },
        { text: '고객', value: 'customer' },
        { text: '사원', value: 'employee' },
        { text: '계약일자', value: 'contractDate' }
      ],
      contracts: [
        { id: 1, insuranceProduct: '아무곳이나클릭해주세요보험', customer: '희준곤쥬', employee: '조사원', contractDate: '2023-01-01' }
      ],
    };
  },
};
</script>

<style scoped>

</style>
