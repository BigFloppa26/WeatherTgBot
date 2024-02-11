package ustin.bot;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ustin.excel.ReadExcelFileToList;
import ustin.service.requestToAPI.RequestToYandex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class BotForGirl extends TelegramLongPollingBot {

    private final Logger logger = LoggerFactory.getLogger(BotForGirl.class);

    private static final String START = "/start";
    private static final String GETWEATHER = "/get_weather";
    private static final String EXCEL = "/excel";
    private static final String MESSAGE = "/phone";
    private final RequestToYandex request;
    private final ReadExcelFileToList readExcelFileToList;

    @Value("${excelPath}")
    private String excelPath;

    public BotForGirl(@Value("${bot.token}") String token, RequestToYandex request, ReadExcelFileToList readExcelFileToList) {
        super(token);
        this.request = request;
        this.readExcelFileToList = readExcelFileToList;
    }

    @Override
    public void onUpdateReceived(Update update) throws RuntimeException {
        if (update.hasMessage() && update.getMessage().hasText()) {
            try {
                sendMessage(update.getMessage().getChatId(), "Пожалуйста, выберите действие из списка предложенных.");
                execute(sendMessageWithInlineKeyboard(update.getMessage().getChatId()));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
        if (update.hasCallbackQuery()) {
            String callBackData = update.getCallbackQuery().getData();
            Long chatIdCallback = update.getCallbackQuery().getMessage().getChatId();
            String username = update.getCallbackQuery().getFrom().getUserName();

            switch (callBackData) {
                case START -> {
                    startCommand(chatIdCallback, username);
                    try {
                        execute(sendMessageWithInlineKeyboard(chatIdCallback));
                    } catch (TelegramApiException e) {
                        logger.error(e.getMessage());
                    }
                }
                case GETWEATHER -> {
                    try {
                        String weather = request.getWeather();
                        sendMessage(chatIdCallback, weather);
                        execute(sendMessageWithInlineKeyboard(chatIdCallback));

                    } catch (JsonProcessingException | TelegramApiException e) {
                        logger.error(e.getMessage());
                    }
                }
                case EXCEL -> {
                    try {
                        sendMessage(chatIdCallback, readExcelFileToList.readExcelData(excelPath).toString());
                        execute(sendMessageWithInlineKeyboard(chatIdCallback));
                    } catch (IOException | TelegramApiException e) {
                        logger.error(e.getMessage());
                    }
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "Picturesque28Bot";
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.error("Ошибка отправки сообщения");
        }
    }

    private void startCommand(Long chatId, String username) {
        String startCommand = "Hello from my bot, " + username;
        sendMessage(chatId, startCommand);
    }

    public SendMessage sendMessageWithInlineKeyboard(Long chatId) throws TelegramApiException, RuntimeException {
        InlineKeyboardButton startButton = new InlineKeyboardButton("Start");
        InlineKeyboardButton weatherButton = new InlineKeyboardButton("Get Weather");
        InlineKeyboardButton excelButton = new InlineKeyboardButton("Read Excel");

        startButton.setCallbackData("/start");
        weatherButton.setCallbackData("/get_weather");
        excelButton.setCallbackData("/excel");

        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        {
            buttonList.add(0, startButton);
            buttonList.add(1, weatherButton);
            buttonList.add(2, excelButton);
        }
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(Collections.singletonList(buttonList));
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Выберите действие: ");
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }
}
