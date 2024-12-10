package ru.glebdos.clientmicroservice.repository;

import ru.glebdos.clientmicroservice.dto.ClientDto;
import ru.glebdos.clientmicroservice.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    Optional<Client> findByPhoneNumber(String phoneNumber);
    void deleteClientByPhoneNumber(String phoneNumber);
}
