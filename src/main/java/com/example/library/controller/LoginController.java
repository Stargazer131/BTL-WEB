/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.library.controller;

import com.example.library.model.User;
import com.example.library.service.RecordService;
import com.example.library.service.UserService;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Hao
 */
@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    UserService userService;
    
    @Autowired
    RecordService recordService;
    
    
    // hien thi trang dang nhap
    @GetMapping
    public String login() {
        return "main/login";
    }
    
    // xu ly du lieu dang nhap
    @PostMapping
    public String handleLoginData(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session, Model model) {
        User user = userService.findUserWithAccount(email, password);
        if(user != null) {
            recordService.updateOverdueBook();
            session.setMaxInactiveInterval(60*30);
            session.setAttribute("USER", user);
            
            // neu la nguoi dung
            if(user.getPosition().equals("user")) {
                return "redirect:/user/mainPage";
            }
            // neu la admin
            else {
                return "redirect:/admin/mainPage";
            }
        }
        else {
            model.addAttribute("errorMessage", "Tên email hoặc mật khẩu không đúng");
            model.addAttribute("email", email);
            model.addAttribute("password", password);
            return "main/login";
        }
    }
}
