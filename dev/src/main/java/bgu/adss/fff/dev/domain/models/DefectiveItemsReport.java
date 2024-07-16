package bgu.adss.fff.dev.domain.models;

import bgu.adss.fff.dev.data.ProductRepository;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name="DefectiveItemsReport")
public class DefectiveItemsReport extends Report{

    public DefectiveItemsReport() {}

    /**
     * DefectiveItemsReport constructor
     * @param reportId report id
     * @param reportDate report date
     * @param title report title
     * @param content report content
     */
    public DefectiveItemsReport(long reportId, LocalDateTime reportDate, String title, String content, Branch branch){
        super(reportId, reportDate, title, content, ReportType.DEFECTIVE_ITEMS, branch);
    }

    /**
     * Write a report
     * @param repository product repository
     */
    @Override
    public void writeReport(ProductRepository repository) {
        if(repository == null)
            throw new NullPointerException("Product repository is null");

        // write report

        StringBuilder content = new StringBuilder();
        for (Product product : repository.findAll()) {
            List<Item> shelveItems = product.getShelves()
                    .stream()
                    .filter(i -> i.getBranch().equals(getBranch()))
                    .toList();
            List<Item> storageItems = product.getStorage()
                    .stream()
                    .filter(i -> i.getBranch().equals(getBranch()))
                    .toList();

            String productRow = product.getProductName() + " defective and expired items:\n";

            // print defective and expired items on shelves
            int defectiveItems = product.defectiveItemsNum(shelveItems);
            int expiredItems = product.expiredItemsNum(shelveItems);

            productRow += "\tShelves:\n";
            productRow += "\t\tDefective Items: " + defectiveItems + "\n";
            productRow += "\t\tExpired Items: " + expiredItems + "\n";

            // print defective and expired items in storage
            defectiveItems = product.defectiveItemsNum(storageItems);
            expiredItems = product.expiredItemsNum(storageItems);
            productRow += "\tStorage:\n";
            productRow += "\t\tDefective Items: " + defectiveItems + "\n";
            productRow += "\t\tExpired Items: " + expiredItems + "\n";

            content.append(productRow);

        }

        setContent(content.toString());

    }

}
