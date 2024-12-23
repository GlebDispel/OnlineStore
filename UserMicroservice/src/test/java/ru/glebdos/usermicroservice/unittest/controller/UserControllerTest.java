package ru.glebdos.usermicroservice.unittest.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.client.HttpClientErrorException;
import ru.glebdos.usermicroservice.controller.UserController;
import ru.glebdos.usermicroservice.dto.UserDto;
import ru.glebdos.usermicroservice.service.UserService;
import ru.glebdos.usermicroservice.util.ClientErrorResponse;
import ru.glebdos.usermicroservice.util.GlobalExceptionHandler;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {


    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;
    @Mock
    GlobalExceptionHandler globalExceptionHandler;

    private ObjectMapper objectMapper;

    private UserDto validUserDto;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
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
    void createUser_shouldReturnBadRequest_WhenInvalidUserDto() {

        UserDto invalidUserDto = new UserDto(
                "", "Doe", "john.doe@example.com", "+79219008833", "123 Main St");

        String jsonContent = objectMapper.writeValueAsString(invalidUserDto);

     MvcResult result = mockMvc.perform(post("/users/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isBadRequest())
          .andReturn();

     String test = result.getResponse().toString();
        System.out.println(test);
// хуйню возвращает , нужно исправить
  verify(userService, never()).createUser(validUserDto);





    }





}
