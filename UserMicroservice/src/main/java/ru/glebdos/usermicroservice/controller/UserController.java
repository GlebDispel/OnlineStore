package ru.glebdos.usermicroservice.controller;


import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.glebdos.usermicroservice.dto.DynamicDto;
import ru.glebdos.usermicroservice.dto.UserDto;
import ru.glebdos.usermicroservice.service.UserService;


@RestController
@RequestMapping("/users")
public class UserController {

   private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }



    @PostMapping(value = "/registration",produces = MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")
    public ResponseEntity<String> createUser(@RequestBody @Valid UserDto userDto) {
        LOGGER.info("Creating user: {}", userDto);
        userService.createUser(userDto);
        return ResponseEntity.ok("Пользователь создан");
    }

    @GetMapping("/search")
    public ResponseEntity<UserDto> getUserByPhoneNumber(@RequestParam String phoneNumber) {

        return ResponseEntity.ok(userService.getUserByPhoneNumber(phoneNumber));

    }


    @PostMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody @Valid DynamicDto updateUserDto,
                                                   @RequestParam String phoneNumber) {
        LOGGER.info("Number here : {}",phoneNumber);

        LOGGER.info("User here : {}", updateUserDto);
        userService.updateUser(updateUserDto, phoneNumber);

        return ResponseEntity.ok("Пользователь обновлен");

    }


    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestParam String phoneNumber) {
        userService.deleteUser(phoneNumber);
        return ResponseEntity.ok("Пользователь удален");
    }


}
