package com.suyh.base.web.audit;

import com.suyh.base.web.user.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * @author suyh
 * @since 2024-10-18
 */
@RequiredArgsConstructor
@Slf4j
public abstract class AbstractAuditComponent {
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

    protected abstract void doAuditRecord(
            IAudit auditOperation,
            Object spelReturnValue,
            HttpServletRequest request,
            LoginUser loginUser,
            Object... reqArgs);
//    {
//        AuditLogEntity recordEntity = new AuditLogEntity();
//        recordEntity.setUserId(loginUser.getId()).setUserNickname(loginUser.getNickname())
//                .setPage(auditOperation.getPage()).setOperation(auditOperation.getOperation())
//                .setReqArgument(JsonUtils.serializable(reqArgs))
//                .setResult(JsonUtils.serializable(spelReturnValue))
//                .setReqPath(request.getServletPath()).setReqMethod(request.getMethod())
//                .setCreated(new Date());
//
//        Object traceId = request.getAttribute(SysWebConstants.TRACE_ID);
//        if (traceId instanceof Long) {
//            recordEntity.setTraceId(Long.parseLong(traceId + ""));
//        } else {
//            log.warn("LOST TRACE ID");
//        }
//
//        recordEntity.setSqlList(SqlHandler.AUDIT_SQL_LIST.get());
//
//        executorService.submit(() -> {
//            auditLogMapper.insert(recordEntity);
//        });
//    }

}
