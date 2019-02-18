package ovcharka.conceptservice.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConceptsUpdateRequest {

    private List<ConceptUpdateRequest> concepts;
}