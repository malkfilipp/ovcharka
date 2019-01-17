package ovcharka.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ovcharka.userservice.domain.Stats;
import ovcharka.userservice.domain.User;
import ovcharka.userservice.repository.UserRepository;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.*;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Mono<Void> deleteAll() {
        return userRepository.deleteAll();
    }

    public Mono<User> updateByUsername(User user) {
        return userRepository
                .findByUsername(user.getUsername())
                .map(updated -> {
                    updated.setName(user.getName());
                    updated.setPassword(user.getPassword());
                    return updated;
                }).switchIfEmpty(just(user))
                .flatMap(userRepository::save);
    }
}
