import {defineStore} from 'pinia';

export const useSalesRankingStore = defineStore('SalesRankingStore', {
  state: () => ({
    startDate: "",
  }),

  getters: {
    getStartDate() {
      return this.$state.startDate;
    },

  },

  actions: {
    toggleDropdown() {
      this.isDropdown = !this.$state.isDropdown;
    },
  },
});
