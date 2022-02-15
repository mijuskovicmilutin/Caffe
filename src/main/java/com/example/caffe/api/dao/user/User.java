package com.example.caffe.api.dao.user;

import com.example.caffe.api.dao.orderheader.OrderHeader;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table (name = "users", schema = "usersdb")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "user_id")
    @JsonIgnore
    private Long userId;

    @Column(name = "first_name",nullable = false)
    private String firstName;

    @Column(name = "last_name",nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false, length = 13)
    private String jmbg;

    @Column(unique = true)
    private String username;

    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String email;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "user"
    )
    @JsonIgnore
    private List<OrderHeader> orderHeader;
}
