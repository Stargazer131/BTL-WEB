/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.model.Record;
import com.example.library.model.User;
import com.example.library.service.BookService;
import com.example.library.service.RecordService;
import com.example.library.service.UserService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

/**
 *
 * @author Hao
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    RecordService recordService;

    @Autowired
    BookService bookService;

    @Autowired
    UserService userService;

    // hien thi trang thong tin nguoi dung
    @GetMapping("/homepage")
    public String userHomePage() {
        return "/user/userHomepage";
    }

    //hien thi trang thong tin nguoi dung sau khi thay doi
    @PostMapping("/homepage")
    public String changeInfoUser(@SessionAttribute("USER") User user, @RequestParam("birthdayParam") String birthdayParam, @RequestParam("nameParam") String nameParam, HttpSession session) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date birthday = sdf.parse(birthdayParam);
            user.setBirthday(birthday);
        } catch (ParseException e) {
            System.out.println(e);
        }

        user.setName(nameParam);
        userService.update(user);

        session.setMaxInactiveInterval(60 * 30);
        session.setAttribute("USER", user);

        return "/user/userHomepage";
    }

    @PostMapping("/homepage/changePasswordForm")
    public String changePasswordForm(@RequestParam("userId") Long userId, HttpSession session, Model model) {
        User user = userService.findById(userId);
        model.addAttribute("user", user);
        return "/user/changePasswordForm";
    }

    @PostMapping("/homepage/changePassword")
    public String changePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, @RequestParam("retypePassword") String retypePassword, HttpSession session, Model model) {
        String messageOldPassword = "";
        String messageNewPassword = "";
        String messageRetypePassword = "";
        String changeSuccess = "";

        User user = (User) session.getAttribute("USER");

        if (!user.getPassword().equals(oldPassword)) {
            messageOldPassword = "Vui lòng nhập đúng mật khẩu cũ";
            model.addAttribute("messageOldPassword", messageOldPassword);
            model.addAttribute("oldPassword", oldPassword);
            model.addAttribute("newPassword", newPassword);
            model.addAttribute("retypePassword", retypePassword);
            return "/user/changePassword";
        } else {
            if (newPassword.length() < 1) {
                messageNewPassword = "Mật khẩu ít nhất 1 ký tự";
                model.addAttribute("messageNewPassword", messageNewPassword);
                model.addAttribute("oldPassword", oldPassword);
                model.addAttribute("newPassword", newPassword);
                model.addAttribute("retypePassword", retypePassword);
                return "/user/changePassword";
            } else {
                if (!retypePassword.equals(newPassword)) {
                    messageRetypePassword = "Nhập lại mật khẩu không đúng";
                    model.addAttribute("messageRetypePassword", messageRetypePassword);
                    model.addAttribute("oldPassword", oldPassword);
                    model.addAttribute("newPassword", newPassword);
                    model.addAttribute("retypePassword", retypePassword);
                    return "/user/changePassword";
                } else {
                    user.setPassword(newPassword);
                    userService.add(user);

                    changeSuccess = "Đổi mật khẩu thành công";
                    model.addAttribute("messageSuccess", changeSuccess);

                    session.setMaxInactiveInterval(60 * 30);
                    session.setAttribute("USER", user);

                    return "/user/changePassword";
                }
            }
        }
    }

    // hien thi danh sach dang cho phe duyet
    @GetMapping("/waitingList")
    public String waitingList(@SessionAttribute("USER") User USER, Model model) {
        model.addAttribute("waitingList", recordService.findWaitingList(USER.getId()));
        return "user/waitingList";
    }

    // hien thi sach dang muon
    @GetMapping("/borrowingList")
    public String borrowingList(@SessionAttribute("USER") User USER, Model model) {
        model.addAttribute("borrowingList", recordService.findBorrowingList(USER.getId()));
        return "user/borrowingList";
    }

    // hien thi sach da muon
    @GetMapping("/borrowedList")
    public String borrowedList(@SessionAttribute("USER") User USER, Model model) {
        model.addAttribute("borrowedList", recordService.findBorrowedList(USER.getId()));
        return "user/borrowedList";
    }

    // hien thi trang chu nguoi dung
    @GetMapping("/mainPage")
    public String mainPage(Model model) {
        model.addAttribute("listBooks", bookService.findListOfBookForMainPage());
        model.addAttribute("newBooks", bookService.findListOfNewBook());
        model.addAttribute("mostBorrowedBooks", bookService.findListOfMostBorrowedBook());
        return "user/mainPage";
    }

    // hien thi trang tim sach
    @GetMapping("/findBook")
    public String findBook(Model model) {
        Sort sorter = Sort.by("title").ascending();
        model.addAttribute("listBooks", bookService.findListOfBook(sorter));
        return "user/findBook";
    }

    // nhan ket qua tu trang tim sach -> hien thi cac sach tim duoc theo tu khoa
    @PostMapping("/findBook")
    public String listOfBook(@RequestParam("pattern") String pattern, Model model) {
        model.addAttribute("listBooks", bookService.findBookByTitleContaining(pattern));
        return "user/findBook";
    }

    // hien thi trang thong tin chi tiet sach
    @PostMapping("/findBook/display")
    public String displayBook(@RequestParam("bookId") Long bookId, Model model) {
        Book book = bookService.findBookById(bookId);
        model.addAttribute("book", book);

        List<Book> similarBooks = bookService.findBookByGenre(book.getGenre());
        similarBooks.remove(book);
        model.addAttribute("similarBooks", similarBooks);
        return "user/displayBook";
    }

    // xu ly khi nguoi dung tra sach
    @PostMapping("/returnBook")
    public String returnBook(@RequestParam("recordId") Long recordId) {
        Record record = recordService.findRecordById(recordId);
        record.setStatus("Yêu cầu trả");
        Date today = new Date();
        record.setReturnDate(today);
        recordService.updateRecordByObject(record);
        return "redirect:/user/waitingList";
    }

    // xu ly khi nguoi dung muonsach
    @PostMapping("/borrowBook")
    public String borrowBook(@SessionAttribute("USER") User user, @RequestParam("bookId") Long bookId, Model model) {
        List<Record> records = recordService.findRecordByBookAndUser(bookId, user.getId());
        boolean checkDuplicateRequest = false;
        // kiem tra xem nguoi dung da muon hoac yeu cau sach nay hay chua
        for (Record record : records) {
            if (record.getStatus().equals("Đang mượn") || record.getStatus().equals("Yêu cầu mượn") || record.getStatus().equals("Yêu cầu trả") || record.getStatus().equals("Quá hạn")) {
                checkDuplicateRequest = true;
            }
        }

        // kiem tra xem con sach de muon khong
        Book book = bookService.findBookById(bookId);
        boolean checkAvailableBook = bookService.checkIfBookAvailable(bookId);
        if (!checkDuplicateRequest && checkAvailableBook) {
            Record record = new Record();
            record.setBook(book);
            record.setUser(user);
            record.setIssueDate(new Date());
            record.setStatus("Yêu cầu mượn");

            recordService.add(record);
            return "redirect:/user/waitingList";
        } else if (checkDuplicateRequest) {
            model.addAttribute("book", bookService.findBookById(bookId));
            model.addAttribute("error", "Bạn đã yêu cầu hoặc mượn sách này rồi");

            List<Book> similarBooks = bookService.findBookByGenre(book.getGenre());
            similarBooks.remove(book);
            model.addAttribute("similarBooks", similarBooks);
            return "user/displayBook";
        } else {
            model.addAttribute("book", bookService.findBookById(bookId));
            model.addAttribute("error", "Sách đã hết");

            List<Book> similarBooks = bookService.findBookByGenre(book.getGenre());
            similarBooks.remove(book);
            model.addAttribute("similarBooks", similarBooks);
            return "user/displayBook";
        }
    }
}
