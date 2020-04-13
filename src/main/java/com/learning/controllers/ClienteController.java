package com.learning.controllers;

import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.learning.models.Cliente;
import com.learning.models.JsonResp;
import com.learning.service.cliente.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin()
@RestController
@RequestMapping("/api")
public class ClienteController {


    @Autowired
    private ClienteService clienteSerivice;
    public JsonResp resp;

    public ClienteController() {
        resp = new JsonResp();
    }

    @GetMapping("/clientes")
    public List<Cliente> index() {
        return clienteSerivice.findAll();
    }

    @GetMapping("/clientes/{id}")
    public JsonResp showById(@PathVariable Long id) {

        Cliente cliente = clienteSerivice.findById(id);

        JsonResp resp = new JsonResp();

        resp.success = true;
        resp.message = "ok";
        resp.data = cliente;

        return resp;    
    }

    @PostMapping("/clientes/crear")
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente create(@RequestBody Cliente cliente) {
        cliente.setCreateAt(new Date());
        return clienteSerivice.save(cliente);

    }

    @PutMapping("/clientes/editar")
    public Cliente update(@RequestBody Cliente cliente) {

        Cliente clienteActual = clienteSerivice.findById(cliente.getId());
        clienteActual.setApellidos(cliente.getApellidos());
        clienteActual.setNombre(cliente.getNombre());
        clienteActual.setEmail(cliente.getEmail());

        return clienteSerivice.save(clienteActual);
    }

    @DeleteMapping("/clientes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        clienteSerivice.delete(id);
    }

}