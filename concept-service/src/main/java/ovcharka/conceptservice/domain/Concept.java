package ovcharka.conceptservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Concept {
    @Id
    private String id;
    private String word;
    private String definition;
    private Integer score;
    private List<String> related;
}
