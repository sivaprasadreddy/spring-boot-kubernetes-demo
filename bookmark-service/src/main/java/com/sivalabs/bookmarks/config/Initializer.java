package com.sivalabs.bookmarks.config;

import com.sivalabs.bookmarks.domain.Bookmark;
import com.sivalabs.bookmarks.domain.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class Initializer implements CommandLineRunner {

    private final BookmarkRepository bookmarkRepository;

    @Override
    public void run(String... args) throws IOException {
        log.info("Running Initializer.....");
        if(bookmarkRepository.count() > 0) {
            return;
        }
        List<Bookmark> bookmarks = readBookmarksFromCSV();
        bookmarkRepository.saveAll(bookmarks);
    }

    private List<Bookmark> readBookmarksFromCSV() throws IOException {
        List<Bookmark> bookmarks = new ArrayList<>();
        Resource resource = new ClassPathResource("/data/bookmarks.csv");
        List<String> lines = IOUtils.readLines(resource.getInputStream(), StandardCharsets.UTF_8);
        for (String line : lines) {
            if(line == null || line.trim().isBlank()) {
                continue;
            }
            String[] split = line.split(",");
            Bookmark bookmark = new Bookmark(null, split[1], split[0], 0, 0, LocalDateTime.now(), null);
            bookmarks.add(bookmark);
        }
        return bookmarks;
    }
}
