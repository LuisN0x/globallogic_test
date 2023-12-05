package com.desafiobci.userapi.repositories;

import com.desafiobci.userapi.entities.Telefono;
import com.desafiobci.userapi.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,String> {

    boolean existsByEmail(String email);

    Optional<Usuario> findByEmail(String email);

    List<Telefono> findPhonesById(String id);
}
