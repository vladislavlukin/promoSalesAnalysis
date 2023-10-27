package promoSalesAnalysis.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(indexes = {@Index(columnList = "product_id")})
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Chain_name")
    private String chainName;

    @ManyToOne
    private Product product;

    @Column(name = "Regular_price_per_unit")
    private double regularPricePerUnit;
}
