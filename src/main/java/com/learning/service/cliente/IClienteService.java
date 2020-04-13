package com.learning.service.cliente;

import java.util.List;
import com.learning.models.Cliente;

public interface IClienteService {

    public List<Cliente> findAll();

    public Cliente save(Cliente cliente);

    public void delete(Long id);

    public Cliente findById(Long id);
}