package com.web.sys.util;

import org.springframework.lang.Nullable;

import java.io.File;

/**
 * @author suyh
 * @since 2024-10-07
 */
public class FileUtils {

    public static String removeEndWithSeparatorChar(@Nullable String dir) {
        return removeEndWithSeparatorChar(dir, false);
    }

    /**
     * 剔除目录尾巴上的 文件分隔符
     *
     * @param rootEmpty 如果是根目录，是否需要处理成 空字符串，true: "" false: '/'
     */
    public static String removeEndWithSeparatorChar(@Nullable String dir, boolean rootEmpty) {
        if (dir == null) {
            return null;
        }

        dir = dir.trim();

        if (dir.isEmpty()) {
            return dir;
        }

        String separatorStr = File.separatorChar + "";
        if (dir.equals(separatorStr)) {
            return rootEmpty ? "" : dir;
        }

        if (dir.endsWith(separatorStr)) {
            return dir.substring(0, dir.length() - 1);
        }

        return dir;
    }
}
