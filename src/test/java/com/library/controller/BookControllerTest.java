package com.library.controller;

import com.library.dto.BookDto;
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
class BookControllerTest {

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    void testAddBook() {
        //Given
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<BookDto> entity = new HttpEntity<>(new BookDto("Author", "Title", 2020), headers);
        //When
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(""),
                HttpMethod.POST, entity, String.class);
        String bookId = response.getBody();
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotEquals("0", bookId);
        //CleanUp
        removeBook(bookId);
    }

    @Test
    void testGetBook() {
        //Given
        String author = "AuthorGetBook";
        String title = "TitleGetBook";
        int year = 1444;
        String bookId = createBook(author, title, year);
        //When
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<BookDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("" + bookId),
                HttpMethod.GET, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(Objects.requireNonNull(response.getBody()).contains(createBookJsonData(author, title, year)));
        //CleanUp
        removeBook(bookId);
    }

    @Test
    void testGetAllBook() {
        //Given
        String author1 = "GetAllBookAuthor1";
        String title1 = "GetAllBookTitle1";
        int year1 = 1555;
        String author2 = "GetAllBookAuthor2";
        String title2 = "GetAllBookTitle2";
        int year2 = 1666;
        String bookId1 = createBook(author1, title1, year1);
        String bookId2 = createBook(author2, title2, year2);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<BookDto> entity = new HttpEntity<>(null, headers);
        //When
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(""),
                HttpMethod.GET, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(Objects.requireNonNull(response.getBody()).contains(createBookJsonData(author1, title1, year1)));
        assertTrue(Objects.requireNonNull(response.getBody()).contains(createBookJsonData(author1, title1, year1)));
        //CleanUp
        removeBook(bookId1);
        removeBook(bookId2);
    }

    @Test
    void testDeleteBook() {
        //Given
        String bookId = createBook("AuthorDeleteBook", "TitleDeleteBook", 1777);
        //When
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<BookDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("" + bookId),
                HttpMethod.DELETE, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testUpdateBook() {
        //Given
        String author = "AuthorUpdateBook";
        String title = "TitleUpdateBook";
        int year = 1888;
        String updatedAuthor = "NewAuthor";
        String updatedTitle = "NewTitle";
        int updatedYear = 1999;

        String bookId = createBook(author, title, year);
        //When
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<BookDto> entity = new HttpEntity<>(new BookDto(updatedAuthor, updatedTitle, updatedYear), headers);
        ResponseEntity<String> response = restTemplate
                .exchange(createURLWithPort("" + bookId),
                HttpMethod.PUT, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(Objects.requireNonNull(response.getBody())
                .contains(createBookJsonData(updatedAuthor, updatedTitle, updatedYear)));
        //CleanUp
        removeBook(bookId);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + "/library/book/" + uri;
    }

    private String createBookJsonData(String author, String title, int year) {
        return "\"author\":\"" + author + "\",\"title\":\"" + title + "\",\"year\":" + year;
    }

    private String createBook(String author, String title, int year) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<BookDto> entity = new HttpEntity<>(new BookDto(author, title, year), headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(""),
                HttpMethod.POST, entity, String.class);
        System.out.println("book created: " + author + " " + title + ", " + year + ", id: " + response.getBody());
        return response.getBody();
    }

    private void removeBook(String id) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<BookDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("" + id),
                HttpMethod.DELETE, entity, String.class);
    }
}