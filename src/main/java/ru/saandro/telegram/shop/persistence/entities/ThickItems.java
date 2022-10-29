package ru.saandro.telegram.shop.persistence.entities;

import ru.saandro.telegram.shop.controller.ContentFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ThickItems implements Items {

    private Items origin;

    public ThickItems(Items items) {
        this.origin = items;
    }

    @Override
    public Iterable<Item> browseItemsByGenre(long genreId) throws IOException {
        return origin.browseItemsByGenre(genreId);
    }

    @Override
    public Iterable<Item> getPurchasedItemsByUser(long userId) throws IOException {
        return origin.getPurchasedItemsByUser(userId);
    }

    @Override
    public Item add(String title, String description, String author, Integer price, String previewPath, String contentPath, Genre genre) throws IOException {
        Item add = origin.add(title, description, author, price, previewPath, contentPath, genre);
        saveFile(Paths.get(previewPath));
        saveFile(Paths.get(contentPath));
        return add;
    }

    private void saveFile(Path path) throws IOException {
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        Files.write(path, ContentFile.of(path).data);
    }
}
