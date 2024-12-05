package com.kosa.moimeasy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 메시지 브로커를 사용하여 클라이언트로 전달될 경로를 설정
        config.enableSimpleBroker("/topic", "/queue");  // /topic과 /queue로 시작하는 경로를 브로커가 처리
        config.setApplicationDestinationPrefixes("/app");  // 클라이언트가 메시지를 보낼 때 /app으로 시작하는 경로로 보냄
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
        .setAllowedOrigins("http://localhost:3000")
        .withSockJS(); // 모든 Vue 앱에서 접근 허용

    }

}
