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
import com.example.library.service.GenreService;
import com.example.library.service.RecordService;
import com.example.library.service.UserService;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

/** 
 *
 * @author Hao
 */
@Controller
@RequestMapping("/admin")
public class AdminController 
{
    @Autowired
    UserService userService;
    
    @Autowired
    BookService bookService;
    
    @Autowired
    RecordService recordService;
    
    @Autowired
    GenreService genreService;
    
    
    // tra ve trang chu cua admin
    @GetMapping("/mainPage")
    public String mainPage(Model model) {
        model.addAttribute("listBooks", bookService.findListOfBookForMainPage());
        model.addAttribute("newBooks", bookService.findListOfNewBook());
        model.addAttribute("mostBorrowedBooks", bookService.findListOfMostBorrowedBook());
        return "admin/mainPage";
    }
    
    // tra ve trang tim sach
    @GetMapping("/findBook")
    public String findBook(Model model) 
    {
        Sort sorter = Sort.by("title").ascending();
        List <Book> listBooks = bookService.findListOfBook(sorter);
        model.addAttribute("listBooks", listBooks);
        return "admin/findBook";
    }
    
    // nhan ket qua tu trang tim sach -> hien thi cac sach tim duoc theo tu khoa
    @PostMapping("/findBook")
    public String findBookResult(@RequestParam("pattern") String pattern, Model model) {
        List <Book> findResultBooks = bookService.findBookByTitleContaining(pattern);
        model.addAttribute("listBooks", findResultBooks);
        return "admin/findBook";
    }
    
    // hien thi trang thong tin chi tiet sach
    @PostMapping("/findBook/display")
    public String displayBook(@RequestParam("bookId") Long bookId, Model model) {
        Book book = bookService.findBookById(bookId);
        model.addAttribute("book", book);
        
        List <Book> similarBooks = bookService.findBookByGenre(book.getGenre());
        similarBooks.remove(book);
        model.addAttribute("similarBooks", similarBooks);
        return "admin/displayBook";
    }
    
    // hien thi trang thong tin cua admin
    @GetMapping("/adminHomePage")
    public String homePage() 
    {
        return "admin/adminHomePage";
    }
    
