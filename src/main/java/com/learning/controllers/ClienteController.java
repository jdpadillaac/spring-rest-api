package com.learning.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.learning.models.Cliente;
import com.learning.models.JsonResp;
import com.learning.service.cliente.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<JsonResp> index() {

        List<Cliente> clienteList;

        try {
            clienteList = clienteSerivice.findAll();
        } catch (DataAccessException e) {

            resp.success = false;
            resp.message = "Error en la base de datos al consultar lista de clientes";
            resp.error = e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage());
            return new ResponseEntity<JsonResp>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        resp.message = "Clientes cargado correctamente";
        resp.success = true;
        resp.data = clienteList;
        return new ResponseEntity<JsonResp>(resp, HttpStatus.OK);
    }


    @GetMapping("/clientes/{id}")
    public ResponseEntity<JsonResp> showById(@PathVariable Long id) {

        Cliente cliente = null;
        try {
            cliente = clienteSerivice.findById(id);
        } catch (DataAccessException e) {
            resp.success = false;
            resp.message = "Error en la base de datos al consultar cliente con id " + id;
            resp.error = e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage());
            return new ResponseEntity<JsonResp>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        resp.success = true;
        resp.message = "ok";
        resp.data = cliente;
        return new ResponseEntity<>(resp, HttpStatus.OK);    
    }

    @PostMapping("/clientes/crear")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<JsonResp> create(@Valid @RequestBody Cliente cliente, BindingResult result) {

        cliente.setCreateAt(new Date());
        Cliente clienteNew;


        if (result.hasErrors()) {


            List<String> errors =  result.getFieldErrors()
            .stream()
            .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
            .collect(Collectors.toList());

            resp.success = false;
            resp.message = "Error de validación - Datos enviados incorrectamente";
            resp.error = errors;
            return new ResponseEntity<JsonResp>(resp, HttpStatus.BAD_REQUEST);
        }

        try {
            clienteNew = clienteSerivice.save(cliente);
        } catch (DataAccessException e) {
            resp.success = false;
            resp.message = "Error en la base de datos al crear cliente";
            resp.error = e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage());
            return new ResponseEntity<JsonResp>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            resp = new JsonResp();
        }

        resp.success = true;
        resp.message = "Cliente creado de manera satisfactoria";
        resp.data = clienteNew;
        return new ResponseEntity<JsonResp>(resp, HttpStatus.OK);
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
 
    @PostMapping("/clientes/upload/{id}")
    public ResponseEntity<JsonResp> uploadImage(@RequestParam("archivo") MultipartFile archivo, @PathVariable Long id) {
        
        Cliente cliente;

        try {
            cliente = clienteSerivice.findById(id);
        } catch (DataAccessException e) {
            resp.success = false;
            resp.message =  "Error en la base de datos al consultar usuario por id: " + id;
            resp.error = e;
            return new ResponseEntity<JsonResp>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (!archivo.isEmpty()){
            String imageName = archivo.getOriginalFilename();
            Path rutaArchivo = Paths.get("clienteImagenes").resolve(imageName).toAbsolutePath();

            try {
                Files.copy(archivo.getInputStream(), rutaArchivo);
            } catch (IOException e) {
                resp.success = false;
                resp.message =  "Error en servidor al momento de mover imagen";
                resp.error = e;
                return new ResponseEntity<JsonResp>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            cliente.setFoto(imageName);
            cliente = clienteSerivice.save(cliente);

            resp.success = true;
            resp.message =  "Imagen de cliente actualiozada correctamente";
            resp.data = cliente;
            return new ResponseEntity<JsonResp>(resp, HttpStatus.CREATED);

            

        }


        return new ResponseEntity<JsonResp>(resp, HttpStatus.CREATED);
    }

}