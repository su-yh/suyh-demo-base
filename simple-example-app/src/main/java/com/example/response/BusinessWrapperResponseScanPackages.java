package com.example.response;

import com.base.web.response.wrapper.WrapperResponseScanPackages;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author suyh
 * @since 2025-05-17
 */
@Component
public class BusinessWrapperResponseScanPackages implements WrapperResponseScanPackages {
    @Override
    public Collection<String> getScanPackages() {
        return Arrays.asList("com.example.business.controller");
    }
}
