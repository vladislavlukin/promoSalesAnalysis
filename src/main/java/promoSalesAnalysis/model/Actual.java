package promoSalesAnalysis.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(indexes = {@Index(columnList = "product_id"), @Index(columnList = "Chain_name")})
public class Actual {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Date date;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Customer customer;

    @Column(name = "Chain_name")
    private String chainName;

    @Column(name = "Volume_units")
    private int volumeUnits;

    @Column(name = "Actual_Sales_Value")
    private double actualSalesValue;

    private Boolean promo;
}

