package com.web.sys.excel.export;

import com.base.mp.mybatis.PageParam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author suyh
 * @since 2025-06-21
 */
public class PageParamUtilsTest {

    /**
     * 按总数，计算指定所在页的记录数量
     */
    public static int calculatePageSize(PageParam pageParam, int totalSize) {
        int pageStart = pageParam.getPageStart();
        Integer pageSize = pageParam.getPageSize();
        int pageEnd = pageStart + pageSize;
        if (totalSize < pageEnd) {
            pageEnd = totalSize;
        }

        int currentSize = pageEnd - pageStart;
        return Math.max(currentSize, 0);
    }

    @Test
    public void testCalculatePageSize() {
        final int totalSize = 233;

        {
            PageParam pageParam = new PageParam();

            int i = 0;
            for (; i < totalSize / pageParam.getPageSize(); i++) {
                pageParam.setPageNo(i + 1);
                int size = calculatePageSize(pageParam, totalSize);
                Assertions.assertEquals(pageParam.getPageSize(), size, "当前页下标：" + i);
            }

            {
                pageParam.setPageNo(i + 1);
                int size = calculatePageSize(pageParam, totalSize);
                Assertions.assertEquals(totalSize % pageParam.getPageSize(), size, "最后一页");
                i++;
            }

            {
                pageParam.setPageNo(i + 1);
                int size = calculatePageSize(pageParam, totalSize);
                Assertions.assertEquals(0, size, "超过最大页");
            }
        }
    }
}
