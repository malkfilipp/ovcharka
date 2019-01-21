package ovcharka.conceptservice.payload.response;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ovcharka.common.payload.AbstractResponse;
import ovcharka.conceptservice.domain.Concept;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConceptResponse extends AbstractResponse<Concept> {

    public ConceptResponse(Concept concept) {
        super(SUCCESS, concept, null);
    }
}
