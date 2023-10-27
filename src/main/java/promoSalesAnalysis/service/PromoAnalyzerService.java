package promoSalesAnalysis.service;

import lombok.AllArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import promoSalesAnalysis.repositories.ActualRepository;

@Service
@AllArgsConstructor
public class PromoAnalyzerService {
    private final ActualRepository actualRepository;
    public void calculate(){
        try {
            actualRepository.updateActualWithPromo();
        } catch (Exception e) {
            throw new ServiceException("Ошибка при вычислении", e);
        }
    }
}
