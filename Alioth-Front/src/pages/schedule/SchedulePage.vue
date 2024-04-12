<template>
  <AppSidebar></AppSidebar>
  <v-main>
    <AppHeader></AppHeader>
    <v-container fluid>
      <div>
        <h1>일정</h1>
      </div>
      <div id="calendar" style="height: 40vw;"></div>
      <div id="clicked-event"></div>
      <div>
        <button @click="changeView('week')">주간</button>
        <button @click="changeView('month')">월간</button>
        <button @click="changeView('day')">일간</button>
      </div>
    </v-container>
  </v-main>
</template>

<script>
import { ref, onMounted } from 'vue';
import AppSidebar from "@/layouts/AppSidebar.vue";
import AppHeader from "@/layouts/AppHeader.vue";
import Calendar from '@toast-ui/calendar';
import '@toast-ui/calendar/dist/toastui-calendar.min.css';
import 'tui-date-picker/dist/tui-date-picker.css';
import 'tui-time-picker/dist/tui-time-picker.css';

export default {
  components: {AppHeader, AppSidebar},
  setup() {
    const calendarInstance = ref(null);

    onMounted(() => {
      const container = document.getElementById('calendar');
      const options = {
        defaultView: 'month',
        usageStatistics: false,
        useFormPopup: true,
        useDetailPopup: true,
        timezone: {
          zones: [
            {
              timezoneName: 'Asia/Seoul',
              displayLabel: 'Seoul',
            },
            {
              timezoneName: 'Europe/London',
              displayLabel: 'London',
            },
          ],
        },
        calendars: [
          {
            id: 'cal1',
            name: '개인',
            backgroundColor: '#03bd9e',
          },
          {
            id: 'cal2',
            name: '직장',
            backgroundColor: '#00a9ff',
          },
        ],
        template: {
          time(event) {
            const { start, end, title } = event;
            const formatTime = (time) => {
              const hours = `${time.getHours()}`.padStart(2, '0');
              const minutes = `${time.getMinutes()}`.padStart(2, '0');
              return `${hours}:${minutes}`;
            };
            return `<span style="color: white;">${formatTime(start)}~${formatTime(end)} ${title}</span>`;
          },
          allday(event) {
            return `<span style="color: gray;">${event.title}</span>`;
          },
        },
        views: ['week', 'month', 'day'],
      };
      calendarInstance.value = new Calendar(container, options);
    });

    onMounted(() => {
      calendarInstance.value.on('clickEvent', ({ event }) => {
        const el = document.getElementById('clicked-event');
        el.innerText = event.title;
      });
    });

    const changeView = (view) => {
      calendarInstance.value.changeView(view); // 선택한 뷰로 전환
    };

    return {
      calendarInstance,
      changeView
    };
  }
}
</script>

<style scoped>

</style>
