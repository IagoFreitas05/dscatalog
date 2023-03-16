package com.devfactor.dscatalog.resources.exceptions;

import java.io.Serializable;

public class FieldMessage implements Serializable {
    private static final long  serialVersionUID = 1L;
    private String fieldName;
    private String fieldMessageError;

    public FieldMessage(String fieldName, String fieldMessageError) {
        this.fieldName = fieldName;
        this.fieldMessageError = fieldMessageError;
    }

    public FieldMessage() {
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldMessageError() {
        return fieldMessageError;
    }

    public void setFieldMessageError(String fieldMessageError) {
        this.fieldMessageError = fieldMessageError;
    }
}
