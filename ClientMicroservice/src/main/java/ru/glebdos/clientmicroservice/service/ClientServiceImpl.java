package ru.glebdos.clientmicroservice.service;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.glebdos.clientmicroservice.dto.ClientDto;
import ru.glebdos.clientmicroservice.dto.PartialUpdateClientDto;
import ru.glebdos.clientmicroservice.model.Client;
import ru.glebdos.clientmicroservice.repository.ClientRepository;

import java.time.LocalDateTime;

@Service
public class ClientServiceImpl implements ClientService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, ModelMapper modelMapper) {
        this.clientRepository = clientRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public void createClient(ClientDto clientDto) {

        Client localClient = convertClientDtoToClient(clientDto);
        setTimeRegistration(localClient);
        clientRepository.save(localClient);

    }

    @Override
    public ClientDto getClientByPhoneNumber(String phoneNumber) {

        Client client = clientRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new EntityNotFoundException("Client not found " + phoneNumber));

        return convertClientToClientDto(client);
    }

    @Override
    public void updateClient(PartialUpdateClientDto updateClientDto, String phoneNumber) {
        if(phoneNumber == null)
            throw new NullPointerException("phoneNumber is null");
      Client client =  clientRepository.findByPhoneNumber(phoneNumber)
              .orElseThrow(() -> new EntityNotFoundException("Client not found " + phoneNumber));
      LOGGER.info("founded client: {}", client);
        if (updateClientDto.getFirstName() != null) client.setFirstName(updateClientDto.getFirstName());
        if (updateClientDto.getSecondName() != null) client.setSecondName(updateClientDto.getSecondName());
        if (updateClientDto.getEmail() != null) client.setEmail(updateClientDto.getEmail());
        if (updateClientDto.getAddress() != null) client.setAddress(updateClientDto.getAddress());
        if (updateClientDto.getPhoneNumber() != null) client.setPhoneNumber(updateClientDto.getPhoneNumber());
        LOGGER.info("updated client: {}", client);
      clientRepository.save(client);

    }

    @Override
    public void deleteClient(String phoneNumber) {
        if(phoneNumber == null)
            throw new NullPointerException("phoneNumber is null");
        LOGGER.info("deleting client: {}", phoneNumber);
        clientRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new EntityNotFoundException("Client not found " + phoneNumber));
         clientRepository.deleteClientByPhoneNumber(phoneNumber);
    }


    private Client convertClientDtoToClient(ClientDto clientDto) {
        return modelMapper.map(clientDto, Client.class);
    }

    private ClientDto convertClientToClientDto(Client client) {
        return modelMapper.map(client, ClientDto.class);
    }

    private void setTimeRegistration(Client client) {
        client.setClientRegistrationTime(LocalDateTime.now());
    }
}
