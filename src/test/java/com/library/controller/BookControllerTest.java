package com.library.controller;

import com.library.dto.BookDto;
import com.library.dto.UserDto;
import com.library.repository.UserRepository;
import com.library.security.jwt.JwtUtils;
import com.library.service.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
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
class BookControllerTest {

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
    void testAddBook() {
        //Given
        String username = "usernameA";
        String password = "passwordA";
        Long userId = createUser(username, password);
        HttpHeaders headers = createHttpHeaders(username, password);
        HttpEntity<BookDto> entity = new HttpEntity<>(new BookDto("Author", "Title", 2020), headers);
        //When
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("book", ""),
                HttpMethod.POST, entity, String.class);
        String bookId = response.getBody();
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotEquals("0", bookId);
        //CleanUp
        removeBook(bookId, username, password);
        userRepository.deleteById(userId);
    }

    @Test
    void testGetBook() {
        //Given
        String username = "usernameB";
        String password = "passwordB";
        Long userId = createUser(username, password);
        String author = "AuthorGetBook";
        String title = "TitleGetBook";
        int year = 1444;
        String bookId = createBook(author, title, year, username, password);
        //When
        HttpHeaders headers = createHttpHeaders(username, password);
        HttpEntity<BookDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("book", "" + bookId),
                HttpMethod.GET, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(Objects.requireNonNull(response.getBody()).contains(createBookJsonData(author, title, year)));
        //CleanUp
        removeBook(bookId, username, password);
        userRepository.deleteById(userId);
    }

    @Test
    void testGetAllBook() {
        //Given
        String username = "usernameC";
        String password = "passwordC";
        Long userId = createUser(username, password);
        String author1 = "GetAllBookAuthor1";
        String title1 = "GetAllBookTitle1";
        int year1 = 1555;
        String author2 = "GetAllBookAuthor2";
        String title2 = "GetAllBookTitle2";
        int year2 = 1666;
        String bookId1 = createBook(author1, title1, year1, username, password);
        String bookId2 = createBook(author2, title2, year2, username, password);

        HttpHeaders headers = createHttpHeaders(username, password);
        HttpEntity<BookDto> entity = new HttpEntity<>(null, headers);
        //When
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("book", ""),
                HttpMethod.GET, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(Objects.requireNonNull(response.getBody()).contains(createBookJsonData(author1, title1, year1)));
        assertTrue(Objects.requireNonNull(response.getBody()).contains(createBookJsonData(author1, title1, year1)));
        //CleanUp
        removeBook(bookId1, username, password);
        removeBook(bookId2, username, password);
        userRepository.deleteById(userId);
    }

    @Test
    void testDeleteBook() {
        //Given
        String username = "usernameD";
        String password = "passwordD";
        Long userId = createUser(username, password);
        String bookId = createBook("AuthorDeleteBook", "TitleDeleteBook", 1777, username, password);
        //When
        HttpHeaders headers = createHttpHeaders(username, password);
        HttpEntity<BookDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("book", "" + bookId),
                HttpMethod.DELETE, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        //CleanUp
        userRepository.deleteById(userId);
    }

    @Test
    void testUpdateBook() {
        //Given
        String username = "usernameE";
        String password = "passwordE";
        Long userId = createUser(username, password);
        String author = "AuthorUpdateBook";
        String title = "TitleUpdateBook";
        int year = 1888;
        String updatedAuthor = "NewAuthor";
        String updatedTitle = "NewTitle";
        int updatedYear = 1999;

        String bookId = createBook(author, title, year, username, password);
        //When
        HttpHeaders headers = createHttpHeaders(username, password);
        HttpEntity<BookDto> entity = new HttpEntity<>(new BookDto(updatedAuthor, updatedTitle, updatedYear), headers);
        ResponseEntity<String> response = restTemplate
                .exchange(createURLWithPort("book", "" + bookId),
                HttpMethod.PUT, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(Objects.requireNonNull(response.getBody())
                .contains(createBookJsonData(updatedAuthor, updatedTitle, updatedYear)));
        //CleanUp
        removeBook(bookId, username, password);
        userRepository.deleteById(userId);
    }

    private String createURLWithPort(String controller, String uri) {
        return "http://localhost:" + port + "/library/" + controller + "/" + uri;
    }

    private String createBookJsonData(String author, String title, int year) {
        return "\"author\":\"" + author + "\",\"title\":\"" + title + "\",\"year\":" + year;
    }

    private String createBook(String author, String title, int year, String username, String password) {
        HttpHeaders headers = createHttpHeaders(username, password);
        HttpEntity<BookDto> entity = new HttpEntity<>(new BookDto(author, title, year), headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("book", ""),
                HttpMethod.POST, entity, String.class);
        System.out.println("book created: " + author + " " + title + ", " + year + ", id: " + response.getBody());
        return response.getBody();
    }

    private void removeBook(String id, String username, String password) {
        HttpHeaders headers = createHttpHeaders(username, password);
        HttpEntity<BookDto> entity = new HttpEntity<>(null, headers);
        restTemplate.exchange(createURLWithPort("book", "" + id),
                HttpMethod.DELETE, entity, String.class);
    }

    private Long createUser(String username, String password) {
        Set<String> roles = new HashSet<>();
        roles.add("admin");
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserDto> entity = new HttpEntity<>(new UserDto(username, username, username,
                username + "@dot.com", roles, password),
                headers);
        restTemplate.exchange(createURLWithPort("user", "auth/signup"),
                HttpMethod.POST, entity, String.class);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(0L))
                .getId();
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