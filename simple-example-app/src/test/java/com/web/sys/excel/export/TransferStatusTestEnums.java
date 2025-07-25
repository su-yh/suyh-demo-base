package com.web.sys.excel.export;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.web.ruoyi.excel.annotation.ExcelEnumMessageCategory;
import com.web.ruoyi.excel.annotation.ExcelEnumMessageCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * @author suyh
 * @since 2024-09-03
 */
@Getter
@ExcelEnumMessageCategory("transfer-status")
public enum TransferStatusTestEnums {
    @Schema(description = "1-调度中")
    WAITING(1),
    @Schema(description = "2-成功（已付款已发货）")
    SUCCESS(2),
    @Schema(description = "3-调度失败")
    FAILED(3),
    @Schema(description = "4-已付款未发货")
    PAY_WAITING_DELIVER(4),
    @Schema(description = "5-审核中")
    AUDITING(5),
    @Schema(description = "6-黑名单uid订单")
    DENY_UID_ORDER(6),
    @Schema(description = "7-重复下单,订单失效")
    DUPLICATE_INVALID_ORDER(7),
    // todo zard 8 的状态缺失 目前开发先占位sql 还未同步
    @Schema(description = "8-目前开发先占位,sql 还未同步, 前端目前先不要使用此字段后续还要修改")
    @JsonIgnore
    RESERVE_A_SEAT(8),
    @Schema(description = "-1 未知类型，后端做处理未同步给我方定义的类型, 前端不要使用")
    @JsonIgnore
    UNKNOWN_CODE(-1),

    ;



    @EnumValue
    @ExcelEnumMessageCode
    @JsonValue
    private final int code;

    TransferStatusTestEnums(int code) {
        this.code = code;
    }
}
