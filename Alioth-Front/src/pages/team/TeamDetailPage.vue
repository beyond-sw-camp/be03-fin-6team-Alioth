<template>
  <AppSidebar></AppSidebar>
  <v-container fluid>
    <v-main>
      <AppHeader></AppHeader>
      <v-card flat>
        <v-row>
          <v-col cols="3" class="d-flex align-center">
            <v-card-title class="mr-2 custom-font">팀명</v-card-title>
            <div class="custom-font" v-if="!modify">{{ state.teamName }}</div>
            <v-col v-if="modify" cols="6">
              <v-text-field v-model="state.teamName"></v-text-field>
            </v-col>
          </v-col>
          <v-col cols="3" class="d-flex align-center">
            <v-card-title class="mr-2 custom-font">팀장</v-card-title>
            <div class="custom-font" v-if="!modify">{{ state.teamManagerName }}</div>
            <v-text-field v-if="modify" v-model="state.teamManagerName" @click="showModal()" readonly></v-text-field>
          </v-col>
          <v-col cols="3" class="d-flex align-center">
            <v-card-title class="mr-2 custom-font">매출 목표</v-card-title>
            <div class="custom-font">{{ state.monthlyTargetCount }}건 | {{ state.monthlyTargetPrice }}원
            </div>
            <v-btn class="small-btn" v-if="modify && loginStore.memberRank==='HQ'" variant="tonal" color="#1A237E"
                   style="margin-left:2vw" @click="showSalesModal()">입력</v-btn>
          </v-col>
          <v-col cols="3">
            <v-col class="d-flex align-center">
              <v-card-title class="mr-2 custom-font">고과평가</v-card-title>
              <v-card-title v-if="!modify" class="custom-font">
                {{ state.performanceReview }}
              </v-card-title>
              <v-select v-if="modify" v-model="state.performanceReview" :items="['A', 'B', 'C', 'D']"></v-select>
            </v-col>
          </v-col>
        </v-row>
      </v-card>

      <div style="margin-bottom: 16px;"></div>


      <v-card style="margin-top: 10px;">
        <v-row align="center" class="d-flex justify-space-between">
          <v-col cols="4" class="pa-2 ma-2">
            <v-text-field style="margin-bottom: 15px; margin-left: 15px; margin-top: 15px;"
                          v-model="MySearch"
                          label="Search"
                          prepend-inner-icon="mdi-magnify"
                          variant="outlined"
                          dense
            >
            </v-text-field>
          </v-col>

          <v-col class="text-right">
            <v-btn variant="tonal" color="#2979FF" @click="navigateToAdd" style="margin-right: 1vw">팀원 추가</v-btn>
          </v-col>
        </v-row>
        <v-spacer></v-spacer>
        <ListComponent :columns="state.tableColumns" :rows="state.tableRows" @click:row="navigateToDetail"/>
      </v-card>
      <v-row style="margin-top: 0.2vw">
        <v-col>
          <v-btn color="#2979FF" variant="tonal" v-if="loginStore.memberRank==='HQ'&& !modify" @click="isModify">정보 수정</v-btn>
          <v-btn color="#2979FF" variant="tonal" v-if="loginStore.memberRank==='HQ'&& modify" @click="updateTeam">수정 완료
          </v-btn>
        </v-col>
        <v-col class="text-right">
          <v-btn color="primary" variant="tonal" v-if="loginStore.memberRank==='HQ'" @click="deleteTeam">팀 삭제 </v-btn>
        </v-col>
      </v-row>
    </v-main>
  </v-container>

  <v-dialog v-model="state.modalOpen" width="auto">
    <v-card>
      <v-card-text>
        <v-container>
          <div>
            <v-text-field v-model="search" density="compact" label="Search" prepend-inner-icon="mdi-magnify"
                          variant="solo-filled" flat hide-details single-line></v-text-field>
            <v-data-table :headers="state.tableColumns" :items="state.rows" item-value="name"
                          v-model="state.selectedItems">
              <template v-slot:headers="">
                <tr>
                  <th>
                    <v-checkbox @click="toggleAll"></v-checkbox>
                  </th>
                  <th v-for="header in state.tableColumns" :key="header.title">{{ header.title }}</th>
                </tr>
              </template>

              <template v-slot:item="{ item }">
                <tr @click="toggleCheckbox(item)">
                  <td>
                    <v-checkbox v-model="item.isSelected"></v-checkbox>
                  </td>
                  <td>{{ item.id }}</td>
                  <td>{{ item.name }}</td>
                  <td>{{ item.salesMemberCode }}</td>
                  <td>{{ item.rank }}</td>
                  <td>{{ item.teamName }}</td>
                  <td>{{ item.teamCode }}</td>
                  <td>{{ item.email }}</td>
                  <td>{{ item.phone }}</td>
                  <td>{{ item.officeAddress }}</td>
                </tr>
              </template>
            </v-data-table>
          </div>
        </v-container>
      </v-card-text>
      <v-card-actions>
        <v-btn color="#2979FF" variant="tonal" @click="selectMembers">확인</v-btn>
        <v-btn color="#2C3E50" variant="tonal" @click="closeModal">닫기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <v-dialog v-model="updateTeamManager" width="auto">
    <v-card>
      <v-card-text>
        <v-container>
          <div>
            <v-text-field v-model="search" density="compact" label="Search" prepend-inner-icon="mdi-magnify"
                          variant="solo-filled" flat hide-details single-line></v-text-field>
            <ListComponent :columns="tableColumns" :rows="rows" @click:row="select"></ListComponent>
          </div>
        </v-container>
      </v-card-text>
      <v-card-actions>
        <v-btn color="#2C3E50" variant="tonal" @click="updateTeamManager=false">닫기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <v-dialog v-model="updateSales" width="40vw">
    <v-card align="center">
      <v-card-title class="custom-font"> 매출 목표</v-card-title>
      <v-card-text>
        <v-container>
          <v-row>
            <v-col cols="6">
              <v-text-field label="목표 건수" v-model="state.monthlyTargetCount"></v-text-field>
            </v-col>
            <v-col cols="6">
              <v-text-field label="목표 금액" v-model="state.monthlyTargetPrice"></v-text-field>
            </v-col>
          </v-row>
          <v-row>
            <v-col class="text-right">
              <v-btn color="#2979FF" variant="tonal" @click="saveSalesTarget" style="margin-right: 0.25vw">저장</v-btn>
              <v-btn color="#2C3E50" variant="tonal" @click="updateSales=false">닫기</v-btn>
            </v-col>
          </v-row>
        </v-container>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
