package com.devfactor.dscatalog.dto;

import com.devfactor.dscatalog.services.validation.UserInsertValid;
import com.devfactor.dscatalog.services.validation.UserUpdateValid;

@UserUpdateValid
public class UserUpdateDTO extends UserDTO{
    private static final long serialVersionUID=1L;
}
