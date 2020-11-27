package com.library.controller;

import com.library.dto.UserDto;
import com.library.model.Copy;
import com.library.dto.BookDto;
import com.library.dto.CopyDto;
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
class CopyControllerTest {

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
    void testAddCopy() {
        //Given
        String username = "username2A";
        String password = "password2A";
        Long userId = createUser(username, password);
        String bookId = createBook("BookAuthor", "BookTitle", 1111, username, password);
        HttpHeaders headers = createHttpHeaders(username, password);
        HttpEntity<CopyDto> entity = new HttpEntity<>(null, headers);
        //When
        ResponseEntity<String> response = restTemplate
                .exchange(createURLWithPort("copy", "?bookId=") + bookId,
                HttpMethod.POST, entity, String.class);
        String copyId = response.getBody();
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotEquals("0", copyId);
        //CleanUp
        removeBook(bookId, username, password);
        userRepository.deleteById(userId);
    }

    @Test
    void testGetCopy() {
        //Given
        String username = "username2B";
        String password = "password2B";
        Long userId = createUser(username, password);
        String author = "AuthorGetBookCopy";
        String title = "TitleGetBookCopy";
        int year = 1444;
        String bookId = createBook(author, title, year, username, password);
        String copyId = createCopy(bookId, username, password);
        //When
        HttpHeaders headers = createHttpHeaders(username, password);
        HttpEntity<BookDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate
                .exchange(createURLWithPort("copy", "" + copyId),
                HttpMethod.GET, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(Objects.requireNonNull(response.getBody()).contains(createCopyJsonData(bookId)));
        //CleanUp
        removeBook(bookId, username, password);
        userRepository.deleteById(userId);
    }

    @Test
    void testGetAll() {
        //Given
        String username = "username2C";
        String password = "password2C";
        Long userId = createUser(username, password);
        String author1 = "Author1GetBook";
        String title1 = "Title1GetBook";
        int year1 = 1444;
        String author2 = "Author2GetBook";
        String title2 = "Title2GetBook";
        int year2 = 1442;
        String book1Id = createBook(author1, title1, year1, username, password);
        String book2Id = createBook(author2, title2, year2, username, password);
        createCopy(book1Id, username, password);
        createCopy(book2Id, username, password);
        //When
        HttpHeaders headers = createHttpHeaders(username, password);
        HttpEntity<CopyDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("copy", ""),
                HttpMethod.GET, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(Objects.requireNonNull(response.getBody()).contains(createCopyJsonData(book1Id)));
        assertTrue(Objects.requireNonNull(response.getBody()).contains(createCopyJsonData(book2Id)));
        //CleanUp
        removeBook(book1Id, username, password);
        removeBook(book2Id, username, password);
        userRepository.deleteById(userId);
    }

    @Test
    void testDeleteCopy() {
        //Given
        String username = "username2D";
        String password = "password2D";
        Long userId = createUser(username, password);
        String author = "AuthorDeleteBook";
        String title = "TitleDeleteBook";
        int year = 1888;
        String bookId = createBook(author, title, year, username, password);
        String copyId = createCopy(bookId, username, password);
        //When
        HttpHeaders headers = createHttpHeaders(username, password);
        HttpEntity<CopyDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate
                .exchange(createURLWithPort("copy", "" + copyId),
                HttpMethod.DELETE, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        //CleanUp
        removeBook(bookId, username, password);
        userRepository.deleteById(userId);
    }

    @Test
    void testUpdateCopy() {
        //Given
        String username = "username2E";
        String password = "password2E";
        Long userId = createUser(username, password);
        String author1 = "AuthorUpdateBook1";
        String title1 = "TitleUpdateBook1";
        int year1 = 1010;
        String author2 = "AuthorUpdateBook2";
        String title2 = "TitleUpdateBook2";
        int year2 = 2020;
        String book1Id = createBook(author1, title1, year1, username, password);
        String book2Id = createBook(author2, title2, year2, username, password);
        String copyId = createCopy(book1Id, username, password);
        //When
        HttpHeaders headers = createHttpHeaders(username, password);
        HttpEntity<CopyDto> entity = new HttpEntity<>(new CopyDto(Long.valueOf(book2Id), Copy.Status.toRent), headers);
        ResponseEntity<String> response = restTemplate
                .exchange(createURLWithPort("copy", "" + copyId),
                        HttpMethod.PUT, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(Objects.requireNonNull(response.getBody())
                .contains(createCopyJsonData(book2Id)));
        //CleanUp
        removeBook(book1Id, username, password);
        removeBook(book2Id, username, password);
        userRepository.deleteById(userId);
    }

    private String createURLWithPort(String controller, String uri) {
        return "http://localhost:" + port + "/library/" + controller + "/" + uri;
    }

    private String createCopyJsonData(String bookId) {
        return "\"bookId\":" + bookId + ",\"status\":\"toRent\"";
    }

    private String createCopy(String bookId, String username, String password) {
        HttpHeaders headers = createHttpHeaders(username, password);
        HttpEntity<CopyDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate
                .exchange(createURLWithPort("copy", "?bookId=" + bookId),
                        HttpMethod.POST, entity, String.class);
        return response.getBody();
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
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("book", "" + id),
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