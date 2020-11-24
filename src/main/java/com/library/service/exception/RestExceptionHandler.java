package com.library.service.exception;

import com.library.service.error.ApiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler({BookExistException.class})
    public ResponseEntity<ApiError> handleBookExistException(BookExistException ex) {
        return handleException(ex);
    }

    @ExceptionHandler({BookNotFoundException.class})
    public ResponseEntity<ApiError> handleBookNotFoundException(BookNotFoundException ex) {
        return handleException(ex);
    }

    @ExceptionHandler({CopyExistException.class})
    public ResponseEntity<ApiError> handleCopyExistException(CopyExistException ex) {
        return handleException(ex);
    }

    @ExceptionHandler({CopyNotFoundException.class})
    public ResponseEntity<ApiError> handleCopyNotFoundException(CopyNotFoundException ex) {
        return handleException(ex);
    }

    @ExceptionHandler({CopyIsBorrowedException.class})
    public ResponseEntity<ApiError> handleCopyIsBorrowedException(CopyIsBorrowedException ex) {
        return handleException(ex);
    }

    @ExceptionHandler({RentalExistException.class})
    public ResponseEntity<ApiError> handleRentalExistException(RentalExistException ex) {
        return handleException(ex);
    }

    @ExceptionHandler({RentalNotFoundException.class})
    public ResponseEntity<ApiError> handleRentalNotFoundException(RentalNotFoundException ex) {
        return handleException(ex);
    }

    @ExceptionHandler({UserExistException.class})
    public ResponseEntity<ApiError> handleUserExistException(UserExistException ex) {
        return handleException(ex);
    }

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException ex) {
        return handleException(ex);
    }

    private ResponseEntity<ApiError> handleException(RuntimeException ex) {
        String message = ex.getMessage();
        logger.error("ErrorMessage: {} , exceptionBody: {}", message, ex);
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message, ex);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
