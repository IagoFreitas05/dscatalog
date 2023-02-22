package com.devfactor.dscatalog.services.exceptions;

public class EntityNotFoundException extends RuntimeException{
    private final long serialVersionUID = 1L;

    public EntityNotFoundException(String erroMessage){
        super(erroMessage);
    }
}
