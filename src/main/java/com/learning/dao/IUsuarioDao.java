package com.learning.dao;

import com.learning.models.Usuario;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface IUsuarioDao extends CrudRepository<Usuario, Long>{


    // Manera uno de hacer consulta personalizada 
    public Usuario findByUsernameAndEmail(String username, String email);


    // Segunda forma com anotacion query
    @Query("select u from Usuario u  where u.username =?1 ")
    public Usuario findByUsernameAndEmail2(String username, String email);


}