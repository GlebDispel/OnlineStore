package ru.glebdos.usermicroservice.service;


import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.glebdos.usermicroservice.config.SecurityConfig;
import ru.glebdos.usermicroservice.config.UserDetailsImpl;
import ru.glebdos.usermicroservice.dto.DynamicDto;
import ru.glebdos.usermicroservice.dto.UserDto;
import ru.glebdos.usermicroservice.model.User;
import ru.glebdos.usermicroservice.repository.UserRepository;

import java.time.LocalDateTime;


@Service
public class UserServiceImpl implements UserService , UserDetailsService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final SecurityConfig securityConfig = new SecurityConfig();

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public void createUser(UserDto userDto) {

        LOGGER.info("Сервис создания вызван");
        String encodedPassword = securityConfig.passwordEncoder().encode(userDto.getPassword());
        User localUser = convertUserDtoToUser(userDto);

        localUser.setPassword(encodedPassword);


//        localUser.setRole(Role.USER);

        setTimeRegistration(localUser);
        userRepository.save(localUser);

    }

    @Override
    public UserDto getUserByPhoneNumber(String phoneNumber) {

        checkFormatPhoneNumber(phoneNumber);
        User user = findUserOrNotFound(phoneNumber);


        return convertUserToUserDto(user);
    }
    @Transactional
    @Override
    public void updateUser(DynamicDto updateUserDto, String phoneNumber) {
        checkFormatPhoneNumber(phoneNumber);
        User user = findUserOrNotFound(phoneNumber);
      LOGGER.info("founded user: {}", user);
        if (updateUserDto.getFirstName() != null) user.setFirstName(updateUserDto.getFirstName());
        if (updateUserDto.getSecondName() != null) user.setSecondName(updateUserDto.getSecondName());
        if (updateUserDto.getEmail() != null) user.setEmail(updateUserDto.getEmail());
        if (updateUserDto.getAddress() != null) user.setAddress(updateUserDto.getAddress());
        if (updateUserDto.getPhoneNumber() != null) user.setPhoneNumber(updateUserDto.getPhoneNumber());
        LOGGER.info("updated user: {}", user);
      userRepository.save(user);

    }
    @Transactional
    @Override
    public void deleteUser(String phoneNumber) {
        checkFormatPhoneNumber(phoneNumber);
        LOGGER.info("deleting user: {}", phoneNumber);
        userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new EntityNotFoundException(phoneNumber));
         userRepository.deleteUserByPhoneNumber(phoneNumber);
    }


    private User convertUserDtoToUser(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    private UserDto convertUserToUserDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    private void setTimeRegistration(User user) {
        user.setUserRegistrationTime(LocalDateTime.now());
    }

    private void checkFormatPhoneNumber(String phoneNumber) {
        if (!phoneNumber.matches("^\\+7[0-9]{10}$"))
            throw new IllegalArgumentException("Неправильный формат телефонного номера. Ожидаемый формат: +79219008833");
    }
    private User findUserOrNotFound(String phoneNumber) {
      return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new EntityNotFoundException(phoneNumber));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.info("loadUserByUsername вызван, передеанный аргумент  {}", username);
        User user = userRepository.findUserByFirstName(username)
                .orElseThrow(() -> new EntityNotFoundException("not found user: " + username));
        return UserDetailsImpl.build(user);
    }


}
