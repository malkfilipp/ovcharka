package user.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("user-app")
public class UserProperties {

    private TelegramBot telegramBot;

    public TelegramBot getTelegramBot() {
        return telegramBot;
    }

    public void setTelegramBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public static class TelegramBot {
        private String username;
        private String token;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
