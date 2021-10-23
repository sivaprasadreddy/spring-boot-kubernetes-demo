package com.sivalabs.urlmetadata;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class UrlMetadataServiceApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldFetchMetadataForGivenUrl() throws Exception {
        this.mockMvc
            .perform(get("/api/v1/url-metadata?url=https://sivalabs.in"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title", is("SivaLabs - My Experiments with Technology")));
    }

    @Test
    void shouldReturnUrlAsTitleIfMetadataFetchingFails() throws Exception {
        this.mockMvc
            .perform(get("/api/v1/url-metadata?url=https://nonexisting-uel.com"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title", is("https://nonexisting-uel.com")));
    }
}
