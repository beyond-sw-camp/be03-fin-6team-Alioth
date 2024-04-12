import { createApp } from 'vue';
import App from './App.vue';
import { initializeApp } from 'firebase/app';
import { getMessaging, getToken, onMessage } from 'firebase/messaging';
import { registerPlugins } from '@/plugins';

// Firebase 프로젝트 설정
const firebaseConfig = {
    apiKey: "AIzaSyDB5dyvXp-1ZOYFejptRE8FUWBIUY_YvEw",
    authDomain: "alioth-fcm.firebaseapp.com",
    projectId: "alioth-fcm",
    storageBucket: "alioth-fcm.appspot.com",
    messagingSenderId: "449895392026",
    appId: "1:449895392026:web:fca3c50602be1e35b742a3",
    measurementId: "G-9V311JSHBT"
};

// Firebase 초기화
const firebaseApp = initializeApp(firebaseConfig);
const messaging = getMessaging(firebaseApp);

// 애플리케이션 인스턴스 생성
const app = createApp(App);

// 환경 변수를 전역 프로퍼티로 설정
app.config.globalProperties.$apiBaseUrl = process.env.VUE_APP_API_BASE_URL;

// 플러그인 등록
registerPlugins(app);

// 서비스 워커 등록
if ('serviceWorker' in navigator) {
    navigator.serviceWorker.register('/firebase-messaging-sw.js').then(registration => {
        console.log('Service Worker 등록 성공:', registration.scope);
    }).catch(err => {
        console.log('Service Worker 등록 실패:', err);
    });
}

// Notification 권한 요청 및 토큰 가져오기
Notification.requestPermission().then(permission => {
    if (permission === 'granted') {
        console.log('알림 권한이 허용되었습니다.');
        getToken(messaging, { vapidKey: 'BLlTuHlScEMdKr_MVbESYvaPlaqlyL9jnjbwhUEPdTwBEHVidSbfzh73jqSmZf8ciMXuJc8Ic9jtzeYbA2S9zFs' }).then(currentToken => {
            if (currentToken) {
                console.log('FCM 토큰:', currentToken);
            } else {
                console.log('인스턴스 ID 토큰을 사용할 수 없습니다.');
            }
        }).catch(err => {
            console.log('토큰을 받아오는 중에 문제가 발생했습니다.', err);
        });
    } else {
        console.log('알림을 보낼 권한을 얻을 수 없습니다.');
    }
});

// 메시지 수신 대기
onMessage(messaging, payload => {
    console.log('Message received. ', payload);
    alert(`새 메시지: ${payload.notification.title} - ${payload.notification.body}`);
});

// 애플리케이션 마운트
app.mount('#app');
