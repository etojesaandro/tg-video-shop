package ru.saandro.telegram.shop.dao;

import ru.saandro.telegram.shop.controller.VideoGenres;

public interface Items {

    Iterable<Item> browseItemsByUserAndGenre(long userId, VideoGenres genre);

    Iterable<Item> getPurchasedItemsByUser(long userId);
}
