import HomePage from "/src/pages/main/HomePage.vue";
import MyPage from "@/pages/mypage/MyPage.vue";
import BoardListPage from "@/pages/board/BoardListPage.vue";
import SchedulePage from "@/pages/schedule/SchedulePage.vue";
import SalesMembersListPage from "@/pages/salesMember/SalesMembersListPage.vue";
import LoginPage from "@/pages/login/LoginPage.vue";
import SalesPage from "@/pages/sales/SalesPage.vue";
import ContractListPage from "@/pages/contract/ContractListPage.vue";
import BoardAddPage from "@/pages/board/BoardAddPage.vue";
import BoardModifyPage from "@/pages/board/BoardModifyPage.vue";
import SuggestionBoardAddPage from "@/pages/board/SuggestionBoardAddPage.vue"
import SuggestionBoardModifyPage from "@/pages/board/SuggestionBoardModifyPage.vue"
import SalesMembersAddPage from "@/pages/salesMember/SalesMembersAddPage.vue"
import SalesMembersDetailPage from "@/pages/salesMember/SalesMembersDetailPage.vue"
import SalesMembersModifyPage from "@/pages/salesMember/SalesMembersModifyPage.vue"
import PasswordFind from "@/pages/login/PasswordFind.vue"
import SalesPersonal from "@/pages/sales/SalesPersonal.vue"
import SalesRanking from "@/pages/sales/SalesRanking.vue"
import SalesTeam from "@/pages/sales/SalesTeam.vue"
import SalesTotal from "@/pages/sales/SalesTotal.vue"
import ContractAddModifyPage from "@/pages/contract/ContractAddModifyPage.vue"
import ContractCancelPage from "@/pages/contract/ContractCancelPage.vue"
import ContractDetailPage from "@/pages/contract/ContractDetailPage.vue"
import ContractModifyPage from "@/pages/contract/ContractModifyPage.vue"
import BoardDetailPage from "@/pages/board/BoardDetailPage.vue"
import SuggestionBoardDetail from "@/pages/board/SuggestionBoardDetailPage.vue"
import TeamListPage from "@/pages/team/TeamListPage.vue";
import TeamDetailPage from "@/pages/team/TeamDetailPage.vue";
import TeamAddPage from "@/pages/team/TeamAddPage.vue";


const routes = [
  {
    path: '/',
    component: HomePage,
    meta: {
      title: '대시보드'
    }
  },

// board
  {
    path: '/BoardList',
    component: BoardListPage,
    meta: {
      title: '게시판'
    }
  },
  {
    path: '/BoardList/Detail/:boardId',
    component: BoardDetailPage,
    meta: {
      title: '게시글 상세'
    }
  },
  {
    path: '/BoardList/Add',
    component: BoardAddPage,
    meta: {
      title: '게시글 작성'
    }
  },
  {
    path: '/BoardList/Modify/:boardId',
    component: BoardModifyPage,
    meta: {
      title: '게시글 수정'
    }
  },
  {
    path: '/BoardList/SuggestionBoardDetail',
    component: SuggestionBoardDetail,
    meta: {
      title: '건의사항 상세'
    }
  },
  {
    path: '/BoardList/SuggestionBoardAdd',
    component: SuggestionBoardAddPage,
    meta: {
      title: '건의사항 작성'
    }
  },
  {
    path: '/BoardList/SuggestionBoardModify',
    component: SuggestionBoardModifyPage,
    meta: {
      title: '건의사항 수정'
    }
  },

// schedule
  {
    path: '/Schedule',
    component: SchedulePage,
    meta: {
      title: '일정'
    }
  },

  // mypage
  {
    path: '/MyPage',
    component: MyPage,
    meta: {
      title: '마이페이지'
    }
  },

  //salesMember
  {
    path: '/SalesMembersList',
    component: SalesMembersListPage,
    meta: {
      title: '사원 목록'
    }
  },
  {
    path: '/SalesMembersList/Add',
    component: SalesMembersAddPage,
    meta: {
      title: '사원 추가'
    }
  },
  {
    path: '/SalesMembersList/Detail/:salesMembersCode',
    component: SalesMembersDetailPage,
    props:true,
    meta: {
      title: '사원 상세 정보'
    }
  },
  {
    path: '/SalesMembersList/Modify',
    component: SalesMembersModifyPage,
    meta: {
      title: '사원 정보 수정'
    }
  },

  //login
  {
    path: '/Login',
    component: LoginPage
  },
  {
    path: '/Login/PasswordFind',
    component: PasswordFind
  },


  //sales
  {
    path: '/Sales',
    component: SalesPage,
    meta: {
      title: '매출 메인'
    }
  },
  {
    path: '/Sales/Personal',
    component: SalesPersonal,
    meta: {
      title: '개인 매출'
    }
  },
  {
    path: '/Sales/Ranking',
    component: SalesRanking,
    meta: {
      title: '순위'
    }
  },
  {
    path: '/Sales/Team',
    component: SalesTeam,
    meta: {
      title: '팀 매출'
    }
  },
  {
    path: '/Sales/Total',
    component: SalesTotal,
    meta: {
      title: '전사 매출'
    }
  },


  //contract
  {
    path: '/ContractList',
    component: ContractListPage,
    meta: {
      title: '계약 목록'
    }
  },
  {
    path: '/ContractList/AddModify',
    component: ContractAddModifyPage,
    meta: {
      title: '계약 추가'
    }
  },
  {
    path: '/ContractList/Cancel/:id',
    component: ContractCancelPage,
    meta: {
      title: '계약 해지'
    }
  },
  {
    path: '/ContractList/Detail/:id',
    component: ContractDetailPage,
    meta: {
      title: '계약 상세정보'
    }
  },
  {
    path: '/ContractList/Modify/:id',
    component: ContractModifyPage,
    meta: {
      title: '계약 수정'
    }
  },
  {
    path: '/Team/List',
    component: TeamListPage,
    meta: {
      title: '팀 목록'
    }
  },
  {
    path: '/Team/Detail/:teamCode',
    component: TeamDetailPage,
    props: true,
    meta: {
      title: '팀 상세정보'
    }
  },
  {
    path: '/Team/Add',
    component: TeamAddPage,
    meta: {
      title: '팀 추가'
    }
  },
]

export default routes
