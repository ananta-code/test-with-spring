package com.testwithspring.intermediate.web;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.testwithspring.intermediate.config.ExampleApplicationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.context.TestExecutionListeners.MergeMode;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ExampleApplicationContext.class})
@WebAppConfiguration
@TestExecutionListeners(value = { DbUnitTestExecutionListener.class },
        mergeMode = MergeMode.MERGE_WITH_DEFAULTS
)
@DisplayName("Find all messages")
class FindAllMessagesNestedIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void configureSystemUnderSpecification() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    @DisplayName("Should return the HTTP status code OK")
    void shouldReturnHttpStatusCodeOk() throws Exception {
        findAllMessages()
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return the found messages as JSON")
    void shouldReturnFoundMessagesAsJson() throws Exception {
        findAllMessages()
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Nested
    @DisplayName("When no messages is found from the database")
    @ContextConfiguration(classes = {ExampleApplicationContext.class})
    @WebAppConfiguration
    @TestExecutionListeners(value = { DbUnitTestExecutionListener.class },
            mergeMode = MergeMode.MERGE_WITH_DEFAULTS
    )
    @DatabaseSetup("/com/testwithspring/intermediate/no-messages.xml")
    class WhenNoMessagesIsFound {

        @Test
        @DisplayName("Should return an empty list")
        void shouldReturnEmptyList() throws Exception {
            findAllMessages()
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }

    @Nested
    @DisplayName("When one message is found from the database")
    @ContextConfiguration(classes = {ExampleApplicationContext.class})
    @WebAppConfiguration
    @TestExecutionListeners(value = { DbUnitTestExecutionListener.class },
            mergeMode = MergeMode.MERGE_WITH_DEFAULTS
    )
    @DatabaseSetup("/com/testwithspring/intermediate/messages.xml")
    class WhenOneMessageIsFound {

        @Test
        @DisplayName("Should return a list that has one message")
        void shouldReturnEmptyList() throws Exception {
            findAllMessages()
                    .andExpect(jsonPath("$", hasSize(1)));
        }

        @Test
        @DisplayName("Should return the information of the found message")
        void shouldReturnInformationOfFoundMessage() throws Exception {
            findAllMessages()
                    .andExpect(jsonPath("$[0].id", is(1)))
                    .andExpect(jsonPath("$[0].text", is("Hello World!")));
        }
    }

    private ResultActions findAllMessages() throws Exception {
        return mockMvc.perform(get("/api/message"));
    }
}
