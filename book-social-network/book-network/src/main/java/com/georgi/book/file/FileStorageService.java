package com.georgi.book.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static java.io.File.separator;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageService {
    @Value("${application.file.upload.photos-output-path}")
    private String fileUploadPath;

    public String saveFile(
            @NonNull MultipartFile sourceFile,
            @NonNull Integer bookId,
            @NonNull Integer userId) {

             final String fileUploadSubPath = "users" + separator + userId;

        return uploadFile(sourceFile, fileUploadSubPath);
    }

    private String uploadFile(
            @NonNull MultipartFile sourceFile,
            @NonNull String fileUploadSubPath) {

        final String finalUploadPath = fileUploadPath + separator + fileUploadSubPath;

        File targetFolder = new File(finalUploadPath);
        if (!targetFolder.exists()) {
            boolean folderCreated = targetFolder.mkdirs();
            if (!folderCreated) {
                log.warn("Could not create folder {}", targetFolder.getAbsolutePath());
                return null;
            }
        }

        final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());
        // ./upload/users/1/245135.jpg
        String targetFilePath = fileUploadSubPath + separator + System.currentTimeMillis() + "." + fileExtension;

        Path targetPath = Paths.get(targetFilePath);
        try {
            Files.write(targetPath, sourceFile.getBytes());
            log.info("Successfully uploaded file {}", targetFilePath);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return targetFilePath;
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }

        // something .jpg
        int lastDotIndex = fileName.lastIndexOf('.');

        if (lastDotIndex == -1) {
            return "";
        }

        // .JPG -> jpg
        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }
}
