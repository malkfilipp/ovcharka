package ovcharka.telegramclient.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ovcharka.telegramclient.repository.ChatIdRepository;

@Service
public class BotService {

    private static final String START_BOT_MESSAGE = "/start ";

    private final ChatIdRepository chatIdRepository;
    private final BotClient botClient;

    @Autowired
    public BotService(ChatIdRepository chatIdRepository, BotClient botClient) {
        this.chatIdRepository = chatIdRepository;
        this.botClient = botClient;
    }

    public String process(Long chatId, String message) {
        if (message.startsWith(START_BOT_MESSAGE)) {
            var token = message.substring(START_BOT_MESSAGE.length());
            var username = botClient.getUsernameFrom(token);

            chatIdRepository.put(chatId.toString(), username);
            return "Authenticated";
        } else {
            var username = chatIdRepository.getUsername(chatId.toString());
            if (!username.isPresent())
                return "Not Authenticated";

            return botClient.processTraining(username.get(), message);
        }
    }
}
