<template>
    <AppSidebar></AppSidebar>
    <v-container fluid>
    <v-main>
      <AppHeader></AppHeader>

        <v-row align="center">
          <v-col cols="2">
            <h2>매출 순위</h2>
            <v-btn class="mt-1" @click="showDatePickerDialog">
                <v-icon left>mdi-calendar</v-icon> <!-- 날짜 아이콘 -->
                날짜 선택
            </v-btn>

          </v-col>

          <v-col cols="12">
              <!-- <v-switch v-model="model"
                        :label="model === '개인' ? '개인' : '팀'"
                        :color="model === '개인' ? 'success' : 'info'"
                        :false-value="'개인'"
                        :true-value="'팀'"
                        hide-details
                        @change="fetchData">
              </v-switch> -->
              <v-text-field v-model="startDate" class="mt-3"></v-text-field>
              <v-dialog v-model="datePickerDialog" persistent max-width="300px">
                <v-card>
                  <v-card-title>날짜 선택</v-card-title>
                  <v-card-text>
                    <v-row>
                      <v-col cols="12">
                        <v-text-field v-model="startDate" label="선택 날짜" type="date"></v-text-field>
                      </v-col>
                    </v-row>
                  </v-card-text>
                  <v-card-actions>
                    <v-btn color="primary" @click="applyDate">적용</v-btn>
                    <v-btn color="secondary" @click="cancelDateRange">취소</v-btn>
                  </v-card-actions>
                </v-card>
              </v-dialog>
          </v-col>
        </v-row>
        <v-divider></v-divider>

        <!-- ListComponent에 데이터를 표시하는 부분 -->
        <ListComponent
          v-if="model === '개인'"
          :columns="headers"
          :rows="formattedItems"
        />
        <!-- <ListComponent
          v-if="model === '개인'"
          :columns="teamHeaders"
          :rows="formattedTeamItems"
        /> -->

        <v-row class="mt-10">
          <v-col cols="12" md="6">
            <v-card>
              <SalesPagePieChart :loaded_PricePie="loaded_PricePie"></SalesPagePieChart>
            </v-card>
          </v-col>

          <v-col cols="12" md="6">
            <v-card>
              <SalesPageCountPieChart :loaded_CountPie="loaded_CountPie"></SalesPageCountPieChart>
            </v-card>
          </v-col>
      </v-row>

      </v-main>
    </v-container>

</template>

<script>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import AppSidebar from "@/layouts/AppSidebar.vue";
import AppHeader from "@/layouts/AppHeader.vue";
import ListComponent from "@/layouts/ListComponent.vue";
import SalesPagePieChart from "@/pages/sales/charts/SalesPagePieChart"
import SalesPageCountPieChart from "@/pages/sales/charts/SalesPageCountPieChart"
import { useSalesRankingStore } from '@/stores/SalesRankingStore';
import axios from 'axios';

