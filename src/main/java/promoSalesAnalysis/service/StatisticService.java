package promoSalesAnalysis.service;

import promoSalesAnalysis.dto.response.ActualsResponse;
import promoSalesAnalysis.dto.response.StatisticsResponse;

import java.util.List;

public interface StatisticService {
    StatisticsResponse getStatistics();
    ActualsResponse getActualsByProductAndChain(List<Integer> materialNo, List<String> chainName);

}
