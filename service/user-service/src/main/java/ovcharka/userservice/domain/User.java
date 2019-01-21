package ovcharka.userservice.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import static ovcharka.userservice.domain.Role.USER;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "with")
@Document
public class User {
    @Id
    private String id;
    @NonNull
    private String name;
    @NonNull
    @TextIndexed
    private String username;
    @NonNull
    private String password;
    @NonNull
    private Role role = USER;
    private Stats stats = new Stats();

    public User(@NonNull String name, @NonNull String username, @NonNull String password, @NonNull Role role) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
