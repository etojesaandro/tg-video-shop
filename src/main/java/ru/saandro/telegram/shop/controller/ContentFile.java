package ru.saandro.telegram.shop.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ContentFile {
    public static final ContentFile INVALID = new ContentFile("", "", new byte[]{});

    public final String fileUniqueId;
    public final String filePath;
    public final byte[] data;

    public ContentFile(String fileUnique, String fileUniqueId, byte[] data) {
        this.fileUniqueId = fileUniqueId;
        this.filePath = fileUniqueId;
        this.data = data;
    }

    public static ContentFile of(Path contentPath) {
        try {
            return new ContentFile(contentPath.toString(), contentPath.getFileName().toString(), Files.readAllBytes(contentPath));
        } catch (IOException e) {
            return INVALID;
        }
    }

    public String getExtention() {
        return com.google.common.io.Files.getFileExtension(filePath);
    }

    public boolean isInvalid() {
        return this == INVALID;
    }
}
