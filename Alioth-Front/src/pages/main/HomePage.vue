<template>
  <AppSidebar></AppSidebar>
  <v-container fluid>

    <v-main>
      <AppHeader></AppHeader>

      <v-row class="mt-10">
        <!-- First Place -->
        <v-col cols="6">
          <v-card class="pa-5 text-center" color="primary" dark>
            <v-row align="center" justify="start">
              <v-col cols="5">
                <v-icon size="72" color="yellow">mdi-crown</v-icon>
              </v-col>
              <v-col cols="auto">
                <h1 class="headline white--text">보험의 신</h1>
              </v-col>
            </v-row>
          </v-card>
          <v-card class="pa-5 text-center" color="#FF9800" dark>
            <h1 class="headline white--text">{{ godMember }}</h1>
          </v-card>
          <v-card class="pa-5 text-center" color="#FF5252" dark>
            <v-row align="center" justify="center">
              <h2 class="headline white--text">계약 금액 : </h2>
              <h2 class="headline white--text ml-5">{{ godPrice }}</h2>
            </v-row>
          </v-card>
          <v-card class="pa-5 text-center" color="#D50000" dark>
            <v-row align="center" justify="center">
              <h2 class="headline white--text">계약 건 : </h2>
              <h2 class="headline white--text ml-5">{{ godCount }}</h2>
            </v-row>
          </v-card>
        </v-col>

        <!-- Second Place -->
        <v-col cols="6">
          <v-card class="pa-5 text-center" color="#1E88E5" dark>
            <v-row align="center" justify="start">
              <v-col cols="5">
                <v-icon size="72" color="yellow">mdi-medal</v-icon>
              </v-col>
              <v-col cols="auto">
                <h1 class="headline white--text">최우수 지점</h1>
              </v-col>
            </v-row>
          </v-card>
          <v-card class="pa-5 text-center" color="#4DD0E1" dark>
            <h1 class="headline white--text">{{ bestTeamName }}</h1>
          </v-card>
          <v-card class="pa-5 text-center" color="#039BE5">
            <v-row align="center" justify="center">
              <h2 class="headline white--text">계약 금액 : </h2>
              <h2 class="headline white--text ml-5">{{ bestTeamPrice }}</h2>
            </v-row>
          </v-card>
          <v-card class="pa-5 text-center" color="#0277BD" dark>
            <v-row align="center" justify="center">
              <h2 class="headline white--text">계약 건 : </h2>
              <h2 class="headline white--text ml-5">{{ bestTeamCount }}</h2>
            </v-row>
          </v-card>
        </v-col>
      </v-row>

      <v-row class="mt-10" align="center" justify="center"/>
      <v-row class="mt-10" align="center" justify="center"/>
      <v-card class="pa-5 mt-10 text-center" color="#C8E6C9" dark>
        <v-row align="center" justify="center">
          <h2 class="headline white--text">공지사항 미리보기</h2>
        </v-row>
      </v-card>
      <ListComponent
          :columns="sugHeaders"
          :rows="formattedItems"
          @click="this.$router.push('/BoardList')"
        />

      <v-row class="mt-10" align="center" justify="center"/>
      <!-- <v-card class="pa-5 mt-10 text-center" color="#C8E6C9" dark>
        <v-row align="center" justify="center">
          <h2 class="headline white--text">건의사항 미리보기</h2>
        </v-row>
      </v-card> -->
      <!-- <ListComponent
          :columns="annHeaders"
          :rows="formattedAnnItems"
          @click="this.$router.push('/BoardList')"
        /> -->

    </v-main>
  </v-container>

</template>

<script>
import AppHeader from "@/layouts/AppHeader.vue";
import AppSidebar from "@/layouts/AppSidebar.vue";
import { ref } from 'vue';
import axios from 'axios';
import ListComponent from "@/layouts/ListComponent.vue";

