package ru.saandro.telegram.shop.persistence.entities;

import ru.saandro.telegram.shop.controller.ContentFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.common.io.Resources;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ThickItemTest {

    @Test
    public void testThickItem() throws IOException {

        ContentFile preview = createContentFile("preview.png");
        ContentFile content = createContentFile("content.mp4");
        ThickItem item = new ThickItem(new TestItem(Files.createTempDirectory("previews").resolve(preview.filePath), Files.createTempDirectory("contents").resolve(content.filePath)), preview, content);
        item.store();
        assertTrue(Files.exists(Paths.get(item.previewPath())));
        assertTrue(Files.exists(Paths.get(item.contentPath())));
    }

    private ContentFile createContentFile(String name) {
        File file = new File(Resources.getResource(getClass(), name).getFile());
        return ContentFile.of(file.toPath());
    }
}
