package com.desafiobci.userapi;

import com.desafiobci.userapi.entities.Telefono;
import com.desafiobci.userapi.exceptions.EmailValidationException;
import com.desafiobci.userapi.exceptions.PasswordValidationException;
import com.desafiobci.userapi.exceptions.UserAlreadyExistsException;
import com.desafiobci.userapi.models.TelefonoModel;
import com.desafiobci.userapi.models.UsuarioModel;
import com.desafiobci.userapi.models.UsuarioSignUpModel;
import com.desafiobci.userapi.repositories.TelefonoRepository;
import com.desafiobci.userapi.services.UsuarioService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class UsuarioServiceTest {

    @Autowired
    TelefonoRepository telefonoRepository;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void signUpTest() {
        //Caso correcto con telefonos
        UsuarioSignUpModel usuarioSignUpModel1 = new UsuarioSignUpModel();
        usuarioSignUpModel1.setName("Luis");
        usuarioSignUpModel1.setEmail("luis@nox.cl");
        usuarioSignUpModel1.setPassword("a2asfGfdfdf4");
        usuarioSignUpModel1.setPhones(Arrays.asList(
                new TelefonoModel(12345678, 9, "+56"),
                new TelefonoModel(11122333, 9, "+56")
        ));
        UsuarioModel usuarioModel1 = usuarioService.signUp(usuarioSignUpModel1);
        List<Telefono> telefonos1 = telefonoRepository.findAllByUsuarioId(usuarioModel1.getId());
        Assertions.assertEquals(usuarioSignUpModel1.getEmail(),
                usuarioModel1.getEmail());
        Assertions.assertTrue(passwordEncoder.matches(usuarioSignUpModel1.getPassword(),
                usuarioModel1.getPassword()));
        Assertions.assertTrue(usuarioModel1.getIsActive());
        Assertions.assertTrue(usuarioModel1.getId() != null);
        Assertions.assertTrue(usuarioModel1.getCreated() != null);
        Assertions.assertTrue(usuarioModel1.getLastLogin() == null);
        Assertions.assertTrue(telefonos1.size() == 2);

        //Caso correcto sin telefonos
        UsuarioSignUpModel usuarioSignUpModel2 = new UsuarioSignUpModel();
        usuarioSignUpModel2.setEmail("john@doo.cl");
        usuarioSignUpModel2.setPassword("a2asfGfdfdf4");
        final UsuarioModel usuarioModel2 = usuarioService.signUp(usuarioSignUpModel2);
        List<Telefono> telefonos2 = telefonoRepository.findAllByUsuarioId(
                usuarioModel2.getId());
        Assertions.assertEquals(usuarioSignUpModel2.getEmail(),
                usuarioModel2.getEmail());
        Assertions.assertTrue(passwordEncoder.matches(usuarioSignUpModel2.getPassword(),
                usuarioModel2.getPassword()));
        Assertions.assertTrue(usuarioModel1.getIsActive());
        Assertions.assertTrue(usuarioModel2.getId() != null);
        Assertions.assertTrue(usuarioModel2.getCreated() != null);
        Assertions.assertTrue(usuarioModel2.getLastLogin() == null);
        Assertions.assertTrue(telefonos2.size() <= 0);

        //Caso incorrecto email no válido
        UsuarioSignUpModel usuarioSignUpModel3 = new UsuarioSignUpModel();
        usuarioSignUpModel3.setEmail("martasanchez");
        usuarioSignUpModel3.setPassword("a2asfGfdfdf4");
        EmailValidationException emailValidationException =
                Assertions.assertThrows(EmailValidationException.class, () -> {
                    usuarioService.signUp(usuarioSignUpModel3);
        });
        Assertions.assertEquals("Formato de correo electrónico incorrecto",
                emailValidationException.getMessage());

        //Caso incorrecto contraseña no válida
        UsuarioSignUpModel usuarioSignUpModel4 = new UsuarioSignUpModel();
        usuarioSignUpModel4.setEmail("marta@sanchez.cl");
        usuarioSignUpModel4.setPassword("aaaaa1");
        PasswordValidationException passwordValidationException =
                Assertions.assertThrows(PasswordValidationException.class, () -> {
                    usuarioService.signUp(usuarioSignUpModel4);
                });
        Assertions.assertEquals("Formato de Contraseña incorrecta",
                passwordValidationException.getMessage());

        //Caso incorrecto Usuario ya existe
        UsuarioSignUpModel usuarioSignUpModel5 = new UsuarioSignUpModel();
        usuarioSignUpModel5.setEmail("john@doo.cl");
        usuarioSignUpModel5.setPassword("a2asfGfdfdf4");
        UserAlreadyExistsException userAlreadyExistsException =
                Assertions.assertThrows(UserAlreadyExistsException.class, () -> {
                    usuarioService.signUp(usuarioSignUpModel5);
                });
        Assertions.assertEquals("El usuario ya existe con este correo electrónico",
                userAlreadyExistsException.getMessage());
    }
}
