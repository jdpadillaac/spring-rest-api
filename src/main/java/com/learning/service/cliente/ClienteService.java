package com.learning.service.cliente;

import java.util.List;

import com.learning.dao.ClienteDao;
import com.learning.models.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ClienteService implements IClienteService {

    @Autowired
    private ClienteDao clienteDao;

    @Override
    public List<Cliente> findAll() {
        return (List<Cliente>) clienteDao.findAll();
    }

    @Override
    public Cliente save(Cliente cliente) {
        // TODO Auto-generated method stub
        return clienteDao.save(cliente);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        clienteDao.deleteById(id);

    }

    @Override
    public Cliente findById(Long id) {
        // TODO Auto-generated method stub
        return clienteDao.findById(id).orElse(null);
    }

}