package com.security.csrf.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
public class SimpleCORSFilter implements Filter {


    @Value("${api.access.control.origin.header.value}")
    private String headerValue;

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;

        String url = request.getRequestURI();

        //allowed origins
        String[] domains = headerValue.split(",");

        String originHeader = request.getHeader("origin");

        //postman_token not equal to null, means request came from postman
        String postman_token = request.getHeader("postman-token");

        System.out.println("Origin passed : " + originHeader);

        response.setHeader("Access-Control-Allow-Origin", originHeader);

        if(originHeader != null && postman_token == null){
            boolean isHeaderMatched = false;
            for (String domain : domains) {
                if (originHeader.equals(domain)) {
                    response.setHeader("Access-Control-Allow-Origin", originHeader);
                    isHeaderMatched =true;
                    break;
                }
            }
            if (!isHeaderMatched){
                response.sendError(401,"Unauthorized");
                return;
            }
        } else {
            response.sendError(401,"Unauthorized");
            return;
        }

        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization, X-Requested-With");

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            try {
                chain.doFilter(req, res);
            } catch (RuntimeException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig){}
    @Override
    public void destroy(){}
}