    //hien thi trang thong tin cua admin sau khi thay doi
    @PostMapping("/adminHomePage")
    public String changeInfoUser(@SessionAttribute("USER") User user, @RequestParam("birthdayParam") String birthdayParam, @RequestParam("nameParam") String nameParam, HttpSession session) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date birthday = sdf.parse(birthdayParam);
            user.setBirthday(birthday);
        } catch (ParseException e) {
        }
        
        user.setName(nameParam);
        userService.update(user);
        session.setMaxInactiveInterval(60 * 30);
        session.setAttribute("USER", user);

        return "/admin/adminHomePage";
    }
    
    // hien thi trang doi mat khau
    @PostMapping("/homepage/changePasswordForm")
    public String changePasswordForm(@RequestParam("userId") Long userId, HttpSession session, Model model) {
        User user =  userService.findById(userId);
        model.addAttribute("user", user);
        return "/admin/changePasswordForm";
    }
    
    // sau khi click vao nut doi mat khau o trang doi mat khau
    @PostMapping("/homepage/changePassword")
    public String changePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, @RequestParam("retypePassword") String retypePassword, HttpSession session, Model model)
    {
        String messageOldPassword = "";
        String messageNewPassword = "";
        String messageRetypePassword = "";
        String changeSuccess="";
        
        User user = (User) session.getAttribute("USER");
        
        if(!user.getPassword().equals(oldPassword))  
        {
            messageOldPassword = "Vui lòng nhập đúng mật khẩu cũ";
            model.addAttribute("messageOldPassword", messageOldPassword);
            model.addAttribute("oldPassword", oldPassword);
            model.addAttribute("newPassword", newPassword);
            model.addAttribute("retypePassword", retypePassword);
            return "/admin/changePassword";
        } 
        else {
            if(newPassword.length() < 1) 
            {
                messageNewPassword = "Mật khẩu ít nhất 1 ký tự";
                model.addAttribute("messageNewPassword", messageNewPassword);
                model.addAttribute("oldPassword", oldPassword);
                model.addAttribute("newPassword", newPassword);
                model.addAttribute("retypePassword", retypePassword);
                return "/admin/changePassword";
             }
            else 
            {
                if(!retypePassword.equals(newPassword)) 
                {
                    messageRetypePassword = "Nhập lại mật khẩu không đúng";
                    model.addAttribute("messageRetypePassword", messageRetypePassword);
                    model.addAttribute("oldPassword", oldPassword);
                    model.addAttribute("newPassword", newPassword);
                    model.addAttribute("retypePassword", retypePassword);
                    return "/admin/changePassword";
                }
                 else 
                {
                    user.setPassword(newPassword);
                    userService.update(user);
                    
                    changeSuccess = "Đổi mật khẩu thành công";
                    model.addAttribute("messageSuccess", changeSuccess);
                    
                    session.setMaxInactiveInterval(60 * 30);
                    session.setAttribute("USER", user);
                    
                    return "/admin/changePassword";
                }
            }
        }
    }
    
    // hien thi trang danh sach nguoi dung
    @GetMapping("/listOfUser")
    public String listOfUser(Model model) 
    {
        List <User> users = userService.findListOfUser();
        model.addAttribute("users", users);
        return "admin/listOfUser";
    }
    
    // hien thi trang lich su cua nguoi dung
    @PostMapping("/listOfUser/history")
    public String historyOfUser(@RequestParam("userId") Long userId, Model model)
    {
        List <Record> records = recordService.findRecordByUser(userId);
        User user = userService.findById(userId);
        model.addAttribute("userName", user.getName());
        model.addAttribute("records", records);
        return "admin/historyOfUser"; 
    } 
    
    // hien thi danh sach cac yeu cau cho phe duyet
    @GetMapping("/approvedList")
    public String approvedList(Model model) 
    {
        List <Record> records = recordService.findApprovedList();
        model.addAttribute("records", records);
        return "admin/approvedList";
    }
    
    // tu choi yeu cau
    @PostMapping("/approvedList/deny")
    public String denyRequest(@RequestParam("recordId") Long recordId) {
        Record record = recordService.findRecordById(recordId);
        
        // kiem tra xem yeu cau nay da duoc admin khac xu ly chua
        if(record != null && record.getStatus().equals("Yêu cầu mượn")) {
            recordService.deleteRecordById(recordId);
        } 
        else if(record != null && record.getStatus().equals("Yêu cầu trả"))
        {
            Date today = new Date();
            record.setReturnDate(null);
            int cmp = today.compareTo(record.getDueDate());
            if(cmp > 0) {
                record.setStatus("Quá hạn");
            } else {
                record.setStatus("Đang mượn");
            }
            recordService.updateRecordByObject(record);
        }
        return "redirect:/admin/approvedList";
    }
    
    // chap nhan yeu cau 
    @PostMapping("/approvedList/accept")
    public String acceptRequest(@RequestParam("recordId") Long recordId) {
        Record record = recordService.findRecordById(recordId);
        
        // kiem tra xem yeu cau nay da duoc admin khac xu ly chua
        if (record != null && record.getStatus().equals("Yêu cầu mượn")) {
            if (bookService.checkIfBookAvailable(record.getBook().getId())) {
                recordService.acceptBorrowingBook(record);
                bookService.decreaseBookQuantity(record.getBook().getId());
            }
        }
        else if(record != null && record.getStatus().equals("Yêu cầu trả"))
        {
            Date today = new Date();
            record.setReturnDate(today);
            int cmp = record.getDueDate().compareTo(record.getReturnDate());
            if(cmp < 0)
            {
                record.setStatus("Trả muộn");
            } else {
                record.setStatus("Trả đúng hạn");
            }
            bookService.increaseBookQuantity(record.getBook().getId());
            recordService.updateRecordByObject(record);
        }
       
        return "redirect:/admin/approvedList";
    }
    
    // hien thi danh sach cac sach de quan ly
    @GetMapping("/listOfBook")
    public String listOfBook(Model model)
    {
        Sort sorter = Sort.by("title").ascending();
        List <Book> books = bookService.findListOfBook(sorter);
        model.addAttribute("books", books);
        return "admin/listOfBook";
    }
    
    // bam vao nut them sach -> hien thi trang cap nhat sach
    @GetMapping("/listOfBook/add")
    public String addBook(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("listOfGenre", genreService.findAllGenres());
        return "admin/updateBook";
    }
    
    // bam vao nut sua sach -> hien thi trang cap nhat sach
    @PostMapping("/listOfBook/update")
    public String updateBook(@RequestParam("bookId") Long bookId, Model model) {
        Book book = bookService.findBookById(bookId);
        model.addAttribute("book", book);
        model.addAttribute("listOfGenre", genreService.findAllGenres());
        return "admin/updateBook";
    }
    
    // xu ly cap nhat sach hoac them sach
    @PostMapping("/listOfBook/save")
    public String saveBook(@ModelAttribute("book") Book book, Model model, @RequestParam("image") MultipartFile file) {
        saveBookImage(file, book.getId());
        bookService.saveBook(book);
        model.addAttribute(book);
        
        List <Book> similarBooks = bookService.findBookByGenre(book.getGenre());
        similarBooks.remove(book);
        model.addAttribute("similarBooks", similarBooks);
        
        return "admin/displayBook";
    }
    
    ///////
    
    // luu anh bia sach vao server
    private void saveBookImage(MultipartFile file, Long bookId) {
        if(!file.isEmpty()) {
            try(InputStream inputStream = file.getInputStream()) {
                String filename = file.getOriginalFilename();
                if(!filename.endsWith("png") && !filename.endsWith("jpg") && !filename.endsWith("jpeg")) {
                    return;
                }
                
                // neu them moi sach
                if(bookId == null) {
                    bookId = bookService.getLastBookId()+1;
                }
                
                String extensionPart = filename.substring(filename.lastIndexOf('.'));
                Path path = Paths.get("src/main/resources/static/images/books/"+bookId+".png");
                
                // get the input stream for the file content
                byte[] bytes = new byte[inputStream.available()];
                int bytesRead = inputStream.read(bytes);

                // load the image
                BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));

                // Check if the file size exceeds 3MB
                while(bytes.length > 3 * 1024 * 1024) {
                    // Calculate the new width and height
                    int newWidth = (int) (image.getWidth() * 0.9);
                    int newHeight = (int) (image.getHeight() * 0.9);

                    // Resize the image
                    BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, image.getType());
                    resizedImage.getGraphics().drawImage(image, 0, 0, newWidth, newHeight, null);

                    // Convert the resizedImage to a byte array
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(resizedImage, "png", baos);
                    bytes = baos.toByteArray();        
                    
                    // Update the image for the next iteration
                    image = resizedImage;
                }

                // Save the resized image to the file system
                Files.write(path, bytes);
            } 
            catch(IOException e) {

            }
        }
    }
}

