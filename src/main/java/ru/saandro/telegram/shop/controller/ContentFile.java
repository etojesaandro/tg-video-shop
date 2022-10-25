package ru.saandro.telegram.shop.controller;

import com.google.common.io.Files;
import com.pengrad.telegrambot.model.File;

public class ContentFile {
    public static final ContentFile INVALID = new ContentFile(null, new byte[]{});

    public final File file;
    public final byte[] data;

    public ContentFile(File file, byte[] data) {
        this.file = file;
        this.data = data;
    }

    public String getExtention() {
        return Files.getFileExtension(file.filePath());
    }

    public boolean isInvalid() {
        return this == INVALID;
    }
}
