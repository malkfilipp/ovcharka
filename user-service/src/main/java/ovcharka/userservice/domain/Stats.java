package ovcharka.userservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public
class Stats {

    private Integer totalTrainings;
    private Integer passed;
    private String gpa;
}
