package com.example.business.controller;

import com.base.mp.mybatis.PageParam;
import com.base.mp.mybatis.PageResult;
import com.base.web.exception.ExceptionUtil;
import com.example.business.entity.mysql.business.ExampleTestEntity;
import com.example.business.service.ExampleTestService;
import com.example.constant.enums.ErrorCodeEnums;
import com.web.sys.authentication.annotation.Permit;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author suyh
 * @since 2025-05-16
 */
@Tag(name = "示例接口")
@RestController
@RequestMapping("/example/test")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ExampleTestController {
    private final ExampleTestService exampleTestService;

    @Permit(required = false)
    @GetMapping("/pageList")
    public PageResult<ExampleTestEntity> pageList(PageParam pageParam) {
        if (true) {
            throw ExceptionUtil.business(ErrorCodeEnums.UNKNOWN_ERROR, "params");
        }
        return exampleTestService.pageList(pageParam);
    }
}
