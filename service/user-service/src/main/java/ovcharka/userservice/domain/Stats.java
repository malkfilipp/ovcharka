package ovcharka.userservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stats {

    private Integer totalTrainings = 0;
    private Integer passed = 0;
    private Double gpa = Grade.F.getValue();
}
