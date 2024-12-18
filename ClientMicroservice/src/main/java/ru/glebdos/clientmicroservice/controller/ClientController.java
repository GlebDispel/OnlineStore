package ru.glebdos.clientmicroservice.controller;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.glebdos.clientmicroservice.dto.ClientDto;
import ru.glebdos.clientmicroservice.dto.PartialUpdateClientDto;
import ru.glebdos.clientmicroservice.service.ClientService;





@RestController
@RequestMapping("/clients")
public class ClientController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }


    @Transactional
    @PostMapping("/registration")
    public ResponseEntity<String> createClient(@RequestBody @Valid ClientDto clientDto) {
        clientService.createClient(clientDto);
        return ResponseEntity.ok("Client created");
    }

    @GetMapping("/search")
    public ResponseEntity<ClientDto> getClientByPhoneNumber(@RequestBody @Valid String phoneNumber) {

        return ResponseEntity.ok(clientService.getClientByPhoneNumber(phoneNumber));

    }

    @Transactional
    @PostMapping("/update/{phoneNumber}")
    public ResponseEntity<String> updateClient(@RequestBody @Valid PartialUpdateClientDto updateClientDto,
                                                  @PathVariable String phoneNumber) {
        LOGGER.info("Number here : {}",phoneNumber);
        LOGGER.info("Client here : {}", updateClientDto);
        clientService.updateClient(updateClientDto, phoneNumber);

        return ResponseEntity.ok("Client updated");

    }

    @Transactional
    @DeleteMapping("/delete{phoneNumber}")
    public ResponseEntity<String> deleteClient(@PathVariable String phoneNumber) {
        clientService.deleteClient(phoneNumber);
        return ResponseEntity.ok("Client deleted");
    }


}
