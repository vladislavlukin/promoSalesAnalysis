package promoSalesAnalysis.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import promoSalesAnalysis.model.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {

}
