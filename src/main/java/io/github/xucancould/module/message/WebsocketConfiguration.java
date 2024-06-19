package io.github.xucancould.module.message;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * created by Xu on 2024/3/21 18:58.
 * websocket配置
 */
@Configuration
@ConditionalOnProperty(prefix = "io.github.xucancould.websocket", value = "enable", havingValue = "true")
@EnableWebSocket
public class WebsocketConfiguration implements WebSocketConfigurer {

    @Value("${io.github.xucancould.websocket.intercept:false}")
    private boolean intercepted;

    @Bean
    public MessageWebSocketHandler messageWebSocketHandler() {
        return new MessageWebSocketHandler();
    }

    @Bean
    public WsHandler wsHandler() {
        return new WsHandlerImpl();
    }

    @Bean
    @ConditionalOnProperty(prefix = "io.github.xucancould.websocket", value = "intercept", havingValue = "true")
    public WebSocketInterceptor webSocketInterceptor() {
        return new WebSocketInterceptor();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry handlerRegistry) {
        if (intercepted) {
            handlerRegistry.addHandler(messageWebSocketHandler(), "ws/message")
                    .addInterceptors(webSocketInterceptor())
                    .setAllowedOrigins("*");
        } else {
            handlerRegistry.addHandler(messageWebSocketHandler(), "ws/message")
                    .setAllowedOrigins("*");
        }
    }
}
