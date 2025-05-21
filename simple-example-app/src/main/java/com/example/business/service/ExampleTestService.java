package com.example.business.service;

import com.example.business.entity.mysql.business.ExampleTestEntity;
import com.example.business.mapper.mysql.business.ExampleTestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author suyh
 * @since 2025-05-16
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExampleTestService {
    private final ExampleTestMapper exampleTestMapper;

    @PostConstruct
    public void init() {
        List<ExampleTestEntity> entities = exampleTestMapper.selectList();
        log.info("size: {}", entities != null ? entities.size() : 0);
    }
}
