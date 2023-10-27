package promoSalesAnalysis.repositories;

import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import promoSalesAnalysis.model.Actual;

import java.util.List;

@Repository
public interface ActualRepository extends CrudRepository<Actual, Integer> {
    @Query("SELECT " +
            "    DATE_FORMAT(a.date, '%Y-%m') AS month, " +
            "    a.chainName AS chainName, " +
            "    a.product.productCategoryName AS productCategoryName, " +
            "    SUM(a.volumeUnits * CASE WHEN a.promo = true THEN 1 ELSE 0 END) AS volumeUnitsByPromo, " +
            "    SUM(a.volumeUnits * CASE WHEN a.promo = false THEN 1 ELSE 0 END) AS volumeUnitsByRegular, " +
            "    SUM(a.actualSalesValue * CASE WHEN a.promo = true THEN 1 ELSE 0 END) AS salesValueByPromo, " +
            "    SUM(a.actualSalesValue * CASE WHEN a.promo = false THEN 1 ELSE 0 END) AS salesValueByRegular " +
            "FROM Actual a " +
            "GROUP BY month, chainName, productCategoryName")
    List<Tuple> getStatistic();

    @Query("SELECT a FROM Actual a WHERE a.product.materialNo IN :materialNo AND a.chainName IN :chainName")
    List<Actual> getActualsByProductAndChain(@Param("materialNo") List<Integer> materialNo, @Param("chainName") List<String> chainName);

    @Transactional
    @Modifying
    @Query("UPDATE Actual a " +
            "SET a.promo = " +
            "CASE " +
            "  WHEN ABS(a.actualSalesValue / a.volumeUnits) < (SELECT p.regularPricePerUnit FROM Price p WHERE p.product = a.product AND p.chainName = a.chainName) " +
            "  THEN true " +
            "  ELSE false " +
            "END")
    void updateActualWithPromo();

}


