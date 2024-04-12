importScripts('https://www.gstatic.com/firebasejs/8.10.0/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/8.10.0/firebase-messaging.js');

firebase.initializeApp({
    apiKey: "AIzaSyDB5dyvXp-1ZOYFejptRE8FUWBIUY_YvEw",
    authDomain: "alioth-fcm.firebaseapp.com",
    projectId: "alioth-fcm",
    storageBucket: "alioth-fcm.appspot.com",
    messagingSenderId: "449895392026",
    appId: "1:449895392026:web:fca3c50602be1e35b742a3",
    measurementId: "G-9V311JSHBT"
});

const messaging = firebase.messaging();

messaging.onBackgroundMessage((payload) => {
    console.log('[firebase-messaging-sw.js] Received background message ', payload);
    const notificationTitle = payload.notification.title;
    const notificationOptions = {
        body: payload.notification.body,
        icon: '/icon.png',
        data: {
            url: payload.data.url
        }
    };

    self.addEventListener('notificationclick', (event) => {
        event.notification.close(); 
        event.waitUntil(
            clients.openWindow(burl+payload.data.url)
        );
    });
    
    self.registration.showNotification(notificationTitle, notificationOptions);
});

