package ru.glebdos.usermicroservice.unittest.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.glebdos.usermicroservice.controller.UserController;
import ru.glebdos.usermicroservice.dto.UserDto;
import ru.glebdos.usermicroservice.service.UserService;
import ru.glebdos.usermicroservice.util.ClientErrorResponse;
import ru.glebdos.usermicroservice.util.GlobalExceptionHandler;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
public class UserControllerTest {


    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;
    @Mock
    private GlobalExceptionHandler globalExceptionHandler;

    private ObjectMapper objectMapper;

    private UserDto validUserDto;


    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .build();
        objectMapper = new ObjectMapper();
        validUserDto = new UserDto(
                "John", "Doe", "john.doe@example.com", "+79219008833", "123 Main St");
    }

    @Test
    @DisplayName("Успешное создание пользователя")
    @SneakyThrows
    void createUser_ShouldReturnOk_WhenValidUserDto() {
        String jsonContent = objectMapper.writeValueAsString(validUserDto);


        mockMvc.perform(post("/users/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("Пользователь создан"));


        verify(userService, times(1)).createUser(validUserDto);

    }

    @Test
    @DisplayName("Ошибка, не задано имя пользователя")
    @SneakyThrows
    void createUser_shouldReturnBadRequest_WhenFirstNameIsNull() {

        UserDto invalidUserDto = validUserDto;
        invalidUserDto.setFirstName(null);

        String jsonContent = objectMapper.writeValueAsString(invalidUserDto);

        MvcResult result = mockMvc.perform(post("/users/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andReturn();


        Exception exception = result.getResolvedException();
        ;

        assertNotNull(exception);
        assertInstanceOf(MethodArgumentNotValidException.class, exception);
        verify(userService, never()).createUser(validUserDto);

    }

    @Test
    @DisplayName("Ошибка, неверный формат номера телефона")
    @SneakyThrows
    void createUser_shouldReturnBadRequest_whenWrongPhoneNumberFormat() {
        UserDto invalidUserDto = validUserDto;
        invalidUserDto.setPhoneNumber("+09281234");
        String jsonContent = objectMapper.writeValueAsString(invalidUserDto);

        MvcResult result = mockMvc.perform(post("/users/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andReturn();

        Exception exception = result.getResolvedException();

        assertNotNull(exception);
        assertInstanceOf(MethodArgumentNotValidException.class, exception);
        verify(userService, never()).createUser(validUserDto);

    }

    @Test
    @DisplayName("Ошибка, неверный формат почты")
    @SneakyThrows
    void createUser_shouldReturnBadRequest_whenWrongEmailFormat() {
        UserDto invalidUserDto = validUserDto;
        invalidUserDto.setEmail("some email");
        String jsonContent = objectMapper.writeValueAsString(invalidUserDto);

        MvcResult result = mockMvc.perform(post("/users/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andReturn();

        Exception exception = result.getResolvedException();

        assertNotNull(exception);
        assertInstanceOf(MethodArgumentNotValidException.class, exception);
        verify(userService, never()).createUser(validUserDto);

    }

    @Test
    @DisplayName("Успешное получение пользователя")
    @SneakyThrows
    void getUserByPhoneNumber_ShouldReturnOk_WhenValidPhoneNumber() {
        String phoneNumber = validUserDto.getPhoneNumber();

        when(userService.getUserByPhoneNumber(phoneNumber)).thenReturn(validUserDto);

        mockMvc.perform(get("/users/search")
                        .param("phoneNumber",phoneNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value(validUserDto.getFirstName()))
                .andExpect(jsonPath("$.secondName").value(validUserDto.getSecondName()))
                .andExpect(jsonPath("$.phoneNumber").value(validUserDto.getPhoneNumber()))
                .andExpect(jsonPath("$.email").value(validUserDto.getEmail()))
                .andExpect(jsonPath("$.address").value(validUserDto.getAddress()));

        verify(userService,times(1)).getUserByPhoneNumber(phoneNumber);
    }

    @Test
    @DisplayName("Ошибка, неверный формат номера телефона")
    @SneakyThrows
    void getUserByPhoneNumber_shouldReturnBadRequest_whenWrongPhoneNumberFormat() {
        String invalidPhoneNumber = "123456789";
        when(userService.getUserByPhoneNumber(invalidPhoneNumber))
                .thenThrow(new IllegalArgumentException("Неправильный формат телефонного номера. Ожидаемый формат: +79219008833"));

        mockMvc.perform(get("/users/search")
                        .param("phoneNumber", invalidPhoneNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        //возвращает 200 . нужно исправить.

    }


}
