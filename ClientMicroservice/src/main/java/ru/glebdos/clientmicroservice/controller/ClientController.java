package ru.glebdos.clientmicroservice.controller;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.glebdos.clientmicroservice.dto.ClientDto;
import ru.glebdos.clientmicroservice.service.ClientService;
import ru.glebdos.clientmicroservice.util.ClientException;

import java.util.Optional;


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
    public ResponseEntity<String> createClient(@RequestBody @Valid ClientDto clientDto) {
        clientService.createClient(clientDto);
        return ResponseEntity.ok("Client created");
    }

    @GetMapping("/search")
    public ResponseEntity<ClientDto> getClientByPhoneNumber(@RequestBody @Valid String phoneNumber) {

        return ResponseEntity.ok(clientService.getClientByPhoneNumber(phoneNumber));

    }

    @PostMapping("/update/{phoneNumber}")
    public ResponseEntity<ClientDto> updateClient(@RequestBody @Valid ClientDto clientDto, @PathVariable @Valid String phoneNumber) {
        LOGGER.info("Number here : {}",phoneNumber);
        LOGGER.info("Client here : {}",clientDto);


        return ResponseEntity.ok(clientService.updateClient(clientDto, phoneNumber));

        // ОСТАНОВИЛСЯ ТУТ. НУЖНО РЕШИТЬ ТЕРКИ ВАЛИДАТОРОВ !!!
    }

    @Transactional
    @PostMapping("/delete")
    public String deleteClient(@RequestBody @Valid String phoneNumber) {
        clientService.deleteClient(phoneNumber);
        return "ResponseEntity.ok()";
    }


}
