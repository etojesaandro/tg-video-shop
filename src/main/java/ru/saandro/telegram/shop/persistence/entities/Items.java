package ru.saandro.telegram.shop.persistence.entities;

import ru.saandro.telegram.shop.controller.VideoGenres;

public interface Items {

    Iterable<Item> browseItemsByGenre(VideoGenres genre);

    Iterable<Item> getPurchasedItemsByUser(long userId);
}
