import {defineStore} from 'pinia';

export const useMyPageUpdateStore = defineStore('myPageUpdate', {
  state: () => ({
    memberInfo: null,
  }),

  getters: {
    getMemberInfo() {
      return this.$state.memberInfo;
    },
  },

  actions: {
    toggleDropdown() {
      this.isDropdown = !this.$state.isDropdown;
    },
  },
});
