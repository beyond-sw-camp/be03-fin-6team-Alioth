import {defineStore} from 'pinia';

export const useSalesStore = defineStore('SalesStore', {
  state: () => ({
    startDate: "",
    startTeamDate: "",
    startHQDate: "",
    salesPersonal: "월",
    salesTeam: "월",
    salesHQ: "월",
  }),

  getters: {
    getStartDate() {
      return this.$state.startDate;
    },
    getStartTeamDate() {
      return this.$state.startTeamDate;
    },
    getStartHQDate() {
      return this.$state.startHQDate;
    },
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
