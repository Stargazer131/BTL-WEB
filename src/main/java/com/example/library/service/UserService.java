/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.library.service;

import com.example.library.model.User;
import com.example.library.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Hao
 */
@Service
public class UserService {          
    @Autowired
    UserRepository userRepository;
    
    // tim nguoi dung theo ID
    public User findById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()) {
            return user.get();
        }
        else {
            return null;
        }
    }
    
    // them nguoi dung
    public void add(User user) {
        userRepository.save(user);
    }
    
    public void update(User user) {
        userRepository.save(user);
    }
    
    // kiem tra ten tai khoan (email) co ton tai khong
    public boolean checkExistedEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    // kiem tra tai khoan (email va password)
    public User findUserWithAccount(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }
    
    /////////////
    
    // tim tat ca nguoi dung (co position = user)
    public List<User> findListOfUser() {
        List <User> listOfUser = userRepository.findAll();
        return listOfUser.stream().filter(u -> u.getPosition().equals("user")).collect(Collectors.toList());
    }
}
