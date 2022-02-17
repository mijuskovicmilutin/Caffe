package com.example.caffe.api.emailsender;

import com.example.caffe.api.dao.product.ProductUrgent;
import com.example.caffe.api.repository.product.ProductUrgentRepo;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class CreatePDF {

    private final ProductUrgentRepo productUrgentRepo;

    public CreatePDF(ProductUrgentRepo productUrgentRepo) {
        this.productUrgentRepo = productUrgentRepo;
    }

    public void createPdf() {

        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Report_" + LocalDate.now() + ".pdf"));
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 20f);
            Font headerFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 8f);
            Font cellFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 8f);

            Paragraph p = new Paragraph(Element.ALIGN_CENTER);
            p.setAlignment(Element.ALIGN_CENTER);
            p.setFont(titleFont);
            p.add("Izvestaj urgentnih artikala");

            document.add(p);
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(8);

            table.addCell(new Paragraph(Element.ALIGN_CENTER,"Product Id", headerFont));
            table.addCell(new Paragraph(Element.ALIGN_CENTER,"Name", headerFont));
            table.addCell(new Paragraph(Element.ALIGN_CENTER,"Type", headerFont));
            table.addCell(new Paragraph(Element.ALIGN_CENTER,"Barcode", headerFont));
            table.addCell(new Paragraph(Element.ALIGN_CENTER,"Price", headerFont));
            table.addCell(new Paragraph(Element.ALIGN_CENTER,"Quantity In Stock", headerFont));
            table.addCell(new Paragraph(Element.ALIGN_CENTER,"Minimum Quantity In Stock", headerFont));
            table.addCell(new Paragraph(Element.ALIGN_CENTER,"Date", headerFont));

            List<ProductUrgent> dailyListOfUrgentProducts =
                    productUrgentRepo.findAll().stream().filter(products -> products.getDate().isEqual(LocalDate.now())).collect(Collectors.toList());

            for (int i = 0; i < dailyListOfUrgentProducts.size(); i++){
                table.addCell(new Paragraph(dailyListOfUrgentProducts.get(i).getProductId().toString(), cellFont));
                table.addCell(new Paragraph(dailyListOfUrgentProducts.get(i).getName(), cellFont));
                table.addCell(new Paragraph(dailyListOfUrgentProducts.get(i).getType(), cellFont));
                table.addCell(new Paragraph(dailyListOfUrgentProducts.get(i).getBarcode(), cellFont));
                table.addCell(new Paragraph(dailyListOfUrgentProducts.get(i).getPrice().toString(), cellFont));
                table.addCell(new Paragraph(dailyListOfUrgentProducts.get(i).getQuantityInStock().toString(), cellFont));
                table.addCell(new Paragraph(dailyListOfUrgentProducts.get(i).getMinimumQuantityInStock().toString(), cellFont));
                table.addCell(new Paragraph(dailyListOfUrgentProducts.get(i).getDate().toString(), cellFont));
            }
            dailyListOfUrgentProducts.stream().sorted(Comparator.comparing(ProductUrgent::getQuantityInStock)).map(productUrgent ->
            {table.addCell(productUrgent.getProductId().toString());
                table.addCell(productUrgent.getName());
                table.addCell(productUrgent.getType());
                table.addCell(productUrgent.getBarcode());
                table.addCell(productUrgent.getPrice().toString());
                table.addCell(productUrgent.getQuantityInStock().toString());
                table.addCell(productUrgent.getMinimumQuantityInStock().toString());
                table.addCell(productUrgent.getDate().toString());
                return null;
            });
            document.add(table);

            document.close();
            writer.close();

        }catch (DocumentException e){
            e.printStackTrace();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}