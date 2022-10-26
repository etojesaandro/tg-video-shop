package ru.saandro.telegram.shop.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.pengrad.telegrambot.model.File;

public class ContentFile {
    public static final ContentFile INVALID = new ContentFile(null, new byte[]{});

    public final File file;
    public final byte[] data;

    public ContentFile(File file, byte[] data) {
        this.file = file;
        this.data = data;
    }

    public static ContentFile of(Path contentPath) {
        try {
            return new ContentFile(new File(), Files.readAllBytes(contentPath));
        } catch (IOException e) {
            return INVALID;
        }
    }

    public String getExtention() {
        return com.google.common.io.Files.getFileExtension(file.filePath());
    }

    public boolean isInvalid() {
        return this == INVALID;
    }
}
