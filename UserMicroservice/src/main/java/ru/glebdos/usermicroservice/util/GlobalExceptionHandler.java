package ru.glebdos.usermicroservice.util;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ClientErrorResponse> handleGenericException(Exception e) {
        LOGGER.error("Unexpected error: {}", e.getMessage(), e);
        ClientErrorResponse response = new ClientErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //ловит исключения, связанные с нарушением валидации полей
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ClientErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        LOGGER.error("MethodArgumentNotValidException here : {}", e.getMessage());
        StringBuilder errorMessage = new StringBuilder();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            errorMessage.append(fieldError.getField())
                    .append(" - ")
                    .append(fieldError.getDefaultMessage());
        }
        ClientErrorResponse response = new ClientErrorResponse(
                errorMessage.toString(),
                System.currentTimeMillis()
        );
        LOGGER.error(response.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //Ловит ошибки, возникающие при неверном формате тела запроса, например, синтаксические ошибки JSON.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ClientErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        LOGGER.error("HttpMessageNotReadableException: {}", e.getMessage());
        ClientErrorResponse response = new ClientErrorResponse(
                "Ошибка в запросе",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Ловит ошибки, связанные с отсутствием объекта в базе данных
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ClientErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        LOGGER.error("Entity number: {}", e.getMessage());

        ClientErrorResponse patternResponse = new ClientErrorResponse(

                "Пользователь c номером " + e.getMessage() + " не найден",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(patternResponse, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ClientErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        LOGGER.error("IllegalArgumentException: {}", e.getMessage());
        ClientErrorResponse response = new ClientErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ClientErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        LOGGER.error("MissingServletRequestParameterException: {}", e.getMessage());
        ClientErrorResponse response = new ClientErrorResponse(
                e.getLocalizedMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ClientErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        LOGGER.error("DataIntegrityViolationException: {}", e.getMessage());

        ClientErrorResponse response = new ClientErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
       if(e.getMessage().contains("customer_phone_number_key")){
        response = new ClientErrorResponse(
                "Пользователь с таким номером телефона уже существует",System.currentTimeMillis());

       }
       if(e.getMessage().contains("customer_email_key")){
         response = new ClientErrorResponse(
                 "Пользователь с таким электронным адресом уже существует",System.currentTimeMillis());
       }

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }


}


