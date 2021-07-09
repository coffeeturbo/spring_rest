package com.example.auth.controller;

import com.example.auth.AuthApplication;
import com.example.auth.domain.Person;
import com.example.auth.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = AuthApplication.class)
@AutoConfigureMockMvc
class PersonControllerTest {

    @MockBean
    private PersonService service;

    @Autowired
    private MockMvc mockMvc;


    @Test
    @SneakyThrows
    void whenPostCreateSuccess() {
        var person = Person.builder().id(1).login("test@mail.ru").password("password").build();
        when(service.create(person)).thenReturn(person);
        mockMvc.perform(
                post("/person")
                        .content(asJsonString(person))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.length()", is(3)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.login", is("test@mail.ru")))
                .andExpect(jsonPath("$.password", is("password")))
        ;

        ArgumentCaptor<Person> argument = ArgumentCaptor.forClass(Person.class);
        verify(service).create(argument.capture());
        assertEquals(argument.getValue().getLogin(), person.getLogin());
        assertEquals(argument.getValue().getId(), person.getId());
        assertEquals(argument.getValue().getPassword(), person.getPassword());
    }

    @Test
    @SneakyThrows
    void whenPutUpdateSuccess() {
        var person = Person.builder().id(1).login("updateTest@mail.ru")
                .password("update_password").build();
        mockMvc.perform(
                put("/person")
                        .content(asJsonString(person))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
        ;

        ArgumentCaptor<Person> argument = ArgumentCaptor.forClass(Person.class);
        verify(service).update(argument.capture());
        assertEquals(argument.getValue().getLogin(), person.getLogin());
        assertEquals(argument.getValue().getId(), person.getId());
        assertEquals(argument.getValue().getPassword(), person.getPassword());
    }

    @Test
    public void whenGetByIdThenReturnJson() throws Exception {
        Person person = new Person(1, "Person", "Password");
        when(service.findById(1)).thenReturn(Optional.of(person));
        this.mockMvc.perform(get("/person/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(3)))
                .andExpect(jsonPath("$.id", is(person.getId())))
                .andExpect(jsonPath("$.login", is(person.getLogin())))
                .andExpect(jsonPath("$.password", is(person.getPassword())));
    }

    @Test
    @SneakyThrows
    void whenGetAllThenReturnJsonObjects() {
        List<Person> personList = List.of(
                new Person(1, "Person1", "Password1"),
                new Person(2, "Person2", "Password2"),
                new Person(3, "Person3", "Password3")
        );


        when(service.findAll()).thenReturn(personList);
        this.mockMvc.perform(get("/person")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.length()", is(3)))
                .andExpect(jsonPath("$[0].id", is(personList.get(0).getId())))
                .andExpect(jsonPath("$[0].login", is(personList.get(0).getLogin())))
                .andExpect(jsonPath("$[0].password", is(personList.get(0).getPassword())))
                .andExpect(jsonPath("$[1].id", is(personList.get(1).getId())))
                .andExpect(jsonPath("$[1].login", is(personList.get(1).getLogin())))
                .andExpect(jsonPath("$[1].password", is(personList.get(1).getPassword())))
        ;
    }


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}