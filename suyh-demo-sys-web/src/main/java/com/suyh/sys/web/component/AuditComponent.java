package com.suyh.sys.web.component;

import com.suyh.base.mp.handler.SqlHandler;
import com.suyh.base.web.util.JsonUtils;
import com.suyh.sys.web.audit.IAudit;
import com.suyh.sys.web.constants.SysWebConstants;
import com.suyh.sys.web.mybatis.entity.AuditLogEntity;
import com.suyh.sys.web.mybatis.mapper.AuditLogMapper;
import com.suyh.sys.web.user.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author suyh
 * @since 2024-10-18
 */
@Component("audit")
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("unused")
public class AuditComponent {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final AuditLogMapper auditLogMapper;

    // 审计日志记录
    public boolean auditRecord(
            IAudit auditOperation,
            Object spelReturnValue,
            HttpServletRequest request,
            LoginUser loginUser,
            Object... reqArgs) {

        try {
            doAuditRecord(auditOperation, spelReturnValue, request, loginUser, reqArgs);
        } catch (Exception e) {
            log.warn("audit record exception.", e);
        }

        return true;
    }

    private void doAuditRecord(
            IAudit auditOperation,
            Object spelReturnValue,
            HttpServletRequest request,
            LoginUser loginUser,
            Object... reqArgs) {
        AuditLogEntity recordEntity = new AuditLogEntity();
        recordEntity.setUserId(loginUser.getId()).setUserNickname(loginUser.getNickname())
                .setPage(auditOperation.getPage()).setOperation(auditOperation.getOperation())
                .setReqArgument(JsonUtils.serializable(reqArgs))
                .setResult(JsonUtils.serializable(spelReturnValue))
                .setReqPath(request.getServletPath()).setReqMethod(request.getMethod())
                .setCreated(new Date());

        Object traceId = request.getAttribute(SysWebConstants.TRACE_ID);
        if (traceId instanceof Long) {
            recordEntity.setTraceId(Long.parseLong(traceId + ""));
        } else {
            log.warn("LOST TRACE ID");
        }

        recordEntity.setSqlList(SqlHandler.AUDIT_SQL_LIST.get());

        executorService.submit(() -> {
            auditLogMapper.insert(recordEntity);
        });
    }

}
