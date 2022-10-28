package ru.saandro.telegram.shop.persistence.entities;

public interface Items {

    Iterable<Item> browseItemsByGenre(long genreId);

    Iterable<Item> getPurchasedItemsByUser(long userId);
}
