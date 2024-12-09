package ru.glebdos.clientmicroservice.repository;

import ru.glebdos.clientmicroservice.dto.ClientDto;
import ru.glebdos.clientmicroservice.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    Client findByPhoneNumber(String phoneNumber);
    void deleteClientByPhoneNumber(String phoneNumber);
}
