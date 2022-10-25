package ru.saandro.telegram.shop.dao;

import java.util.List;

public interface User {

    List<PgItem> getPurchasedItems();
}
