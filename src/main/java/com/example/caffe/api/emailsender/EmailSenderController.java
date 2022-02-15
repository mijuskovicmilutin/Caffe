package com.example.caffe.api.emailsender;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class EmailSenderController {

    private final EmailSenderService emailSenderService;

    public EmailSenderController(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @PostMapping
    public void sendSimpleEmail(@RequestParam String toEmail, @RequestParam String body, @RequestParam String subject){
        emailSenderService.sendSimpleEmail(toEmail, body, subject);
    }

    @PostMapping ("/attachment")
    public void sendMailWithAttachment (@RequestParam String toEmail, @RequestParam String body,
                                        @RequestParam String subject, @RequestParam String attachment) {
        try {
            emailSenderService.sendMailWithAttachment(toEmail, body, subject, attachment);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
