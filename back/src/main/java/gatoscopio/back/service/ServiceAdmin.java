package gatoscopio.back.service;


import gatoscopio.back.model.Usuario;

import java.util.List;

public interface ServiceAdmin {

    void createUser(Usuario usuario);
    void modifyUser(Usuario usuario);
    void deleteUser(Usuario usuario);
    List<Usuario> getUsers();

}
