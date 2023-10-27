package promoSalesAnalysis.repositories;

import jakarta.persistence.Tuple;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import promoSalesAnalysis.dto.request.PromoSaleStatistic;
import promoSalesAnalysis.service.DataInitializerService;
import promoSalesAnalysis.model.Actual;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ActualRepositoryTests {
    @Autowired
    private ActualRepository actualRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeAll
    void setUp() {
        DataInitializerService dataInitializerService = new DataInitializerService(actualRepository, productRepository, priceRepository, customerRepository);
        dataInitializerService.addDataFromExel();

        actualRepository.updateActualWithPromo();
    }
    @AfterAll
    void cleanup() {
        jdbcTemplate.execute("DELETE FROM actual");
        jdbcTemplate.execute("DELETE FROM customer");
        jdbcTemplate.execute("DELETE FROM price");
        jdbcTemplate.execute("DELETE FROM product");

    }
    @Test
    void testGetStatistic() {
        List<PromoSaleStatistic> statistics = statistics();

        assertNotNull(statistics);
        assertTrue(statistics.stream().anyMatch(s -> s.getMonth().equals("2021-04")
                && s.getChainName().equals("Chain 1")
                && s.getVolumeUnitsByPromo() == 325608));

    }

    @Test
    void testGetActualsByProductAndChain() {
        List<Integer> material = new ArrayList<>();
        material.add(70158202);
        material.add(70172900);

        List<String> chainName = new ArrayList<>();
        chainName.add("Chain 1");
        chainName.add("Chain 2");

        List<Actual> statistics = actualRepository.getActualsByProductAndChain(material, chainName);

        assertNotNull(statistics);
        assertTrue(statistics.stream().anyMatch(Actual::getPromo));
        assertTrue(statistics.stream().anyMatch(s -> s.getProduct().getMaterialNo() == 70158202
                && s.getChainName().equals("Chain 2")));
    }

    private List<PromoSaleStatistic> statistics(){
        List<Tuple> result = actualRepository.getStatistic();
        List<PromoSaleStatistic> promoStatistics = new ArrayList<>();

        for (Tuple row : result) {
            String month = row.get(0, String.class);
            String chainName = row.get(1, String.class);
            String productCategoryName = row.get(2, String.class);
            long volumeUnitsByPromo = row.get(3, Long.class);
            long volumeUnitsByRegular = row.get(4, Long.class);
            double salesValueByPromo = row.get(5, Double.class);
            double salesValueByRegular = row.get(6, Double.class);
            double percentPromo = (salesValueByPromo / (salesValueByPromo + salesValueByRegular)) * 100;

            PromoSaleStatistic promoStatistic = new PromoSaleStatistic(month, chainName, productCategoryName, volumeUnitsByRegular, volumeUnitsByPromo, salesValueByRegular, salesValueByPromo, percentPromo);
            promoStatistics.add(promoStatistic);
        }

        return promoStatistics;

    }
}
