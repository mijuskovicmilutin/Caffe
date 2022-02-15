package com.example.caffe.api.service.user;

import com.example.caffe.api.dto.user.SaveUserDto;
import com.example.caffe.api.emailsender.EmailSenderService;
import com.example.caffe.api.exception.ExceptionBadRequest;
import com.example.caffe.api.exception.ExceptionOk;
import com.example.caffe.api.dao.user.User;
import com.example.caffe.api.repository.user.UserRepo;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Transactional
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailSenderService emailSenderService;

    public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder, EmailSenderService emailSenderService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.emailSenderService = emailSenderService;
    }

    @Override
    public void saveUser(SaveUserDto saveUser) {

        Integer userExists = userRepo.countByJmbg(saveUser.getJmbg());
        boolean testEmailPatternBool = testEmailPattern(saveUser.getEmail());

        if (userExists == 0 && testEmailPatternBool){
            try {
                userRepo.save(
                    User.builder()
                        .firstName(saveUser.getFirstName())
                        .lastName(saveUser.getLastName())
                        .jmbg(saveUser.getJmbg())
                        .username(saveUser.getUsername())
                        .password(passwordEncoder.encode(saveUser.getPassword()))
                        .role(saveUser.getRole())
                        .email(saveUser.getEmail())
                        .build()
                );
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }else if (userExists > 0){
            throw new ExceptionBadRequest("Korisnik sa unetim JMBG-om vec postoji.");
        }else if (!testEmailPatternBool){
            throw new ExceptionBadRequest("Uneta Email adresa nije u validnom formatu. Primer validnog formata - username@domain.com");
        }
    }

    @Override
    public List<User> getAllUsers (){
        return userRepo.findAll();
    }

    @Override
    public List<User> getAllUsersSorted(Boolean sortByFirstName, Boolean sortByLastName, Boolean sortByJmbg, Boolean sortByRole) {
        if (sortByFirstName){
            return userRepo.findAll().stream()
                    .sorted(Comparator.comparing(User::getFirstName)).collect(Collectors.toList());
        }else if (sortByLastName){
            return userRepo.findAll().stream()
                    .sorted(Comparator.comparing(User::getLastName)).collect(Collectors.toList());
        }else if (sortByJmbg){
            return userRepo.findAll().stream()
                    .sorted(Comparator.comparing(User::getJmbg)).collect(Collectors.toList());
        }else if (sortByRole){
            return userRepo.findAll().stream()
                    .sorted(Comparator.comparing(User::getRole).thenComparing(User::getLastName)).collect(Collectors.toList());
        }else {
            throw new ExceptionBadRequest("Nisu uneti ulazni parametri");
        }
    }

    @Override
    public List<User> searchUserByFirstName (String firstName){
        return userRepo.searchByFirstName(firstName).stream()
                .sorted(Comparator.comparing(User::getRole).thenComparing(User::getLastName)).collect(Collectors.toList());
    }

    @Override
    public List<User> searchUserByLastName(String lastName) {
        return userRepo.searchByLastName(lastName).stream()
                .sorted(Comparator.comparing(User::getRole).thenComparing(User::getLastName)).collect(Collectors.toList());
    }

    @Override
    public User findByJmbg (String jmbg){
        User user = userRepo.findByJmbg(jmbg);
        if (user != null){
            return user;
        }else {
            throw new ExceptionOk ("Korisnik sa unetim JMBG-om ne postoji.");
        }
    }

    @Override
    public List<User> findByRole (String role){
        try {
            return userRepo.findByRole(role).stream()
                    .sorted(Comparator.comparing(User::getLastName)).collect(Collectors.toList());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public void randomGenerateNewPassword(String username) {
        try {
            User user = userRepo.findByUsername(username);

            String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}|;:,<.>/?";
            String pwd = RandomStringUtils.random(10, characters);
            System.out.println(pwd);

            try {
                userRepo.updatePassword(passwordEncoder.encode(pwd), user.getJmbg());
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            System.out.println(pwd);
            //emailSenderService.sendSimpleEmail(user.getEmail(), pwd, "New Password");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updatePassword(String newPassword) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            User user = userRepo.findByUsername(username);
            userRepo.updatePassword(passwordEncoder.encode(newPassword), user.getJmbg());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteByJmbg(String jmbg) {
        try {
            userRepo.deleteByJmbg(jmbg);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("Korisnik sa unetim username ne postoji.");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    public static boolean testEmailPattern (String email){
        String regexPatter = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" +
                "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regexPatter)
                .matcher(email)
                .matches();
    }
}
