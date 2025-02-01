package ru.glebdos.usermicroservice.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.glebdos.usermicroservice.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByPhoneNumber(String phoneNumber);
    void deleteUserByPhoneNumber(String phoneNumber);
    Optional<User> findUserByFirstName(String name);
}
