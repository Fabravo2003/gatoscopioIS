package gatoscopio.back.service.impl;

import java.util.List;

import gatoscopio.back.model.Usuario;
import gatoscopio.back.repository.UsuarioRepository;
import gatoscopio.back.service.ServiceAdmin;

public class ServiceAdminImpl implements ServiceAdmin {
     private UsuarioRepository repository;

    public ServiceAdminImpl(UsuarioRepository repository){this.repository = repository;}

    public void createUser(Usuario usuario) {
        //TODO
    }

    public void modifyUser(Usuario usuario) {
        //TODO
    }

    public void deleteUser(Usuario usuario) {
        //TODO
    }

    public List<Usuario> getUsers() {
        //TODO
        return List.of();
    }

    public Usuario changeRol(Usuario usuario, String rol) {
        Usuario temp = repository.findById(usuario.getId()).orElseThrow();
        temp.setRol(rol);
        return repository.save(temp);
    }
}
