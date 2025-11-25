package com.proyect.DTO.mapper;


import com.proyect.domain.User;
import com.proyect.DTO.UserDTO;

public class UserMapper {

    public static UserDTO userToDTO(User user) {
        if (user == null) return null;

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        dto.setAccepted(user.getAccepted());

        return dto;
    }

    public static User dtoToUser(UserDTO dto) {
        if (dto == null) return null;

        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setAccepted(dto.isAccepted());

        return user;
    }
}
