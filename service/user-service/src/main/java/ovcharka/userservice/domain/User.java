package ovcharka.userservice.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private Role role;
    private Stats stats = new Stats();
}
