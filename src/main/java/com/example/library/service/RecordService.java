/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.library.service;

import com.example.library.model.Record;
import com.example.library.repository.RecordRepository;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 *
 * @author Hao
 */
@Service
public class RecordService {
    @Autowired
    RecordRepository recordRepository;
    
    
    // them ban ghi vao (them yeu cau muon sach)
    public void add(Record record) {
        recordRepository.save(record);
    }
    
    // tim ban ghi theo ID
    public Record findRecordById(Long recordId) {
        Optional<Record> record = recordRepository.findById(recordId);
        if(record.isPresent()) {
            return record.get();
        }
        else {
            return null;
        }
    }
    
    // cap nhat ban ghi
    public void updateRecordByObject(Record record) {
        recordRepository.save(record);
    } 
    
    // cap nhat cac ban ghi nao bi qua han ma chua tra
    public void updateOverdueBook() {
        recordRepository.updateOverdueBook();
    }
    
    // tim ban ghi theo nguoi dung
    public List<Record> findRecordByUser(Long userId) {
        List<Record> records = recordRepository.findByUserId(userId);
        return records;
    }
    
    // tim ban ghi theo nguoi dung va sach
    public List<Record> findRecordByBookAndUser(Long bookId, Long userId) {
        List<Record> records = recordRepository.findByBookIdAndUserId(bookId, userId);
        return records;
    }
    
    // tim ban ghi dang cho xu ly
    public List<Record> findWaitingList(Long userId) {
        Sort sorter = Sort.by("issueDate").descending();
        List<Record> records = recordRepository.findByUserId(userId, sorter);
        return records.stream().filter(r -> (r.getStatus().equals("Yêu cầu mượn") || r.getStatus().equals("Yêu cầu trả"))).collect(Collectors.toList());
    }
    
    // tim ban ghi dang muon (sach dang muon)
    public List<Record> findBorrowingList(Long userId) {
        Sort sorter = Sort.by("issueDate").descending();
        List<Record> records = recordRepository.findByUserId(userId, sorter);
        return records.stream().filter(r -> r.getStatus().equals("Đang mượn") || r.getStatus().equals("Quá hạn")).collect(Collectors.toList());
    }
    
    // tim ban ghi da muon (sach da muon)
    public List<Record> findBorrowedList(Long userId) {
        Sort sorter = Sort.by("returnDate").descending();
        List<Record> records = recordRepository.findByUserId(userId, sorter);
        return records.stream().filter(r -> r.getStatus().equals("Trả đúng hạn") || r.getStatus().equals("Trả muộn")).collect(Collectors.toList());
    }
    
    ///////////
    
    // tim ban ghi dang cho xu ly
    public List <Record> findApprovedList () {
        List <Record> records = recordRepository.findAll();
        for(Record record : records)
        {
            if(record.getStatus().equals("Yêu cầu mượn") && record.getBook().getQuantity() <= 0)
            {
                deleteRecordById(record.getId());
            }
        }
        return records.stream().filter(r -> ((r.getStatus().equals("Yêu cầu mượn") && r.getBook().getQuantity() > 0) || r.getStatus().equals("Yêu cầu trả"))).collect(Collectors.toList());
        
    }
        
    // xoa ban ghi (admin tu choi yeu cau cho muon sach)
    public void deleteRecordById(Long recordId) {
        if(recordRepository.existsById(recordId)) {
            recordRepository.deleteById(recordId);
        }
    }
    
    
    // chap nhan yeu cau muon sach (cap nhat lai trang thai)
    public void acceptBorrowingBook(Record record) {
        setIssueAndDuedate(record);
        record.setStatus("Đang mượn");
        recordRepository.save(record);
    }
    
    // dat ngay muon va ngay phai tra
    private void setIssueAndDuedate(Record record) {
        Date issueDate = new Date();
        Date dueDate = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dueDate);
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        dueDate = calendar.getTime();
        
        record.setIssueDate(issueDate);
        record.setDueDate(dueDate);
    }
}