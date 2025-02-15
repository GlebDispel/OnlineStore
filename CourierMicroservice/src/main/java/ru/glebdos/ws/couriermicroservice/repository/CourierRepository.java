package ru.glebdos.ws.couriermicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.glebdos.ws.couriermicroservice.model.Courier;
import ru.glebdos.ws.couriermicroservice.model.CourierStatus;

import java.util.List;

@Repository
public interface CourierRepository extends JpaRepository<Courier,Long> {


    List<Courier> findByStatus(CourierStatus status);
}
