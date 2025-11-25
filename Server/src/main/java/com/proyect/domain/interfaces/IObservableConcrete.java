package com.proyect.domain.interfaces;

import com.proyect.DTO.UserDTO;

public interface IObservableConcrete extends IObservable {
    void notifyLogin(UserDTO user);
    void notifyLogout(UserDTO user);
}
