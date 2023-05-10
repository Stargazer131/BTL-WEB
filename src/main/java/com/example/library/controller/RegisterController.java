/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.library.controller;

import com.example.library.model.User;
import com.example.library.service.UserService;
import java.util.Calendar;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Hao
 */
@Controller
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    UserService userService;
    
    
    // hien thi trang dang ky
    @GetMapping
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "main/register";
    }
    
    // xu ly du lieu dang ky
    @PostMapping
    public String createNewUser(@ModelAttribute("user") User user, Model model,
            @RequestParam("day") Integer day,
            @RequestParam("month") Integer month,
            @RequestParam("year") Integer year,
            @RequestParam("reenteredPassword") String reenteredPassword) {

        // dat du lieu ngay sinh
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month-1, day);
        Date birthDay = calendar.getTime();
        user.setBirthday(birthDay);
            
        if(!checkValidDate(day, month, year) || userService.checkExistedEmail(user.getEmail()) || !reenteredPassword.equals(user.getPassword())) {
            if(!checkValidDate(day, month, year)) {
                model.addAttribute("dateError", "Ngày tháng không hợp lệ");
            }
            else if(userService.checkExistedEmail(user.getEmail())) {
                model.addAttribute("emailError", "Email này đã tồn tại");
            }
            else {
                model.addAttribute("passwordError", "Mật khẩu nhập lại không khớp");
            }
            return "main/register";
        }
             
        String position = "user";
        user.setPosition(position);
        
        userService.add(user);
        return "main/login";
    }
    
    
    // kiem tra ngay thang hop le
    private static boolean checkValidDate(Integer day, Integer month, Integer year) {
        int maxDays = 31;
        if(month == 4 || month == 6 || month == 9 || month == 11) {
            maxDays = 30;
        } 
        else if(month == 2) {
            // Check for leap year
            if((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                maxDays = 29;
            } 
            else {
                maxDays = 28;
            }
        }
        
        return day <= maxDays;
    }
}
