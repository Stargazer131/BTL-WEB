package com.example.library;

import com.example.library.model.Genre;
import com.example.library.repository.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LibraryApplicationTests {
        @Autowired
        GenreRepository genreRepository;
    
	@Test
	void contextLoads() {
            String s = "Lịch sử\n" +
"Kinh dị\n" +
"Giật gân\n" +
"Phiêu lưu\n" +
"Hành động\n" +
"Huyền ảo\n" +
"Tình cảm\n" +
"Tài chính\n" +
"Khoa học viễn tưởng\n" +
"Văn học \n" +
"Trinh thám\n" +
"Hài hước\n" +
"Triết học\n" +
"Tâm lý học\n" +
"Sức khỏe\n" +
"Thể thao\n" +
"Công nghệ\n" +
"Chính trị\n" +
"Thiếu nhi\n" +
"Truyện tranh\n" +
"Tôn giáo\n" +
"Hồi ký\n" +
"Sách hướng dẫn\n" +
"Nghệ thuật\n" +
"Lịch sử\n" +
"Địa lý\n" +
"Nấu ăn\n" +
"Văn hóa\n" +
"Thiên nhiên\n" +
"Vũ trụ\n" +
"Self-help\n" +
"Giáo dục";
            String[] data = s.split("\\n");
            for(String d : data) {
                Genre genre = new Genre();
                genre.setName(d);
                genreRepository.save(genre);
            }
	}

}
