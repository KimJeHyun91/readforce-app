package com.readforce.authentication.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.readforce.authentication.service.AuthenticationService;
import com.readforce.authentication.util.JwtUtil;
import com.readforce.common.enums.HeaderEnum;
import com.readforce.common.enums.PrefixEnum;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final AuthenticationService authenticationService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader(HeaderEnum.AUTHORIZATION.getContent());

        String username = null;
        String accessToken = null;

        if (authorizationHeader != null && authorizationHeader.startsWith(PrefixEnum.BEARER.getContent())) {
            accessToken = authorizationHeader.substring(PrefixEnum.BEARER.getContent().length());

            try {
                username = jwtUtil.extractUsername(accessToken);
            } catch (ExpiredJwtException exception) {
                log.warn("요청된 JWT 토큰이 만료되었습니다: {}", exception.getMessage());
            } catch (Exception exception) {
                log.warn("JWT 토큰 파싱 중 오류 발생: {}", exception.getMessage());
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.authenticationService.loadUserByUsername(username);

            if (jwtUtil.isExpiredToken(accessToken)) {
                log.warn("만료된 JWT 토큰입니다.");
                // ❌ 인증 정보 저장하지 않고 필터는 통과
            } else if (jwtUtil.validateToken(accessToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                log.warn("유효하지 않은 JWT 토큰입니다.");
                // ❌ 인증 정보 저장하지 않고 필터는 통과
            }
        }

        // ✅ 토큰이 없거나 인증 실패해도 무조건 통과
        filterChain.doFilter(request, response);
    }
}
