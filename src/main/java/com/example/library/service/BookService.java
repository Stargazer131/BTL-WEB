/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.library.service;

import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import com.example.library.repository.RecordRepository;
import java.util.ArrayList;
import java.util.Collections;
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
public class BookService {  
    @Autowired
    BookRepository bookRepository;
    
    @Autowired
    RecordRepository recordRepository;
    
    // kiem tra xem sach con khong
    public boolean checkIfBookAvailable(Long bookId) {
        int quantity = bookRepository.findQuantity(bookId);
        return quantity > 0;
    }
    
    // tang sach len 1 (khi tra sach)
    public void increaseBookQuantity(Long bookId) {
        bookRepository.updateQuantity(bookId, 1);
    }
    
    // giam sach di 1 (khi muon sach)
    public void decreaseBookQuantity(Long bookId) {
        bookRepository.updateQuantity(bookId, -1);
    }
    
    // tim id cua sach cuoi cung (dung cho cap nhat anh moi)
    public Long getLastBookId() {
        Book lastBook = bookRepository.findFirstByOrderByIdDesc();
        return lastBook.getId();
    }
    
    // tim sach cho trang main page (theo thu tu cu nhat den moi nhat) (chi lay 10 sach)
    public List<Book> findListOfBookForMainPage() {
        List <Book> listBooks = findListOfBook();
        if(listBooks.size() > 10) {
            listBooks = listBooks.subList(0, 10);
        }
        return listBooks;
    }
    
    // tim sach cho trang main page (theo thu tu moi nhat den cu nhat) (chi lay 10 sach)
    public List<Book> findListOfNewBook() {
        Sort sorter = Sort.by("id").descending();
        List <Book> listBooks = findListOfBook(sorter);
        if(listBooks.size() > 10) {
            listBooks = listBooks.subList(0, 10);
        }
        return listBooks;
    }
    
    // tim sach duoc muon nhieu nhat (chi lay 10 sach)
    public List<Book> findListOfMostBorrowedBook() {
        List<Object[]> bookAndBorrowTime = recordRepository.findBookAndBorrowTime();
        List<Book> mostBorrowedBooks = new ArrayList<>();
        for(Object[] o : bookAndBorrowTime) {
            mostBorrowedBooks.add((Book)o[0]);
        }
        if(mostBorrowedBooks.size() > 10) {
            mostBorrowedBooks = mostBorrowedBooks.subList(0, 10);
        }
        return mostBorrowedBooks;
    }
    
    //////////////////////////////////////
    
    
    // tim tat ca sach
    public List <Book> findListOfBook() {
        return bookRepository.findAll();
    }
    
    // tim tat ca sach (sap xep theo thu tu cua sorter)
    public List <Book> findListOfBook(Sort sorter) {
        return bookRepository.findAll(sorter);
    }
    
    // tim sach bang ID
    public Book findBookById(Long bookId) {
        Optional <Book> book = bookRepository.findById(bookId);
        if(book.isPresent())
        {
            return book.get();
        }
        else 
        {
            return null;
        }
    }

    // tim sach co cung the loai (chi lay 5 sach)
    public List<Book> findBookByGenre(String bookGenre) {
        List<Book> allBooks = bookRepository.findAll();
        List<Book> filterBooks = allBooks.stream().filter(b -> b.getGenre().equals(bookGenre)).collect(Collectors.toList());
        // sap xep ngau nhien danh sach
        Collections.shuffle(filterBooks);
        if (filterBooks.size() <= 5) {
            return filterBooks;
        }
        return filterBooks.subList(0, 5);
    }
   
    // them sach hoac la cap nhat sach
    public void saveBook(Book book) {
        bookRepository.save(book);
    }
   
    // tim sach theo tu khoa (sap xep theo ten sach)
    public List <Book> findBookByTitleContaining(String pattern)
    {
        Sort sorter = Sort.by("title").ascending();
        return bookRepository.findByTitleIgnoreCaseContaining(pattern, sorter);
    }
}

