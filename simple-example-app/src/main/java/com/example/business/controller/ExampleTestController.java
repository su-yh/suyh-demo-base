package com.example.business.controller;

import com.example.business.service.ExampleTestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
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

}
