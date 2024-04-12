package com.alioth.server.common.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;

@Configuration
public class FireBaseConfig {

    @PostConstruct
    public void init(){
        try{
            FileInputStream serviceAccount = new FileInputStream(System.getProperty("user.dir")+"/server/src/main/resources/firebase/alioth-fcm-firebase-adminsdk-tforn-c7ffd79e05.json");
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp.initializeApp(options);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}