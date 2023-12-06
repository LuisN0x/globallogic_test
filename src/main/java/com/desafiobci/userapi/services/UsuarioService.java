package com.desafiobci.userapi.services;

import com.desafiobci.userapi.models.UsuarioModel;
import com.desafiobci.userapi.models.UsuarioSignUpModel;

public interface UsuarioService {

    UsuarioModel signUp(UsuarioSignUpModel usuarioSignUpModel);

    UsuarioModel getUsuarioModelByToken(String token);
}
