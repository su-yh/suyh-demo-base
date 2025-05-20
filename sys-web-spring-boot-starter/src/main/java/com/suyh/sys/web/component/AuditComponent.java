package com.suyh.sys.web.component;

import com.base.mp.handler.SqlHandler;
import com.base.web.audit.AbstractAuditComponent;
import com.base.web.audit.IAudit;
import com.base.web.user.AbstractLoginUser;
import com.base.web.util.JsonUtils;
import com.suyh.sys.web.constants.SysWebConstants;
import com.suyh.sys.web.mybatis.entity.AuditLogEntity;
import com.suyh.sys.web.mybatis.mapper.AuditLogMapper;
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
public class AuditComponent extends AbstractAuditComponent {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final AuditLogMapper auditLogMapper;

    @Override
    protected void doAuditRecord(
            IAudit auditOperation,
            Object spelReturnValue,
            HttpServletRequest request,
            AbstractLoginUser loginUser,
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
