package promoSalesAnalysis.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import promoSalesAnalysis.model.Customer;
import promoSalesAnalysis.model.Product;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {

}
