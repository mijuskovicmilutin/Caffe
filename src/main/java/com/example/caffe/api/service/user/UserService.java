package com.example.caffe.api.service.user;
import com.example.caffe.api.dto.user.SaveUserDto;
import com.example.caffe.api.dao.user.User;

import java.util.List;

public interface UserService {

    void saveUser (SaveUserDto userDto);
    List<User> getAllUsers ();
    List<User> getAllUsersSorted (Boolean sortByFirstName, Boolean sortByLastName, Boolean sortByJmbg, Boolean sortByRole);
    List<User> searchUserByFirstName (String firstName);
    List<User> searchUserByLastName (String lastName);
    User findByJmbg (String jmbg);
    List<User> findByRole (String role);
    void randomGenerateNewPassword (String username);
    void updatePassword (String newPassword);
    void deleteByJmbg (String jmbg);
}
