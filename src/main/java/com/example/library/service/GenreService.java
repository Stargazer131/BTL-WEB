/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.library.service;

import com.example.library.model.Genre;
import com.example.library.repository.GenreRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 *
 * @author Hao
 */
@Service
public class GenreService {
    @Autowired
    GenreRepository genreRepository;
    
    // lay ra tat ca cac the loai va sap xep theo bang chu cai
    public List<String> findAllGenres() {
        Sort sorter = Sort.by("name").ascending();
        List<Genre> genres = genreRepository.findAll(sorter);
        List<String> genreNames = new ArrayList<>();
        genres.forEach(g -> genreNames.add(g.getName()));
        return genreNames;
    }
}
