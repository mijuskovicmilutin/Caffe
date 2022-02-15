package com.example.caffe.api.repository.user;

import com.example.caffe.api.dao.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepo extends JpaRepository<User, Long>{

    @Query("select count(u) from User u where u.jmbg = ?1")
    Integer countByJmbg (String jmbg);

    void deleteByJmbg (String jmbg);

    User findByJmbg (String jmbg);

    List<User> findByRole (String role);

    @Query("select u from User u where u.firstName like CONCAT(?1, '%')")
    List<User> searchByFirstName (String firstName);

    @Query("select u from User u where u.lastName like CONCAT(?1, '%')")
    List<User> searchByLastName (String lastName);

    User findByUsername (String username);

    @Modifying
    @Query (value = "UPDATE User u SET u.password = ?1 where u.jmbg = ?2")
    void updatePassword (String password, String jmbg);

}
