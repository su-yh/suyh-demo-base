package com.suyh.sys.web.response;

import com.suyh.base.web.response.wrapper.WrapperResponseScanPackages;
import com.suyh.sys.web.constants.SysWebConstants;

import java.util.Collection;
import java.util.Collections;

/**
 * @author suyh
 * @since 2025-05-17
 */
public class SysWebWrapperResponseScanPackages implements WrapperResponseScanPackages {

    @Override
    public Collection<String> getScanPackages() {
        return Collections.singletonList(SysWebConstants.BASE_PACKAGE_CONTROLLER);
    }
}
