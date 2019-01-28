package ovcharka.tokenservice.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static java.util.Optional.ofNullable;

@Repository
public class TokenRepository {
    private static final String TOKEN_PREFIX = "token:";
    private static final String USER_PREFIX = "user:";

    private final RedisTemplate<String, Object> redisTemplate;
    private final ValueOperations<String, Object> operations;

    @Autowired
    public TokenRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        operations = this.redisTemplate.opsForValue();
    }

    public void putForOneHour(String token, String username) {
        var oldToken = (String) operations.get(USER_PREFIX + username);
        if (oldToken != null)
            redisTemplate.delete(TOKEN_PREFIX + oldToken);

        operations.set(USER_PREFIX + username, token);
        operations.set(TOKEN_PREFIX + token, username);
        redisTemplate.expire(TOKEN_PREFIX + token, 1, TimeUnit.HOURS);
        redisTemplate.expire(USER_PREFIX + username, 1, TimeUnit.HOURS);

    }

    public Optional<String> getUsername(String token) {
        return ofNullable((String) operations.get(TOKEN_PREFIX + token));
    }
}
