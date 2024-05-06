// src/stores/boardTypeStore.js
import { defineStore } from 'pinia';

export const useBoardTypeStore = defineStore('boardType', {
  state: () => ({
    currentType: 'Announcement'
  }),
  actions: {
    setBoardType(type) {
      this.currentType = type;
    }
  },
  persist: {
    enabled: true
  }
});
