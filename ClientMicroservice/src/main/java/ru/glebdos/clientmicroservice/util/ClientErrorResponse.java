package ru.glebdos.clientmicroservice.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class ClientErrorResponse {

    private String message;
    private Long timestamp;


}
