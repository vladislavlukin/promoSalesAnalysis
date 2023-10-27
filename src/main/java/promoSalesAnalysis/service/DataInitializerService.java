package promoSalesAnalysis.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.transaction.annotation.Transactional;
import promoSalesAnalysis.model.Actual;
import promoSalesAnalysis.model.Customer;
import promoSalesAnalysis.model.Price;
import promoSalesAnalysis.model.Product;
import promoSalesAnalysis.repositories.ActualRepository;
import promoSalesAnalysis.repositories.CustomerRepository;
import promoSalesAnalysis.repositories.PriceRepository;
import promoSalesAnalysis.repositories.ProductRepository;

@Component
@Transactional
@AllArgsConstructor
public class DataInitializerService {
    private static final String PATH_ACTUAL = "src/test/resources/exel/actual.xlsx";
    private static final String PATH_CUSTOMER = "src/test/resources/exel/customer.xlsx";
    private static final String PATH_PRICE = "src/test/resources/exel/price.xlsx";
    private static final String PATH_PRODUCT = "src/test/resources/exel/product.xlsx";

    private final ActualRepository actualRepository;
    private final ProductRepository productRepository;
    private final PriceRepository priceRepository;
    private final CustomerRepository customerRepository;

    public void addDataFromExel() {
        try {
            List<Product> products = getProductFromExel();
            List<Price> prices = getPriceFromExel(products);
            List<Customer> customers = getCustomerFromExel();
            productRepository.saveAll(products);
            priceRepository.saveAll(prices);
            customerRepository.saveAll(customers);

            saveActual(products, customers);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private List<Product> getProductFromExel() throws IOException {
        List<Product> products = new ArrayList<>();
        FileInputStream excelFile = new FileInputStream(DataInitializerService.PATH_PRODUCT);
        Workbook workbook = new XSSFWorkbook(excelFile);
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            if (row.getCell(0).getCellType() == CellType.STRING){
                continue;
            }
            Product product = Product.builder()
                    .materialNo((int) row.getCell(0).getNumericCellValue())
                    .materialDescription(row.getCell(1).getStringCellValue())
                    .productCategoryCode(row.getCell(2).getStringCellValue())
                    .productCategoryName(row.getCell(3).getStringCellValue())
                    .build();
            products.add(product);
        }

        workbook.close();

        return products;
    }

    private List<Price> getPriceFromExel(List<Product> products) throws IOException {
        List<Price> prices = new ArrayList<>();
        FileInputStream excelFile = new FileInputStream(DataInitializerService.PATH_PRICE);
        Workbook workbook = new XSSFWorkbook(excelFile);
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            String first = (row.getCell(0) == null) ? null : row.getCell(0).getStringCellValue();
            if (first == null || first.equals("Chain_name") || first.equals("Общий итог")){
                continue;
            }
            Price price = Price.builder()
                    .chainName(row.getCell(0).getStringCellValue())
                    .product(products
                            .stream()
                            .filter(p -> p.getMaterialNo() == Integer.parseInt(row.getCell(1).getStringCellValue()))
                            .findFirst()
                            .orElse(null))
                    .regularPricePerUnit(row.getCell(2).getNumericCellValue())
                    .build();
            prices.add(price);
        }

        workbook.close();

        return prices;
    }

    private List<Customer> getCustomerFromExel() throws IOException {
        List<Customer> customers = new ArrayList<>();
        FileInputStream excelFile = new FileInputStream(DataInitializerService.PATH_CUSTOMER);
        Workbook workbook = new XSSFWorkbook(excelFile);
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            if (row.getCell(0).getStringCellValue().equals("CH3 Ship To Code")){
                continue;
            }
            Customer customer = Customer.builder()
                    .shipToCode(row.getCell(0).getStringCellValue())
                    .shipToName(row.getCell(1).getStringCellValue())
                    .chainName(row.getCell(2).getStringCellValue())
                    .build();
            customers.add(customer);
        }

        workbook.close();

        return customers;
    }

    private void saveActual(List<Product> products, List<Customer> customers) throws IOException, ParseException {
        List<Actual> actuals = new ArrayList<>();
        FileInputStream excelFile = new FileInputStream(DataInitializerService.PATH_ACTUAL);
        Workbook workbook = new XSSFWorkbook(excelFile);
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            if (row.getCell(0).getStringCellValue().equals("Date")){
                continue;
            }
            Actual actual = Actual.builder()
                    .date(parseStringToDate(row.getCell(0).getStringCellValue()))
                    .product(products
                            .stream()
                            .filter(p -> p.getMaterialNo() == Integer.parseInt(row.getCell(1).getStringCellValue()))
                            .findFirst()
                            .orElse(null))
                    .customer(customers
                            .stream()
                            .filter(c -> c.getShipToCode().equals(row.getCell(2).getStringCellValue()))
                            .findFirst()
                            .orElse(null))
                    .chainName(row.getCell(3).getStringCellValue())
                    .volumeUnits((int) row.getCell(4).getNumericCellValue())
                    .actualSalesValue(row.getCell(5).getNumericCellValue())
                    .build();

            int stopSave = 100000;
            if (actuals.size() == stopSave){
                actualRepository.saveAll(actuals);
                actuals.clear();
            }

            actuals.add(actual);
        }
        actualRepository.saveAll(actuals);

        workbook.close();
    }

    private static Date parseStringToDate(String dateString) throws ParseException {
        String format = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.parse(dateString);
    }
}

