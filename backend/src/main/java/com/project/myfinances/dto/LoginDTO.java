package com.project.myfinances.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginDTO {

    private String email;

    private String senha;

}
