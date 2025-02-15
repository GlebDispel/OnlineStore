package ru.glebdos.ws.couriermicroservice.model;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "couriers")
public class Courier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private CourierStatus status;
    private Integer orderCount;
    private Long orderId;

}
