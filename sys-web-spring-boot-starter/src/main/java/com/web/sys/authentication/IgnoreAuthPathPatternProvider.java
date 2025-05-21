package com.web.sys.authentication;

import java.util.Collection;

/**
 * @author suyh
 * @since 2025-05-21
 */
public interface IgnoreAuthPathPatternProvider {
    Collection<String> getPathPatterns();
}
