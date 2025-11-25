
package com.proyectofinal.interfaces;

import com.proyectofinal.DTO.MessageDTO;
import com.proyectofinal.DTO.UserDTO;

public interface IObserverConcrete extends IObserver {
    public void updateUser(String type,  UserDTO user);
    public void updateMessage(String type,  MessageDTO message);
}
