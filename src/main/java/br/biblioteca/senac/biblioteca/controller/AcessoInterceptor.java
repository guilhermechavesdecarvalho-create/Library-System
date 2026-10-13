package br.biblioteca.senac.biblioteca.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AcessoInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String uri = request.getRequestURI();
        if (uri.startsWith("/css/") || uri.startsWith("/js/") || uri.startsWith("/images/") || uri.endsWith(".css")
                || uri.endsWith(".js") || uri.endsWith(".png") || uri.endsWith(".jpg"))
            return true;
        if (uri.equals("/") || uri.equals("/login") || uri.equals("/cadastro") || uri.equals("/recuperar-senha")
                || uri.equals("/favicon.ico"))
            return true;
        if (request.getSession(false) != null && request.getSession(false).getAttribute("usuarioId") != null)
            return true;
        response.sendRedirect(request.getContextPath() + "/");
        return false;
    }
}
