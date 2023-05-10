/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.library.repository;

import com.example.library.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Hao
 */
public interface GenreRepository extends JpaRepository<Genre, Integer> { 
    
}
