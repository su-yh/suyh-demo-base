package com.web.sys.excel.export;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.web.sys.excel.annotation.Excel;
import lombok.Data;

/**
 * @author suyh
 * @since 2025-06-19
 */
@Data
public class ExportDetailDto {
    @Excel(sort = 3, name = "id")
    private Long id;
    @Excel(sort = 3, name = "uuid")
    private String uuid;
    @JsonIgnore
    private TransferStatusTestEnums statusTest;
}
