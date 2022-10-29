package ru.saandro.telegram.shop.persistence.entities;

import ru.saandro.telegram.shop.core.*;

import javax.sql.*;
import java.io.*;

public interface Items {

    Iterable<Item> browseItemsByGenre(long genreId) throws IOException;

    Iterable<Item> getPurchasedItemsByUser(long userId) throws IOException;

    Item add(String title, String description, String author, Integer price, String previewPath, String contentPath, Genre genre) throws IOException;
}
