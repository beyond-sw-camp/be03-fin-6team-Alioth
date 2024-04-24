<template>
  <AppSidebar></AppSidebar>
  <v-main>
    <AppHeader></AppHeader>
    <v-container fluid>
      <h2>개인 및 팀 매출</h2>
      <v-divider></v-divider>

      <v-toolbar flat>
      </v-toolbar>
      <div>
        <h2>개인 일일 순위</h2>
        <ListComponent 
          :columns="headers"
          :rows="formattedItems"
        />
      </div>

      <v-toolbar flat/>

      <div>
        <h2>팀 일일 순위</h2>
        <ListComponent 
          :columns="teamHeaders"
          :rows="formattedTeamItems"
        />
      </div>


    </v-container>
  </v-main>
</template>

<script>
import { useRouter } from 'vue-router';
import AppSidebar from "@/layouts/AppSidebar.vue";
import AppHeader from "@/layouts/AppHeader.vue";
import ListComponent from "@/layouts/ListComponent.vue"; // ListComponent를 임포트하세요.
import axios from 'axios';

export default {
  components: {AppHeader, AppSidebar, ListComponent},
  setup() {
    const router = useRouter();
    return { router };
  },
  data() {
    return {
      items: [],
      headers: [
        { title: '사원 이름', key: 'salesMemberName' },
        { title: '사원 코드', key: 'salesMemberCode' },
        { title: '계약 총금액', key: 'contractPrice' },
        { title: '계약 건수', key: 'contractCount' },
      ],
      teamItems: [],
      teamHeaders: [
        { title: '팀 이름', key: 'teamName' },
        { title: '팀 코드', key: 'teamCode' },
        { title: '계약 총금액', key: 'contractPrice' },
        { title: '계약 건수', key: 'contractCount' },
      ],
    }
  },
  mounted() {
    this.getSalesMemberData();
    this.getSalesTeamData();
  },
  computed: {
    formattedItems() {
      return this.items.map(item => ({
        ...item
      }));
    },
    formattedTeamItems() {
      return this.teamItems.map(item => ({
        ...item
      }));
    }
  },
  methods: {
    getSalesMemberData() {
      axios.get("http://localhost:8081/api/batch/sales-member/day")
            .then(response => {
              console.log("SalesRanking 응답결과 : ");
              // console.log(response.data.result);
              this.items = response.data.result || [];
              console.log(this.items);
            })
            .catch(error => {
              console.log("요청할 수 없습니다.1s : ", error);
            });
    },
    getSalesTeamData() {
      axios.get("http://localhost:8081/api/batch/sales-team/day")
            .then(response => {
              console.log("SalesRanking 응답결과 : ");
              // console.log(response.data.result);
              this.teamItems = response.data.result || [];
              console.log(this.items);
            })
            .catch(error => {
              console.log("요청할 수 없습니다.1s : ", error);
            });
    },
  },
}
</script>

<style scoped>

</style>
