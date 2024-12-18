package ru.glebdos.usermicroservice.controller;


import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.glebdos.usermicroservice.dto.PartialUpdateUserDto;
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


    @Transactional
    @PostMapping("/registration")
    public ResponseEntity<String> createUser(@RequestBody @Valid UserDto userDto) {
        userService.createUser(userDto);
        return ResponseEntity.ok("User created");
    }

    @GetMapping("/search")
    public ResponseEntity<UserDto> getUserByPhoneNumber(@RequestBody @Valid String phoneNumber) {

        return ResponseEntity.ok(userService.getUserByPhoneNumber(phoneNumber));

    }

    @Transactional
    @PostMapping("/update/{phoneNumber}")
    public ResponseEntity<String> updateUser(@RequestBody @Valid PartialUpdateUserDto updateUserDto,
                                                  @PathVariable String phoneNumber) {
        LOGGER.info("Number here : {}",phoneNumber);

        LOGGER.info("User here : {}", updateUserDto);
        userService.updateUser(updateUserDto, phoneNumber);

        return ResponseEntity.ok("User updated");

    }

    @Transactional
    @DeleteMapping("/delete{phoneNumber}")
    public ResponseEntity<String> deleteUser(@PathVariable String phoneNumber) {
        userService.deleteUser(phoneNumber);
        return ResponseEntity.ok("User deleted");
    }


}
