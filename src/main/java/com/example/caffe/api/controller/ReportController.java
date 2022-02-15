package com.example.caffe.api.controller;

import com.example.caffe.api.dto.report.DailyListOfItemsDto;
import com.example.caffe.api.dao.orderheader.OrderHeader;
import com.example.caffe.api.service.report.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping ("/api/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/dailyreport")
    public List<OrderHeader> dailyReport (@RequestParam ("date")
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return reportService.dailyReport(date);
    }

    @GetMapping("/dailyreportbyuser")
    public List<OrderHeader> dailyReportByUser(@RequestParam ("jmbg") String userJmbg,
                                               @RequestParam ("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return reportService.dailyReportByUser(userJmbg, date);
    }

    @GetMapping("/weeklyreport")
    public List<OrderHeader> weeklyReport(@RequestParam ("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return reportService.weeklyReport(date);
    }

    @GetMapping("/weeklyreportbyuser")
    public List<OrderHeader> weeklyReportByUser(@RequestParam ("jmbg") String userJmbg,
                                                @RequestParam ("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return reportService.weeklyReportByUser(userJmbg, date);
    }

    @GetMapping("/montlyreport")
    public List<OrderHeader> montlyReport(@RequestParam ("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return reportService.montlyReport(date);
    }

    @GetMapping("/montlyreportbyuser")
    public List<OrderHeader> montlyReportByUser(@RequestParam ("jmbg") String userJmbg,
                                                @RequestParam ("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return reportService.montlyReportByUser(userJmbg, date);
    }

    @GetMapping("/yearlyreport")
    public List<OrderHeader> yearlyReport(@RequestParam ("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return reportService.yearlyReport(date);
    }

    @GetMapping("/yearlyreportbyuser")
    public List<OrderHeader> yearlyReportByUser(@RequestParam ("jmbg") String userJmbg,
                                                @RequestParam ("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return reportService.yearlyReportByUser(userJmbg, date);
    }

    @GetMapping("/dailylistofitems")
    public List<DailyListOfItemsDto> dailyListOfItems(@RequestParam ("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return reportService.dailyListOfItems(date);
    }
}
