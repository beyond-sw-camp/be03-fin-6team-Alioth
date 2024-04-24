import {defineStore} from 'pinia';

export const useSalesStore = defineStore('SalesStore', {
  state: () => ({
    salesPersonal: "월",
    salesTeam: "월",
    salesHQ: "월",
  }),

  getters: {
    getSalesPersonal() {
      return this.$state.salesPersonal;
    },
    getSalesTeam() {
      return this.$state.salesTeam;
    },
    getSalesHQ() {
      return this.$state.salesHQ;
    },
  },

  actions: {
    toggleDropdown() {
      this.isDropdown = !this.$state.isDropdown;
    },
  },
});
