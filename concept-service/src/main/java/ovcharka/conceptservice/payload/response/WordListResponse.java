package ovcharka.conceptservice.payload.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class WordListResponse extends AbstractResponse<List<String>> {

    public WordListResponse(String status, List<String> words, String message) {
        super(status, words, message);
    }
}