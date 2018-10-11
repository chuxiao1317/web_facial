package com.chuxiao.web_facial.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(value = "/facial/enrollSuccess")
public class enrollSuccessFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(enrollSuccessFilter.class);

    /**
     * 初始化过滤器
     */
    @Override
    public void init(FilterConfig filterConfig) /*throws ServletException*/ {
        logger.info("注册过滤器初始化");
    }

    /**
     * 执行过滤器
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("过滤器正在使用");
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        logger.info("请求url：" + httpServletRequest.getRequestURI());
        HttpSession session = httpServletRequest.getSession();
        String username = (String) session.getAttribute("username");
//        String username = (String) httpServletRequest.getAttribute("username");
        logger.info("过滤器收到request传来的username：" + username);
        if (username != null && username.length() > 0) {
            logger.info("通过过滤器！");
            filterChain.doFilter(servletRequest, servletResponse);
            // 销毁session
            session.invalidate();
            return;
        }
        httpServletResponse.sendRedirect("/facial");
    }

    /**
     * 销毁过滤器
     */
    @Override
    public void destroy() {
        logger.info("过滤器销毁");
    }
}
