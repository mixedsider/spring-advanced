package org.example.expert.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Slf4j
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestUri = request.getRequestURI();
        String requestUserId = request.getAttribute("userId").toString();
        UserRole role = UserRole.of(request.getAttribute("userRole").toString());

        log.info("requestUri : {}, requestUserId : {}, requestUserRole : {}, LocalDateTime : {}", new Object[]{requestUri, requestUserId, role.toString(), LocalDateTime.now()});

        if( !UserRole.ADMIN.equals(role) ) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "관리자 권한이 없습니다.");
            return false;
        }


        return true;
    }
}
