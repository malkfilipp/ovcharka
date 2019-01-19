package ovcharka.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ovcharka.userservice.domain.Grade;
import ovcharka.userservice.domain.User;
import ovcharka.userservice.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername(String username) {
        var user = userRepository.findByUsername(username);

        if (!user.isPresent())
            throw new IllegalArgumentException("No such user");

        return user.get();
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }

    public void updateByUsername(User user) {
        var found = userRepository.findByUsername(user.getUsername());

        User saved;
        if (found.isPresent()) {
            saved = found.get();
            saved.setName(user.getName());
            saved.setPassword(user.getPassword());
        } else {
            saved = user;
        }

        userRepository.save(saved);
    }

    public void updateUserStats(String username, String grade) {
        var found = userRepository.findByUsername(username);

        if (!found.isPresent())
            throw new IllegalArgumentException("No such user");

        Double gradeValue;

        try {
            gradeValue = Grade.valueOf(grade).getValue();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("No such grade: " + grade);
        }

        var user = found.get();
        var stats = user.getStats();

        var totalTrainings = stats.getTotalTrainings();
        totalTrainings++;

        var curGpa = stats.getGpa();
        stats.setGpa(
                curGpa + (gradeValue - curGpa) / totalTrainings
        );

        stats.setTotalTrainings(totalTrainings);

        if (!grade.equals(Grade.F.getSymbol()))
            stats.setPassed(stats.getPassed() + 1);

        userRepository.save(user);
    }
}
