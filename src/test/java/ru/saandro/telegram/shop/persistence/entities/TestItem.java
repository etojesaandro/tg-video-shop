package ru.saandro.telegram.shop.persistence.entities;

import ru.saandro.telegram.shop.core.ShopBot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.pengrad.telegrambot.request.AbstractSendRequest;

public class TestItem implements Item {
    private Path previews;
    private Path contents;

    public TestItem(Path previews, Path contents) {
        this.previews = previews;
        this.contents = contents;
    }

    @Override
    public AbstractSendRequest<? extends AbstractSendRequest<?>> preparePreview(ShopBot bot, long chatId) throws IOException {
        return null;
    }

    @Override
    public AbstractSendRequest<? extends AbstractSendRequest<?>> prepareContent(ShopBot bot, long chatId) throws IOException {
        return null;
    }

    @Override
    public Long id() {
        return 0L;
    }

    @Override
    public String title() throws IOException {
        return "Title";
    }

    @Override
    public String description() throws IOException {
        return "Description";
    }

    @Override
    public String author() throws IOException {
        return "Author";
    }

    @Override
    public Integer price() throws IOException {
        return 42;
    }

    @Override
    public String previewPath() throws IOException {
        return previews.toAbsolutePath().toString();
    }

    @Override
    public String contentPath() throws IOException {
        return contents.toAbsolutePath().toString();
    }

    @Override
    public Genre genre() throws IOException {
        return new TestGenre();
    }
}
