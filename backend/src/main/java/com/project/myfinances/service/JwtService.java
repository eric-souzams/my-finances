package com.project.myfinances.service;

import com.project.myfinances.model.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

public interface JwtService {

    String generateToken(Usuario user);

    Claims getClaims(String token) throws ExpiredJwtException;

    boolean isValidToken(String token);

    String getUserLogin(String token);

}
