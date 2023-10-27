package promoSalesAnalysis.dto.response;

import lombok.Builder;
import lombok.Data;
import promoSalesAnalysis.dto.request.PromoSaleStatistic;

import java.util.List;

@Data
@Builder
public class StatisticsResponse {
    private boolean result;
    private List<PromoSaleStatistic> statistics;
}
