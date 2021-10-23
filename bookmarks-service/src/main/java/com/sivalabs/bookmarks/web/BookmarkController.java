package com.sivalabs.bookmarks.web;

import com.sivalabs.bookmarks.domain.Bookmark;
import com.sivalabs.bookmarks.domain.BookmarkRepository;
import com.sivalabs.bookmarks.domain.UrlMetadata;
import com.sivalabs.bookmarks.domain.UrlMetadataServiceClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bookmarks")
@RequiredArgsConstructor
@Slf4j
public class BookmarkController {

    private final BookmarkRepository bookmarkRepository;
    private final UrlMetadataServiceClient urlMetadataServiceClient;

    @GetMapping
    public List<Bookmark> getAllBookmarks() {
        return bookmarkRepository.findAll(Sort.by(Direction.DESC, "createdAt"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Bookmark createBookmark(@RequestBody @Validated Bookmark bookmark) {
        if(StringUtils.isBlank(bookmark.getTitle())) {
            UrlMetadata urlMetadata = urlMetadataServiceClient.getUrlMetadata(bookmark.getUrl());
            bookmark.setTitle(urlMetadata.getTitle());
        }
        return bookmarkRepository.save(bookmark);
    }

    @PutMapping("/{bookmarkId}/votes/up")
    public ResponseEntity<Bookmark> upVote(@PathVariable Long bookmarkId) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId).orElseThrow();
        bookmark.setUpVotes(bookmark.getUpVotes() + 1);
        bookmarkRepository.save(bookmark);
        return ResponseEntity.ok(bookmark);
    }

    @PutMapping("/{bookmarkId}/votes/down")
    public ResponseEntity<Bookmark> downVote(@PathVariable Long bookmarkId) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId).orElseThrow();
        bookmark.setDownVotes(bookmark.getDownVotes() + 1);
        bookmarkRepository.save(bookmark);
        return ResponseEntity.ok(bookmark);
    }
}
