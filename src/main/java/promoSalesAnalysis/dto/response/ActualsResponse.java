package promoSalesAnalysis.dto.response;

import lombok.Builder;
import lombok.Data;
import promoSalesAnalysis.model.Actual;

import java.util.List;

@Data
@Builder
public class ActualsResponse {
    private boolean result;
    private List<Actual> actuals;
}
