package ovcharka.conceptservice.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
@Document
public class Concept {
    @Id
    private String id;
    @NonNull
    @TextIndexed
    private String word;
    @NonNull
    private String definition;
    @NonNull
    private Integer score;
    private List<String> related;
}
