package promoSalesAnalysis.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import promoSalesAnalysis.dto.response.ActualsResponse;
import promoSalesAnalysis.dto.response.StatisticsResponse;
import promoSalesAnalysis.service.StatisticService;

import java.util.List;

@RestController
@RequestMapping("/statistic")
@AllArgsConstructor
public class AnalysisController {
    private final StatisticService service;
    @GetMapping("/month")
    public ResponseEntity<StatisticsResponse> getStatistic() {
        return ResponseEntity.ok(service.getStatistics());
    }

    @GetMapping("/day")
    public ResponseEntity<ActualsResponse> getActual(List<Integer> materialNo, List<String> chainName) {
        return ResponseEntity.ok(service.getActualsByProductAndChain(materialNo, chainName));
    }
}
