package ru.glebdos.usermicroservice.service;


import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import ru.glebdos.usermicroservice.dto.DynamicDto;
import ru.glebdos.usermicroservice.dto.UserDto;
import ru.glebdos.usermicroservice.model.User;
import ru.glebdos.usermicroservice.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;




    @Test
    void createUserTest_shouldCreateUser_whenUserValid() {

      UserDto userDto = new UserDto(
                "Fred","Pink",
                "fred@mail.ru","+79998887766","street 3");
      User user = new User("Fred","Pink",
              "fred@mail.ru","+79998887766","street 3");

        when(modelMapper.map(userDto, User.class)).thenReturn(user);

        userService.createUser(userDto);

      verify(userRepository, times(1)).save(user);
      assertNotNull(user.getUserRegistrationTime());


    }

   @Test
    void getUserTest_shouldReturnUser_whenUserExists() {
        String phoneNumber = "+79998887766";
       User user = new User("Fred","Pink",
               "fred@mail.ru",phoneNumber,"street 3");
       UserDto userDto = new UserDto(
               "Fred","Pink",
               "fred@mail.ru","+79998887766","street 3");

       when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(user));
       when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

       UserDto returnedUserDto = userService.getUserByPhoneNumber(phoneNumber);

        verify(userRepository, times(1)).findByPhoneNumber(phoneNumber);
        assertEquals(user.getFirstName(), returnedUserDto.getFirstName());
        assertEquals(user.getSecondName(), returnedUserDto.getSecondName());
        assertEquals(user.getPhoneNumber(), returnedUserDto.getPhoneNumber());
        assertEquals(user.getEmail(), returnedUserDto.getEmail());
        assertEquals(user.getAddress(), returnedUserDto.getAddress());


   }

   @Test
    void getUserTest_shouldThrowEntityNotFoundException_whenUserDoesNotExist() {
        String phoneNumber = "+79998887766";

        when(userRepository.findByPhoneNumber(phoneNumber)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class , () -> userService.getUserByPhoneNumber(phoneNumber));
    }

    @Test
    void getUserTest_shouldThrowIllegalArgumentException_whenInvalidPhoneNumber() {
        String invalidPhoneNumber = "+7986";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class , () -> userService
                .getUserByPhoneNumber(invalidPhoneNumber));

        assertEquals("Неправильный формат телефонного номера. Ожидаемый формат: +79219008833", exception.getMessage());

    }

    @Test
    void updateUserTest_shouldUpdatePartOfUser_whenUserExists() {
        DynamicDto newDynamicDto = new DynamicDto();
        newDynamicDto.setFirstName("Fred");
        String phoneNumber = "+79998887766";
        User user = new User("Frenk","Pink",
                "fred@mail.ru",phoneNumber,"street 3");

        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(user));


        userService.updateUser(newDynamicDto, phoneNumber);

        verify(userRepository, times(1)).save(user);
        assertEquals(newDynamicDto.getFirstName(), user.getFirstName());


    }

    @Test
    void updateUserTest_shouldUpdateFullUser_whenUserExists() {
        DynamicDto newDynamicDto = new DynamicDto("Fred","Yellow",
                "frenk@gmail.com","+72287771488","street 4");
        String phoneNumber = "+79998887766";
        User user = new User("Frenk","Pink",
                "fred@mail.ru",phoneNumber,"street 3");

        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(user));


        userService.updateUser(newDynamicDto, phoneNumber);

        verify(userRepository, times(1)).save(user);
        assertEquals(newDynamicDto.getFirstName(), user.getFirstName());
        assertEquals(newDynamicDto.getSecondName(), user.getSecondName());
        assertEquals(newDynamicDto.getPhoneNumber(), user.getPhoneNumber());
        assertEquals(newDynamicDto.getEmail(), user.getEmail());
        assertEquals(newDynamicDto.getAddress(), user.getAddress());


    }







}
