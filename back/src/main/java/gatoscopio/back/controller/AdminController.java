package gatoscopio.back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import gatoscopio.back.model.Usuario;
import gatoscopio.back.service.impl.ServiceAdminImpl;


@RestController
public class AdminController {
    @Autowired
    private ServiceAdminImpl service;


    @PostMapping("/crearUsuario")
    public void createUser() {
        
    }

    @PostMapping("/cambiarRol")
    public Usuario changeRol(@RequestBody Usuario user, @PathVariable String rol) {
        return service.changeRol(user, rol);
    }

}
