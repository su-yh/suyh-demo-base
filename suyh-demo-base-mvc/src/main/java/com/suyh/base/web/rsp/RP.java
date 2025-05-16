package com.suyh.base.web.rsp;

import lombok.Getter;

/**
 * 额外的数据记录
 *
 * @author suyh
 * @since 2024-10-16
 */
@Getter
public class RP<T, S> extends R<T> {
    private final S summary;

    public RP(T data, S summary) {
        super(R.SUCCESS_CODE, R.SUCCESS_MSG, data);
        this.summary = summary;
    }
}
