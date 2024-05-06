import {defineStore} from 'pinia';

export const useLoginInfoStore = defineStore('loginInfo', {
  state: () => ({
    memberCode: -1,
    memberRank: "",
    memberTeamCode: "",
    memberEmail: '',
    memberName: '',
    memberImage: '',
    fcmToken: '',
  }),

  getters: {
    getMemberCode() {
      return this.$state.memberCode;
    },
    getMemberRank() {
      return this.$state.memberRank;
    },
    getMemberTeamCode() {
      return this.$state.memberTeamCode;
    },
    getmemberEmail() {
      return this.$state.memberEmail;
    },
    getmemberName() {
      return this.$state.memberName;
    },
    getmemberImage() {
      return this.$state.memberImage;
    },
    getFcmToken() {
      return this.$state.fcmToken;
    },    
  },

  actions: {
    toggleDropdown() {
      this.isDropdown = !this.$state.isDropdown;
    },
  },
  persist: {
    enabled: true
  }
});
