package com.library.controller;

import com.library.dto.BookDto;
import com.library.dto.CopyDto;
import com.library.dto.RentalDto;
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

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RentalControllerTest {

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
    void testAddRental() {
        //Given
        String username = "username41";
        String password = "password41";
        String userId = createUser(username, password);
        String bookId = createBook("BookAuthorAddRental", "BookTitleAddRental", 1111, username, password);
        String copyId = createCopy(bookId, username, password);
        HttpHeaders headers = createHttpHeaders(username, password);
        HttpEntity<RentalDto> entity = new HttpEntity<>(null, headers);
        //When
        ResponseEntity<String> response = restTemplate
                .exchange(createURLWithPort("rental", "?userId=" + userId + "&copyId=" + copyId),
                        HttpMethod.POST, entity, String.class);
        String rentalId = response.getBody();
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotEquals("0", rentalId);
        //CleanUp
        removeBook(bookId, username, password);
        removeUser(userId, username, password);
    }

    @Test
    void testGetRental() {
        //Given
        String username = "username42";
        String password = "password42";
        String userId = createUser(username, password);
        String bookId = createBook("BookAuthorGetRental", "BookTitleGetRental", 1211, username, password);
        String copyId = createCopy(bookId, username, password);
        String rentalId = createRental(userId, copyId, username, password);
        //When
        HttpHeaders headers = createHttpHeaders(username, password);
        HttpEntity<RentalDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate
                .exchange(createURLWithPort("rental", "" + rentalId),
                        HttpMethod.GET, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(Objects.requireNonNull(response.getBody()).contains(createRentalJsonData(userId, copyId)));
        //CleanUp
        removeBook(bookId, username, password);
        removeUser(userId, username, password);
    }

    @Test
    void testGetAll() {
        //Given
        String username = "username43";
        String password = "password43";
        String userId = createUser(username, password);
        String book1Id = createBook("GetAllRental1Author", "GetAllRental1Title", 1983, username, password);
        String copy1Id = createCopy(book1Id, username, password);
        String rental1Id = createRental(userId, copy1Id, username, password);
        String book2Id = createBook("GetAllRental2Author", "GetAllRental2Title", 1983, username, password);
        String copy2Id = createCopy(book2Id, username, password);
        String username2 = "username53";
        String password2 = "password53";
        String user2Id = createUser(username2, password2);
        String rental2Id = createRental(user2Id, copy2Id, username, password);
        //When
        HttpHeaders headers = createHttpHeaders(username, password);
        HttpEntity<RentalDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("rental", ""),
                HttpMethod.GET, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(Objects.requireNonNull(response.getBody()).contains(createRentalJsonData(userId, copy1Id)));
        assertTrue(Objects.requireNonNull(response.getBody()).contains(createRentalJsonData(user2Id, copy2Id)));
        //CleanUp
        removeBook(book1Id, username, password);
        removeBook(book2Id, username, password);
        removeUser(user2Id, username, password);
        removeUser(userId, username, password);
    }

    @Test
    void testDeleteRental() {
        //Given
        String username = "username44";
        String password = "password44";
        String userId = createUser(username, password);
        String bookId = createBook("BookToDeleteAuthor", "BookToDeleteTitle", 1234, username, password);
        String copyId = createCopy(bookId, username, password);
        String rentalId = createRental(userId, copyId, username, password);
        //When
        HttpHeaders headers = createHttpHeaders(username, password);
        HttpEntity<RentalDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate
                .exchange(createURLWithPort("rental", "" + rentalId),
                        HttpMethod.DELETE, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        //CleanUp
        removeBook(bookId, username, password);
        removeUser(userId, username, password);
    }

    @Test
    void testUpdateRental() {
        //Given
        String username = "username45";
        String password = "password45";
        String userId = createUser(username, password);
        String book1Id = createBook("BookToUpdateAuthor", "BookToUpdateTitle", 2000, username, password);
        String copy1Id = createCopy(book1Id, username, password);
        String rentalId = createRental(userId, copy1Id, username, password);
        String book2Id = createBook("UpdatedAuthor", "UpdatedTitle", 2020, username, password);
        String copy2Id = createCopy(book2Id, username, password);
        //When
        HttpHeaders headers = createHttpHeaders(username, password);
        HttpEntity<RentalDto> entity = new HttpEntity<>(new RentalDto(Long.valueOf(userId),
                Long.valueOf(copy2Id),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(28)),
                headers);
        ResponseEntity<String> response = restTemplate
                .exchange(createURLWithPort("rental", "" + rentalId),
                        HttpMethod.PUT, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(Objects.requireNonNull(response.getBody())
                .contains(createRentalJsonData(userId, copy2Id)));
        //CleanUp
        removeBook(book1Id, username, password);
        removeBook(book2Id, username, password);
        removeUser(userId, username, password);
    }

    private String createURLWithPort(String controller, String uri) {
        return "http://localhost:" + port + "/library/" + controller + "/" + uri;
    }

    private String createRentalJsonData(String userId, String copyId) {
        return "\"userId\":" + userId + ",\"copyId\":" + copyId + "";
    }

    private String createUser(String username, String password) {
        Set<String> roles = new HashSet<>();
        roles.add("admin");
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserDto> entity = new HttpEntity<>(new UserDto(username, username, username,
                username + "@dot.com", roles, password),
                headers);
        restTemplate.exchange(createURLWithPort("user", "auth/signup"),
                HttpMethod.POST, entity, String.class);
        return Long.toString(userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(0L))
                .getId());
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

    private String createRental(String userId, String copyId, String username, String password) {
        HttpHeaders headers = createHttpHeaders(username, password);
        HttpEntity<CopyDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate
                .exchange(createURLWithPort("rental", "?userId=" + userId + "&copyId=" + copyId),
                        HttpMethod.POST, entity, String.class);
        return response.getBody();
    }

    private void removeBook(String id, String username, String password) {
        HttpHeaders headers = createHttpHeaders(username, password);
        HttpEntity<BookDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("book", "" + id),
                HttpMethod.DELETE, entity, String.class);
    }

    private void removeUser(String id, String username, String password) {
        HttpHeaders headers = createHttpHeaders(username, password);
        HttpEntity<BookDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("user", "" + id),
                HttpMethod.DELETE, entity, String.class);
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