<template>
  <AppSidebar></AppSidebar>
  <v-container fluid>
    <v-main>
      <AppHeader></AppHeader>
      <div>
        <v-divider></v-divider>
        <v-col align="center" class="text-center">
          <v-btn variant="tonal" color="#1A237E" onclick="location.href=`/Sales/Ranking`" style="margin-right: 1vw">
            매출순위
          </v-btn>
        </v-col>
      </div>
      <v-row align="center">
        <v-col cols="2">
          <v-btn class="mt-1" @click="showDatePickerDialog">
            <v-icon left>mdi-calendar</v-icon>
            날짜 선택
          </v-btn>
        </v-col>

        <v-col cols="12">
          <!-- <v-text-field v-model="startDate" class="mt-3" readonly></v-text-field> -->
          <v-dialog v-model="datePickerDialog" persistent max-width="300px">
            <v-card>
              <v-card-title>날짜 선택</v-card-title>

              <v-card-text>
                <v-row>
                  <v-col cols="12">
                    <v-text-field v-model="startDate" label="선택 날짜" type="month"></v-text-field>
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

      <v-row>
        <v-card class="pa-8 ml-3 text-center" color="#E1BEE7" dark>
          <v-row align="center" justify="center">
            <h2 class="headline white--text">{{ startDate }}</h2>
          </v-row>
        </v-card>
      </v-row>


      <v-row class="mt-4">
        <v-col cols="12" md="5">
          <v-card class="pa-5 text-center" color="#C8E6C9" dark>
            <v-row align="center" justify="center">
              <h2 class="headline white--text">개인 현황</h2>
            </v-row>
          </v-card>
          <v-card class="p-20">
            <SalesPageTargetChart></SalesPageTargetChart>
          </v-card>
        </v-col>

        <v-col cols="12" md="5">
          <v-card class="pa-5 text-center" color="#C8E6C9" dark>
            <v-row align="center" justify="center">
              <h2 class="headline white--text">개인 판매 실적</h2>
            </v-row>
          </v-card>
          <VCard>
            <VCardText class="pt-10 mt-14">
              <VRow>
                <VCol cols="12" sm="6" md="6" class="mb-5">
                  <VCard color="primary" class="text-center">
                    <div class="d-flex align-center justify-center gap-x-3 py-4">
                      <div class="d-flex flex-column">
                        <div class="text-body-1">
                          {{ statistics[0].title }}
                        </div>
                        <h5 class="text-h5">
                          {{ statistics[0].stats }}
                        </h5>
                      </div>
                    </div>
                  </VCard>
                </VCol>
                <VCol cols="12" sm="6" md="6" class="mb-5">
                  <VCard color="success" class="text-center">
                    <div class="d-flex align-center justify-center gap-x-3 py-4">
                      <div class="d-flex flex-column">
                        <div class="text-body-1">
                          {{ statistics[1].title }}
                        </div>
                        <h5 class="text-h5">
                          {{ statistics[1].stats }}
                        </h5>
                      </div>
                    </div>
                  </VCard>
                </VCol>
                <VCol cols="12" sm="6" md="6" class="mb-8">
                  <VCard color="warning" class="text-center">
                    <div class="d-flex align-center justify-center gap-x-3 py-4">
                      <div class="d-flex flex-column">
                        <div class="text-body-1">
                          {{ statistics[2].title }}
                        </div>
                        <h5 class="text-h5">
                          {{ statistics[2].stats }}
                        </h5>
                      </div>
                    </div>
                  </VCard>
                </VCol>
                <VCol cols="12" sm="6" md="6">
                  <VCard color="info" class="text-center">
                    <div class="d-flex align-center justify-center gap-x-3 py-4">
                      <div class="d-flex flex-column">
                        <div class="text-body-1">
                          {{ statistics[3].title }}
                        </div>
                        <h5 class="text-h5">
                          {{ statistics[3].stats }}
                        </h5>
                      </div>
                    </div>
                  </VCard>
                </VCol>
              </VRow>
            </VCardText>
          </VCard>
        </v-col>
      </v-row>

      <v-card class="pa-5 mt-10 text-center" color="#C8E6C9" dark>
        <v-row align="center" justify="center">
          <h2 class="headline white--text">계약 리스트</h2>
        </v-row>
      </v-card>
      <ListComponent
        v-if="model === 'FP' || model === 'MANAGER' || model === 'HQ'"
        :columns="headers"
        :rows="formattedItems"
      />

      <v-divider></v-divider>
      <v-divider></v-divider>

      <!-- 팀 데이터를 표시하는 부분 -->
      <!-- 팀 데이터를 표시하는 부분 -->
      <v-card class="mt-12" v-if="model === 'MANAGER' && teamCode !== ''">
        <v-row class="mt-12">
          <v-card-title> 팀 매출</v-card-title>
        </v-row>

        <v-col cols="2">
          <v-btn class="mt-1" @click="showDatePickerTeamDialog">
            <v-icon left>mdi-calendar</v-icon>
            날짜 선택
          </v-btn>
        </v-col>

        <v-col cols="12">
          <v-dialog v-model="datePickerTeamDialog" persistent max-width="300px">
            <v-card>
              <v-card-title>날짜 선택</v-card-title>

              <v-card-text>
                <v-row>
                  <v-col cols="12">
                    <v-text-field v-model="startTeamDate" label="선택 날짜" type="month"></v-text-field>
                  </v-col>
                </v-row>
              </v-card-text>

              <v-card-actions>
                <v-btn color="primary" @click="applyTeamDate">적용</v-btn>
                <v-btn color="secondary" @click="cancelTeamDateRange">취소</v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-col>
        <v-row>
          <v-card-title> {{ startTeamDate }}</v-card-title>
        </v-row>


        <v-row>
          <v-col cols="12" md="5">
            <v-card class="p-20">
              <SalesPageTeamTarget></SalesPageTeamTarget>
            </v-card>
          </v-col>

          <v-col cols="12" md="5">
            <VCard title="판매 실적">
              <VCardText class="pt-10">
                <VRow>
                  <VCol cols="12" sm="6" md="6" class="mb-5">
                    <VCard color="primary" class="text-center">
                      <div class="d-flex align-center justify-center gap-x-3 py-4">
                        <div class="d-flex flex-column">
                          <div class="text-body-1">
                            {{ teamStatistics[0].title }}
                          </div>
                          <h5 class="text-h5">
                            {{ teamStatistics[0].stats }}
                          </h5>
                        </div>
                      </div>
                    </VCard>
                  </VCol>
                  <VCol cols="12" sm="6" md="6" class="mb-5">
                    <VCard color="success" class="text-center">
                      <div class="d-flex align-center justify-center gap-x-3 py-4">
                        <div class="d-flex flex-column">
                          <div class="text-body-1">
                            {{ teamStatistics[1].title }}
                          </div>
                          <h5 class="text-h5">
                            {{ teamStatistics[1].stats }}
                          </h5>
                        </div>
                      </div>
                    </VCard>
                  </VCol>
                  <VCol cols="12" sm="6" md="6" class="mb-8">
                    <VCard color="warning" class="text-center">
                      <div class="d-flex align-center justify-center gap-x-3 py-4">
                        <div class="d-flex flex-column">
                          <div class="text-body-1">
                            {{ teamStatistics[2].title }}
                          </div>
                          <h5 class="text-h5">
                            {{ teamStatistics[2].stats }}
                          </h5>
                        </div>
                      </div>
                    </VCard>
                  </VCol>
                  <VCol cols="12" sm="6" md="6">
                    <VCard color="info" class="text-center">
                      <div class="d-flex align-center justify-center gap-x-3 py-4">
                        <div class="d-flex flex-column">
                          <div class="text-body-1">
                            {{ teamStatistics[3].title }}
                          </div>
                          <h5 class="text-h5">
                            {{ teamStatistics[3].stats }}
                          </h5>
                        </div>
                      </div>
                    </VCard>
                  </VCol>
                </VRow>
              </VCardText>
            </VCard>
          </v-col>
        </v-row>
      </v-card>

      <v-divider></v-divider>
      <v-divider></v-divider>

      <!-- 전사 데이터 보여주는 부분 -->
      <v-card class="mt-12" v-if="model === 'HQ'">
        <v-row class="mt-12">
          <v-card-title> 전사 매출</v-card-title>
        </v-row>

        <v-col cols="2">
          <v-btn class="mt-1" @click="showDatePickerHQDialog">
            <v-icon left>mdi-calendar</v-icon>
            날짜 선택
          </v-btn>
        </v-col>

        <v-col cols="12">
          <v-dialog v-model="datePickerHQDialog" persistent max-width="300px">
            <v-card>
              <v-card-title>날짜 선택</v-card-title>

              <v-card-text>
                <v-row>
                  <v-col cols="12">
                    <v-text-field v-model="startHQDate" label="선택 날짜" type="month"></v-text-field>
                  </v-col>
                </v-row>
              </v-card-text>

              <v-card-actions>
                <v-btn color="primary" @click="applyHQDate">적용</v-btn>
                <v-btn color="secondary" @click="cancelHQDateRange">취소</v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-col>
        <v-row>
          <v-card-title> {{ startHQDate }}</v-card-title>
        </v-row>

        <v-col cols="12" md="5">
          <VCard title="판매 실적">
            <VCardText class="pt-10">
              <VRow>
                <VCol cols="12" sm="6" md="6" class="mb-5">
                  <VCard color="primary" class="text-center">
                    <div class="d-flex align-center justify-center gap-x-3 py-4">
                      <div class="d-flex flex-column">
                        <div class="text-body-1">
                          {{ hqStatistics[0].title }}
                        </div>
                        <h5 class="text-h5">
                          {{ hqStatistics[0].stats }}
                        </h5>
                      </div>
                    </div>
                  </VCard>
                </VCol>
                <VCol cols="12" sm="6" md="6" class="mb-5">
                  <VCard color="success" class="text-center">
                    <div class="d-flex align-center justify-center gap-x-3 py-4">
                      <div class="d-flex flex-column">
                        <div class="text-body-1">
                          {{ hqStatistics[1].title }}
                        </div>
                        <h5 class="text-h5">
                          {{ hqStatistics[1].stats }}
                        </h5>
                      </div>
                    </div>
                  </VCard>
                </VCol>
                <VCol cols="12" sm="6" md="6" class="mb-8">
                  <VCard color="warning" class="text-center">
                    <div class="d-flex align-center justify-center gap-x-3 py-4">
                      <div class="d-flex flex-column">
                        <div class="text-body-1">
                          {{ hqStatistics[2].title }}
                        </div>
                        <h5 class="text-h5">
                          {{ hqStatistics[2].stats }}
                        </h5>
                      </div>
                    </div>
                  </VCard>
                </VCol>
                <VCol cols="12" sm="6" md="6">
                  <VCard color="info" class="text-center">
                    <div class="d-flex align-center justify-center gap-x-3 py-4">
                      <div class="d-flex flex-column">
                        <div class="text-body-1">
                          {{ hqStatistics[3].title }}
                        </div>
                        <h5 class="text-h5">
                          {{ hqStatistics[3].stats }}
                        </h5>
                      </div>
                    </div>
                  </VCard>
                </VCol>
              </VRow>
            </VCardText>
          </VCard>
        </v-col>
      </v-card>
      <v-card v-if="model === 'HQ'" class="pa-5 mt-10 text-center" color="#C8E6C9" dark>
        <v-row align="center" justify="center">
          <h2 class="headline white--text">팀 실적 리스트</h2>
        </v-row>
      </v-card>
      <ListComponent
        v-if="model === 'HQ'"
        :columns="teamHeaders"
        :rows="formattedTeamItems"
      />

    </v-main>
  </v-container>
