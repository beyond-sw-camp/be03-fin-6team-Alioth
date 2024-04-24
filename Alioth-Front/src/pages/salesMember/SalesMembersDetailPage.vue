<template>
  <AppSidebar></AppSidebar>
  <v-main>
    <AppHeader></AppHeader>
    <v-container fluid>
      <v-col class="text-right">
        <v-btn variant="outlined" @click="isModify" v-if="!modify">수정</v-btn>
        <v-btn variant="outlined" @click="submitChange" v-if="modify"> 완료</v-btn>
        <v-btn variant="outlined" @click="deleteMember" v-if="!modify">삭제</v-btn>
      </v-col>
      <!--   이미지 들어오는지 확인 해봐야함-->
      <v-col cols="12" md="12">
        <v-card>
          <v-card-title>사진</v-card-title>
          <v-img :width="300" aspect-ratio="16/9" cover :src="profile"></v-img>
        </v-card>
      </v-col>
      <v-row>
        <v-col cols="12" md="4">
          <v-card>
            <v-card-title>이름</v-card-title>
            <v-card-text>{{ name }}</v-card-text>
          </v-card>
        </v-col>
        <v-col cols="12" md="4">
          <v-card>
            <v-card-title>사원 코드</v-card-title>
            <v-card-text>{{ salesMembersCode }}</v-card-text>
          </v-card>
        </v-col>
        <v-col cols="12" md="4">
          <v-card>
            <v-card-title>직급</v-card-title>
            <v-col cols="12" md="4" class="text-right">
            </v-col>
            <v-card-text v-if="!modify">{{ rank }}</v-card-text>
            <v-select v-if="modify" v-model="rank" :items="['FP', 'MANAGER', 'HQ']"></v-select>
          </v-card>
        </v-col>
        <v-col cols="12" md="4">
          <v-card-title>팀</v-card-title>
          <v-col cols="12" md="4" class="text-right">
            <v-btn variant="outlined" @click="navigateToChangeTeam" v-if="modify"> 팀 목록</v-btn>
          </v-col>
          <v-card>
            <v-card-title>팀 명</v-card-title>
            <v-card-text>{{ teamName }}</v-card-text>
            <v-card-title>팀 코드</v-card-title>
            <v-card-text>{{ teamCode }}</v-card-text>
          </v-card>
        </v-col>
        <v-col cols="12" md="4">
          <v-card>
            <v-card-title>휴대전화</v-card-title>
            <v-card-text>{{ phone }}</v-card-text>
          </v-card>
        </v-col>
        <v-col cols="12" md="4">
          <v-card>
            <v-card-title>이메일</v-card-title>
            <v-card-text>{{ email }}</v-card-text>
          </v-card>
        </v-col>
        <v-col cols="12" md="4">
          <v-card>
            <v-card-title>생년월일</v-card-title>
            <v-card-text>{{ birthDay }}</v-card-text>
          </v-card>
        </v-col>
        <v-col cols="12" md="4">
          <v-card>
            <v-card-title>자택 주소</v-card-title>
            <v-card-text>{{ address }}</v-card-text>
          </v-card>
        </v-col>
        <v-col cols="12" md="4">
          <v-card>
            <v-card-title>사무실 위치</v-card-title>
            <v-card-text>{{ officeAddress }}</v-card-text>
          </v-card>
        </v-col>
        <v-col cols="12" md="4">
          <v-card>
            <v-card-title>내선 번호</v-card-title>
            <v-card-text>{{ extensionNumber }}</v-card-text>
          </v-card>
        </v-col>
      </v-row>
      <v-row>
        <v-col cols="12">
          <v-card>
            <v-card-title>고과평가</v-card-title>
            <v-col cols="12" md="4" class="text-right">
            </v-col>
            <v-card-text v-if="!modify">{{ performanceReview }}</v-card-text>
            <v-select v-if="modify" v-model="performanceReview" :items="['A', 'B', 'C', 'D']"></v-select>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </v-main>
  <v-dialog v-model="modalOpen" width="auto">
    <v-card>
      <v-card-title>
      </v-card-title>
      <v-card-text>
        <v-container>
          <div>
            <v-text-field v-model="search" density="compact" label="Search" prepend-inner-icon="mdi-magnify"
                          variant="solo-filled" flat hide-details single-line></v-text-field>
            <ListComponent :columns="tableColumns" :rows="rows" @click:row="selectTeam"></ListComponent>
          </div>
        </v-container>
      </v-card-text>
      <v-card-actions>
        <v-btn color="" @click="closeModal">닫기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import AppSidebar from "@/layouts/AppSidebar.vue";
