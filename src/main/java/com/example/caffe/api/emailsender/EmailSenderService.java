package com.example.caffe.api.emailsender;

import com.example.caffe.api.dao.product.ProductUrgent;
import com.example.caffe.api.repository.product.ProductUrgentRepo;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmailSenderService {

    private final JavaMailSender mailSender;
    private final ProductUrgentRepo productUrgentRepo;

    public EmailSenderService(JavaMailSender mailSender, ProductUrgentRepo productUrgentRepo) {
        this.mailSender = mailSender;
        this.productUrgentRepo = productUrgentRepo;
    }

    public void sendSimpleEmail(String toEmail, String body, String subject){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("bk45mmi@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);
        System.out.println("Mail sent");
    }

    public void sendMailWithAttachment (String toEmail, String body, String subject, String attachment)
                                        throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setFrom("bk45mmi@gmail.com");
        mimeMessageHelper.setTo(toEmail);
        mimeMessageHelper.setText(body);
        mimeMessageHelper.setSubject(subject);

        FileSystemResource fileSystem = new FileSystemResource(new File(attachment));

        mimeMessageHelper.addAttachment(fileSystem.getFilename(), fileSystem);

        mailSender.send(mimeMessage);
        System.out.println("Mail sent");
    }

    public List<ProductUrgent> generatePdf (){
        return productUrgentRepo.findAll().stream()
                .sorted(Comparator.comparing(ProductUrgent::getQuantityInStock).reversed()).collect(Collectors.toList());
    }
}
