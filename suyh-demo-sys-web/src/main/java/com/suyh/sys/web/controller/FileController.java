package com.suyh.sys.web.controller;

import com.suyh.base.web.authentication.annotation.Permit;
import com.suyh.base.web.response.annotation.WrapperResponseAdvice;
import com.suyh.base.web.response.dto.R;
import com.suyh.sys.web.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author suyh
 * @since 2024-09-07
 */
@Tag(name = "文件系统")
@RestController
@RequestMapping("/system/file")
@RequiredArgsConstructor
@Validated
@Slf4j
public class FileController {
    private final FileService fileService;

    @Value("${snots_file_show_path:}")
    private String snotsFileShowPath;

    // 剔除尾巴上的 '/'
    public String resetEndWith(String path) {
        if (path == null) {
            path = "";
        }

        path = path.trim();
        if (StringUtils.hasText(path) && path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        return path;
    }

    @PostConstruct
    public void init() {
        this.snotsFileShowPath = resetEndWith(this.snotsFileShowPath);

        log.info("snotsFileShowPath: {}", this.snotsFileShowPath);
    }

    @Operation(summary = "文件上传")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public R<String> uploadFiles(@RequestParam(value = "file") MultipartFile file) {
        String uuidFileName = UUID.randomUUID().toString().replaceAll("-", "");
        String suffix = fileService.uploadFile(file, uuidFileName);
        String res = String.format("%s/%s", snotsFileShowPath, uuidFileName + suffix);
        return R.ofSuccess(res);
    }

    // 文件上传之后会把路径写到数据库中，所以这里的WEB API 的路径不能修改。
    @Permit(required = false)
    @Operation(summary = "文件下载")
    @RequestMapping(value = "/download/{fileName}", method = RequestMethod.GET)
    @WrapperResponseAdvice(enabled = false)
    public void uploadFiles(
            HttpServletResponse response,
            @PathVariable("fileName") String fileName) {
        fileService.downloadFile(response, fileName);
    }
}
