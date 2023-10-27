package promoSalesAnalysis.service;

import jakarta.persistence.Tuple;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import promoSalesAnalysis.dto.response.ActualsResponse;
import promoSalesAnalysis.dto.request.PromoSaleStatistic;
import promoSalesAnalysis.dto.response.StatisticsResponse;
import promoSalesAnalysis.model.Actual;
import promoSalesAnalysis.repositories.ActualRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class StatisticServiceImpl implements StatisticService{
    private final ActualRepository actualRepository;
    @Override
    public StatisticsResponse getStatistics() {
        try {
            List<PromoSaleStatistic> statistics = statistics();
            return StatisticsResponse.builder()
                    .result(true)
                    .statistics(statistics)
                    .build();
        } catch (Exception e) {
            return StatisticsResponse.builder()
                    .result(false)
                    .statistics(null)
                    .build();
        }
    }

    @Override
    public ActualsResponse getActualsByProductAndChain(List<Integer> materialNo, List<String> chainName) {
        try {
            List<Actual> actualList = actualRepository.getActualsByProductAndChain(materialNo, chainName);
            return ActualsResponse.builder()
                    .result(true)
                    .actuals(actualList)
                    .build();
        } catch (Exception e) {
            return ActualsResponse.builder()
                    .result(true)
                    .actuals(null)
                    .build();
        }
    }

    private List<PromoSaleStatistic> statistics(){
        List<Tuple> result = actualRepository.getStatistic();
        List<PromoSaleStatistic> promoStatistics = new ArrayList<>();

        for (Tuple row : result) {
            String month = row.get(0, String.class);
            String chainName = row.get(1, String.class);
            String productCategoryName = row.get(2, String.class);
            long volumeUnitsByPromo = row.get(3, Long.class);
            long volumeUnitsByRegular = row.get(4, Long.class);
            double salesValueByPromo = row.get(5, Double.class);
            double salesValueByRegular = row.get(6, Double.class);
            double percentPromo = (salesValueByPromo / (salesValueByPromo + salesValueByRegular)) * 100;

            PromoSaleStatistic promoStatistic = new PromoSaleStatistic(month, chainName, productCategoryName, volumeUnitsByRegular, volumeUnitsByPromo, salesValueByRegular, salesValueByPromo, percentPromo);
            promoStatistics.add(promoStatistic);
        }

        return promoStatistics;

    }
}
