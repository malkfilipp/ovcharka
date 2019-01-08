package user.telegram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import user.config.UserProperties;
import user.service.ConceptService;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final ConceptService conceptService;
    private final UserProperties properties;

    @Autowired
    public TelegramBot(ConceptService conceptService, UserProperties properties) {
        this.conceptService = conceptService;
        this.properties = properties;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            var answer = update.getMessage().getText();
            var next = conceptService.process(answer);

            var message = new SendMessage()
                    .setChatId(update.getMessage().getChatId())
                    .setText(next);

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return properties.getTelegramBot().getUsername();
    }

    @Override
    public String getBotToken() {
        return properties.getTelegramBot().getToken();
    }
}

