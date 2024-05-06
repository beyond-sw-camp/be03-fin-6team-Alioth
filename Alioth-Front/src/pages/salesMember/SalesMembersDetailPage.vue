<template>
  <AppSidebar></AppSidebar>
  <v-container fluid>
  <v-main>
    <AppHeader></AppHeader>
      <v-col class="text-right">
        <v-btn variant="tonal" color="#2979FF" style="margin-right: 0.5vw;" @click="isModify" v-if="!modify && (loginStore.getMemberRank !== 'FP') && (salesMembersCode.toString() !== loginStore.memberCode.toString())">수정</v-btn>
        <v-btn variant="tonal" color="#2979FF" @click="submitChange" v-if="modify"> 완료</v-btn>
        <v-btn variant="tonal" color="primary" @click="deleteMember" v-if="!modify && loginStore.getMemberRank === 'HQ' && (salesMembersCode.toString() !== loginStore.memberCode.toString())">삭제</v-btn>
      </v-col>
      <v-row>
        <!-- Image Upload -->
        <v-col cols="4">
          <v-card class="myimage pa-3">
            <input type="file" style="display: none" ref="imageInput" @change="handleImageUpload">
            <img class="default-image" :src="imageUrl" @click="openImageUploader" v-if="(loginStore.memberCode.toString()===salesMembersCode)">
          </v-card>
        </v-col>
        <v-col cols="7">
          <v-card class="pa-3">
            <!-- Name, Position, and Employee Number -->
            <v-row>
              <v-col cols="4">
                <h5>이름</h5>
                <h3>{{ name }}</h3>
              </v-col>
              <v-col cols="4">
                <h5>사원번호</h5>
                <h3>{{ salesMembersCode }}</h3>
              </v-col>
              <v-col cols="4">
                <h5>직급</h5>
                <div v-if="!modify">{{ rank }}</div>
                <v-select v-if="modify" v-model="rank" :items="['FP', 'MANAGER', 'HQ']"></v-select>
              </v-col>
            </v-row>
            <v-row>
              <v-col cols="4">
                <h5>팀명</h5>
                <div class="d-flex align-center justify-end">
                  <v-btn @click="navigateToChangeTeam" v-if="modify && loginStore.getMemberRank==='HQ'" class="btn-small">팀 목록</v-btn>
                </div>
                <div>{{ teamName }}</div>
              </v-col>
              <v-col cols="4">
                <h5>고과평가</h5>
                <v-col cols="12" md="4" class="text-right">
                </v-col>
                <v-card-text v-if="!modify">{{ performanceReview }}</v-card-text>
                <v-select v-if="modify" v-model="performanceReview" :items="['A', 'B', 'C', 'D']"></v-select>
              </v-col>
            </v-row>
            <v-card class="pa-2 mt-3"> <!-- Added mt-3 for some margin-top -->
              <h3>상세 설명</h3>
              <div class="d-flex align-end mb-2">
                <div class="flex-grow-1"></div>
                <v-btn variant="tonal" color="#2979FF" class="detail-text ma-2 pa-2" @click="openDetailModal" v-if="loginStore.memberCode.toString()===salesMembersCode"> 수정 </v-btn>
              </div>
              <div class="divider"></div>
              <div class="d-flex align-start mb-2">
                <h4 class="ma-2 pa-2">회사 주소</h4>
                <h4 class="ma-2 pa-2">{{ officeAddress }}</h4>
              </div>
              <div class="d-flex align-start mb-2">
                <h4 class="ma-2 pa-2">집 주소</h4>
                <h4 class="ma-2 pa-2">({{zoneCode}}) {{roadAddress}} {{detailAddress}}</h4>
              </div>
              <div class="d-flex align-start mb-2">
                <h4 class="ma-2 pa-2">이메일</h4>
                <h4 class="ma-2 pa-2">{{ email }}</h4>
              </div>
              <div class="d-flex align-start mb-2">
                <h4 class="ma-2 pa-2">연락처</h4>
                <h4 class="ma-2 pa-2">{{ phone }}</h4>
              </div>
              <div class="d-flex align-start mb-2">
                <h4 class="ma-2 pa-2">내선번호</h4>
                <h4 class="ma-2 pa-2">{{ extensionNumber }}</h4>
              </div>
              <div class="d-flex align-start mb-2">
                <h4 class="ma-2 pa-2">생년월일</h4>
                <h4 class="ma-2 pa-2">{{ birthDay }}</h4>
              </div>
            </v-card>
          </v-card>
        </v-col>
      </v-row>
  </v-main>
  </v-container>
  <v-dialog v-model="modalOpen">
    <v-card>
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
        <v-col class="text-right">
          <v-btn color="#2C3E50" variant="tonal" @click="closeModal">닫기</v-btn>
        </v-col>
      </v-card-actions>
    </v-card>
  </v-dialog>
  <v-dialog v-model="isModalOpen" width="auto">
    <v-card prepend-icon="" title="정보 수정">
      <v-card-text>
        <v-row dense>
          <v-col cols="12" md="4" sm="6">
            <v-text-field label="이메일" v-model="email"></v-text-field>
          </v-col>
          <v-col cols="12" md="4" sm="6">
            <v-text-field label="휴대폰번호" v-model="phone"></v-text-field>
          </v-col>
          <v-col cols="12" md="4" sm="6">
            <v-text-field label="내선 번호" v-model="extensionNumber"></v-text-field>
          </v-col>
          <v-col cols="12" md="4" sm="6">
            <v-text-field label="생년월일" type="date" v-model="birthDay"></v-text-field>
          </v-col>
          <v-col cols="12" md="8" sm="6">
            <v-text-field label="사무실"></v-text-field>
          </v-col>
          <v-col cols="12" md="12" sm="12">
            <span>자택주소</span>
            <v-spacer></v-spacer>
            <span style="font-size: 0.8em;">우편번호</span>
            <v-spacer></v-spacer>
            <v-text-field type="text" v-model="zoneCode" placeholder="우편번호" readonly/>
            <v-btn id="postcode" type="button" @click="openPostCode" value="우편번호 찾기">우편번호 찾기</v-btn>
            <v-text-field type="text" v-model="roadAddress" placeholder="도로명주소" readonly/>
            <span id="guide" style="color:#999;display:none"></span>
            <v-text-field type="text" v-model="detailAddress" placeholder="상세주소"/>
          </v-col>
        </v-row>
      </v-card-text>
      <v-divider></v-divider>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn text="닫기" variant="plain" @click="closeMyPageModal"></v-btn>
        <v-btn color="primary" text="저장" variant="tonal" @click="updateMyPage"></v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import AppSidebar from "@/layouts/AppSidebar.vue";
