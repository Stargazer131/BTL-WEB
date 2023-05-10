/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.library.repository;

import com.example.library.model.Book;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Hao
 */
public interface BookRepository extends JpaRepository<Book, Long> {
    // cap nhat so luong sach
    @Transactional
    @Modifying
    @Query("update Book b set b.quantity = b.quantity + ?2 where b.id = ?1")
    public void updateQuantity(Long bookId, int quantity);
    
    // tim so luong sach
    @Query("select b.quantity from Book b where b.id = ?1")
    public int findQuantity(Long bookId);
    
    // tim quyen sach co ID cuoi cung
    public Book findFirstByOrderByIdDesc();
    
    //////
    
    // tim sach theo tu khoa
    public List <Book> findByTitleIgnoreCaseContaining(String pattern);
    
    // tim sach theo tu khoa (sap xep theo sorter)
    public List <Book> findByTitleIgnoreCaseContaining(String pattern, Sort sort);
}
