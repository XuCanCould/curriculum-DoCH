package io.github.xucancould.module.message;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.Claim;
import io.github.talelin.autoconfigure.exception.TokenExpiredException;
import io.github.talelin.core.token.DoubleJWT;
import io.github.xucancould.model.UserDO;
import io.github.xucancould.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.io.IOException;
import java.util.Map;

import static io.github.xucancould.module.message.MessageConstant.USER_KEY;

/**
 * created by Xu on 2024/3/21 19:14.
 * websocket 拦截器，主要是jwt鉴权
 */
public class WebSocketInterceptor implements HandshakeInterceptor {

    @Autowired
    private DoubleJWT jwt;

    @Autowired
    private UserService userService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest httpServletRequest = (ServletServerHttpRequest) request;
            String tokenStr = httpServletRequest.getServletRequest().getParameter("token");
            if (tokenStr == null && tokenStr.isEmpty()) {
                writeMessageToBody(response, "authorization field is required");
                return false;
            }
            Map<String, Claim> claims;
            try {
                claims = jwt.decodeAccessToken(tokenStr);
            } catch (TokenExpiredException e) {
                writeMessageToBody(response, "token expired");
                return false;
            } catch (AlgorithmMismatchException | SignatureVerificationException | JWTDecodeException |
                    InvalidClaimException e) {
                writeMessageToBody(response, "token is invalid");
                return false;
            }
            if (claims == null) {
                writeMessageToBody(response, "token is invalid, can't be decode");
                return false;
            }
            int identity = claims.get("identity").asInt();
            UserDO user = userService.getById(identity);
            if (user == null) {
                writeMessageToBody(response, "user not found");
                return false;
            }
            attributes.put(USER_KEY, user);
            return true;
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }


    private void writeMessageToBody(ServerHttpResponse response, String message) throws IOException {
        response.setStatusCode(org.springframework.http.HttpStatus.BAD_REQUEST);
        response.getBody().write(message.getBytes());
    }
}