import AppHeader from "@/layouts/AppHeader.vue";
import axiosInstance from "@/plugins/loginaxios";
import router from "@/router";
import {onMounted, ref} from "vue";
import ListComponent from "@/layouts/ListComponent.vue";

export default {
  components: {ListComponent, AppHeader, AppSidebar},
  props: ["salesMembersCode"],
  setup(props) {
    const profile = ref('');
    const rank = ref('');
    const name = ref('');
    const phone = ref('');
    const email = ref('');
    const birthDay = ref('');
    const extensionNumber = ref('');
    const address = ref('');
    const officeAddress = ref('');
    const performanceReview = ref('');
    const teamName = ref('');
    const teamCode = ref('');
    const modify = ref(false);
    const modalOpen = ref(false);
    const rows = ref([]);
    const tableColumns = [
      {title: "팀", key: "teamName"},
      {title: "팀 코드", key: "teamCode"},
      {title: "팀장", key: "teamManagerName"},
    ];

    const baseUrl = process.env.VUE_APP_API_BASE_URL || 'http://localhost:8080';

    const fetchData = () => {
      axiosInstance.get(`${baseUrl}/api/members/details/${props.salesMembersCode}`)
        .then(response => {
          console.log(response.data.result)
          if (response.data && response.data.result) {
            const {
              profile: profileImageUrl,
              rank: memberRank,
              birthDay: birthday,
              name: memberName,
              phone: mobile,
              email: emailAddress,
              extensionNumber: extensionN,
              address: homeAddress,
              officeAddress: office,
              performanceReview: pr,
              teamName: teamNames,
              teamCode: teamCodes
            } = response.data.result;

            console.log(teamName)
            console.log(teamCode)

            profile.value = profileImageUrl
            rank.value = memberRank
            birthDay.value = birthday
            name.value = memberName
            phone.value = mobile
            email.value = emailAddress
            address.value = homeAddress
            extensionNumber.value = extensionN
            officeAddress.value = office
            performanceReview.value = pr
            teamName.value = teamNames
            teamCode.value = teamCodes
          } else {
            console.error('Empty response or missing result data');
          }
        })
        .catch(error => {
          console.error('Error fetching data:', error);
        });
      //팀 목록
      axiosInstance.get(`${baseUrl}/api/team/list`)
        .then(response => {
          const data = response.data.result;
          console.log(data)
          data.forEach((item, index) => {
            item.id = index + 1;
          });
          // tableRows에 데이터를 할당합니다.
          rows.value = data;
        })
        .catch(error => {
          console.log('Error fetching data:', error);
        });
    };


    const submitChange = () => {
      const data = {
        teamCode: teamCode.value,
        performanceReview: performanceReview.value,
        rank: rank.value,
      }
      if (confirm("수정하시겠습니까?")) {
        console.log(props.salesMembersCode)
        axiosInstance.patch(`${baseUrl}/api/members/admin/update/${props.salesMembersCode}`, data)
          .then(res => {
            console.log(res)
            alert("수정되었습니다.");
            modify.value = false;
            router.push({path: `/SalesMembersList/Detail/${props.salesMembersCode}`});
          })
          .catch(error => {
            console.error('Error updating data:', error);
          });
      }
    }

    function isModify() {
      modify.value = !modify.value
    }

    function modifyInfo() {
      submitChange();
      modify.value = false
    }

    function navigateToChangeTeam() {
      modalOpen.value = !modalOpen.value
    }

    function selectTeam(event, {item}) {
      teamName.value = item.teamName
      teamCode.value = item.teamCode
      closeModal();
    }

    function closeModal() {
      modalOpen.value = false
    }

    function deleteMember() {
      if (confirm("퇴사 처리하시겠습니까?")) {
        axiosInstance.delete(`${baseUrl}/api/members/delete/${props.salesMembersCode}`)
          .then(() => {
            alert("퇴사처리 되었습니다.")
            router.push({path: `/SalesMembersList`});
          })
          .catch(error => {
            console.log('Error fetching data:', error);
          });
      }
    }

    onMounted(() => {
      fetchData();
    });

    return {
      modifyInfo,
      isModify,
      closeModal,
      deleteMember,
      submitChange,
      navigateToChangeTeam,
      selectTeam,
      tableColumns,
      rows,
      modalOpen,
      profile,
      rank,
      birthDay,
      name,
      phone,
      email,
      address,
      extensionNumber,
      officeAddress,
      performanceReview,
      teamName,
      teamCode,
      modify,
    }
  }
}

</script>

<style scoped>

</style>

