<template>
  <AppSidebar></AppSidebar>
  <v-main>
    <AppHeader></AppHeader>
    <v-container fluid>
      <v-row justify="center">
        <v-col cols="12" md="8">
          <v-card>
            <v-card-text>
              <v-form @submit.prevent="submitForm">
                <span>팀 명</span>
                <v-text-field v-model="form.teamName" label="이름을 입력하세요" required></v-text-field>
                <span>팀장</span>
                <v-spacer></v-spacer>
                <v-btn id="postcode" type="button" @click="navigateToList" value="매니저 목록">조회</v-btn>
                <v-text-field type="text" v-model="form.name" placeholder="이름" readonly/>
                <v-text-field type="text" v-model="form.teamManagerCode" placeholder="사원번호" readonly/>
                <v-spacer></v-spacer>
                <v-btn color="primary" type="submit">등록</v-btn>
              </v-form>
            </v-card-text>
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
            <ListComponent :columns="tableColumns" :rows="rows" @click:row="select"></ListComponent>
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

import {onMounted, ref} from 'vue';
import AppSidebar from "@/layouts/AppSidebar.vue";
import AppHeader from "@/layouts/AppHeader.vue";
import axiosInstance from "@/plugins/loginaxios";
import router from "@/router";
import ListComponent from "@/layouts/ListComponent.vue";

export default {
  components: {ListComponent, AppHeader, AppSidebar},
  setup() {
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
    const modalOpen = ref(false);

    function navigateToList() {
      modalOpen.value = !modalOpen.value
    }

    function select(event, {item}) {
      form.value.name = item.name
      form.value.teamManagerCode = item.salesMemberCode
      closeModal();
    }

    const fetchData = () => {
      const baseUrl = process.env.VUE_APP_API_BASE_URL || 'http://localhost:8080';
      axiosInstance.get(`${baseUrl}/api/members/list/manager`)
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
    onMounted(() => {
      fetchData();
    });

    const submitForm = () => {
      if (form.value) {
        const formData = {
          ...form.value,
        };
        console.log(formData)
        const baseUrl = process.env.VUE_APP_API_BASE_URL || 'http://localhost:8080';
        axiosInstance.post(`${baseUrl}/api/team/create`, formData)
          .then(response => {
            alert(response.data.message)
            router.push('/Team/List');
          }).catch(error => {
          console.error(error.response.data.message);
          alert(error.response.data.message)
        });
      }
    };

    function closeModal() {
      modalOpen.value = false
    }

    return {
      teamName,
      modalOpen,
      tableColumns,
      rows,
      name,
      teamManagerCode,
      select,
      closeModal,
      form,
      navigateToList,
      submitForm,
    };
  }
}
</script>

<style scoped>

</style>
