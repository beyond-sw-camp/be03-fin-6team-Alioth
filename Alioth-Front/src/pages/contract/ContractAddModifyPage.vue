<template>
  <AppSidebar></AppSidebar>
  <v-container fluid>
    <v-main>
      <AppHeader></AppHeader>
      <v-row justify="center">
        <v-col cols="12" md="8">
          <v-card>
            <v-card-text>
              <v-form ref="form" @submit.prevent="submitForm">
                <v-text-field v-model="form.contractDate" label="계약 시작 날짜" type="date" required></v-text-field>
                <v-text-field v-model="form.contractExpireDate" label="계약 만료 날짜" type="date" required></v-text-field>
                <v-text-field v-model="form.contractPeriod" label="계약 기간(년)" required></v-text-field>
                <v-text-field v-model="form.contractTotalPrice" label="계약 총 금액(원)" required></v-text-field>
                <v-text-field v-model="form.contractPaymentAmount" label="납입 금액(원)" required></v-text-field>
                <v-select v-model="form.contractPaymentFrequency" :items="paymentFrequencies" label="납입 빈도"
                          required></v-select>
                <v-text-field v-model="form.contractPaymentMaturityInstallment" label="만기 회차" type="number"
                              required></v-text-field>
                <v-text-field v-model="form.contractCount" label="납입 회차" type="number" required></v-text-field>
                <v-select v-model="form.contractPaymentMethod" :items="paymentMethods" label="납입 방식"
                          required></v-select>
                <v-text-field v-model="form.contractPayer" label="납입자" required></v-text-field>

                <!-- 보험상품 선택 모달 -->
                <v-text-field

                  readonly
                  v-model="form.selectedInsuranceProductName"
                  label="보험상품명"
                  @click="showInsuranceDialog = true"
                  required>
                  <template v-slot:append>
                    <v-icon @click="showInsuranceDialog = true">mdi-menu-down</v-icon>
                  </template>
                </v-text-field>

                <!-- 계약사원 선택 모달 -->
                <v-text-field
                  readonly
                  v-model="form.selectedContractMemberName"
                  label="계약사원 선택"
                  @click="showContractMemberDialog = true"
                  required>
                  <template v-slot:append>
                    <v-icon @click="showContractMemberDialog = true">mdi-menu-down</v-icon>
                  </template>
                </v-text-field>

                <!-- 고객 선택 모달 -->
                <v-text-field
                  readonly
                  v-model="form.selectedCustomerName"
                  label="고객 선택"
                  @click="showCustomerDialog = true"
                  required>
                  <template v-slot:append>
                    <v-icon @click="showCustomerDialog = true">mdi-menu-down</v-icon>
                  </template>
                </v-text-field>
                <v-select v-model="form.contractStatus" :items="contractStatuses" label="계약 상태" required></v-select>
                <v-textarea v-model="form.contractConsultation" label="상담 내용" required></v-textarea>

                <v-btn variant="tonal" color="#2979FF" style="margin-top: 15px;" type="submit">계약 생성</v-btn>
              </v-form>
            </v-card-text>
          </v-card>
          <!-- 모달 다이얼로그들 -->
          <v-dialog v-model="showInsuranceDialog" max-width="600px">
            <v-card>
              <v-card-title>보험상품 선택</v-card-title>
              <v-card-text>
                <v-list>
                  <v-list-item v-for="product in insuranceProducts" :key="product.id" @click="selectInsuranceProduct(product)">{{ product.name }}</v-list-item>
                </v-list>
              </v-card-text>
              <v-card-actions>
                <v-btn color="#2C3E50" @click="showInsuranceDialog = false">닫기</v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>

          <v-dialog v-model="showContractMemberDialog" max-width="600px">
            <v-card>
              <v-card-title>계약사원 선택</v-card-title>
              <v-card-text>
                <v-list>
                  <v-list-item
                    v-for="member in contractMembers"
                    :key="member.id"
                    @click="selectContractMember(member)">
                    {{ member.name }}
                  </v-list-item>
                </v-list>
              </v-card-text>
              <v-card-actions>
                <v-btn variant="tonal" color="#2C3E50" @click="showContractMemberDialog = false">닫기</v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>

          <v-dialog v-model="showCustomerDialog" max-width="600px">
            <v-card>
              <v-card-title>고객 선택</v-card-title>
              <v-card-text>
                <v-list>
                  <v-list-item
                    v-for="customer in customers"
                    :key="customer.id"
                    @click="selectCustomer(customer)">
                    {{ customer.name }}
                  </v-list-item>
                </v-list>
              </v-card-text>
              <v-card-actions>
                <v-btn variant="tonal" color="#2C3E50" @click="showCustomerDialog = false">닫기</v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-col>
      </v-row>
    </v-main>
  </v-container>
