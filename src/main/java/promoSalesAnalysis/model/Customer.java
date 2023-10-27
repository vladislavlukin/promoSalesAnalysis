package promoSalesAnalysis.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "CH3_Ship_To_Code")
    private String shipToCode;

    @Column(name = "CH3_Ship_To_Name")
    private String shipToName;

    @Column(name = "Chain_name")
    private String chainName;

}
