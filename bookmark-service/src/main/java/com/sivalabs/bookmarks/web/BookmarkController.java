package com.sivalabs.bookmarks.web;

import com.sivalabs.bookmarks.domain.Bookmark;
import com.sivalabs.bookmarks.domain.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bookmarks")
@RequiredArgsConstructor
@Slf4j
public class BookmarkController {

    public static final int SIZE = 5;
    private final BookmarkRepository bookmarkRepository;

    @GetMapping
    public BookmarksDTO getBookmarks(@RequestParam(name = "page", defaultValue = "1") Integer page) {
        int pageNo = (page > 0)? page - 1 : 0;
        Sort sort = Sort.by(Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageNo, SIZE, sort);
        return new BookmarksDTO(bookmarkRepository.findAll(pageable));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Bookmark createBookmark(@RequestBody @Validated Bookmark bookmark) {
        bookmark.setId(null);
        return bookmarkRepository.save(bookmark);
    }

    @PutMapping("/{bookmarkId}/votes/up")
    public ResponseEntity<Bookmark> upVote(@PathVariable Long bookmarkId) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId).orElse(null);
        if(bookmark == null) {
            return ResponseEntity.notFound().build();
        }
        bookmark.setUpVotes(bookmark.getUpVotes() + 1);
        bookmarkRepository.save(bookmark);
        return ResponseEntity.ok(bookmark);
    }

    @PutMapping("/{bookmarkId}/votes/down")
    public ResponseEntity<Bookmark> downVote(@PathVariable Long bookmarkId) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId).orElse(null);
        if(bookmark == null) {
            return ResponseEntity.notFound().build();
        }
        bookmark.setDownVotes(bookmark.getDownVotes() - 1);
        bookmarkRepository.save(bookmark);
        return ResponseEntity.ok(bookmark);
    }
}
