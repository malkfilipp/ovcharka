package ovcharka.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ovcharka.userservice.domain.User;
import ovcharka.userservice.domain.WordList;
import ovcharka.userservice.repository.UserRepository;
import ovcharka.userservice.repository.WordListRepository;
import reactor.core.publisher.Mono;

import java.security.SecureRandom;
import java.util.List;

import static reactor.core.publisher.Mono.*;

@Service
public class WordListService {

    private final WordListRepository wordListRepository;
    private final UserRepository userRepository;

    private final SecureRandom rand = new SecureRandom();

    @Autowired
    public WordListService(WordListRepository wordListRepository, UserRepository userRepository) {
        this.wordListRepository = wordListRepository;
        this.userRepository = userRepository;
    }

    public Mono<Void> deleteAll() {
        return wordListRepository.deleteAll();
    }

    private Mono<WordList> getWordList(String username) {
        return userRepository
                .findByUsername(username)
                .map(User::getId)
                .flatMap(wordListRepository::findByUserId);
    }

    public Mono<WordList> update(String username, List<String> words) {
        return userRepository
                .findByUsername(username)
                .map(User::getId)
                .flatMap(userId -> wordListRepository
                        .findByUserId(userId)
                        .switchIfEmpty(just(new WordList(null, userId, null)))
                        .map(updated -> {
                            updated.setWords(words);
                            return updated;
                        }).flatMap(wordListRepository::save));
    }

    public Mono<String> getRandomWord(String username) {
        return getWordList(username)
                .flatMap(list -> {
                    var words = list.getWords();
                    var index = rand.nextInt(words.size());
                    var word = words.get(index);
                    words.remove(index);
                    return wordListRepository.save(list)
                                             .then(just(word));
                }).onErrorResume(e -> error(new IllegalStateException("No words left")));
    }

    public Mono<Boolean> contains(String username, String word) {
        return getWordList(username)
                .map(list -> list.getWords().contains(word));
    }

    public Mono<Boolean> delete(String username, String word) {
        return getWordList(username)
                .flatMap(list -> {
                    var removed = list.getWords().remove(word);
                    return wordListRepository.save(list)
                                             .then(just(removed));
                });
    }
}
