package com.sivalabs.bookmarks.web;

import com.sivalabs.bookmarks.domain.Bookmark;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class BookmarkDTO {
    private Long id;
    private String title;
    private String url;
    private Integer upVotes = 0;
    private Integer downVotes = 0;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static BookmarkDTO toBookmarkDTO(Bookmark bookmark) {
        BookmarkDTO dto = new BookmarkDTO();
        dto.setId(bookmark.getId());
        dto.setTitle(bookmark.getTitle());
        dto.setUrl(bookmark.getUrl());
        dto.setCreatedAt(bookmark.getCreatedAt());
        dto.setUpdatedAt(bookmark.getUpdatedAt());
        dto.setUpVotes(bookmark.getUpVotes());
        dto.setDownVotes(bookmark.getDownVotes());
        return dto;
    }
}
