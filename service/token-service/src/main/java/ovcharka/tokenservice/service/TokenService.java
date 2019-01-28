package ovcharka.tokenservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ovcharka.tokenservice.repository.TokenRepository;

import java.security.SecureRandom;

@Service
public class TokenService {

    private static final String SYMBOLS = "_0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-abcdefghijklmnopqrstuvwxyz";
    private static final int TOKEN_LEN = 16;

    private final SecureRandom rnd = new SecureRandom();

    private final TokenRepository tokenRepository;
    private final TokenClient tokenClient;

    @Autowired
    public TokenService(TokenRepository tokenRepository, TokenClient tokenClient) {
        this.tokenRepository = tokenRepository;
        this.tokenClient = tokenClient;
    }

    public String create(String username) {
        tokenClient.checkPresenceOf(username);

        var token = getRandomString();
        tokenRepository.putForOneHour(token, username);
        return token;
    }

    private String getRandomString() {
        var builder = new StringBuilder(TOKEN_LEN);
        for (int i = 0; i < TOKEN_LEN; i++)
            builder.append(SYMBOLS.charAt(rnd.nextInt(SYMBOLS.length())));
        return builder.toString();
    }

    public String getUsername(String token) {
        var username = tokenRepository.getUsername(token);

        if (!username.isPresent())
            throw new IllegalArgumentException("No such token");

        return username.get();
    }
}
