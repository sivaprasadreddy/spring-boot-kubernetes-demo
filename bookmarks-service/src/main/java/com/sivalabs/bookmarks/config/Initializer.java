package com.sivalabs.bookmarks.config;

import com.sivalabs.bookmarks.domain.Bookmark;
import com.sivalabs.bookmarks.domain.BookmarkRepository;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class Initializer implements CommandLineRunner {

    private final BookmarkRepository bookmarkRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Running Initializer.....");
        this.importBookmarks("/data/bookmarks.csv");
    }

    public void importBookmarks(String fileName) throws Exception {
        log.info("Importing bookmarks from file: {}", fileName);

        try (InputStream stream = new ClassPathResource(fileName).getInputStream()) {
            final List<String> lines = readFromInputStream(stream);
            List<Bookmark> bookmarks = new ArrayList<>(lines.size());
            for (String line : lines) {
                String[] tokens = line.split("\\|");
                Bookmark bookmark =
                        new Bookmark(null, tokens[1], tokens[0], LocalDateTime.now(), null);
                bookmarks.add(bookmark);
            }
            bookmarkRepository.saveAll(bookmarks);
            log.info("Imported {} bookmarks from file {}", lines.size(), fileName);
        }
    }

    private List<String> readFromInputStream(InputStream inputStream) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }
}
