package br.com.luckymoney;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/***
 *
 *
 * @author Lucas Oliveira
 *
 */

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

    @Value("${origem.permitida.cors}")
    private String origemPermitada;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        res.setHeader("Access-Control-Allow-Origin", origemPermitada);
        res.setHeader("Access-Control-Allow-Credentials", "true");

        if("OPTIONS".equals(req.getMethod()) && origemPermitada.equals(req.getHeader("Origin"))) {
            res.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, OPTIONS");
            res.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
            res.setHeader("Access-Control-Max-Age", "3600");
            res.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {}

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}
}
