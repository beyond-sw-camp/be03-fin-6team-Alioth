<template>
  <v-navigation-drawer location="left" permanent image=""  color="">
    <v-list >
      <v-list-item >
        <v-list-item-avatar >
          <v-row>
            <img src="/Alioth.png" style="width: 60px; height: 60px; margin-right: 10px;"/>
            <v-list-item-title style="align-self: center;"  @click="handleMenuClick(`/`);" >Alioth</v-list-item-title>
          </v-row>
        </v-list-item-avatar>
      </v-list-item>
    </v-list>
    <v-divider style="margin-top: 1vw;"></v-divider>
    <v-list density="compact" nav>
      <v-list-item prepend-icon="mdi-home" value="dashboard" @click="handleMenuClick(`/`);" :class="{ 'custom-icon-color': isMenuActive(`/`) }">
        <span :style="{ 'font-weight': isMenuActive(`/`) ? 'bold' : 'normal', 'font-size': isMenuActive(`/`) ? '11pt' : '10pt'  }">대시보드</span>
      </v-list-item>
      <v-list-item prepend-icon="mdi-network-pos" value="sales" ref="parent" :class="{ 'custom-icon-color': isMenuActive(`/Sales`) }">
        <v-list-item-title v-for="(folder, index) in folders" :key="index" >
          <v-list-item-title  @click="handleSubMenuClick(`/Sales`);">
            <span :style="{ 'font-weight': isMenuActive(`/Sales`) ? 'bold' : 'normal', 'font-size': isMenuActive(`/Sales`) ? '11pt' : '10pt'  }">{{ folder.title }}</span>
            </v-list-item-title>
          <v-list v-if="dropDownStore.getDropdown">
            <v-list-item v-for="(subItem, subIndex) in folder.subItems" :key="subIndex" :class="{ 'custom-icon-color': isMenuActive(subItem.url.toString()) }"
                         @click="this.$router.push(subItem.url.toString())">
              <v-list-item-title>
                <span :style="{ 'font-weight': isMenuActive(subItem.url.toString()) ? 'bold' : 'normal', 'font-size': '10.5pt' }">{{ subItem.title  }}</span>
              </v-list-item-title>
            </v-list-item>
          </v-list>
        </v-list-item-title>
      </v-list-item>
      <v-list-item v-if="loginStore.memberRank === 'HQ'" prepend-icon="mdi-account-multiple" value="shared" :class="{ 'custom-icon-color': isMenuActive(`/SalesMembersList`) }"
                   @click="this.$router.push(`/SalesMembersList`);">
        <span :style="{ 'font-weight': isMenuActive(`/SalesMembersList`) ? 'bold' : 'normal', 'font-size': isMenuActive(`/SalesMembersList`) ? '11pt' : '10pt' }">사원</span>
      </v-list-item>
      <v-list-item prepend-icon="mdi-format-list-bulleted" value="starred" :class="{ 'custom-icon-color': isMenuActive(`/BoardList`) }"
                   @click="handleMenuClick(`/BoardList`);">
        <span :style="{ 'font-weight': isMenuActive(`/BoardList`) ? 'bold' : 'normal', 'font-size': isMenuActive(`/BoardList`) ? '11pt' : '10pt' }">게시판</span>
      </v-list-item>
      <v-list-item prepend-icon="mdi-file-sign"  value="starred" :class="{ 'custom-icon-color': isMenuActive(`/ContractList`) }"
                   @click="handleMenuClick(`/ContractList`);">
        <span :style="{ 'font-weight': isMenuActive('/ContractList') ? 'bold' : 'normal', 'font-size': isMenuActive(`/ContractList`) ? '11pt' : '10pt' }">계약</span>
      </v-list-item>
      <v-list-item prepend-icon="mdi-calendar-check" value="starred" :class="{ 'custom-icon-color': isMenuActive(`/Schedule`) }"
                   @click="handleMenuClick(`/Schedule`);">
        <span :style="{ 'font-weight': isMenuActive('/Schedule') ? 'bold' : 'normal', 'font-size': isMenuActive(`/Schedule`) ? '11pt' : '10pt' }">일정</span>
      </v-list-item>
      <v-list-item v-if="loginStore.memberRank === 'HQ'" prepend-icon="mdi-account-details" value="starred" :class="{ 'custom-icon-color': isMenuActive(`/Team/List`) }"
                   @click="handleMenuClick(`/Team/List`);">
        <span :style="{ 'font-weight': isMenuActive('/Team/List') ? 'bold' : 'normal', 'font-size': isMenuActive(`/Team/List`) ? '11pt' : '10pt'  }">팀 목록</span>
      </v-list-item>
      <v-list-item v-if="loginStore.memberRank === 'MANAGER'" prepend-icon="mdi-badge-account-horizontal-outline" :class="{ 'custom-icon-color': isMenuActive(`/Team/Detail/` + this.loginStore.memberTeamCode) }"
                   value="starred" @click="handleMenuClickWithTeamCodeCheck">
        <span :style="{ 'font-weight': isMenuActive(`/Team/Detail/` + this.loginStore.memberTeamCode) ? 'bold' : 'normal', 'font-size': isMenuActive(`/Team/Detail/` + this.loginStore.memberTeamCode) ? '11pt' : '10pt'  }">팀</span>
      </v-list-item>
    </v-list>

    <template v-slot:append>
      <v-list>
        <span class="small-text"> 오늘도 좋은 하루 보내세요! </span>
        <v-list-item :prepend-avatar="loginStore.memberImage"
                     @click="this.$router.push(`/SalesMembersList/Detail/${loginStore.memberCode}`);">
          <template v-if="loginStore.memberRank">
            <v-list-item-title>{{ loginStore.memberEmail }}</v-list-item-title>
            <v-row align="center" no-gutters>
              <v-col cols="11">
                <v-list-item-subtitle>{{ loginStore.memberName }}</v-list-item-subtitle>
              </v-col>
              <v-col cols="1" class="d-flex justify-end">
                <v-icon color="#424242" @click="confirmLogout">mdi-logout</v-icon>
              </v-col>
            </v-row>
          </template>
        </v-list-item>
      </v-list>
    </template>
  </v-navigation-drawer>

