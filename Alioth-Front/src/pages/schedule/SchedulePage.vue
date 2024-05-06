<template>
  <AppSidebar></AppSidebar>
  <v-container fluid>
  <v-main>
    <AppHeader></AppHeader>
      <v-row no-gutters>
        <v-col>
          <v-sheet class="pa-2 ma-2">
            <div ref="firstCalendar" style="width: 50vh; height: auto; font-size: 75%" id="calendarList"></div>
          </v-sheet>
        </v-col>
        <v-col>
          <v-sheet class="pa-2 ma-2">
            <FullCalendar ref="secondCalendar" style="width: 100vh; font-size: 80%"
                          :options="calendarOptions"></FullCalendar>
          </v-sheet>
        </v-col>
      </v-row>
  </v-main>
  </v-container>
  <v-dialog v-model="modalOpen" max-width="600">
    <v-card>
      <v-card-title>
        <span class="headline" v-if="!isUpdate">일정 등록</span>
        <span class="headline" v-if="isUpdate">일정 수정</span>
      </v-card-title>
      <v-card-text>
        <!-- 모달 내용 -->
        <v-container>
          <v-row>

            <v-col cols="12">
              <v-text-field v-model="newEvent.title" label="일정 제목"></v-text-field>
            </v-col>

            <v-col cols="12">
              <v-select v-model="newEvent.type" color="back" :items="eventTypes" item-title="text" item-value="value" label="일정 유형"></v-select>
            </v-col>

            <v-col cols="12" sm="6">
              <VueDatePicker locale="ko" v-model="newEvent.start" />
            </v-col>

            <v-col cols="12" sm="6">
              <VueDatePicker locale="ko" v-model="newEvent.end" />
            </v-col>

            <v-col cols="12">
              <v-checkbox v-model="newEvent.allDay" label="하루 종일"></v-checkbox>
              <v-checkbox v-model="newEvent.share" v-if="this.LoginInfoStore.getMemberRank === 'MANAGER'" label="팀원 공유"></v-checkbox>
            </v-col>

            <v-col cols="12" class="d-flex justify-center">
              <v-color-picker v-model="newEvent.color"> </v-color-picker>
            </v-col>
            <v-col cols="12">
              <v-textarea v-model="newEvent.content" label="일정 세부내용" rows="3"></v-textarea>
            </v-col>
          </v-row>
        </v-container>
      </v-card-text>
      <v-card-actions>
        <v-btn color="#2979FF" variant="tonal" v-if="isUpdate" @click="updateEvent">수정</v-btn>
        <v-btn color="primary" variant="tonal" v-if="isUpdate" @click="deleteEvent">삭제</v-btn>
        <v-spacer></v-spacer>
        <v-btn color="#2979FF" variant="tonal" v-if="!isUpdate" @click="saveEvent">저장</v-btn>
        <v-btn color="#2C3E50" variant="tonal" @click="closeModal">닫기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import AppSidebar from "@/layouts/AppSidebar.vue";
import AppHeader from "@/layouts/AppHeader.vue";

import {Calendar} from '@fullcalendar/core';
import FullCalendar from '@fullcalendar/vue3'
import dayGridPlugin from '@fullcalendar/daygrid'
import timeGridPlugin from '@fullcalendar/timegrid'
import interactionPlugin from '@fullcalendar/interaction'
import listPlugin from '@fullcalendar/list';
import axiosInstance from "@/plugins/loginaxios";
import {ref} from 'vue';

import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css'
import { useLoginInfoStore } from '@/stores/loginInfo';

const baseUrl = import.meta.env.VITE_API_SERVER_BASE_URL || 'http://localhost:8080';
console.log("확인")

