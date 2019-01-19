package ovcharka.trainingservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
@Document
public class Training {
    private static final Integer INITIAL_NUM_QUESTIONS = 5;

    @Id
    private String id;
    @NonNull
    @TextIndexed
    private String userId;
    private List<String> words;
    private String curWord;
    private Integer questionsLeft = INITIAL_NUM_QUESTIONS;
    private Integer score = 0;
    private Integer debt = 0;
    private Integer max = 0;
}
