package ovcharka.conceptservice.payload.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ovcharka.conceptservice.domain.Concept;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ConceptListResponse extends AbstractResponse<List<Concept>> {

    public ConceptListResponse(List<Concept> concepts) {
        super(AbstractResponse.SUCCESS, concepts, null);
    }
}
