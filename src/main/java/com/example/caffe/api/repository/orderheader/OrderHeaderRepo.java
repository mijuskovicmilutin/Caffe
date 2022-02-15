package com.example.caffe.api.repository.orderheader;

import com.example.caffe.api.dto.report.DailyListOfItemsDto;
import com.example.caffe.api.dao.orderheader.OrderHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderHeaderRepo extends JpaRepository<OrderHeader, Long> {

    @Query (value = "SELECT o " +
            "FROM OrderHeader o " +
            "WHERE o.time BETWEEN ?1 AND ?2")
    List<OrderHeader> dailyReport (LocalDateTime time1, LocalDateTime time2);

    @Query (value = "SELECT o " +
            "FROM OrderHeader o " +
            "JOIN o.user u " +
            "WHERE u.jmbg = ?1 AND o.time BETWEEN ?2 and ?3")
    List<OrderHeader> dailyReportByUser (String userJmbg, LocalDateTime time1, LocalDateTime time2);

    @Query (value = "SELECT o " +
            "FROM OrderHeader o " +
            "WHERE o.time BETWEEN ?1 AND ?2")
    List<OrderHeader> weeklyReport (LocalDateTime time1, LocalDateTime time2);

    @Query (value = "SELECT o " +
            "FROM OrderHeader o " +
            "JOIN o.user u " +
            "WHERE u.jmbg = ?1 " +
            "AND o.time BETWEEN ?2 AND ?3")
    List<OrderHeader> weeklyReportByUser (String userJmbg, LocalDateTime time1, LocalDateTime time2);

    @Query (value = "SELECT o " +
            "FROM OrderHeader o " +
            "WHERE o.time BETWEEN ?1 AND ?2")
    List<OrderHeader> montlyReport (LocalDateTime time1, LocalDateTime time2);

    @Query (value = "SELECT o " +
            "FROM OrderHeader o " +
            "JOIN o.user u " +
            "WHERE u.jmbg = ?1 " +
            "AND o.time BETWEEN ?2 AND ?3")
    List<OrderHeader> montlyReportByUser (String userJmbg, LocalDateTime tim1, LocalDateTime time2);

    @Query (value = "SELECT o " +
            "FROM OrderHeader o " +
            "WHERE o.time BETWEEN ?1 AND ?2")
    List<OrderHeader> yearlyReport (LocalDateTime time1, LocalDateTime time2);

    @Query (value = "SELECT o " +
            "FROM OrderHeader o " +
            "JOIN o.user u " +
            "WHERE u.jmbg = ?1 " +
            "AND o.time BETWEEN ?2 AND ?3")
    List<OrderHeader> yearlyReportByUser (String userJmbg, LocalDateTime time1, LocalDateTime time2);

    @Query (value = "select s.name as name, s.quantity as quantity, (s.quantity * pr.price) as total_price, pr.price as price from\n" +
            "(select p.product_id as product, p.name as name, sum(oi.quantity) as quantity \n" +
            "from order_header oh\n" +
            "join order_item oi on oh.order_header_id = oi.order_header_order_header_id\n" +
            "join products p on p.product_id = oi.product_id\n" +
            "where oh.time BETWEEN ?1 AND ?2 \n" +
            "group by p.product_id) s, products pr\n" +
            "where s.product = pr.product_id", nativeQuery = true)
    List<DailyListOfItemsDto> dailyListOfItems (LocalDateTime time1, LocalDateTime time2);
}
