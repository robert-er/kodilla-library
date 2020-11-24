package com.library.controller;

import com.library.dto.UserDto;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    void testAddUser() {
        //Given
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserDto> entity = new HttpEntity<>(new UserDto("Tomas",
                "Surname",
                LocalDateTime.of(1999, 1, 15,2,3, 4))
                , headers);
        //When
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(""),
                HttpMethod.POST, entity, String.class);
        String userId = response.getBody();
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotEquals("0", userId);
        //CleanUp
        removeUser(userId);
    }

    @Test
    void testGetUser() {
        //Given
        String name = "Jason";
        String surname = "Testovsky";
        String userId = createUser(name,surname);
        //When
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("" + userId),
                HttpMethod.GET, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(Objects.requireNonNull(response.getBody()).contains(createUserJsonData(name, surname)));
        //CleanUp
        removeUser(userId);
    }

    @Test
    void testGetAll() {
        //Given
        String user1name = "John";
        String user1surname = "Smith";
        String user2name = "Edvard";
        String user2surname = "Norton";
        String userId1 = createUser(user1name, user1surname);
        String userId2 = createUser(user2name, user2surname);
        //When
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(""),
                HttpMethod.GET, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(Objects.requireNonNull(response.getBody()).contains(createUserJsonData(user1name, user1surname)));
        assertTrue(Objects.requireNonNull(response.getBody()).contains(createUserJsonData(user2name, user2surname)));
        //CleanUp
        removeUser(userId1);
        removeUser(userId2);
    }

    @Test
    void testDeleteUser() {
        //Given
        String userId = createUser("Nicole", "Henderson");
        //When
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("" + userId),
                HttpMethod.DELETE, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testUpdateUser() {
        //Given
        String name = "Mark";
        String surname = "Combo";
        String changedName = "Otto";
        String userId = createUser(name, surname);
        //When
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserDto> entity = new HttpEntity<>(new UserDto(changedName,
                surname,
                LocalDateTime.of(2020, 1, 1,0,0, 0))
                , headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("" + userId),
                HttpMethod.PUT, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(Objects.requireNonNull(response.getBody()).contains(createUserJsonData(changedName, surname)));
        //CleanUp
        removeUser(userId);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + "/library/user/" + uri;
    }

    private String createUserJsonData(String name, String surname) {
        return "\"name\":\"" + name + "\",\"surname\":\"" + surname + "\",\"signUpDate\":\"";
    }

    private String createUser(String name, String surname) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserDto> entity = new HttpEntity<>(new UserDto(name,
                surname,
                LocalDateTime.of(2020, 1, 1,0,0, 0))
                , headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(""),
                HttpMethod.POST, entity, String.class);
        System.out.println("user created: " + name + " " + surname + ", id: " + response.getBody());
        return response.getBody();
    }

    private void removeUser(String id) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserDto> entity = new HttpEntity<>(null, headers);
        restTemplate.exchange(createURLWithPort("" + id),
                HttpMethod.DELETE, entity, String.class);
    }
}