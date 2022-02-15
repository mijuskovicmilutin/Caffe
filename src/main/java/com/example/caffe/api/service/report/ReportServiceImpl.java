package com.example.caffe.api.service.report;

import com.example.caffe.api.dto.report.DailyListOfItemsDto;
import com.example.caffe.api.dao.orderheader.OrderHeader;
import com.example.caffe.api.repository.orderheader.OrderHeaderRepo;
import com.example.caffe.api.repository.orderitem.OrderItemRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional
@Service
public class ReportServiceImpl implements ReportService{

    private final OrderHeaderRepo orderHeaderRepo;
    private final OrderItemRepo orderItemRepo;

    public ReportServiceImpl(OrderHeaderRepo orderHeaderRepo, OrderItemRepo orderItemRepo) {
        this.orderHeaderRepo = orderHeaderRepo;
        this.orderItemRepo = orderItemRepo;
    }

    //Date currentTime = Date.from(LocalDateTime.now(ZoneId.of("GMT+02:00")).toInstant(ZoneId.of("GMT+02:00").getRules().getOffset(Instant.now())));

    @Override
    public List<OrderHeader> dailyReport(LocalDate date) {
        return orderHeaderRepo.dailyReport(date.atTime(00,00), date.atTime(00,00).plusDays(1));
    }

    @Override
    public List<OrderHeader> dailyReportByUser(String userJmbg, LocalDate date) {
        return orderHeaderRepo.dailyReportByUser(userJmbg, date.atTime(00, 00), date.atTime(00,00).plusDays(1));
    }

    @Override
    public List<OrderHeader> weeklyReport(LocalDate date) {
        return orderHeaderRepo.weeklyReport(date.atTime(00, 00), date.atTime(00,00).plusDays(7));
    }

    @Override
    public List<OrderHeader> weeklyReportByUser(String userJmbg, LocalDate date) {
        return orderHeaderRepo.weeklyReportByUser(userJmbg, date.atTime(00, 00), date.atTime(00,00).plusDays(7));
    }

    @Override
    public List<OrderHeader> montlyReport(LocalDate date) {
        return orderHeaderRepo.montlyReport(date.atTime(00, 00), date.atTime(00,00).plusMonths(1));
    }

    @Override
    public List<OrderHeader> montlyReportByUser(String userJmbg, LocalDate date) {
        return orderHeaderRepo.montlyReportByUser(userJmbg, date.atTime(00, 00), date.atTime(00,00).plusMonths(1));
    }

    @Override
    public List<OrderHeader> yearlyReport(LocalDate date) {
        return orderHeaderRepo.yearlyReport(date.atTime(00, 00), date.atTime(00,00).plusMonths(12));
    }

    @Override
    public List<OrderHeader> yearlyReportByUser(String userJmbg, LocalDate date) {
        return orderHeaderRepo.yearlyReportByUser (userJmbg, date.atTime(00, 00), date.atTime(00,00).plusMonths(12));
    }

    @Override
    public List<DailyListOfItemsDto> dailyListOfItems(LocalDate date) {
        return orderHeaderRepo.dailyListOfItems(date.atTime(00, 00), date.atTime(00,00).plusDays(1));
    }

}
