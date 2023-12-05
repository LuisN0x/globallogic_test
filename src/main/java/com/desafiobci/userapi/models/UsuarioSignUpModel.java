package com.desafiobci.userapi.models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({
        "name",
        "email",
        "password",
        "phones"
})
public class UsuarioSignUpModel {
    String name;
    String email;
    String password;
    List<TelefonoModel> phones;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<TelefonoModel> getPhones() {
        return phones;
    }

    public void setPhones(List<TelefonoModel> phones) {
        this.phones = phones;
    }
}