</template>

<script>
import AppSidebar from '@/layouts/AppSidebar.vue';
import AppHeader from '@/layouts/AppHeader.vue';
import axiosInstance from '@/plugins/loginaxios';

export default {
  components: {
    AppHeader,
    AppSidebar
  },
  data() {
    return {
      form: {
        contractDate: '',
        contractExpireDate: '',
        contractPeriod: '',
        contractTotalPrice: '',
        contractPaymentAmount: '',
        contractPaymentFrequency: '',
        contractPaymentMaturityInstallment: '',
        contractCount: '',
        contractPaymentMethod: '',
        contractPayer: '',
        contractConsultation: '',
        insuranceProductId: '',
        contractStatus: '',
        contractMemberId: '',
        customId: '',
        selectedInsuranceProductName: '', // 보험상품 이름
        selectedContractMemberName: '', // 계약사원 이름
        selectedCustomerName: '', // 고객 이름
      },
      paymentFrequencies: ['Monthly', 'Quarter', 'Yearly'],
      paymentMethods: ['계좌', '신용카드'],
      contractStatuses: ['New', 'Renewals'],
      insuranceProducts: [],
      contractMembers: [],
      customers: [],
      showInsuranceDialog: false,
      showContractMemberDialog: false,
      showCustomerDialog: false,
      selectedInsuranceProduct: {},
      selectedContractMember: {},
      selectedCustomer: {}
    };
  },
  mounted() {
    this.loadDropdownData();
  },
  methods: {
    loadDropdownData() {
      axiosInstance.get('/dummy/insurance-products')
        .then(response => {
          this.insuranceProducts = response.data.result;
        })
        .catch(error => {
          console.error('Failed to load insurance products:', error);
        });
      axiosInstance.get('/dummy/contract-members')
        .then(response => {
          this.contractMembers = response.data.result;
        })
        .catch(error => {
          console.error('Failed to load contract members:', error);
        });
      axiosInstance.get('/dummy/customers')
        .then(response => {
          this.customers = response.data.result;
        })
        .catch(error => {
          console.error('Failed to load customers:', error);
        });
    },
    selectInsuranceProduct(product) {
      this.form.insuranceProductId = product.id;
      this.form.selectedInsuranceProductName = product.name;
      this.showInsuranceDialog = false;
    },
    selectContractMember(member) {
      this.form.contractMemberId = member.id;
      this.form.selectedContractMemberName = member.name;
      this.showContractMemberDialog = false;
    },
    selectCustomer(customer) {
      this.form.customId = customer.id;
      this.form.selectedCustomerName = customer.name;
      this.showCustomerDialog = false;
    },
    formatDateTime(date) {
      return `${date}T00:00:00`;
    },
    submitForm() {
      const router = this.$router;
      if (this.$refs.form.validate()) {
        const formData = {
          ...this.form,
          contractDate: this.formatDateTime(this.form.contractDate),
          contractExpireDate: this.formatDateTime(this.form.contractExpireDate)
        };
        axiosInstance.post('/api/contract/create', formData)
          .then(() => {
            alert('계약이 성공적으로 생성되었습니다.');
            router.push('/ContractList');
          }).catch(error => {
          console.error('계약 생성에 실패했습니다:', error);
          alert('계약 생성에 실패했습니다: ' + (error.response.data.message ? error.response.data.message : '서버 에러'));
        });
      }
    }
  }
};
</script>

<style scoped>
.v-card-text {
  background-color: white
}

.v-text-field {
  /* background-color: #E3F2FD; */
  border-radius: 13px;
  padding: 10px;
  /* box-shadow: 0 4px 8px rgba(0,0,0,0.1); */

}

.v-card {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 13px;
}
</style>
