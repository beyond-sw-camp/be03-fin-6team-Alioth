<template>
  <AppSidebar></AppSidebar>
  <v-main>
    <AppHeader></AppHeader>
    <v-container fluid>
      <!-- <h2>팀매출조회</h2> -->
      <v-card>
        <v-btn-toggle v-model="selectedPeriod" mandatory>
            <v-btn @click="changePeriod('월')" :class="{ 'grey': selectedPeriod === '월' }">월</v-btn>
            <v-btn @click="changePeriod('반기')" :class="{ 'grey': selectedPeriod === '반기' }">반기</v-btn>
            <v-btn @click="changePeriod('년')" :class="{ 'grey': selectedPeriod === '년' }">년</v-btn>
        </v-btn-toggle>

        <v-card flat>
          <v-row align="center">
            <v-col cols="8"/>
            <v-col cols="2">
              <v-text-field style="margin-bottom: 15px; margin-left: 15px; margin-top: 15px;"
                v-model="search"
                label="Search"
                prepend-inner-icon="mdi-magnify"
                variant="outlined"
                dense>
              </v-text-field>
            </v-col>

            <v-col cols="2">
              <v-btn
                color="grey"
                text
                @click="downloadExcel">
                엑셀다운로드
              </v-btn>
            </v-col>
          </v-row>
          <ListComponent :columns="tableColumns" :rows="tableRows"/>
        </v-card>
      </v-card>
    </v-container>
  </v-main>
</template>

<script>
import AppSidebar from "@/layouts/AppSidebar.vue";
import AppHeader from "@/layouts/AppHeader.vue";
import SalesTeamTableChart from "@/pages/sales/charts/SalesTeamTableChart"
import { useSalesStore } from '@/stores/SalesStore';
import ListComponent from "@/layouts/ListComponent.vue";
import {ref, onMounted, watch} from 'vue';

export default {
  components: {
    AppHeader, AppSidebar, SalesTeamTableChart, ListComponent
  },
  setup() {
    const tableColumns = ref([
      {title: "No", key: "id"},
      {title: "고객", key: "customName"},
      {title: "계약 기간(년)", key: "contractPeriod"},
      {title: '계약일자', key: 'contractDate'},
      {title: '계약만료일자', key: 'contractExpireDate'},
      {title: '계약상태', key: 'contractStatus'}
    ]);
    const tableRows = ref([]);
    const search = ref('');

    return {
      tableColumns,
      tableRows,
      search,
    }
  },
  data() {
    return {
      selectedPeriod: '월', // 초기 선택값은 월로 설정
      salesStore: useSalesStore(),
    };
  },
  methods: {
    changePeriod(period) {
      this.salesStore.salesTeam = period;
    },
  },
}
</script>

<style scoped>
  .grey {
    background-color: grey !important; /* 회색 배경색 */
    color: white !important; /* 흰색 텍스트색 */
  }
</style>
