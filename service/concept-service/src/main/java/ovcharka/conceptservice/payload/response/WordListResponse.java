package ovcharka.conceptservice.payload.response;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ovcharka.common.payload.response.AbstractResponse;

import java.util.List;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WordListResponse extends AbstractResponse<List<String>> {

    public WordListResponse(List<String> words) {
        super(SUCCESS, words, null);
    }
}