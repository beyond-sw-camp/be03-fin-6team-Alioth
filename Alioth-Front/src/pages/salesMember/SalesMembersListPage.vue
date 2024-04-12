<template>
  <AppSidebar></AppSidebar>
  <v-main>
    <AppHeader></AppHeader>
    <v-container fluid>
      <h2>사원 목록 조회(관리자)</h2>
      <v-divider></v-divider>
      <p><button onclick="location.href=`/SalesMembersList/Add`">사원추가</button></p>


          <thead>
          <tr>
            <th class="text-left">이름</th>
            <th class="text-left">직급</th>
            <th class="text-left">소속</th>
            <th class="text-left">입사일</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="item in employees" :key="item.name">
            <td><router-link :to="{ path: '/SalesMembersList/Detail', query: { name: item.name } }">{{ item.name }}</router-link></td>
            <td>{{ item.position }}</td>
            <td>{{ item.department }}</td>
            <td>{{ item.hireDate }}</td>
          </tr>
          </tbody>


    </v-container>
  </v-main>
</template>

<script>
import AppSidebar from "@/layouts/AppSidebar.vue";
import AppHeader from "@/layouts/AppHeader.vue";

export default {
  components: {AppHeader, AppSidebar},
  setup() {
    const employees = [
      {
        name: '죠니',
        position: '사장',
        department: '마케팅영업',
        hireDate: '1994년 12월 24일',
      },
      // 다른 사원 데이터도 이 배열에 추가할 수 있습니다.
    ];

    return { employees }
  }
}
</script>

<style scoped>

</style>
