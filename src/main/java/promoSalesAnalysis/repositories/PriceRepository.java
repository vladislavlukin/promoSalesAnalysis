package promoSalesAnalysis.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import promoSalesAnalysis.model.Price;

@Repository
public interface PriceRepository extends CrudRepository<Price, Integer> {

}
