package com.sivalabs.bookmarks;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivalabs.bookmarks.domain.Bookmark;
import com.sivalabs.bookmarks.domain.BookmarkRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {PostgreSQLContainerInitializer.class})
class BookmarkServiceApplicationTests {

    @Autowired private BookmarkRepository bookmarkRepository;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private MockMvc mockMvc;

    private List<Bookmark> bookmarkList;

    @BeforeEach
    void setUp() {
        bookmarkRepository.deleteAll();

        Bookmark bookmark1 =
                new Bookmark(null, "title 1", "http://url-1.com", 0,0,LocalDateTime.now(), null);
        Bookmark bookmark2 =
                new Bookmark(null, "title 2", "http://url-2.com", 0,0, LocalDateTime.now(), null);

        bookmarkList = List.of(bookmark1, bookmark2);
        bookmarkRepository.saveAll(bookmarkList);
    }

    @Test
    void shouldFetchAllBookmarks() throws Exception {
        this.mockMvc
                .perform(get("/api/v1/bookmarks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(bookmarkList.size())));
    }

    @Test
    void shouldCreateNewBookmark() throws Exception {
        Bookmark bookmark =
                new Bookmark(null, "new title", "http://hello.com", 0,0,LocalDateTime.now(), null);
        this.mockMvc
                .perform(
                        post("/api/v1/bookmarks")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(bookmark)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldAllowRequestWithBookmarkTitleIsBlank() throws Exception {
        Bookmark bookmark = new Bookmark(null, "", "http://hello.com", 0,0,LocalDateTime.now(), null);
        this.mockMvc
                .perform(
                        post("/api/v1/bookmarks")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(bookmark)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnBadRequestWhenBookmarkUrlIsBlank() throws Exception {
        Bookmark bookmark = new Bookmark(null, "title", "", 0, 0, LocalDateTime.now(), null);
        this.mockMvc
                .perform(
                        post("/api/v1/bookmarks")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(bookmark)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpVoteBookmark() throws Exception {
        Bookmark bookmark = bookmarkList.get(0);
        this.mockMvc
            .perform(put("/api/v1/bookmarks/{bookmarkId}/votes/up", bookmarkList.get(0).getId()).contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(bookmark.getId().intValue())))
            .andExpect(jsonPath("$.upVotes", is(bookmark.getUpVotes() + 1)))
            .andExpect(jsonPath("$.downVotes", is(bookmark.getDownVotes())));
    }

    @Test
    void shouldDownVoteBookmark() throws Exception {
        Bookmark bookmark = bookmarkList.get(0);
        this.mockMvc
            .perform(put("/api/v1/bookmarks/{bookmarkId}/votes/down", bookmark.getId()).contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(bookmark.getId().intValue())))
            .andExpect(jsonPath("$.upVotes", is(bookmark.getUpVotes())))
            .andExpect(jsonPath("$.downVotes", is(bookmark.getDownVotes()+1)));
    }
}
