package com.proyect.domain.interfaces;

import com.proyect.DTO.UserDTO;

public interface IObserverConcrete extends IObserver {
    void updateLogin(UserDTO user);
    void updateLogout(UserDTO user);
}
