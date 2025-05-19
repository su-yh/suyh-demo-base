package com.suyh.sys.web.dto;

import java.util.UUID;

/**
 * @author suyh
 * @since 2024-09-07
 */
public interface UuidRspDto {
    /**
     * 为前端添加唯一ID 值
     */
    default String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
