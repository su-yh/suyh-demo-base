package com.web.sys.filter;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.base.mp.handler.SqlHandler;
import com.web.sys.constants.SysWebConstants;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebFilter("/**")
public class TraceFilter implements Filter {
    public TraceFilter() {
        // 开启审计日志SQL 记录
        SqlHandler.AUDIT_SQL_ENABLED = true;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String traceId = request.getHeader(SysWebConstants.TRACE_ID);
        if (!StringUtils.hasText(traceId)) {
            traceId = IdWorker.getIdStr();
            request.setAttribute(SysWebConstants.TRACE_ID, traceId);
        }

        // 生成或获取 traceId，这里使用 UUID 作为示例
        // 将 traceId 放入 MDC
        MDC.put(SysWebConstants.TRACE_ID, traceId);

        response.setHeader(SysWebConstants.TRACE_ID, traceId);

        try {
            SqlHandler.AUDIT_SQL_LIST.set(new ArrayList<>());
            filterChain.doFilter(request, response);
        } finally {
            SqlHandler.AUDIT_SQL_LIST.remove();
            MDC.remove(SysWebConstants.TRACE_ID);
        }
    }
}
