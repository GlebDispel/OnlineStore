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
        String phoneNumber = "+799988";

        when(userRepository.findByPhoneNumber(phoneNumber)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class , () -> userService.getUserByPhoneNumber(phoneNumber));
    }

    @Test
    void updateUserTest_shouldUpdateUser_whenUserExists() {}



}
