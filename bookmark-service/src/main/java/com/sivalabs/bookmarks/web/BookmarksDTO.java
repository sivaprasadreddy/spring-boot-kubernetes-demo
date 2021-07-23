package com.sivalabs.bookmarks.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sivalabs.bookmarks.domain.Bookmark;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookmarksDTO {
    private List<Bookmark> data;
    private long totalElements;
    private int pageNumber;
    private int totalPages;

    @JsonProperty("isFirst")
    private boolean isFirst;

    @JsonProperty("isLast")
    private boolean isLast;

    @JsonProperty("hasNext")
    private boolean hasNext;

    @JsonProperty("hasPrevious")
    private boolean hasPrevious;

    public BookmarksDTO(Page<Bookmark> linksPage) {
        this.setData(linksPage.getContent());
        this.setTotalElements(linksPage.getTotalElements());
        this.setPageNumber(linksPage.getNumber() + 1); // 1 - based page numbering
        this.setTotalPages(linksPage.getTotalPages());
        this.setFirst(linksPage.isFirst());
        this.setLast(linksPage.isLast());
        this.setHasNext(linksPage.hasNext());
        this.setHasPrevious(linksPage.hasPrevious());
    }
}
