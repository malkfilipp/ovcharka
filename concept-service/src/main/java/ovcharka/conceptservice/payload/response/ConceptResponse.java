package ovcharka.conceptservice.payload.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ovcharka.conceptservice.domain.Concept;

@EqualsAndHashCode(callSuper = true)
@Data
public class ConceptResponse extends AbstractResponse<Concept> {

    public ConceptResponse(Concept concept) {
        super(AbstractResponse.SUCCESS, concept, null);
    }
}
