package ru.glebdos.clientmicroservice.dto;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartialUpdateClientDto {

    @Size(min = 2, max = 50)
    private String firstName;


    @Size(min = 2, max = 50)
    private String secondName;


    @Email
    @Size(max = 50)
    private String email;

    @Pattern(regexp = "^\\+7[0-9]{10}$", message = "Неправильный формат телефонного номера. Ожидаемый формат: +79219008833")
    private String phoneNumber;

    @Size(max = 100)
    private String address;

    @AssertTrue(message = "At least one field must be provided for update")
    public boolean isValid() {
        return firstName != null || secondName != null || email != null ||
                phoneNumber != null || address != null;
    }
}
