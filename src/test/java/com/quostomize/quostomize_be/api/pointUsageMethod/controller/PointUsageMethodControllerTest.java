package com.quostomize.quostomize_be.api.pointUsageMethod.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.Service.PointUsageMethodService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PointUsageMethodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private PointUsageMethodService usageMethodService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    @DisplayName("")
    public void test1() throws Exception{
        // given

        // expected
    }

    @Test
    @DisplayName("")
    public void test2() throws Exception{
        // given

        // expected
    }
}
