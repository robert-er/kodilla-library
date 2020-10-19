package com.library.controller;

import com.library.model.dto.UserDto;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();
    ResponseEntity<String> response;
    HttpEntity<UserDto> entity;

    @Test
    void test_A_getUser() {
        //Given
        createUser("Tom", "Jerry");
        entity = new HttpEntity<>(null, headers);
        //When
        response = restTemplate.exchange(createURLWithPort("/library/user/get?id=1"),
                HttpMethod.GET, entity, String.class);
        //Then
        assertTrue(Objects.requireNonNull(response.getBody()).contains(createUserJsonData()));
        //CleanUp
        removeUser(1);
    }

    @Test
    void test_B_addUser() {
        //Given + When
        createUser("New", "User");
        //Then
        assertEquals(200, response.getStatusCodeValue());
        //CleanUp
        removeUser(2);
    }

    @Test
    void test_C_getAll() {
        //Given
        createUser("Use", "Me");
        createUser("Another", "User");
        //When
        entity = new HttpEntity<>(null, headers);
        response = restTemplate.exchange(createURLWithPort("/library/user/getAll"),
                HttpMethod.GET, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        //CleanUp
        removeUser(3);
        removeUser(4);
    }

    @Test
    void test_D_deleteUser() {
        //Given
        createUser("User", "ToDelete");
        //When
        entity = new HttpEntity<>(null, headers);
        //When
        response = restTemplate.exchange(createURLWithPort("/library/user/get?id=5"),
                HttpMethod.GET, entity, String.class);
        System.out.println("test_delete_user response: " + response.getStatusCode());
        removeUser(5);
        //Then
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void test_E_updateUser() {
        //Given
        createUser("Mood", "ToChange");
        //When
        entity = new HttpEntity<>(new UserDto("Tom",
                "Jerry",
                LocalDateTime.of(2020, 1, 1,0,0, 0))
                , headers);
        response = restTemplate.exchange(createURLWithPort("/library/user/update?id=6"),
                HttpMethod.PUT, entity, String.class);
        System.out.println("response " + response);
        //Then
        assertTrue(Objects.requireNonNull(response.getBody()).contains(createUserJsonData()));
        //CleanUp
        removeUser(6);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    private String createUserJsonData() {
        return "{\"name\":\"Tom\",\"surname\":\"Jerry\",\"signUpDate\":\"";
    }


    private void createUser(String name, String surname) {
         entity = new HttpEntity<>(new UserDto(name,
                surname,
                LocalDateTime.of(2020, 1, 1,0,0, 0))
                , headers);
        response = restTemplate.exchange(createURLWithPort("/library/user/add"),
                HttpMethod.POST, entity, String.class);
        System.out.println("user created: " + name + " " + surname);
    }


    private void removeUser(int id) {
        entity = new HttpEntity<>(null, headers);
        response = restTemplate.exchange(createURLWithPort("/library/user/delete?id=" + id),
                HttpMethod.DELETE, entity, String.class);
    }
}