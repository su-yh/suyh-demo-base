package com.web.sys.cache;

import lombok.Data;

/**
 * @author suyh
 * @since 2024-04-01
 */
@Data
public class CacheWrapper<T> {
    private T data;
}
