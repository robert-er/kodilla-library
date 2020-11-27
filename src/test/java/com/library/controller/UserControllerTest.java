package com.library.controller;

import com.library.dto.UserDto;
import com.library.repository.UserRepository;
import com.library.security.jwt.JwtUtils;
import com.library.service.exception.UserNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Autowired
    UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    void testAddUser() {
        //Given
        String username = "login";
        String password = "thisistestpassword";
        Set<String> roles = new HashSet<>();
        roles.add("user");
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserDto> entity = new HttpEntity<>(new UserDto(username, "Tomas",
                "Surname", "testmail@dot.com", roles,
                password)
                , headers);
        //When
        ResponseEntity<?> response = restTemplate.exchange(createURLWithPort("auth/signup"),
                HttpMethod.POST, entity, Object.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotEquals("User registered successfully!", response.getBody());
        //CleanUp
        String userId = Long.toString(userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(0L))
                .getId());
        removeUser(userId, username, password);
    }

    @Test
    void testGetUser() {
        //Given
        String username = "loginForGetUser";
        String name = "Jason";
        String surname = "Testovsky";
        String email = "testmail@testgetuser.com";
        String role = "user";
        String password = "testgetuserpassword";
        String userId = createUser(username, name, surname, email, role, password);
        //When
        HttpHeaders headers = createHttpHeaders(username, password);
        HttpEntity<UserDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("" + userId),
                HttpMethod.GET, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(Objects.requireNonNull(response.getBody()).contains(createUserJsonData(name, surname, email)));
        //CleanUp
        removeUser(userId, username, password);
    }

    @Test
    void testGetAll() {
        //Given
        String user1username = "testgetallusername1";
        String user1name = "John";
        String user1surname = "Smith";
        String user1email = "test.get.all@email.com";
        String role1 = "admin";
        String user1password = "testgetalluser1pass";
        String user2username = "testgetAlluser2";
        String user2name = "Edvard";
        String user2surname = "Norton";
        String user2email = "testGetAll@email2.com";
        String role2 = "user";
        String user2password = "testgetalluser2pass";
        String userId1 = createUser(user1username, user1name, user1surname, user1email, role1, user1password);
        String userId2 = createUser(user2username, user2name, user2surname, user2email, role2, user2password);
        //When
        HttpHeaders headers = createHttpHeaders(user1username, user1password);
        HttpEntity<UserDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(""),
                HttpMethod.GET, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(Objects.requireNonNull(response.getBody())
                .contains(createUserJsonData(user1name, user1surname, user1email)));
        assertTrue(Objects.requireNonNull(response.getBody())
                .contains(createUserJsonData(user2name, user2surname, user2email)));
        //CleanUp
        removeUser(userId1, user1username, user1password);
        removeUser(userId2, user2username, user2password);
    }

    @Test
    void testDeleteUser() {
        //Given
        String username = "nicoleLogin";
        String name = "Nicole";
        String surname = "Henderson";
        String email = "testmail@nicole.com";
        String role = "user";
        String password = "nicolePassword";
        String userId = createUser(username, name, surname, email, role, password);
        //When
        HttpHeaders headers = createHttpHeaders(username, password);
        HttpEntity<UserDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("" + userId),
                HttpMethod.DELETE, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testUpdateUser() {
        //Given
        String username = "loginForMark";
        String name = "Mark";
        String surname = "Combo";
        String email = "mark@combo.com";
        String role = "user";
        String password = "markPass";
        String userId = createUser(username, name, surname, email, role, password);
        String changedName = "Otto";
        Set<String> roles = new HashSet<>();
        roles.add("user");
        //When
        HttpHeaders headers = createHttpHeaders(username, password);
        HttpEntity<UserDto> entity = new HttpEntity<>(new UserDto(username, changedName,
                surname, email, roles, password), headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("" + userId),
                HttpMethod.PUT, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(Objects.requireNonNull(response.getBody()).contains(createUserJsonData(changedName, surname, email)));
        //CleanUp
        removeUser(userId, username, password);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + "/library/user/" + uri;
    }

    private String createUserJsonData(String name, String surname, String email) {
        return "\"name\":\"" + name + "\",\"surname\":\"" + surname + "\",\"email\":\"" + email;
    }

    private String createUser(String username, String name, String surname, String email, String role, String password) {
        Set<String> roles = new HashSet<>();
        roles.add(role);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserDto> entity = new HttpEntity<>(new UserDto(username, name, surname, email, roles, password),
                headers);
        restTemplate.exchange(createURLWithPort("auth/signup"),
                HttpMethod.POST, entity, String.class);
        return Long.toString(userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(0L))
                .getId());
    }

    private void removeUser(String id, String username, String password) {
        HttpHeaders headers = createHttpHeaders(username, password);
        HttpEntity<UserDto> entity = new HttpEntity<>(null, headers);
        restTemplate.exchange(createURLWithPort("" + id),
                HttpMethod.DELETE, entity, Object.class);
    }

    private HttpHeaders createHttpHeaders(String username, String password) {
        String encodedAuth = "Bearer  " + generateJwt(username, password);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", encodedAuth);
        return headers;
    }

    private String generateJwt(String username, String password) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtUtils.generateJwtToken(authentication);
    }
}