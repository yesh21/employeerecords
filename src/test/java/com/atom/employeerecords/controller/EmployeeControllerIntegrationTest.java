package com.atom.employeerecords.controller;

import com.atom.employeerecords.dto.EmployeeDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crudFlow() throws Exception {
        // create
        EmployeeDto create = new EmployeeDto(null, "John", "Doe", "john.doe@example.com");
        MvcResult r = mvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isCreated())
                .andReturn();

    EmployeeDto created = objectMapper.readValue(r.getResponse().getContentAsString(), EmployeeDto.class);
    Assertions.assertNotNull(created.getId());

    String id = created.getId();

    // get by id
    mvc.perform(get("/api/employees/" + id)).andExpect(status().isOk());

    // update
    created.setFirstName("Jane");
    mvc.perform(put("/api/employees/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(created)))
                .andExpect(status().isOk());

        // delete
        mvc.perform(delete("/api/employees/" + id)).andExpect(status().isNoContent());

        // ensure gone
        mvc.perform(get("/api/employees/" + id)).andExpect(status().isNotFound());
    }
}
