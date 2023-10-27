package promoSalesAnalysis.controllers;

import lombok.AllArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import promoSalesAnalysis.service.PromoAnalyzerService;

@RestController
@AllArgsConstructor
public class FinanceController {
    private final PromoAnalyzerService promoAnalyzerService;

    @GetMapping("/calculate")
    public ResponseEntity<String> calculatePromo() {
        try {
            promoAnalyzerService.calculate();
            return ResponseEntity.ok("Success!");
        }catch (ServiceException e){
            return ResponseEntity.ok(e.getMessage());
        }
    }
}
