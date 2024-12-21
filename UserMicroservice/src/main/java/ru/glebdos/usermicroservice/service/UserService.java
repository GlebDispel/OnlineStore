package ru.glebdos.usermicroservice.service;


import ru.glebdos.usermicroservice.dto.DynamicDto;
import ru.glebdos.usermicroservice.dto.UserDto;

public interface UserService {


     void createUser(UserDto userDto);
     UserDto getUserByPhoneNumber(String phoneNumber);

     void updateUser(DynamicDto updateUserDto, String phoneNumber);

     void deleteUser(String userPhoneNumber);
}