</template>

<script>
import {useDropdownStore} from '@/stores/dropDown'
import {useLoginInfoStore} from "@/stores/loginInfo";

export default {
  data() {
    const loginStore = useLoginInfoStore(); // 로그인 정보 스토어 가져오기
    let salesSubItems = []; // 매출 관련 하위 메뉴 초기화

    // 사용자 등급에 따라 매출 하위 메뉴 동적 할당
    if (loginStore.memberRank === 'HQ') {
      salesSubItems = [
        {title: '순위', url: '/Sales/Ranking'},
        {title: '개인', url: '/Sales/Personal'},
        {title: '팀', url: '/Sales/Team'},
        {title: '전사', url: '/Sales/Total'},
      ];
    } else if (loginStore.memberRank === 'MANAGER') {
      salesSubItems = [
        {title: '순위', url: '/Sales/Ranking'},
        {title: '개인', url: '/Sales/Personal'},
        {title: '팀', url: '/Sales/Team'},
      ];
    } else if (loginStore.memberRank === 'FP') {
      salesSubItems = [
        {title: '순위', url: '/Sales/Ranking'},
        {title: '개인', url: '/Sales/Personal'},
      ];
    }

    return {
      dropDownStore: useDropdownStore(),
      loginStore,
      folders: [
        {
          title: '매출',
          subItems: salesSubItems
        }
      ],
    };
  },
  methods: {
    isMenuActive(path){
      return this.$route.path === path;
    },
    handleMenuClickWithTeamCodeCheck() {
      const teamCode = this.loginStore.memberTeamCode;
      if (!teamCode || teamCode === " ") {
        alert('담당 팀이 없습니다.');
        return;
      }
      this.handleMenuClick(`/Team/Detail/` + this.loginStore.memberTeamCode);
    },
    handleMenuClick(route) {
      this.$router.push(route);
    },
    handleSubMenuClick(route) {
      this.dropDownStore.toggleDropdown()
      this.$router.push(route);
    },
    confirmLogout() {
      if (confirm('로그아웃하시겠습니까?')) {
        alert('로그아웃 되었습니다.');
        localStorage.removeItem('accessToken'); // 토큰 삭제
        localStorage.removeItem('refreshToken');
        this.$router.push('/Login');
        window.location.reload();
      }
    }
  }
}
</script>

<style scoped>
.alioth-title {
  font-size: 30pt;
  font-family: Poppins,sans-serif;/* 원하는 폰트 크기로 조정 */
  color: #192230; /* 원하는 색상으로 변경 */
}
.alioth-item .v-avatar {
  width: 100pt; /* 원하는 크기로 조정 */
  height: 100pt; /* 원하는 크기로 조정 */
}
.small-text {
  font-size: 10px; /* 원하는 작은 크기로 조정 */
  text-align: center;
  margin: 0 auto;
  display: block;
}

.v-btn--icon {
  border-radius: 0;
  padding: 0;
  background: transparent;
}

.v-btn--icon .v-icon {
  color: inherit; /* 아이콘 색상을 버튼 색상과 동일하게 설정 */
}
.custom-icon-color {
  color: white;
  background-color: #42597D; /* 아이콘의 색상을 빨간색으로 지정합니다. */
}

</style>

