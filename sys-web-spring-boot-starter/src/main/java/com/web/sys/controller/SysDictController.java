package com.web.sys.controller;

import com.web.sys.authentication.annotation.Permit;
import com.web.sys.service.SysDictService;
import com.web.sys.vo.SysDict;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author suyh
 * @since 2025-07-01
 */
@RestController
@RequestMapping("/system/dict")
@RequiredArgsConstructor
@Validated
@Slf4j
public class SysDictController {
    private static final String SWAGGER_TAG_DICT = "System Dict";
//    private static final String SWAGGER_TAG_DICT_TYPE = "System Dict Type";
//    private static final String SWAGGER_TAG_DICT_DATA = "System Dict Data";

    private final SysDictService sysDictService;

    @Tag(name = SWAGGER_TAG_DICT)
    @Operation(summary = "字典列表（所有）")
    @Permit(required = false)
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<SysDict> sysDictList() {
        return sysDictService.obtainSysDictList();
    }

}