import AppHeader from "@/layouts/AppHeader.vue";
import axiosInstance from "@/plugins/loginaxios";
import router from "@/router";
import {computed, onMounted, ref} from "vue";
import ListComponent from "@/layouts/ListComponent.vue";
import {useLoginInfoStore} from "@/stores/loginInfo";

export default {
  components: { ListComponent, AppHeader, AppSidebar},
  props: ["salesMembersCode"],
  setup(props) {
    const loginStore = useLoginInfoStore();
    const profile = ref('');
    const rank = ref('');
    const name = ref('');
    const phone = ref('');
    const email = ref('');
    const birthDay = ref('');
    const extensionNumber = ref('');
    const zoneCode = ref('');
    const roadAddress = ref('');
    const detailAddress = ref('');
    const officeAddress = ref('');
    const performanceReview = ref('');
    const teamName = ref('');
    const teamCode = ref('');
    const modify = ref(false);
    const modalOpen = ref(false);
    const isModalOpen = ref(false)
    const rows = ref([]);
    const salesMembersCodeTemp = ref('');
    const tableColumns = [
      {title: "팀", key: "teamName"},
      {title: "팀 코드", key: "teamCode"},
      {title: "팀장", key: "teamManagerName"},
    ];
    const modalContainer = ref(null)
    const imageInput = ref(null);
    const formatDateTime = (date) => {
      return `${date}`;
    };
    const baseUrl = import.meta.env.VITE_API_SERVER_BASE_URL || 'http://localhost:8080';

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
              zoneCode: zonecode,
              roadAddress: roadAddr,
              detailAddress: detailAddr,
              officeAddress: office,
              performanceReview: pr,
              teamName: teamNames,
              teamCode: teamCodes
            } = response.data.result;

            profile.value = profileImageUrl
            rank.value = memberRank
            birthDay.value = birthday
            name.value = memberName
            phone.value = mobile
            email.value = emailAddress
            zoneCode.value = zonecode
            roadAddress.value = roadAddr
            detailAddress.value = detailAddr
            extensionNumber.value = extensionN
            officeAddress.value = office
            performanceReview.value = pr
            teamName.value = teamNames
            teamCode.value = teamCodes
            salesMembersCodeTemp.value = props.salesMembersCode
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
        console.log("로그인 : "+ loginStore.getMemberCode)
        axiosInstance.patch(`${baseUrl}/api/members/admin/update/${props.salesMembersCode}`, data)
          .then(res => {
            console.log(res)
            alert("수정되었습니다.");
            modify.value = false;
            router.push({path: `/SalesMembersList/Detail/${props.salesMembersCode}`});
          })
          .catch(error => {
            console.error('Error updating data:', error);
            alert("수정할 수 없습니다. 다시 확인해주세요")
          });
      }
    }

    function handleModalClick(event) {
      // 모달 배경 클릭 시 모달 닫기
      if (event.target === modalContainer.value) {
        closeModal();
      }
    }

    function isModify() {
      modify.value = !modify.value
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
    function openDetailModal() {
      isModalOpen.value = true;
    }
    function closeMyPageModal() {
      isModalOpen.value = false;
      window.location.reload(true);
    }

    const imageUrl = computed(() => loginStore.memberImage);


    function handleImageUpload(event) {
      const file = event.target.files[0];
      // Perform any necessary validation here
      profile.value = URL.createObjectURL(file);

      const formData = new FormData();
      formData.append('memberImage', file);

      const url = `${baseUrl}/api/members/${props.salesMembersCode.toString()}/image`;
      axiosInstance.patch(url, formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
        .then(response => {
          loginStore.memberImage = response.data
        })
        .catch(error => {
          console.error("회원정보를 요청할 수 없습니다. : ", error);
          alert("회원정보를 요청할 수 없습니다.");
        });
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
   function updateMyPage() {
     console.log("회원정보 업데이트 요청");
     const data = {
       email: email.value,
       phone: phone.value,
       birthDay: birthDay.value,
       officeAddress: officeAddress.value,
       extensionNumber: extensionNumber.value,
       zoneCode:zoneCode.value,
       roadAddress:roadAddress.value,
       detailAddress:detailAddress.value
     };
     console.log(data);
     axiosInstance.patch(`${baseUrl}/api/members/details/update`, data)
       .then(response => {
         console.error("res결과: " + response);
         closeMyPageModal();
       })
       .catch(error => {
         console.error("회원정보를 수정할 수 없습니다. : ", error);

       });

   }
    function openImageUploader() {
      imageInput.value.click();
    }

    function openPostCode() {
      new window.daum.Postcode({
        oncomplete: (data) => {
          zoneCode.value = data.zonecode
          roadAddress.value = data.roadAddress
        },
      }).open();
    }

    onMounted(() => {
      fetchData();
      document.addEventListener('click', handleModalClick);
    });

    return {
      isModify,
      closeModal,
      deleteMember,
      submitChange,
      navigateToChangeTeam,
      selectTeam,
      handleModalClick,
      closeMyPageModal,
      handleImageUpload,
      openImageUploader,
      openDetailModal,
      updateMyPage,
      formatDateTime,
      openPostCode,
      isModalOpen,
      tableColumns,
      rows,
      modalOpen,
      profile,
      rank,
      birthDay,
      name,
      phone,
      email,
      zoneCode,
      roadAddress,
      detailAddress,
      extensionNumber,
      officeAddress,
      performanceReview,
      teamName,
      teamCode,
      modify,
      loginStore,
      imageUrl,
      imageInput,
      modalContainer
    }
  }
}

</script>

<style scoped>
.btn-small {
  font-size: smaller;
  padding: 5px 5px; /* 원하는 크기로 조절 */
}
.default-image {
  width: 350px;
  height: 670px;
}
.myimage {
  height: 698px;
  width: 380px;
  position:absolute;
}
</style>

