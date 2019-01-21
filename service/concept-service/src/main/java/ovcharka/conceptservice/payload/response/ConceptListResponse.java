package ovcharka.conceptservice.payload.response;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ovcharka.common.payload.AbstractResponse;
import ovcharka.conceptservice.domain.Concept;

import java.util.List;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConceptListResponse extends AbstractResponse<List<Concept>> {

    public ConceptListResponse(List<Concept> concepts) {
        super(AbstractResponse.SUCCESS, concepts, null);
    }
}
