package com.alioth.server;

import com.google.api.client.json.webtoken.JsonWebToken;
import io.netty.handler.codec.HeadersUtils;
import org.apache.tomcat.util.http.HeaderUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AliothApplication {

	public static void main(String[] args) {
		SpringApplication.run(AliothApplication.class, args);
	}

}
