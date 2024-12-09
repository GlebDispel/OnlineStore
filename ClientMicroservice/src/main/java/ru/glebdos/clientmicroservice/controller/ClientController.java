package ru.glebdos.clientmicroservice.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.glebdos.clientmicroservice.dto.ClientDto;
import ru.glebdos.clientmicroservice.service.ClientService;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/registration")
    public String createClient(@RequestBody ClientDto clientDto) {
        clientService.createClient(clientDto);
        return "Пользователь успешно добавлен в базу";
    }

    @GetMapping("/search")
    public ClientDto getClientByPhoneNumber(@RequestBody String phoneNumber) {

        return clientService.getClientByPhoneNumber(phoneNumber);
    }

    @PostMapping("/update/{phoneNumber}")
    public String updateClient(@RequestBody ClientDto clientDto, @PathVariable String phoneNumber) {
        LOGGER.info("Number here : {}",phoneNumber);
        LOGGER.info("Client here : {}",clientDto);
        clientService.updateClient(clientDto, phoneNumber);

        return "Пользователь успешно обновлен";
    }

    @Transactional
    @PostMapping("/delete")
    public String deleteClient(@RequestBody String phoneNumber) {
        clientService.deleteClient(phoneNumber);
        return "Клиент успешно удален";
    }
}
