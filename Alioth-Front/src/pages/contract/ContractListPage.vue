<template>
  <AppSidebar></AppSidebar>
  <v-container fluid>
    <v-main>
      <AppHeader></AppHeader>
      <v-card style="margin-top: 10px;">
        <v-row align="center">
          <v-col cols="4" class="pa-2 ma-2">
            <v-text-field style="margin-bottom: 15px; margin-left: 15px; margin-top: 15px;"
                          v-model="search"
                          label="Search"
                          prepend-inner-icon="mdi-magnify"
                          variant="outlined"
                          dense>
            </v-text-field>
          </v-col>
          <v-col cols="2">
            <v-select
              v-if="loginStore.memberRank==='HQ'"
              v-model="selectedStatus"
              :items="statusOptions"
              item-title="key"
              item-value="val"
              label="Team Code"
              outlined
              dense>
            </v-select>
          </v-col>

          <v-col cols="2">
            <v-select
              v-if="loginStore.memberRank==='MANAGER' || loginStore.memberRank==='HQ'"
              v-model="selectedSMmember"
              :items="salesMemberOptions"
              label="Sales Member"
              item-title="key"
              item-value="val"
              outlined
              dense>
            </v-select>
          </v-col>

          <v-col class="text-right">
            <v-btn variant="tonal" color="#2979FF" @click="navigateToAddModify" class="button-margin">계약 추가</v-btn>
            <v-btn variant="tonal" color="#558B2F" @click="downloadExcel" style="margin-right: 1vw;">엑셀다운로드</v-btn>
          </v-col>
        </v-row>

        <v-divider></v-divider>
        <v-spacer></v-spacer>
        <v-spacer></v-spacer>
        <ListComponent :columns="tableColumns" :rows="tableRows" @click:row="navigateToDetail"/>
      </v-card>
    </v-main>
  </v-container>
</template>


<script>
import AppSidebar from "@/layouts/AppSidebar.vue";
import AppHeader from "@/layouts/AppHeader.vue";
import ListComponent from "@/layouts/ListComponent.vue";
import {ref, onMounted, watch} from 'vue';
import router from "@/router";
import axiosInstance from '@/plugins/loginaxios';
import {useLoginInfoStore} from "@/stores/loginInfo";

export default {
  components: {AppSidebar, AppHeader, ListComponent},
  setup() {
    const loginStore = useLoginInfoStore();
    const tableColumns = ref([
      {title: "No", key: "id"},
      {title: "보험상품", key: "insuranceProductName"},
      {title: "고객", key: "customName"},
      {title: "팀", key: "salesMemberResDto.teamName"},
      {title: "계약사원", key: "salesMemberName"},
      {title: "계약 기간(년)", key: "contractPeriod"},
      {title: '계약일자', key: 'contractDate'},
      {title: '계약만료일자', key: 'contractExpireDate'},
      {title: '계약상태', key: 'contractStatus'}
    ]);
    const tableRows = ref([]);
    const search = ref('');

    const selectedStatus = ref(null)

    let statusOptions = ref();

    const selectedSMmember = ref(null);

    let salesMemberOptions = ref();

    const fetchData = () => {
      const baseUrl = import.meta.env.VITE_API_SERVER_BASE_URL || 'http://localhost:8080';
      axiosInstance.get(`${baseUrl}/api/contract/list`)
        .then(response => {
          let data = response.data.result;
          statusOptions.value = [{key: "ALL", val: null}];
          salesMemberOptions.value = [{key: "ALL", val: null}];

          if (useLoginInfoStore().memberRank === 'HQ') {
            const newStatusOptions = response.data.result
              .map(contract => ({
                'key': contract.salesMemberResDto.teamName,
                'val': contract.salesMemberResDto.teamCode
              }));
            const uniqueStatusOptions = Array.from(new Set(newStatusOptions.map(JSON.stringify))).map(JSON.parse);
            statusOptions.value = [
              ...statusOptions.value,
              ...uniqueStatusOptions
            ];
            const newSalesMemberOptions = response.data.result
              .filter(contract => contract.salesMemberResDto.teamCode === selectedStatus.value)
              .map(contract => ({
                'key': contract.salesMemberResDto.name,
                'val': contract.salesMemberResDto.salesMemberCode
              }))
            const uniqueSalesMemberOptions = Array.from(new Set(newSalesMemberOptions.map(JSON.stringify))).map(JSON.parse);

            salesMemberOptions.value = [
              ...salesMemberOptions.value,
              ...uniqueSalesMemberOptions
            ];
          }

          if (useLoginInfoStore().memberRank === 'MANAGER') {
            const newSalesMemberOptions = response.data.result
              .filter(contract => contract.salesMemberResDto.teamCode === useLoginInfoStore().memberTeamCode)
              .map(contract => ({
                'key': contract.salesMemberResDto.name,
                'val': contract.salesMemberResDto.salesMemberCode
              }));
            const uniqueSalesMemberOptions = Array.from(new Set(newSalesMemberOptions.map(JSON.stringify))).map(JSON.parse);

            salesMemberOptions.value = [
              ...salesMemberOptions.value,
              ...uniqueSalesMemberOptions
            ];

            data = data.filter(contract => contract.salesMemberResDto.teamCode === useLoginInfoStore().memberTeamCode);
          }

          // console.log(response.data.result)
          if(useLoginInfoStore().memberRank === 'FP'){
            data = data.filter(contract => contract.salesMemberResDto.salesMemberCode === useLoginInfoStore().getMemberCode);
          }

          if (selectedStatus.value !== null) {
            data = data.filter(contract => contract.salesMemberResDto.teamCode === selectedStatus.value);
          }

          if (selectedSMmember.value !== null) {
            data = data.filter(contract => contract.salesMemberResDto.salesMemberCode === selectedSMmember.value);
          }
          tableRows.value = data.map((item, index) => ({
            ...item,
            id: index + 1,
            contractDate: item.contractDate ? new Date(item.contractDate).toLocaleString() : 'N/A',
            contractExpireDate: item.contractExpireDate ? new Date(item.contractExpireDate).toLocaleString() : 'N/A',
          }));
        })
        .catch(error => {
          console.error('Error fetching data:', error);
        });
    };


    const downloadExcel = () => {
      const baseUrl = import.meta.env.VITE_API_SERVER_BASE_URL || 'http://localhost:8080';
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

      axiosInstance.post(url, requestData, {
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
    };

    const navigateToAddModify = () => {
      router.push('/ContractList/AddModify');
    };

    const navigateToDetail = (event, {item}) => {
      console.log(item)
      router.push(`/ContractList/Detail/${item.contractId}`);
    };

    watch(selectedStatus, () => {
      selectedSMmember.value = null;
      fetchData();
    });

    watch(selectedSMmember, () => {
      fetchData();
    });

    onMounted(() => {
      fetchData();
    });


    return {
      tableColumns,
      tableRows,
      search,
      selectedStatus,
      statusOptions,
      selectedSMmember,
      salesMemberOptions,
      fetchData,
      loginStore,
      navigateToAddModify,
      navigateToDetail,
      downloadExcel
    };
  }
};
</script>


<style scoped>
.v-text-field, .v-select {
  height: 50px;
}

.d-flex.justify-end {
  display: flex;
  justify-content: flex-end;
}
.button-margin {
  margin-right: 10px; /* 원하는 간격 값으로 조정하세요 */
}
</style>
