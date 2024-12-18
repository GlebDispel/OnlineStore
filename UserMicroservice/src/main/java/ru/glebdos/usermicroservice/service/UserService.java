package ru.glebdos.usermicroservice.service;


import ru.glebdos.usermicroservice.dto.PartialUpdateUserDto;
import ru.glebdos.usermicroservice.dto.UserDto;

public interface UserService {


     void createUser(UserDto userDto);
     UserDto getUserByPhoneNumber(String phoneNumber);

     void updateUser(PartialUpdateUserDto updateUserDto, String phoneNumber);

     void deleteUser(String phoneNumber);
}