export default {
  components: {
    AppHeader, AppSidebar, ListComponent,
    SalesPageCountPieChart, SalesPagePieChart,
  },
  setup() {
    const router = useRouter();
    const model = ref('개인'); // 리액티브 변수로 선언
    let datePickerDialog = ref(false);
    let startDate = ref(null);
    let endDate = ref(null);
    const loaded_CountPie = ref(true);
    const loaded_PricePie = ref(true);

    // 데이터 로딩 후 loaded 상태 변경
    setTimeout(() => {
      loaded_CountPie.value = true;
    }, 1000); // 예시로 setTimeout 사용, 실제 데이터 로딩 후 변경하는 로직으로 변경

    setTimeout(() => {
      loaded_PricePie.value = true;
    }, 1500);

    const showDatePickerDialog = () => {
      datePickerDialog.value = true;
    };

    const applyDateRange = () => {
      // 날짜 범위 적용 로직 추가
      console.log("시작 날짜:", startDate.value);
      // fetchData 함수 호출 또는 직접 데이터를 업데이트하는 로직 추가
      let url = `http://localhost:8081/api/stat/sales-ranking/member/${startDate.value}`;
      console.log(url);
      axios.get(url)
        .then(response => {
          console.log("SalesRanking 응답결과 : ", response);

          console.log("this.items : ", items);
          items = response.data.result || [];
          items = items.map((items, index) => ({
            ...items,
            id: index + 1,
          }));
          console.log(this.items);
        })
        .catch(error => {
          console.log("요청할 수 없습니다. : ", error);
        });

      datePickerDialog.value = false; // 모달 닫기
    };

    const cancelDateRange = () => {
      datePickerDialog.value = false; // 모달 닫기
    };


    return {
      router, model, datePickerDialog, startDate, endDate,
      showDatePickerDialog, applyDateRange, cancelDateRange,
      loaded_CountPie,
    };
  },
  data() {
    return {
      items: [],
      headers: [
        { title: 'No', key: 'id' },
        { title: '사원 이름', key: 'salesMemberName' },
        { title: '사원 코드', key: 'salesMemberCode' },
        { title: '계약 총금액', key: 'contractPrice' },
        { title: '계약 건수', key: 'contractCount' },
      ],
      teamItems: [],
      teamHeaders: [
        { title: 'No', key: 'id' },
        { title: '팀 이름', key: 'teamName' },
        { title: '팀 코드', key: 'teamCode' },
        { title: '계약 총금액', key: 'contractPrice' },
        { title: '계약 건수', key: 'contractCount' },
      ],
      memberCount: 0,
      teamCount: 0,
    }
  },
  mounted() {
    this.fetchData();
  },
  computed: {
    formattedItems() {
      return this.items.map(item => ({ ...item }));
    },
    formattedTeamItems() {
      return this.teamItems.map(item => ({ ...item }));
    }
  },
  methods: {
    fetchData() {
      if (this.model === '개인') {
        this.getSalesMemberData();
      } else {
        this.getSalesTeamData();
      }
    },
    applyDate() {
      // 날짜 범위 적용 로직 추가
      console.log("시작 날짜:", this.startDate);
      useSalesRankingStore().startDate = this.startDate;
      // fetchData 함수 호출 또는 직접 데이터를 업데이트하는 로직 추가
      let url = `http://localhost:8081/api/stat/sales-ranking/member/${this.startDate}`;
      console.log(url);
      axios.get(url)
        .then(response => {
          console.log("SalesRanking 응답결과 : ", response);

          this.items = response.data.result || [];
          this.items = this.items.map((items, index) => ({
            ...items,
            id: index + 1,
          }));
          console.log(this.items);
        })
        .catch(error => {
          console.log("요청할 수 없습니다. : ", error);
        });
      this.startDate && (this.loaded_CountPie = false);


      this.datePickerDialog = !this.datePickerDialog; // 모달 닫기
    },
    getSalesMemberData() {
      const baseUrl = import.meta.env.VITE_API_STATISTICS_BASE_URL
      let url = `${baseUrl}/api/stat/sales-ranking/member`;
      axios.get(url)
        .then(response => {
          console.log("SalesRanking 응답결과 : ");
          this.items = response.data.result || [];
          this.items = this.items.map((items, index) => ({
            ...items,
            id: index + 1,
          }));
          console.log(this.items);
        })
        .catch(error => {
          console.log("요청할 수 없습니다. : ", error);
        });
    },
    getSalesTeamData() {
      const baseUrl = import.meta.env.VITE_API_STATISTICS_BASE_URL
      axios.get(`${baseUrl}/api/batch/sales-team/day`)
        .then(response => {
          console.log("SalesRanking 응답결과 : ");
          //this.teamItems = response.data.result || [];

          this.teamItems = response.data.result || [];
          this.teamItems = this.teamItems.map((teamItems, index) => ({
            ...teamItems,
            id: index + 1,
          }));
          console.log(this.teamItems);
        })
        .catch(error => {
          console.log("요청할 수 없습니다. : ", error);
        });
    },
  },
}
</script>

<style scoped>

</style>
