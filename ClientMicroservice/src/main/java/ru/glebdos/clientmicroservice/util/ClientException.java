package ru.glebdos.clientmicroservice.util;

public class ClientException extends  RuntimeException{

    public ClientException(String message){
        super(message);
    }
}
