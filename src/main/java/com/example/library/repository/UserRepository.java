/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.library.repository;

import com.example.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Hao
 */
public interface UserRepository extends JpaRepository<User, Long> {
    // kiem tra ten tai khoan (email) co ton tai khong
    public boolean existsByEmail(String email);
    
    // kiem tra tai khoan (email va password) co ton tai khong
    public User findByEmailAndPassword(String email, String password);
}
