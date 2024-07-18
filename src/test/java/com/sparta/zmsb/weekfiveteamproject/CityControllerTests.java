package com.sparta.zmsb.weekfiveteamproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.zmsb.weekfiveteamproject.controllers.CityController;
import com.sparta.zmsb.weekfiveteamproject.service.WorldService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = CityController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class CityControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorldService worldService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void cityControllerCreatesCityReturnsCreated() throws Exception {

    }
}
