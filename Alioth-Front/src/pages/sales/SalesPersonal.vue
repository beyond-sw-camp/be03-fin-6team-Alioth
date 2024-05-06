<template>
  <AppSidebar></AppSidebar>
  <v-main>
    <AppHeader></AppHeader>
    <v-container fluid>
      <v-card>
        <!-- <h1>개인매출조회</h1> -->
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
import SalesPersonalTableChart from "@/pages/sales/charts/SalesPersonalTableChart"
import { useSalesStore } from '@/stores/SalesStore';
import axios from "axios";
import ListComponent from "@/layouts/ListComponent.vue";
import {ref, onMounted, watch} from 'vue';



export default {
  components: {
    AppHeader, AppSidebar, SalesPersonalTableChart, ListComponent
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
      this.salesStore.salesPersonal = period; // 선택된 기간을 업데이트
    },
    downloadExcel() {
      //const baseUrl = process.env.VUE_APP_API_BASE_URL || 'http://localhost:8081';
      const baseUrl = 'http://localhost:8081';
      const requestData = {
        startDate: null,
        endDate: null
      };
      let url = null
      if (useLoginInfoStore().memberRank === 'HQ') {
        if(selectedStatus.value === null && selectedSMmember.value === null){
          url = `${baseUrl}/api/excel/export/contract`
        }else if(selectedStatus.value !== null && selectedSMmember.value === null){
          url = `${baseUrl}/api/excel/export/contract/${selectedStatus.value}`
        }else if(selectedStatus.value !== null && selectedSMmember.value !== null){
          url = `${baseUrl}/api/excel/export/contract/${selectedSMmember.value}`
        }
      }

      if (useLoginInfoStore().memberRank === 'MANAGER') {
        if(selectedSMmember.value === null){
          url = `${baseUrl}/api/excel/export/contract`
        }else {
          url = `${baseUrl}/api/excel/export/contract/${selectedSMmember.value}`
        }
      }

      if (useLoginInfoStore().memberRank === 'FP') {
        url = `${baseUrl}/api/excel/export/contract`
      }

      axios.post(url, requestData, {
        responseType: 'blob',
        headers: {
          'Content-Type': 'application/json'
        }
      })
        .then(response => {
          const url = window.URL.createObjectURL(new Blob([response.data]));
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', 'ContractList.xlsx');
          document.body.appendChild(link);
          link.click();
          window.URL.revokeObjectURL(url);
        })
        .catch(error => {
          console.error('Error downloading the file:', error.response);
        });
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
