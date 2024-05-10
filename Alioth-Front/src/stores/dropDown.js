import {defineStore} from 'pinia';

export const useDropdownStore = defineStore('dropdown', {
  state: () => ({
    isDropdown: false,
  }),

  getters: {
    getDropdown() {
      return this.$state.isDropdown;
    },
  },

  actions: {
    toggleDropdown() {
      this.isDropdown = !this.$state.isDropdown;
    },
  },
  persist: {
    enabled: true,
    strategies: [{ storage: localStorage }],
  }
});
