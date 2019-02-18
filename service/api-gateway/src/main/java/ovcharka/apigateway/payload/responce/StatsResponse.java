package ovcharka.apigateway.payload.responce;


import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ovcharka.common.payload.response.AbstractResponse;
import ovcharka.userservice.domain.Stats;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StatsResponse extends AbstractResponse<Stats> {

    public StatsResponse(Stats stats) {
        super(SUCCESS, stats, null);
    }
}
