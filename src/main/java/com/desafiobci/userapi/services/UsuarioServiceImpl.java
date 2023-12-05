package com.desafiobci.userapi.services;

import com.desafiobci.userapi.entities.Telefono;
import com.desafiobci.userapi.entities.Usuario;
import com.desafiobci.userapi.exceptions.*;
import com.desafiobci.userapi.models.TelefonoModel;
import com.desafiobci.userapi.models.UsuarioLoginModel;
import com.desafiobci.userapi.models.UsuarioModel;
import com.desafiobci.userapi.models.UsuarioSignUpModel;
import com.desafiobci.userapi.repositories.TelefonoRepository;
import com.desafiobci.userapi.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    TelefonoRepository telefonoRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProviderService jwtTokenProviderService;

    @Override
    public UsuarioModel signUp(UsuarioSignUpModel usuarioSignUpModel) {
        //Validar expressiones regulares, antes de insertar el usuario
        validateSignUp(usuarioSignUpModel);
        //Validar si ya existe
        if (usuarioRepository.existsByEmail(usuarioSignUpModel.getEmail())) {
            throw new UserAlreadyExistsException("El usuario ya existe con este correo electrónico");
        }

        //Registrar el usuario en BD
        try {
            Usuario usuario = new Usuario();
            usuario.setId(UUID.randomUUID().toString());
            usuario.setName(usuarioSignUpModel.getName());
            usuario.setEmail(usuarioSignUpModel.getEmail());
            usuario.setPassword(passwordEncoder.encode(usuarioSignUpModel.getPassword()));
            usuario.setToken(jwtTokenProviderService.generateToken(usuario.getEmail()));
            usuarioRepository.save(usuario);
            //Logica para guardar los telefonos
            List<Telefono> phones = Optional.ofNullable(usuarioSignUpModel.getPhones())
                    .map(phonesList -> phonesList.stream()
                            .map(telefonoModel -> {
                                Telefono phone = new Telefono();
                                phone.setNumber(telefonoModel.getNumber());
                                phone.setCitycode(telefonoModel.getCitycode());
                                phone.setCountrycode(telefonoModel.getCountrycode());
                                phone.setUsuarioId(usuario.getId());
                                return phone;
                            })
                            .collect(Collectors.toList()))
                    .orElse(null);

            if (phones != null) {
                telefonoRepository.saveAll(phones);
            }
            return UsuarioEntityToModel(usuario,phones);
        } catch (EmailValidationException | PasswordValidationException | UserAlreadyExistsException e) {
            throw new SignUpException(e.getMessage(),e);
        } catch (Exception e) {
            throw new SignUpException("Error al registrar el Usuario", e);
        }
    }

    @Override
    public UsuarioModel getUsuarioModelByToken(String token) {
        //Decode token
        String email = jwtTokenProviderService.decodeTokenToEmail(token);

        // Buscar el usuario por email
        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(email);
        Usuario usuario = optionalUsuario.orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        /*
        // Verificar la contraseña
        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new LoginValidationException("Credenciales inválidas");
        }

        // Verificar el token
        if (!jwtTokenProviderService.validateToken(jwtToken, usuario.getEmail())) {
            throw new LoginValidationException("Token inválido");
        }

        // Verificar si es Activo
        if(!usuario.getIsActive()) {
            throw new LoginValidationException("Usuario no está Activo");
        }
*/
        // Actualizar el token
        usuario.setToken(jwtTokenProviderService.generateToken(usuario.getEmail()));

        // Actualizar en BD el usuario
        usuarioRepository.save(usuario);

        // Retornar UsuarioModel
        return UsuarioEntityToModel(usuario,telefonoRepository.findAllByUsuarioId(usuario.getId()));
    }

    private void validateSignUp(UsuarioSignUpModel usuarioSignUpModel) {
        //Validar email
        validateEmail(usuarioSignUpModel.getEmail());
        //Validar password
        validatePassword(usuarioSignUpModel.getPassword());
    }

    private void validateEmail(String email) {
        if(email != null) {
            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
            Pattern pattern = Pattern.compile(emailRegex);
            Matcher matcher = pattern.matcher(email);
            if (!matcher.matches()) {
                throw new EmailValidationException("Formato de correo electrónico incorrecto");
            }
        } else {
            throw new EmailValidationException("Email no puede ser nulo");
        }
    }

    private void validatePassword(String password) {
        if(password != null) {
            String passwordRegex = "^(?=.*[A-Z]){1}(?=.*\\d.*\\d){0,2}[a-zA-Z\\d]{8,12}$";
            Pattern pattern = Pattern.compile(passwordRegex);
            Matcher matcher = pattern.matcher(password);
            if (!matcher.matches()) {
                throw new PasswordValidationException("Formato de Contraseña incorrecta");
            }
        } else {
            throw new PasswordValidationException("Password no puede ser nulo");
        }
    }

    private UsuarioModel UsuarioEntityToModel(Usuario usuarioEntity, List<Telefono> phonesEntity) {
        List<TelefonoModel> phonesModel = null;
        if(phonesEntity != null) {
            phonesModel = new ArrayList<>();
            for (Telefono phoneEntity : phonesEntity) {
                TelefonoModel phoneModel = new TelefonoModel();
                phoneModel.setNumber(phoneEntity.getNumber());
                phoneModel.setCitycode(phoneEntity.getCitycode());
                phoneModel.setCountrycode(phoneEntity.getCountrycode());
                phonesModel.add(phoneModel);
            }
        }
        return new UsuarioModel(usuarioEntity.getId(),
                usuarioEntity.getCreated(),
                usuarioEntity.getLastLogin(),
                usuarioEntity.getToken(),
                usuarioEntity.getIsActive(),
                usuarioEntity.getName(),
                usuarioEntity.getEmail(),
                usuarioEntity.getPassword(),
                phonesModel
                );
    }
}
