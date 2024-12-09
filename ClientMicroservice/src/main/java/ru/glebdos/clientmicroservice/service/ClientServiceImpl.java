package ru.glebdos.clientmicroservice.service;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.glebdos.clientmicroservice.dto.ClientDto;
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

        Client client = clientRepository.findByPhoneNumber(phoneNumber);

        return convertClientToClientDto(client);
    }

    @Override
    public void updateClient(ClientDto clientDto, String phoneNumber) {
      Client client =  clientRepository.findByPhoneNumber(phoneNumber);
      LOGGER.info("founded client: {}", client);
        if (clientDto.getFirstName() != null) client.setFirstName(clientDto.getFirstName());
        if (clientDto.getSecondName() != null) client.setSecondName(clientDto.getSecondName());
        if (clientDto.getEmail() != null) client.setEmail(clientDto.getEmail());
        if (clientDto.getAddress() != null) client.setAddress(clientDto.getAddress());
        if (clientDto.getPhoneNumber() != null) client.setPhoneNumber(clientDto.getPhoneNumber());
        if (clientDto.getAddress() != null) client.setAddress(clientDto.getAddress());
        LOGGER.info("updated client: {}", client);
      clientRepository.save(client);

    }

    @Override
    public void deleteClient(String phoneNumber) {
        LOGGER.info("deleting client: {}", phoneNumber);
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
