package com.suyh.sys.web.service;

import com.suyh.base.web.constants.enums.BaseWebErrorCodeEnums;
import com.suyh.base.web.exception.ExceptionUtil;
import com.suyh.sys.web.constants.enums.SysWebErrorCodeEnums;
import com.suyh.sys.web.util.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * @author suyh
 * @since 2024-09-07
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {
    @Value("${snots_file_upload_path}")
    private String rootDir;

    @PostConstruct
    public void init() {
        this.rootDir = FileUtils.removeEndWithSeparatorChar(rootDir, true);
        log.info("file upload dir: {}", rootDir);
    }

    @NonNull
    public String uploadFile(MultipartFile sourceFile, @NonNull String uuidFileName) {
        String filePath = null;
        try {
            String extension = FilenameUtils.getExtension(sourceFile.getOriginalFilename());
            if (extension == null) {
                extension = "";
            }
            if (!extension.isEmpty()) {
                extension = "." + extension;
            }
            uuidFileName = uuidFileName + extension;
            filePath = String.format("%s/%s", rootDir, uuidFileName);
            File dest = new File(filePath);
            dest.getParentFile().mkdirs();
            sourceFile.transferTo(dest);

            return extension;
        } catch (Exception exception) {
            log.error("transferTo failed, filepath: {}", filePath, exception);
            throw ExceptionUtil.business(BaseWebErrorCodeEnums.SERVICE_ERROR);
        }
    }

    public void downloadFile(HttpServletResponse response, @NonNull String fileName) {
        String filePath = String.format("%s/%s", rootDir, fileName);

        File file = new File(filePath);

        doDownloadFile(response, file);
    }

    private void doDownloadFile(HttpServletResponse response, File file) {
        if (!file.exists()) {
            throw ExceptionUtil.business(SysWebErrorCodeEnums.FILE_NOT_EXISTS, file.getPath());
        }

        response.reset();
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());

        try (BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(file.toPath()));) {
            byte[] buff = new byte[1024];
            OutputStream os = response.getOutputStream();
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (IOException exception) {
            log.error("write file failed, file path: {}", file.getPath(), exception);
            throw ExceptionUtil.business(BaseWebErrorCodeEnums.SERVICE_ERROR);
        }
    }
}
