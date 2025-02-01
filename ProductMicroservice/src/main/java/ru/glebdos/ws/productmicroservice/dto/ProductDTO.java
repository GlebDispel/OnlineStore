package ru.glebdos.ws.productmicroservice.dto;


import lombok.*;


@ToString
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private String name;
    private int quantity;
    private double price;
}