import AppSidebar from "@/layouts/AppSidebar.vue";
import AppHeader from "@/layouts/AppHeader.vue";
import ListComponent from "@/layouts/ListComponent.vue";
import {ref, onMounted, reactive, watch} from 'vue';
import axiosInstance from "@/plugins/loginaxios";
import router from "@/router";
import {useLoginInfoStore} from "@/stores/loginInfo";

export default {
  components: {ListComponent, AppHeader, AppSidebar},
  props: {
    teamCode: {
      type: String,
      required: true
    }
  },
  setup(props) {
    const state = reactive({
      teamName: '', // 초기값을 빈 문자열로 설정
      teamManagerName: '',
      performanceReview: '',
      monthlyTargetCount: null,
      monthlyTargetPrice: null,
      salesMemberCode: 'salesMemberCode',
      modalOpen: false,
      tableColumns: [
        {title: "No", key: "id"},
        {title: "이름", key: "name"},
        {title: "사원코드", key: "salesMemberCode"},
        {title: "직급", key: "rank"},
        {title: "팀", key: "teamName"},
        {title: "팀 코드", key: "teamCode"},
        {title: "이메일", key: "email"},
        {title: "전화번호", key: "phone"},
        {title: "사무실", key: "officeAddress"},
      ],
      tableRows: [], // ref를 사용하여 반응형 데이터 생성
      rows: [],
      selectedItems: ref([])
    });
    const form = ref({
      teamName: '',
      teamManagerCode: '',
      name: '',
    });
    const tableColumns = [
      {title: "이름", key: "name"},
      {title: "사원번호", key: "salesMemberCode"},
    ];
    const rows = ref([]);
    const teamName = ref('');
    const teamManagerCode = ref('');
    const name = ref('');
    const updateTeamManager = ref(false);
    const updateSales = ref(false);

    const search = ref('');
    const MySearch = ref('');
    const modify = ref(false);

    const loginStore = useLoginInfoStore();
    const baseUrl = import.meta.env.VITE_API_SERVER_BASE_URL || 'http://localhost:8080';
    const fetchData = () => {

      axiosInstance.get(`${baseUrl}/api/team/detail/${props.teamCode}`)
        .then(response => {
          if (response.data && response.data.result) {
            const {
              teamName: fetchedTeamName,
              teamManagerName: fetchedTeamManagerName,
              performanceReview: fetchedPerformanceReview,
              monthlyTargetPrice: fetchedMonthlyTargetPrice,
              monthlyTargetCount: fetchedMonthlyTargetCount,
              teamMemberList
            } = response.data.result;

            state.teamName = fetchedTeamName; // API 응답에서 받은 값으로 할당
            state.teamManagerName = fetchedTeamManagerName;
            state.performanceReview = fetchedPerformanceReview;
            state.monthlyTargetPrice = fetchedMonthlyTargetPrice;
            state.monthlyTargetCount = fetchedMonthlyTargetCount;

            const filteredData = teamMemberList.filter(item => {
              const name = item.name.toLowerCase();
              const salesMemberCode = item.salesMemberCode.toString().toLowerCase();
              const phone = item.phone.toLowerCase();
              return name.includes(MySearch.value) || salesMemberCode.includes(MySearch.value) || phone.includes(MySearch.value);
            });

            filteredData.forEach((item, index) => {
              item.id = index + 1;
            });

            state.tableRows = filteredData;
          }
        })
        .catch(error => {
          alert("담당팀이 없습니다.")
          console.error('Error fetching data:', error);
          router.push(`/`)
        });


    };

    const membersFetchData = () => {
      axiosInstance.get(`${baseUrl}/api/members/list/FP`)
        .then(response => {
          const data = response.data.result;

          const filteredData = data.filter(item => {
            const name = item.name.toLowerCase();
            const salesMemberCode = item.salesMemberCode.toString().toLowerCase();
            const phone = item.phone.toLowerCase();
            return name.includes(search.value) || salesMemberCode.includes(search.value) || phone.includes(search.value);
          });

          filteredData.forEach((item, index) => {
            item.id = index + 1;
            item.isSelected = false; // isSelected 속성을 추가하고 초기값을 false로 설정합니다.
          });

          state.rows = filteredData;
        })
        .catch(error => {
          console.log('Error fetching data:', error);
        });
    }
    const managerFetchData = () => {
      const baseUrl = import.meta.env.VITE_API_SERVER_BASE_URL || 'http://localhost:8080';
      axiosInstance.get(`${baseUrl}/api/members/list/manager`)
        .then(response => {
          const data = response.data.result;
          const filteredData = data.filter(item => {
            const name = item.name.toLowerCase();
            const salesMemberCode = item.salesMemberCode.toString().toLowerCase();
            return name.includes(search.value) || salesMemberCode.includes(search.value);
          });

          filteredData.forEach((item, index) => {
            item.id = index + 1;
          });

          rows.value = filteredData;
        })
        .catch(error => {
          console.log('Error fetching data:', error);
        });
    };
    let teamMemberList;

    function navigateToAdd() {
      state.modalOpen = true;
    }

    function closeModal() {
      state.modalOpen = false;
    }

    function showModal() {
      updateTeamManager.value = true;
    }

    function isModify() {
      modify.value = !modify.value
    }

    function showSalesModal() {
      updateSales.value = !updateSales.value;
    }

    function select(event, {item}) {
      state.teamManagerName = item.name
      updateTeamManager.value = false;
    }

    const saveSalesTarget=() => {
      updateSales.value = false;
    }

    function toggleCheckbox(item) {
      item.isSelected = !item.isSelected;
    }

    function navigateToDetail(event, {item}) {
      router.push({path: `/SalesMembersList/Detail/${item.salesMemberCode}`});
    }

    function toggleAll() {
      if (state.selectedItems.length === state.rows.length) {
        state.selectedItems = [];
        state.rows.forEach(item => {
          item.isSelected = false;
        });
      } else {
        state.selectedItems = state.rows.slice();
        state.rows.forEach(item => {
          item.isSelected = true;
        });
      }
    }

    const updateTeam = () => {
      const data = {
        teamName: state.teamName,
        teamManagerName: state.teamManagerName,
        performanceReview: state.performanceReview,
        monthlyTargetPrice: state.monthlyTargetPrice,
        monthlyTargetCount: state.monthlyTargetCount
      }
      if (confirm("수정하시겠습니까?")) {
        axiosInstance.patch(`${baseUrl}/api/team/update/${props.teamCode}`, data)
          .then(res => {
            console.log(res)
            alert("수정되었습니다.");
            modify.value = false;
            router.push({path: `/Team/Detail/${props.teamCode}`});
          })
          .catch(error => {
            console.error('Error updating data:', error);
            alert("수정할 수 없습니다. 다시 확인해주세요")
          });
      }
    }


    function selectMembers() {
      const selectedEmployeeCodes = state.rows
        .filter(item => item.isSelected) //
        .map(item => item.salesMemberCode);
      if (confirm("선택하신 사원들을 추가하시겠습니까?")) {
        axiosInstance.post(`${baseUrl}/api/team/addMembers/${props.teamCode}`, selectedEmployeeCodes)
          .then(() => {
            alert("추가되었습니다.")
            closeModal();
            window.location.reload()
          })
          .catch(error => {
            console.log('Error fetching data:', error);
          });
      }
    }

    function deleteTeam() {
      if (confirm("팀을 삭제하시겠습니까?")) {
        axiosInstance.delete(`${baseUrl}/api/team/delete/${props.teamCode}`)
          .then(() => {
            alert("삭제되었습니다.")
            router.push('/Team/List');
          })
          .catch(error => {
            console.log('Error fetching data:', error);
          });
      }
    }

    watch(MySearch, () => {
      fetchData();
    });
    watch(search, () => {
      membersFetchData();
    });

    onMounted(() => {
      fetchData();
      membersFetchData();
      managerFetchData();
    });

    return {
      loginStore,
      state,
      teamMemberList,
      modify,
      form,
      rows,
      teamName,
      teamManagerCode,
      name,
      tableColumns,
      updateTeamManager,
      updateSales,
      saveSalesTarget,
      select,
      showSalesModal,
      showModal,
      toggleAll,
      navigateToDetail,
      toggleCheckbox,
      selectMembers,
      navigateToAdd,
      closeModal,
      isModify,
      deleteTeam,
      updateTeam,
      search,
      MySearch,
    }
  },
}
</script>

<style>

.v-input__details {
  display: none;
}

.custom-font {
  font-family: "Spoqa Han Sans Neo", sans-serif;
}

.team-info-card {

  width: 100%; /* v-card의 너비를 확장합니다. */
  max-width: 350px; /* 최대 너비 설정 (필요에 따라 조정하세요) */
  margin: auto; /* 중앙 정렬 */
}

.team-card {
  padding: 20px; /* 카드 내부의 패딩을 조정하여 내용물이 더 여유롭게 보이도록 합니다. */
  text-align: center; /* 텍스트 중앙 정렬 */
}

.team-label, .team-name {
  font-size: 1.5rem; /* 글씨 크기를 1.5rem으로 설정 */
  font-weight: bold; /* 글씨를 굵게 */
}

.team-name {
  margin-top: 10px; /* 팀 이름과 팀장 이름 사이의 간격 조정 */
}
</style>
