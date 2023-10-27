package promoSalesAnalysis.model;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(indexes = {@Index(columnList = "L3_Product_Category_Name"), @Index(columnList = "Material_No")})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Material_No")
    private int materialNo;

    @Column(name = "Material_Desc_RUS")
    private String materialDescription;

    @Column(name = "L3_Product_Category_Code")
    private String productCategoryCode;

    @Column(name = "L3_Product_Category_Name")
    private String productCategoryName;
}
