package ovcharka.telegramclient.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static java.util.Optional.ofNullable;

@Repository
public class ChatIdRepository {
    private static final String PREFIX = "chatId:";

    private final RedisTemplate<String, Object> redisTemplate;
    private final ValueOperations<String, Object> operations;

    @Autowired
    public ChatIdRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        operations = this.redisTemplate.opsForValue();
    }

    public void put(String chatId, String username) {
        operations.set(PREFIX + chatId, username);
    }

    public Optional<String> getUsername(String chatId) {
        return ofNullable((String) operations.get(PREFIX + chatId));
    }
}