export default {
  components: {
    AppSidebar, AppHeader, ListComponent,
  },
  setup() {
    const godMember = ref("-");
    const godPrice = ref("-");
    const godCount = ref("-");
    const bestTeamName = ref("-");
    const bestTeamPrice = ref("-");
    const bestTeamCount = ref("-");

    return {
      godMember, godPrice, godCount,
      bestTeamName, bestTeamPrice, bestTeamCount,
    }
  },
  data() {
    return {
      sugItems: [],
      sugHeaders: [
        { title: 'No', key: 'id' },
        { title: '제목', key: 'title' },
        { title: '글쓴이', key: 'memberName' },
        { title: '작성시간', key: 'created' },
      ],
      annItems: [],
      annHeaders: [
        { title: 'No', key: 'id' },
        { title: '제목', key: 'title' },
        { title: '글쓴이', key: 'memberName' },
        { title: '작성시간', key: 'created' },
      ],
    }
  },
  mounted() {
    this.getMemberGod();
    this.getBestTeam();
    this.getSugBoard();
    this.getAnnBoard();
  },
  computed: {
    formattedItems() {
      return this.sugItems.map(sugItems => ({ ...sugItems }));
    },
    formattedAnnItems() {
      return this.annItems.map(annItems => ({ ...annItems }));
    },
  },
  methods: {
    async getMemberGod() {
      const baseUrl = import.meta.env.VITE_API_STATISTICS_BASE_URL || 'http://localhost:8081';
      //let url = "http://localhost:8081/statistics/api/dashboard/god";
      const url = `${baseUrl}/api/dashboard/god`;
      console.log(url);
      await axios.get(url)
        .then(response => {
          //console.log("보험의 신 응답결과 : ", response);
          const resultData = response.data.result;
          //console.log("보험의 신 응답결과 : ", resultData);
          this.godMember = resultData.name;
          this.godPrice = resultData.price;
          this.godCount = resultData.count;
        })
        .catch(error => {
          console.log("요청할 수 없습니다. : ", error);
        });
    },
    async getBestTeam() {
      const baseUrl = import.meta.env.VITE_API_STATISTICS_BASE_URL || 'http://localhost:8081';
      //let url = "http://localhost:8081/statistics/api/dashboard/best-team";
      const url = `${baseUrl}/api/dashboard/best-team`;

      console.log(url);
      await axios.get(url)
        .then(response => {
          //console.log("보험의 신 응답결과 : ", response);
          const resultData = response.data.result;
          console.log("최우수 고과팀 응답결과 : ", resultData);
          this.bestTeamName = resultData.teamName;
          this.bestTeamPrice = resultData.price;
          this.bestTeamCount = resultData.count;
        })
        .catch(error => {
          console.log("요청할 수 없습니다. : ", error);
        });
    },
    async getSugBoard() {
      const baseUrl = import.meta.env.VITE_API_STATISTICS_BASE_URL || 'http://localhost:8081';
      const url = `${baseUrl}/api/dashboard/board/sug`;
      console.log(url);
      axios.get(url)
        .then(response => {
          console.log("공지사항 리스트 응답결과 : ", response.data.result);
          //this.teamItems = response.data.result || [];
          this.sugItems = response.data.result || [];
          this.sugItems = this.sugItems.map((sugItems, index) => ({
            ...sugItems,
            id: index + 1,
          }));
          console.log(this.sugItems);
        })
        .catch(error => {
          console.log("요청할 수 없습니다. : ", error);
        });
    },
    async getAnnBoard() {
      const baseUrl = import.meta.env.VITE_API_STATISTICS_BASE_URL || 'http://localhost:8081';
      const url = `${baseUrl}/api/dashboard/board/ann`;
      console.log(url);
      axios.get(url)
        .then(response => {
          console.log("건의사항 리스트 응답결과 : ", response.data.result);
          //this.teamItems = response.data.result || [];
          this.annItems = response.data.result || [];
          this.annItems = this.annItems.map((annItems, index) => ({
            ...annItems,
            id: index + 1,
          }));
          console.log(this.annItems);
        })
        .catch(error => {
          console.log("요청할 수 없습니다. : ", error);
        });
    },

  },

}
</script>

<style scoped>

</style>

