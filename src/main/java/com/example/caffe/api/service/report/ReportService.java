package com.example.caffe.api.service.report;

import com.example.caffe.api.dto.report.DailyListOfItemsDto;
import com.example.caffe.api.dao.orderheader.OrderHeader;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {

    List<OrderHeader> dailyReport (LocalDate date);
    List<OrderHeader> dailyReportByUser (String userJmbg, LocalDate date);
    List<OrderHeader> weeklyReport (LocalDate date);
    List<OrderHeader> weeklyReportByUser (String userJmbg, LocalDate date);
    List<OrderHeader> montlyReport (LocalDate date);
    List<OrderHeader> montlyReportByUser (String userJmbg, LocalDate date);
    List<OrderHeader> yearlyReport (LocalDate date);
    List<OrderHeader> yearlyReportByUser (String userJmbg, LocalDate date);
    List<DailyListOfItemsDto> dailyListOfItems (LocalDate date);
}
