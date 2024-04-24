<template>
  <AppSidebar></AppSidebar>
  <v-main>
    <AppHeader></AppHeader>
    <v-container fluid>
      <v-card flat>
        <v-row>
          <v-col cols="4" offset="3"> <!-- 팀 명 -->
            <v-card text="팀명" variant="outlined"> {{ state.teamName }}</v-card>
          </v-col>
          <v-col cols="4" offset="1"> <!-- 팀장 -->
            <v-card text="팀장" variant="outlined"> {{ state.teamManagerName }}</v-card>
          </v-col>
        </v-row>
        <v-card-actions>

        </v-card-actions>
      </v-card>

      <div style="margin-bottom: 16px;"></div>

      <v-card>
        <v-card-title class="d-flex align-center pe-2">
          <!--          <v-icon icon="fa:fas fa-edit"></v-icon> &nbsp;-->
          팀원 목록
          <v-spacer></v-spacer>
          <v-text-field v-model="search" density="compact" label="Search" prepend-inner-icon="mdi-magnify"
                        variant="solo-filled" flat hide-details single-line></v-text-field>
          <v-row>
            <v-col class="text-right">
              <v-btn variant="outlined" @click="navigateToAdd">팀원 추가</v-btn>
            </v-col>
          </v-row>
        </v-card-title>
        <v-spacer></v-spacer>
        <ListComponent :columns="state.tableColumns" :rows="state.tableRows" @click:row="navigateToDetail"/>
      </v-card>
      <v-col class="text-right">
        <v-btn variant="outlined" @click="downloadExcel">엑셀다운로드</v-btn>
        <v-btn variant="outlined" @click="deleteTeam">팀 삭제</v-btn>
      </v-col>
    </v-container>
  </v-main>
  <v-dialog v-model="state.modalOpen" width="auto">
    <v-card>
      <v-card-title>
      </v-card-title>
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
                  <td>{{ item.profileImage }}</td>
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
        <v-btn color="" @click="selectMembers">확인</v-btn>
        <v-btn color="" @click="closeModal">닫기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

</template>

<script>
import AppSidebar from "@/layouts/AppSidebar.vue";
import AppHeader from "@/layouts/AppHeader.vue";
import ListComponent from "@/layouts/ListComponent.vue";
import {ref, onMounted, reactive} from 'vue';
import axiosInstance from "@/plugins/loginaxios";
import router from "@/router";

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
      salesMemberCode: 'salesMemberCode',
      modalOpen: false,
      tableColumns: [
        {title: "No", key: "id"},
        {title: "프로필사진", key: "profileImage"},
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
    const baseUrl = process.env.VUE_APP_API_BASE_URL || 'http://localhost:8080';
    const fetchData = () => {

      axiosInstance.get(`${baseUrl}/api/team/detail/${props.teamCode}`)
        .then(response => {
          if (response.data && response.data.result) {
            const {
              teamName: fetchedTeamName,
              teamManagerName: fetchedTeamManagerName,
              teamMemberList
            } = response.data.result;
            // 데이터를 Vue 데이터에 할당
            state.teamName = fetchedTeamName; // API 응답에서 받은 값으로 할당
            state.teamManagerName = fetchedTeamManagerName;
            // 데이터를 가져온 후에 각 항목에 대한 ID를 추가
            teamMemberList.forEach((item, index) => {
              item.id = index + 1;
            });
            state.tableRows = teamMemberList;
          } else {
            console.error('Empty response or missing result data');
          }
        })
        .catch(error => {
          console.error('Error fetching data:', error);
        });

      axiosInstance.get(`${baseUrl}/api/members/list/FP`)
        .then(response => {
          const data = response.data.result;
          console.log(data)
          data.forEach((item, index) => {
            item.id = index + 1;
            item.isSelected = false; // isSelected 속성을 추가하고 초기값을 false로 설정합니다.
          });
          state.rows = data;
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

    function selectMembers() {
      const selectedEmployeeCodes = state.rows
        .filter(item => item.isSelected) //
        .map(item => item.salesMemberCode);
      if (confirm("선택하신 사원들을 추가하시겠습니까?")) {
        axiosInstance.post(`${baseUrl}/api/team/addMembers/${props.teamCode}`,selectedEmployeeCodes)
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

    function deleteTeam(){
      if(confirm("팀을 삭제하시겠습니까?")){
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
    function downloadExcel() {
      const requestData = {
        startDate: null,
        endDate: null
      };
      axiosInstance.post(`${baseUrl}/api/excel/export/salesMembers/${props.teamCode}`, requestData, {
        responseType: 'blob'
      })
        .then(response => {
          const url = window.URL.createObjectURL(new Blob([response.data]));
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', '팀원목록.xlsx');
          document.body.appendChild(link);
          link.click();
          window.URL.revokeObjectURL(url);
        })
    }
    onMounted(() => {
      fetchData();
    });

    return {
      state,
      teamMemberList,
      toggleAll,
      navigateToDetail,
      toggleCheckbox,
      selectMembers,
      navigateToAdd,
      closeModal,
      deleteTeam,
      downloadExcel
    }
  },
}
</script>

<style>
.v-input__details {
  display: none;
}
</style>