</template>

<script>
import {ref} from 'vue';
import axios from 'axios';
import axiosInstance from '@/plugins/loginaxios';
import AppSidebar from "@/layouts/AppSidebar.vue";
import AppHeader from "@/layouts/AppHeader.vue";
import SalesPagePieChart from "@/pages/sales/charts/SalesPagePieChart"
import SalesPageCountPieChart from "@/pages/sales/charts/SalesPageCountPieChart"
import SalesPageTargetChart from "@/pages/sales/charts/SalesPageTarget"

import SalesPageTeamTarget from "@/pages/sales/charts/SalesPageTeamTarget"

import SalesTransChart from "@/pages/sales/charts/SalesAnalytics.vue"
import {useSalesStore} from '@/stores/SalesStore';
import {useLoginInfoStore} from '@/stores/loginInfo';
import ListComponent from "@/layouts/ListComponent.vue";


export default {
  components: {
    AppHeader, AppSidebar, SalesPagePieChart,
    SalesPageCountPieChart, SalesPageTargetChart,
    SalesTransChart, ListComponent,

    SalesPageTeamTarget,
  },
  setup() {
    const loaded_CountPie = ref(false);
    const loaded_PricePie = ref(false);
    const model = ref(useLoginInfoStore().getMemberRank); // 리액티브 변수로 선언
    const teamCode = ref(useLoginInfoStore().memberTeamCode); // 리액티브 변수로 선언
    let datePickerDialog = ref(false);
    let startDate = ref("");
    let datePickerTeamDialog = ref(false);
    let startTeamDate = ref("");
    let datePickerHQDialog = ref(false);
    let startHQDate = ref("");
    const SalesStore = useSalesStore();


    // 데이터 로딩 후 loaded 상태 변경
    setTimeout(() => {
      loaded_CountPie.value = true;
    }, 1000); // 예시로 setTimeout 사용, 실제 데이터 로딩 후 변경하는 로직으로 변경

    setTimeout(() => {
      loaded_PricePie.value = true;
    }, 2000);

    const showDatePickerDialog = () => {
      datePickerDialog.value = true;
    };

    const cancelDateRange = () => {
      datePickerDialog.value = false; // 모달 닫기
    };

    const showDatePickerTeamDialog = () => {
      datePickerTeamDialog.value = true;
    };

    const cancelTeamDateRange = () => {
      datePickerTeamDialog.value = false; // 모달 닫기
    };


    const showDatePickerHQDialog = () => {
      datePickerHQDialog.value = true;
    };

    const cancelHQDateRange = () => {
      datePickerHQDialog.value = false; // 모달 닫기
    };

    return {
      loaded_CountPie, loaded_PricePie, model, SalesStore,

      datePickerDialog, startDate, showDatePickerDialog, cancelDateRange,

      startTeamDate, datePickerTeamDialog, cancelTeamDateRange, showDatePickerTeamDialog,

      startHQDate, datePickerHQDialog, showDatePickerHQDialog, cancelHQDateRange
    }
  },
  data() {
    return {
      //selectedPeriod: '월', // 초기 선택값은 월로 설정
      loginStore: useLoginInfoStore(),
      items: [],
      headers: [

        { title: 'No', key: 'id' },
        { title: '계약 총금액', key: 'contractPrice' },
        { title: '계약 건수', key: 'contractCount' },
        { title: '해약 총금액', key: 'cancelPrice' },
        { title: '해약 건수', key: 'cancelCount' },
      ],
      teamItems: [],
      teamHeaders: [
        { title: 'No', key: 'id' },
        { title: '팀 이름', key: 'teamName' },
        { title: '계약 총금액', key: 'contractPrice' },
        { title: '계약 건수', key: 'contractCount' },
        { title: '해약 총금액', key: 'cancelPrice' },
        { title: '해약 건수', key: 'cancelCount' },
      ],
      statistics: [
        {
          title: '매출',
          stats: '-',
          color: 'primary',
        },
        {
          title: '계약건수',
          stats: '-',
          color: 'success',
        },
        {
          title: '해약 매출',
          stats: '-',
          color: 'warning',
        },
        {
          title: '해약 건수',
          stats: '-',
          color: 'info',
        }
      ],
      teamStatistics: [
        {
          title: '매출',
          stats: '-',
          color: 'primary',
        },
        {
          title: '계약건수',
          stats: '-',
          color: 'success',
        },
        {
          title: '해약 매출',
          stats: '-',
          color: 'warning',
        },
        {
          title: '해약 건수',
          stats: '-',
          color: 'info',
        }
      ],
      hqStatistics: [
        {
          title: '전사 매출',
          stats: '-',
          color: 'primary',
        },
        {
          title: '전사 계약건수',
          stats: '-',
          color: 'success',
        },
        {
          title: '전사 해약 매출',
          stats: '-',
          color: 'warning',
        },
        {
          title: '전사 해약 건수',
          stats: '-',
          color: 'info',
        }
      ],
    };
  },
  computed: {
    formattedItems() {
      return this.items.map(item => ({...item}));
    },
    formattedTeamItems() {
      return this.teamItems.map(teamItems => ({...teamItems}));
    },
    formattedTeamItems() {
      return this.teamItems.map(teamItems => ({ ...teamItems }));
    },
  },
  methods: {
    applyDate() {
      console.log("매출메인 개인 선택날짜:", this.startDate);
      this.memberList();
      this.memberShot();

      useSalesStore().startDate = this.startDate;

      this.datePickerDialog = !this.datePickerDialog; // 모달 닫기
    },
    applyTeamDate() {
      useSalesStore().startTeamDate = this.startTeamDate;
      if(this.startTeamDate !== "") {
        this.teamShot();
      }

      this.datePickerTeamDialog = !this.datePickerTeamDialog; // 모달 닫기
    },
    applyHQDate() {
      useSalesStore().startHQDate = this.startHQDate;
      console.log("전사 날짜 선택: ", this.startHQDate);

      if(this.startHQDate !== "") {
        this.hqShot();
        this.hqList();
      }

      this.datePickerHQDialog = !this.datePickerHQDialog; // 모달 닫기
    },

    async memberList() {
      const memberCode = this.loginStore.getMemberCode;
      const date = this.startDate;
      const baseUrl = import.meta.env.VITE_API_STATISTICS_BASE_URL || 'http://localhost:8081/statistics';

      // let url = `http://localhost:8081/api/stat/sales/${memberCode}/${date}`;
      let url = `${baseUrl}/api/stat/sales/${memberCode}/${date}`;
      console.log(url);

      await axios.get(url)
        .then(response => {
          console.log("SalesMemberListr 응답결과 : ", response);
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
    async memberShot() {
      const memberCode = this.loginStore.getMemberCode;
      const date = this.startDate;
      const baseUrl = import.meta.env.VITE_API_STATISTICS_BASE_URL || 'http://localhost:8081/statistics';

      //let url = `http://localhost:8081/api/stat/sales/${memberCode}/${date}/price`;
      let url = `${baseUrl}/api/stat/sales/${memberCode}/${date}/price`;
      console.log(url);

      await axios.get(url)
        .then(response => {
          console.log("사원 맴버 결과 응답결과 : ", response.data.result);
          const result = response.data.result || "-";
          //this.statistics[0].stats = result.contractPrice + "원" || "-";
          this.statistics[0].stats = Number(result.contractPrice).toLocaleString() + "원" || "-";
          this.statistics[1].stats = Number(result.contractCount).toLocaleString() + "건" || "-";
          this.statistics[2].stats = Number(result.cancelPrice).toLocaleString() + "원" || "-";
          this.statistics[3].stats = Number(result.cancelCount).toLocaleString() + "건" || "-";
        })
        .catch(error => {
          console.log("요청할 수 없습니다. : ", error);
        });
    },
    async teamShot() {
      const memberTeamCode = this.loginStore.memberTeamCode;
      const date = this.startTeamDate;

      const baseUrl = import.meta.env.VITE_API_STATISTICS_BASE_URL || 'http://localhost:8081/statistics';
      let url = `${baseUrl}/api/sales/${memberTeamCode}/${date}/price`;
      //let url = `http://localhost:8081/statistics/api/sales/${memberTeamCode}/${date}/price`;
      console.log(url);

      await axios.get(url)
        .then(response => {
          console.log("팀 결과 응답결과 : ", response.data.result);
          const result = response.data.result || "-";

          this.teamStatistics[0].stats = Number(result.contractPrice).toLocaleString() + "원" || "-";
          this.teamStatistics[1].stats = Number(result.contractCount).toLocaleString() + "건" || "-";
          this.teamStatistics[2].stats = Number(result.cancelPrice).toLocaleString() + "원" || "-";
          this.teamStatistics[3].stats = Number(result.cancelCount).toLocaleString() + "건" || "-";
        })
        .catch(error => {
          console.log("요청할 수 없습니다. : ", error);
        });
    },
    async hqShot() {
      // const memberTeamCode = this.loginStore.memberTeamCode;
      const date = this.startHQDate;

      const baseUrl = import.meta.env.VITE_API_STATISTICS_BASE_URL || 'http://localhost:8081/statistics';
      let url = `${baseUrl}/api/sales/hq/${date}/price`;
      //let url = `http://localhost:8081/statistics/api/sales/hq/${date}/price`;
      console.log("hqShot() : ", url);

      await axios.get(url)
        .then(response => {
          console.log("전사 결과 응답결과 : ", response.data.result);
          const result = response.data.result || "-";

          this.hqStatistics[0].stats = Number(result.contractPrice).toLocaleString() + "원" || "-";
          this.hqStatistics[1].stats = Number(result.contractCount).toLocaleString() + "건" || "-";
          this.hqStatistics[2].stats = Number(result.cancelPrice).toLocaleString() + "원" || "-";
          this.hqStatistics[3].stats = Number(result.cancelCount).toLocaleString() + "건" || "-";
        })
        .catch(error => {
          console.log("요청할 수 없습니다. : ", error);
        });
    },
    async hqList() {
      const date = this.startHQDate;
      const baseUrl = import.meta.env.VITE_API_STATISTICS_BASE_URL || 'http://localhost:8081/statistics';
      let url = `${baseUrl}/api/sales/hq/${date}/team-price`;
      //let url = `http://localhost:8081/statistics/api/sales/hq/${date}/team-price`;
      console.log("hqList() ", url);

      await axios.get(url)
        .then(response => {
          console.log("전사 팀 리스트 응답결과 : ", response.data.result);
          this.teamItems = response.data.result || [];
          this.teamItems = this.teamItems.map((teamItems, index) => ({
            ...teamItems,
            id: index + 1,
          }));
        })
        .catch(error => {
          console.log("요청할 수 없습니다. : ", error);
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
