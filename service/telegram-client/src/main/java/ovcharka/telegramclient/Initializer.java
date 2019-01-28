package ovcharka.telegramclient;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import redis.clients.jedis.Jedis;

@Configuration
public class Initializer {


    @EventListener(ApplicationReadyEvent.class)
    public void init(ApplicationReadyEvent event) {
        var jedis = new Jedis();
        var keys = jedis.keys("chatId:*");

        var transaction = jedis.multi();
        keys.forEach(transaction::del);
        transaction.exec();
    }
}
