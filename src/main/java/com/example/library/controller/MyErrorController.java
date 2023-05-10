/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.library.controller;

/**
 *
 * @author Hao
 */
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/error")
public class MyErrorController implements ErrorController {

    @GetMapping("/404")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle404Error() {
        return "error/404error";
    }
    
    @GetMapping("/500")
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handle500Error() {
        return "error/500error";
    }
}

