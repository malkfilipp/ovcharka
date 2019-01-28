package ovcharka.telegramclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ovcharka.telegramclient.config.BotConfig;
import ovcharka.telegramclient.service.BotService;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final BotService botService;

    @Autowired
    public TelegramBot(BotConfig botConfig, BotService botService) {
        this.botConfig = botConfig;
        this.botService = botService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            var chatId = update.getMessage().getChatId();
            var message = update.getMessage().getText();

            var text = botService.process(chatId, message);

            var response = new SendMessage()
                    .setChatId(chatId)
                    .setText(text);
            try {
                execute(response);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getUsername();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }
}
