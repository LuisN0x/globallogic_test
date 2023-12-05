package com.desafiobci.userapi.controllers;

import com.desafiobci.userapi.exceptions.ServiceCallException;
import com.desafiobci.userapi.models.ResponseHandler;
import com.desafiobci.userapi.models.UsuarioLoginModel;
import com.desafiobci.userapi.models.UsuarioModel;
import com.desafiobci.userapi.models.UsuarioSignUpModel;
import com.desafiobci.userapi.repositories.UsuarioRepository;
import com.desafiobci.userapi.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsuarioController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    UsuarioService usuarioService;

    @RequestMapping(value = "/all")
    public ResponseEntity<?> listarUsuarios() {
        return ResponseEntity.ok().
                body(usuarioRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> signUp(@RequestBody UsuarioSignUpModel usuarioSignUpModel) {
        try {
            UsuarioModel usuarioModel = usuarioService.signUp(usuarioSignUpModel);

            //Respuesta custom
            Map<String, Object> response = new HashMap<>();
            response.put("usuario",usuarioModel);
            response.put("id",usuarioModel.getId());
            response.put("created",usuarioModel.getCreated());
            response.put("lastLogin",usuarioModel.getLastLogin());
            response.put("token",usuarioModel.getToken());
            response.put("isActive",usuarioModel.getIsActive());

            return ResponseHandler.generateResponse("Usuario creado con éxito",
                    HttpStatus.OK,"usuario",response);

        } catch (ServiceCallException e) {
            //Respuesta de error
            Map<String, Object> error = new HashMap<>();
            error.put("timestamp", new Date());
            error.put("codigo",HttpStatus.MULTI_STATUS.value());
            error.put("detail",e.getMessage());
            return new ResponseEntity<Object>(error,HttpStatus.MULTI_STATUS);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestHeader("Authorization") String token) {
        try {
            UsuarioModel usuarioModel = usuarioService.getUsuarioModelByToken(token);

            return ResponseEntity.ok(usuarioModel);
        } catch (ServiceCallException e) {
            //Respuesta error
            Map<String, Object> error = new HashMap<>();
            error.put("timestamp", new Date());
            error.put("codigo",HttpStatus.MULTI_STATUS.value());
            error.put("detail",e.getMessage());
            return new ResponseEntity<Object>(error,HttpStatus.MULTI_STATUS);
        }
    }

}
