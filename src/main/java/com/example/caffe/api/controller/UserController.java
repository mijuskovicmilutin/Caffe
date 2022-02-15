package com.example.caffe.api.controller;

import com.example.caffe.api.dto.user.ForgotPasswordDto;
import com.example.caffe.api.dto.user.SaveUserDto;
import com.example.caffe.api.dao.user.User;
import com.example.caffe.api.dto.user.UpdatePasswordDto;
import com.example.caffe.api.service.user.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/save")
    public void saveUser(@Valid @RequestBody SaveUserDto userDto){
        userService.saveUser(userDto);
    }

    @PostMapping("/forgotpassword")
    public void forgotPassword (@RequestBody ForgotPasswordDto forgotPasswordDto){
        userService.randomGenerateNewPassword(forgotPasswordDto.getUsername());
    }

    @PutMapping("/updatepassword")
    public void updatePassword (@RequestBody UpdatePasswordDto updatePasswordDto){
        userService.updatePassword(updatePasswordDto.getNewPassword());
    }

    @GetMapping("/get")
    public List<User> getAllUsers (){
        return userService.getAllUsers();
    }

    @GetMapping("/get/sorted")
    public List<User> getAllUsersSorted(@Valid @RequestParam Boolean sortByFirstName,
                                        @Valid @RequestParam Boolean sortByLastName,
                                        @Valid @RequestParam Boolean sortByJmbg,
                                        @Valid @RequestParam Boolean sortByRole){
        return userService.getAllUsersSorted(sortByFirstName, sortByLastName, sortByJmbg, sortByRole);
    }

    @GetMapping("get/firstname")
    public List<User> searchUserByFirstname (@Valid @RequestParam String firstName){
        return userService.searchUserByFirstName(firstName);
    }

    @GetMapping("get/lastname")
    public List<User> searchUserByLastName (@Valid @RequestParam String lastName){
        return userService.searchUserByLastName(lastName);
    }

    @GetMapping("get/jmbg")
    public User findByJmbg (@Valid @RequestParam @Size(min = 13, max = 13) String jmbg){
        return userService.findByJmbg(jmbg);
    }

    @GetMapping("get/role")
    public List<User> findByRole (@Valid @RequestParam String role){
        return userService.findByRole(role);
    }

    @DeleteMapping("/admin/delete")
    public void deleteByJmbg (@Valid @RequestParam("jmbg") @NotNull String jmbg) {
        userService.deleteByJmbg(jmbg);
    }
}
