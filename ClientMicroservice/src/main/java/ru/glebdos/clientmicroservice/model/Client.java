package ru.glebdos.clientmicroservice.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer", schema = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "first_name")
    @NotNull
    @Size(min = 2, max = 50)
    private String firstName;
    @Column(name = "second_name")
    @NotNull
    @Size(min = 2, max = 50)
    private String secondName;
    @Column(name = "email")
    @NotNull
    @Email
    @Size(max = 50)
    private  String email;
    @Column(name = "phone_number")
    @Pattern(regexp = "^\\+7[0-9]{10}$", message = "Неправильный формат телефонного номера. Ожидаемый формат: +79219008833")
    @NotNull
    private String phoneNumber;
    @Column(name = "address")
    @NotNull
    @Size(max = 100)
    private String address;
    @NotNull
    @Column(name = "registration_time")
    private LocalDateTime clientRegistrationTime;


}
