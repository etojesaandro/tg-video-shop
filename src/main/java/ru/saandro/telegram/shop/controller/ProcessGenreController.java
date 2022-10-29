package ru.saandro.telegram.shop.controller;

import ru.saandro.telegram.shop.core.*;
import ru.saandro.telegram.shop.persistence.entities.*;
import ru.saandro.telegram.shop.session.*;

import java.io.IOException;
import java.util.*;
import java.util.logging.*;

import com.pengrad.telegrambot.model.*;

public class ProcessGenreController extends AbstractScreenController {

    private volatile ProcessGenreState processGenreState = ProcessGenreState.START;

    public ProcessGenreController(ShopBot bot, UserSession session, long chatId) {
        super(bot, session, chatId);
    }

    @Override
    public void processCallback(CallbackQuery callbackQuery) throws IOException {
        if (EnumWithDescription.parse(callbackQuery.data(), BackCommand.class).isPresent()) {
            session.switchTo(BotScreens.CONTROL_ROOM);
            return;
        }
        switch (processGenreState) {
            case START:
                Optional<ProcessGenreCommands> parse = EnumWithDescription.parse(callbackQuery.data(), ProcessGenreCommands.class);
                if (parse.isEmpty()) {
                    return;
                }
                switch (parse.get()) {
                    case ADD -> {
                        prepareAndSendMenu("Введите название нового жанра?", BackCommand.class);
                        processGenreState = ProcessGenreState.ADD;
                        return;
                    }
                    case DELETE -> {
                        try {
                            Iterable<Genre> allGenres = new CachedPgGenres(bot.getSource()).getAllGenres();
                            if (!allGenres.iterator().hasNext())
                            {
                                prepareAndSendMenu("Ещё не создано ни одного жанра!");
                                return;
                            }
                            prepareAndSendMenu("Выберите жанр для удаления", allGenres);
                            processGenreState = ProcessGenreState.DELETE;
                            return;
                        } catch (Exception e) {
                            prepareAndSendMenu("Неудачно");
                            session.switchTo(BotScreens.CONTROL_ROOM);
                            bot.getLogger().log(Level.WARNING, "Unable to select genres", e);
                        }
                    }
                    case BACK -> session.switchTo(BotScreens.CONTROL_ROOM);
                }
                break;
            case ADD:
                return;
            case DELETE:
                try {
                    new CachedPgGenres(bot.getSource()).deleteGenreById(Long.parseLong(callbackQuery.data()));
                } catch (Exception e) {
                    prepareAndSendMenu("Неудачно");
                    bot.getLogger().log(Level.WARNING, "Unable to delete genre", e);
                }
                session.switchTo(BotScreens.CONTROL_ROOM);
                break;
        }
    }

    @Override
    public void processMessage(Message message) throws IOException {
        if (processGenreState == ProcessGenreState.ADD) {
            try {
                new CachedPgGenres(bot.getSource()).add(message.text());
                prepareAndSendMenu("Жанр успешно создан!");
            } catch (Exception e) {
                prepareAndSendMenu("Неудачно...!");
                bot.getLogger().log(Level.WARNING, "Unable to add new Genre");
            }
        }
        session.switchTo(BotScreens.CONTROL_ROOM);
    }

    @Override
    public void onStart() throws IOException {
        prepareAndSendMenu("Что вы хотите сделать?", ProcessGenreCommands.class);

    }
}