export default {
  components: {AppHeader, AppSidebar, FullCalendar, VueDatePicker},
  mounted() {
    const calendarListEl = document.getElementById('calendarList');
    const firstCalendar = new Calendar(calendarListEl, {
      plugins: [listPlugin],
      locale: "ko",
      timeZone: "local",
      initialView: 'listDay',
      buttonText: {
        today: '오늘',
        listDay: '일',
        listWeek: '주',
        listMonth: '월',
      },
      headerToolbar: {
        left: 'prev,next today',
        center: 'title',
        right: 'listDay,listWeek,listMonth'
      }
    });
    firstCalendar.render();

    this.$refs.firstCalendar.calendar = firstCalendar;
    this.getList();

  },
  data() {
    return {
      LoginInfoStore : useLoginInfoStore(),
      isUpdate: true,
      newStart: new Date,
      newEnd: new Date,
      startDatePicker: false,
      endDatePicker: false,
      modalOpen: false,
      newEvent: ref({}),
      eventList: ref({}),
      eventTypes: [
        { text: "회의", value: "MEETING" },
        { text: "고객 방문 일정", value: "CUSTOMER_VISIT" },
        { text: "판매 활동", value: "SALES_ACTIVITY" },
        { text: "업무 일정", value: "TASK" },
        { text: "휴가 및 휴식", value: "LEAVE" },
        { text: "행사 및 전시회", value: "EVENT" },
        { text: "교육 및 트레이닝", value: "EDUCATION" },
        { text: "마케팅 캠페인", value: "MARKETING_CAMPAIGN" }
      ],
      calendarOptions: {
        locale: "ko",
        timeZone: "local",
        plugins: [
          dayGridPlugin,
          timeGridPlugin,
          interactionPlugin
        ],
        headerToolbar: {
          left: 'prev,next today',
          center: 'title',
          right: 'dayGridMonth,timeGridWeek,timeGridDay'
        },
        buttonText: {
          today: '오늘',
          dayGridMonth: '월',
          timeGridWeek: '주',
          timeGridDay: '일',
        },
        initialView: 'dayGridMonth',
        initialEvents: '',
        editable: true,
        selectable: true,
        selectMirror: true,
        dayMaxEvents: true,
        weekends: true,
        select: this.handleDateSelect,
        eventClick: this.handleEventClick,
        eventsSet: this.handleEvents,
        eventDrop: this.handleEventDrop
      }
    }
  },
  methods: {
    calendarEventRemove(){
      const firstCalendar = this.$refs.firstCalendar.calendar;
      firstCalendar.removeAllEvents();
      const secondCalendar = this.$refs.secondCalendar.calendar;
      secondCalendar.removeAllEvents();
    },
    calendarEventCreate(eventList) {
      eventList.forEach(event => {
        this.eventCreate(event)
      });
    },
    eventCreate(event){
      const newEvent = {
        id: event.scheduleId,
        title: event.scheduleTitle,
        content: event.scheduleNote,
        start: event.scheduleStartTime,
        end: event.scheduleEndTime,
        allDay: event.allDay === "true",
        scheduleType : event.scheduleType,
        share: event.share,
        backgroundColor: event.color,
      };

      const firstCalendar = this.$refs.firstCalendar.calendar;
      firstCalendar.addEvent(newEvent);

      const secondCalendar = this.$refs.secondCalendar.calendar;
      secondCalendar.addEvent(newEvent);
    },

    handleDateSelect(selectInfo) {
      if (selectInfo) {
        this.newEvent = {};
        this.isUpdate = false
        this.newEvent.start = selectInfo.startStr;
        this.newEvent.end = selectInfo.endStr;
        this.modalOpen = true;
      }
    },

    handleEventClick(clickInfo) {
      this.newEvent = {};
      this.isUpdate = true

      this.newEvent.id = clickInfo.event._def.publicId
      this.newEvent.title = clickInfo.event._def.title
      this.newEvent.start = clickInfo.event.start
      this.newEvent.end = clickInfo.event.end === null ? clickInfo.event.start : clickInfo.event.end
      this.newEvent.color = clickInfo.event._def.ui.backgroundColor
      this.newEvent.share = clickInfo.event._def.extendedProps.share
      this.newEvent.content = clickInfo.event._def.extendedProps.content
      this.newEvent.type = clickInfo.event._def.extendedProps.scheduleType
      this.newEvent.allDay = clickInfo.event._def.allDay

      this.modalOpen = true;
    },

    handleEventDrop(dropInfo) {

      this.newEvent = {};
      this.isUpdate = true

      this.newEvent.id = dropInfo.event._def.publicId
      this.newEvent.title = dropInfo.event._def.title
      this.newEvent.start = dropInfo.event.start
      this.newEvent.end = dropInfo.event.end === null ? dropInfo.event.start : dropInfo.event.end
      this.newEvent.color = dropInfo.event._def.ui.backgroundColor
      this.newEvent.share = dropInfo.event._def.extendedProps.share
      this.newEvent.content = dropInfo.event._def.extendedProps.content
      this.newEvent.type = dropInfo.event._def.extendedProps.scheduleType
      this.newEvent.allDay = dropInfo.event._def.allDay

      this.updateEvent()
    },

    handleEvents(events) {
      this.eventList = events
    },

    scheduleAddEvent(){
      console.log(this.newEvent)

      if(this.newEvent.title === undefined){
        alert("일정 제목을 입력해 주세요.")
        return false
      }
      if(this.newEvent.type === undefined){
        alert("일정 유형을 선택해 주세요.")
        return false
      }

      if(this.newEvent.start === null){
        alert("시작날을 입력해 주세요.")
        return false
      }

      if(this.newEvent.end === null){
        alert("종료날을 입력해 주세요.")
        return false
      }

      if(new Date(this.newEvent.start) > new Date(this.newEvent.end)){
        alert("시작 시간이 더 늦을 수 없습니다.")
        return false
      }

      if(this.newEvent.content === undefined){
        alert("일정 상세 내용을 입력해 주세요.")
        return false
      }
      let shareData = false;
      if(this.newEvent.share !== null && this.newEvent.share !== undefined){
        shareData = this.newEvent.share
      }
      console.log("share")
      console.log(this.newEvent.share)
      return {
        "scheduleTitle": this.newEvent.title,
        "scheduleStartTime": this.dateTimeFormat(this.newEvent.start),
        "scheduleEndTime": this.dateTimeFormat(this.newEvent.end),
        "color": this.newEvent.color !== undefined ? this.newEvent.color : "#000000",
        "share": shareData,
        "scheduleNote": this.newEvent.content,
        "scheduleType": this.newEvent.type,
        "allDay": this.newEvent.allDay === undefined ? "false" : this.newEvent.allDay === true ?  "true" : "false"
      }
    },

    updateEvent(){
      const data = this.scheduleAddEvent()
      if(data === false){
        return
      }
      axiosInstance.patch(baseUrl + '/api/schedule/update/'+this.newEvent.id , data)
        .then((res) => {
          alert(res.data.message)
          this.calendarEventRemove()
          this.getList()
          this.closeModal()
        })
        .catch(error => {
          alert(error.response.data.message)
          this.calendarEventRemove()
          this.getList()
        });
    },

    saveEvent() {
      const data = this.scheduleAddEvent()
      if(data === false){
        return
      }
      axiosInstance.post(baseUrl + '/api/schedule/create',data)
        .then((res) => {
          alert(res.data.message)
          this.calendarEventRemove()
          this.getList()
          this.closeModal()
        })
        .catch(error => {
          alert(error.response.data.message)
          this.calendarEventRemove()
          this.getList()
        });
    },

    deleteEvent(){
      if(confirm("진짜 일정을 삭제하시겠습니다?")){
        axiosInstance.delete(baseUrl + '/api/schedule/delete/'+this.newEvent.id)
          .then((res) => {
            alert(res.data.message)
            this.calendarEventRemove()
            this.getList()
            this.closeModal()
          })
          .catch(error => {
            alert(error.response.data.message)
            this.calendarEventRemove()
            this.getList()
          });
      }
    },

    closeModal() {
      this.modalOpen = false;
    },

    dateTimeFormat(dateTime){
      const dTime =  new Date(dateTime);
      const offset = dTime.getTimezoneOffset();
      const localDateTime = new Date(dTime.getTime() - (offset * 60000));
      return localDateTime.toISOString().slice(0, 19);
    },
    getList(){
      axiosInstance.get(baseUrl + '/api/schedule/list')
        .then(response => {
          this.eventList.value = response.data.result;
          this.calendarEventCreate(this.eventList.value)
        })
        .catch(error => {
          console.error(error);
        });
    }
  }
}
</script>

<style scoped>
/* 스타일링 */
</style>
