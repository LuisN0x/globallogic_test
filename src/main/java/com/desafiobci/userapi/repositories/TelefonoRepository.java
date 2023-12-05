package com.desafiobci.userapi.repositories;

import com.desafiobci.userapi.entities.Telefono;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TelefonoRepository extends JpaRepository<Telefono,Long> {

    public List<Telefono> findAllByUsuarioId(String usuarioId);
}
