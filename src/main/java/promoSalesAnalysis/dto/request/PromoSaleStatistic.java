package promoSalesAnalysis.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestBody;

@Getter
@Setter
@AllArgsConstructor
public class PromoSaleStatistic {
    private String month;
    private String chainName;
    private String productCategoryName;
    private long volumeUnitsByRegular;
    private long volumeUnitsByPromo;
    private double salesValueByRegular;
    private double salesValueByPromo;
    private double percentPromo;
}
