package com.example.caffe.api.dao.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table (name = "product_import", schema = "usersdb")
public class ProductImport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_import_id")
    private Long productImportId;

    @ManyToOne (
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JoinColumn (
            name = "product_id",
            referencedColumnName = "product_id"
    )
    private Product product;

    private LocalDateTime dateTime;

    private Integer quantity;
}
