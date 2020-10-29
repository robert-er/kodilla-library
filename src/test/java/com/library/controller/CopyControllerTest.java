package com.library.controller;

import com.library.model.Copy;
import com.library.model.dto.BookDto;
import com.library.model.dto.CopyDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CopyControllerTest {

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    void testAddCopy() {
        //Given
        String bookId = createBook("BookAuthor", "BookTitle", 1111);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<CopyDto> entity = new HttpEntity<>(null, headers);
        //When
        ResponseEntity<String> response = restTemplate
                .exchange(createURLWithPort("copy", "add?bookId=") + bookId,
                HttpMethod.POST, entity, String.class);
        String copyId = response.getBody();
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotEquals("0", copyId);
        //CleanUp
        removeBook(bookId);
    }

    @Test
    void testGetCopy() {
        //Given
        String author = "AuthorGetBookCopy";
        String title = "TitleGetBookCopy";
        int year = 1444;
        String bookId = createBook(author, title, year);
        String copyId = createCopy(bookId);
        //When
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<BookDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate
                .exchange(createURLWithPort("copy", "get?id=") + copyId,
                HttpMethod.GET, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(Objects.requireNonNull(response.getBody()).contains(createCopyJsonData(bookId)));
        //CleanUp
        removeBook(bookId);
    }

    @Test
    void testGetAll() {
        //Given
        String author1 = "Author1GetBook";
        String title1 = "Title1GetBook";
        int year1 = 1444;
        String author2 = "Author2GetBook";
        String title2 = "Title2GetBook";
        int year2 = 1442;
        String book1Id = createBook(author1, title1, year1);
        String book2Id = createBook(author2, title2, year2);
        createCopy(book1Id);
        createCopy(book2Id);
        //When
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<CopyDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("copy", "getAll"),
                HttpMethod.GET, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(Objects.requireNonNull(response.getBody()).contains(createCopyJsonData(book1Id)));
        assertTrue(Objects.requireNonNull(response.getBody()).contains(createCopyJsonData(book2Id)));
        //CleanUp
        removeBook(book1Id);
        removeBook(book2Id);
    }

    @Test
    void testDeleteCopy() {
        //Given
        String author = "AuthorDeleteBook";
        String title = "TitleDeleteBook";
        int year = 1888;
        String bookId = createBook(author, title, year);
        String copyId = createCopy(bookId);
        //When
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<CopyDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate
                .exchange(createURLWithPort("copy", "delete?id=") + copyId,
                HttpMethod.DELETE, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        //CleanUp
        removeBook(bookId);
    }

    @Test
    void testUpdateCopy() {
        //Given
        String author1 = "AuthorUpdateBook1";
        String title1 = "TitleUpdateBook1";
        int year1 = 1010;
        String author2 = "AuthorUpdateBook2";
        String title2 = "TitleUpdateBook2";
        int year2 = 2020;
        String book1Id = createBook(author1, title1, year1);
        String book2Id = createBook(author2, title2, year2);
        String copyId = createCopy(book1Id);
        //When
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<CopyDto> entity = new HttpEntity<>(new CopyDto(Long.valueOf(book2Id), Copy.Status.toRent), headers);
        ResponseEntity<String> response = restTemplate
                .exchange(createURLWithPort("copy", "update?id=") + copyId,
                        HttpMethod.PUT, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(Objects.requireNonNull(response.getBody())
                .contains(createCopyJsonData(book2Id)));
        //CleanUp
        removeBook(book1Id);
        removeBook(book2Id);
    }

    private String createURLWithPort(String controller, String uri) {
        return "http://localhost:" + port + "/library/" + controller + "/" + uri;
    }

    private String createCopyJsonData(String bookId) {
        return "\"bookId\":" + bookId + ",\"status\":\"toRent\"";
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

    private void removeBook(String id) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<BookDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("book", "delete?id=" + id),
                HttpMethod.DELETE, entity, String.class);
    }
}