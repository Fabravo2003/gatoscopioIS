package gatoscopio.back.service;


import java.util.List;

import gatoscopio.back.model.Usuario;

public interface ServiceAdmin {
   
    void createUser(Usuario usuario);
    void modifyUser(Usuario usuario);
    void deleteUser(Usuario usuario);
    List<Usuario> getUsers();

}
