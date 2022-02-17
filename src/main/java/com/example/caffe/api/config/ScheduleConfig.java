package com.example.caffe.api.config;

import com.example.caffe.api.emailsender.CreatePDF;
import com.example.caffe.api.emailsender.EmailSenderService;
import com.example.caffe.api.dao.product.Product;
import com.example.caffe.api.dao.product.ProductUrgent;
import com.example.caffe.api.repository.product.ProductRepo;
import com.example.caffe.api.repository.product.ProductUrgentRepo;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Configuration
@EnableScheduling
public class ScheduleConfig {

    private final ProductRepo productRepo;
    private final ProductUrgentRepo productUrgentRepo;
    private final EmailSenderService emailSenderService;
    private final CreatePDF createPDF;

    public ScheduleConfig(ProductRepo productRepo, ProductUrgentRepo productUrgentRepo, EmailSenderService emailSenderService, CreatePDF createPDF) {
        this.productRepo = productRepo;
        this.productUrgentRepo = productUrgentRepo;
        this.emailSenderService = emailSenderService;
        this.createPDF = createPDF;
    }


    // cron = "sec min hours days months dayperweek" - cron = "0 0 15 * * *" se odnosi na job svakog dana u 15:00h
    // @Scheduled (fixedRate = "${productUrgentJob.delay}") se odnosi na varijablu koja se definise u application properties
    @Async
    @Scheduled (cron = "00 17 11 * * *")
    public void productUrgentJob () throws InterruptedException {
        List<Product> listOfUrgentProducts = productRepo.findAllProductsWhereQuantityIsLessThanMinimumQuantity();

        for (int i =0; i< listOfUrgentProducts.size(); i++){
            ProductUrgent productUrgent = ProductUrgent.builder()
                    .productId(listOfUrgentProducts.get(i).getProductId())
                    .name(listOfUrgentProducts.get(i).getName())
                    .type(listOfUrgentProducts.get(i).getType())
                    .barcode(listOfUrgentProducts.get(i).getBarcode())
                    .price(listOfUrgentProducts.get(i).getPrice())
                    .quantityInStock(listOfUrgentProducts.get(i).getQuantityInStock())
                    .minimumQuantityInStock(listOfUrgentProducts.get(i).getMinimumQuantityInStock())
                    .date(LocalDate.now())
                    .build();
            productUrgentRepo.save(productUrgent);
        }
        System.out.println("updateJob");
    }

    @Async
    @Scheduled (cron = "00 16 11 * * *")
    public void deleteUrgentProductsJob (){
        productUrgentRepo.findAll().stream().filter(products -> products.getDate().isBefore(LocalDate.now().minusDays(6)));
        System.out.println("deleteJob");
    }

    @Async
    @Scheduled (cron = "00 27 11 * * *")
    public void createPDFJob () {
        createPDF.createPdf();
        System.out.println("PDF");
    }

    @Async
    @Scheduled (cron = "0 54 13 * * *")
    public void sendMailWithAttachment () throws MessagingException {
        emailSenderService.sendMailWithAttachment("mijuskovicmilutin@gmail.com",
                "Dobar dan/n izvestaj urgnentnih artikala je u prilog/n Srdacan pozdrav",
                "Izvestaj urgentnih artikala",
                "Report_" + LocalDate.now().toString() + ".pdf");
    }
}
