package ru.glebdos.usermicroservice.util;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientErrorResponse {

    private String message;
    private Long timestamp;

}
