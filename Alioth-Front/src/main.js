import { createApp } from 'vue';
import App from './App.vue';
import { initializeApp } from 'firebase/app';
import { getMessaging, getToken, onMessage } from 'firebase/messaging';
import { registerPlugins } from '@/plugins';
import { useNotificationStore } from './stores/notificationStore';
import { useLoginInfoStore } from './stores/loginInfo';
import { createPinia } from 'pinia';
import 'quill/dist/quill.snow.css'; // for snow theme





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


const firebaseApp = initializeApp(firebaseConfig);
const messaging = getMessaging(firebaseApp);
const app = createApp(App);
const pinia = createPinia();
app.use(pinia);




app.config.globalProperties.$apiBaseUrl = process.env.VITE_API_SERVER_BASE_URL;

registerPlugins(app);
const loginStore = useLoginInfoStore();


if ('serviceWorker' in navigator) {
    navigator.serviceWorker.register('/firebase-messaging-sw.js').then(registration => {
        console.log('Service Worker 등록 성공:', registration.scope);
    }).catch(err => {
        console.log('Service Worker 등록 실패:', err);
    });
}

Notification.requestPermission().then(permission => {
    if (permission === 'granted') {
        console.log('알림 권한이 허용되었습니다.');
        getToken(messaging, { vapidKey: 'BLlTuHlScEMdKr_MVbESYvaPlaqlyL9jnjbwhUEPdTwBEHVidSbfzh73jqSmZf8ciMXuJc8Ic9jtzeYbA2S9zFs' }).then(currentToken => {
            if (currentToken) {
                console.log('FCM 토큰:', currentToken);
                loginStore.fcmToken = currentToken;
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

onMessage(messaging, (payload) => {
    if (document.visibilityState === 'visible') { // 앱이 포그라운드 상태일 때만 처리
        console.log('Message received. ', payload);
        const notificationStore = useNotificationStore();
        console.log(payload);
        const notification = {
            messageId: payload.messageId,
            title: payload.notification.title,
            body: payload.notification.body,
            url: payload.data?.url
        };
        notificationStore.addNotification(notification);
    }
});

// 애플리케이션 마운트
app.mount('#app');
