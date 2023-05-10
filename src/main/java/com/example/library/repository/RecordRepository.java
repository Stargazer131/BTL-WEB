/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.library.repository;

import com.example.library.model.Record;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Hao 
 */
public interface RecordRepository extends JpaRepository<Record, Long> {
    // tim ban ghi theo id nguoi dung
    public List<Record> findByUserId(Long userId);
    
    // tim ban ghi theo id nguoi dung (sap xep theo sort)
    public List<Record> findByUserId(Long userId, Sort sort);
    
    // tim ban ghi theo id nguoi dung va id sach
    public List<Record> findByBookIdAndUserId(Long bookId, Long userId);  
    
    // cap nhat ban ghi qua han ma chua tra (sach qua han ma chua tra)
    @Transactional
    @Modifying
    @Query("update Record r set r.status = 'Quá hạn' where r.returnDate = NULL and r.dueDate != NULL and CURRENT_DATE > r.dueDate ")
    public void updateOverdueBook();
    
    // tim sach va so lan muon (qua han hay dang muon cung tinh la mot lan muot)
    @Query("select r.book, count(r.id) from Record r where r.status != 'Yêu cầu mượn' group by r.book.id order by count(r.id) desc")
    public List<Object[]> findBookAndBorrowTime();
    
    ////////
    
    // cap nhat trang thai cho ban ghi
    @Transactional
    @Modifying
    @Query("update Record r set r.status = ?1 where r.id = ?2")
    public void updateStatusRecord(String recordStatus, Long recordId);
}
