package com.library.controller;

import com.library.model.Copy;
import com.library.model.dto.BookDto;
import com.library.model.dto.CopyDto;
import com.library.model.dto.RentalDto;
import com.library.model.dto.UserDto;
import org.junit.jupiter.api.Test;
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
class RentalControllerTest {

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    void testAddRental() {
        //Given
        String bookId = createBook("BookAuthorAddRental", "BookTitleAddRental", 1111);
        String copyId = createCopy(bookId);
        String userId = createUser("UserName", "UserSurname");
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<RentalDto> entity = new HttpEntity<>(null, headers);
        //When
        ResponseEntity<String> response = restTemplate
                .exchange(createURLWithPort("rental", "add?userId=") + userId + "&copyId=" + copyId,
                        HttpMethod.POST, entity, String.class);
        String rentalId = response.getBody();
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotEquals("0", rentalId);
        //CleanUp
        removeBook(bookId);
        removeUser(userId);
    }

    @Test
    void testGetRental() {
        //Given
        String bookId = createBook("BookAuthorGetRental", "BookTitleGetRental", 1211);
        String copyId = createCopy(bookId);
        String userId = createUser("UserNameGetRental", "UserSurnameGetRental");
        String rentalId = createRental(userId, copyId);
        //When
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<RentalDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate
                .exchange(createURLWithPort("rental", "get?id=") + rentalId,
                        HttpMethod.GET, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(Objects.requireNonNull(response.getBody()).contains(createRentalJsonData(userId, copyId)));
        //CleanUp
        removeBook(bookId);
        removeUser(userId);
    }

    @Test
    void testGetAll() {
        //Given
        String book1Id = createBook("GetAllRental1Author", "GetAllRental1Title", 1983);
        String copy1Id = createCopy(book1Id);
        String user1Id = createUser("GetAllUser1name", "GetALlUser1surname");
        String rental1Id = createRental(user1Id, copy1Id);
        String book2Id = createBook("GetAllRental2Author", "GetAllRental2Title", 1983);
        String copy2Id = createCopy(book2Id);
        String user2Id = createUser("GetAllUser2name", "GetALlUser2surname");
        String rental2Id = createRental(user2Id, copy2Id);
        //When
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<RentalDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("rental", "getAll"),
                HttpMethod.GET, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(Objects.requireNonNull(response.getBody()).contains(createRentalJsonData(user1Id, copy1Id)));
        assertTrue(Objects.requireNonNull(response.getBody()).contains(createRentalJsonData(user2Id, copy2Id)));
        //CleanUp
        removeBook(book1Id);
        removeUser(user1Id);
        removeBook(book2Id);
        removeUser(user2Id);
    }

    @Test
    void testDeleteRental() {
        //Given
        String bookId = createBook("BookToDeleteAuthor", "BookToDeleteTitle", 1234);
        String copyId = createCopy(bookId);
        String userId = createUser("UserDeleteRentalName", "UserDeleteRentalSurname");
        String rentalId = createRental(userId, copyId);
        //When
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<RentalDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate
                .exchange(createURLWithPort("rental", "delete?id=") + rentalId,
                        HttpMethod.DELETE, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        //CleanUp
        removeBook(bookId);
        removeUser(userId);
    }

    @Test
    void testUpdateRental() {
        //Given
        String book1Id = createBook("BookToUpdateAuthor", "BookToUpdateTitle", 2000);
        String copy1Id = createCopy(book1Id);
        String userId = createUser("UserToUpdateName", "UserToUpdateSurname");
        String rentalId = createRental(userId, copy1Id);
        String book2Id = createBook("UpdatedAuthor", "UpdatedTitle", 2020);
        String copy2Id = createCopy(book2Id);
        //When
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<RentalDto> entity = new HttpEntity<>(new RentalDto(Long.valueOf(userId),
                Long.valueOf(copy2Id),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(28)),
                headers);
        ResponseEntity<String> response = restTemplate
                .exchange(createURLWithPort("rental", "update?id=") + rentalId,
                        HttpMethod.PUT, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(Objects.requireNonNull(response.getBody())
                .contains(createRentalJsonData(userId, copy2Id)));
        //CleanUp
        removeBook(book1Id);
        removeUser(userId);
        removeBook(book2Id);
    }

    private String createURLWithPort(String controller, String uri) {
        return "http://localhost:" + port + "/library/" + controller + "/" + uri;
    }

    private String createRentalJsonData(String userId, String copyId) {
        return "\"userId\":" + userId + ",\"copyId\":" + copyId + "";
    }

    private String createUser(String name, String surname) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserDto> entity = new HttpEntity<>(new UserDto(name,
                surname,
                LocalDateTime.of(2020, 1, 1,0,0, 0))
                , headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("user", "add"),
                HttpMethod.POST, entity, String.class);
        System.out.println("user created: " + name + " " + surname + ", id: " + response.getBody());
        return response.getBody();
    }

    private String createCopy(String bookId) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<CopyDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate
                .exchange(createURLWithPort("copy", "add?bookId=") + bookId,
                        HttpMethod.POST, entity, String.class);
        return response.getBody();
    }

    private String createBook(String author, String title, int year) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<BookDto> entity = new HttpEntity<>(new BookDto(author, title, year), headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("book", "add"),
                HttpMethod.POST, entity, String.class);
        System.out.println("book created: " + author + " " + title + ", " + year + ", id: " + response.getBody());
        return response.getBody();
    }

    private String createRental(String userId, String copyId) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<CopyDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate
                .exchange(createURLWithPort("rental", "add?userId=") + userId + "&copyId=" + copyId,
                        HttpMethod.POST, entity, String.class);
        return response.getBody();
    }

    private void removeBook(String id) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<BookDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("book", "delete?id=" + id),
                HttpMethod.DELETE, entity, String.class);
    }

    private void removeUser(String id) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<BookDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("user", "delete?id=" + id),
                HttpMethod.DELETE, entity, String.class);
    }
}