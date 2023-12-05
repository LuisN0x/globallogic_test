package com.desafiobci.userapi.models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Date;
import java.util.List;

@JsonPropertyOrder({
        "id",
        "created",
        "lastLogin",
        "token",
        "isActive",
        "name",
        "email",
        "password",
        "phones"
})
public class UsuarioModel {
    String id;
    Date created;
    Date lastLogin;
    String token;
    boolean isActive;
    String name;
    String email;
    String password;
    List<TelefonoModel> phones;

    public UsuarioModel() {
    }

    public UsuarioModel(String id, Date created, Date lastLogin, String token, boolean isActive, String name, String email, String password, List<TelefonoModel> phones) {
        this.id = id;
        this.created = created;
        this.lastLogin = lastLogin;
        this.token = token;
        this.isActive = isActive;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phones = phones;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

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

