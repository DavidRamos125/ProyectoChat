package com.proyect.DTO.mapper;

import com.proyect.domain.*;
import com.proyect.DTO.*;
import com.proyect.domain.interfaces.Content;

import java.util.List;
import java.util.stream.Collectors;

public class MessageMapper {

    public static MessageDTO messageToDTO(Message message) {
        if (message == null) return null;

        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setSender(UserMapper.userToDTO(message.getSender()));
        dto.setReceiver(UserMapper.userToDTO(message.getReceiver()));
        dto.setSessionSender(SessionMapper.sessionToDTO(message.getSessionSender()));
        dto.setDate(message.getDate());

        if (message.getSessionsReceived() != null) {
            List<SessionDTO> sessionsDTO = message.getSessionsReceived()
                    .stream()
                    .map(SessionMapper::sessionToDTO)
                    .collect(Collectors.toList());
            dto.setSessionsReceived(sessionsDTO);
        }


        Content content = message.getContent();
        if (content != null) {
            if (content.getType() == ContentType.TEXT) {
                dto.setType("TEXT");
                TextContent textContent = (TextContent) content;
                dto.setTextContent(textContent.getText());
            } else if (content.getType() == ContentType.FILE) {
                dto.setType("FILE");
                FileContent fileContent = (FileContent) content;
                dto.setFileName(fileContent.getName());
                dto.setFileSize(fileContent.getSize());
                dto.setFileData(fileContent.getData());
            }
        }

        return dto;
    }

    public static Message dtoToMessage(MessageDTO dto) {
        if (dto == null) return null;

        Message message = new Message();
        message.setId(dto.getId());
        message.setSender(UserMapper.dtoToUser(dto.getSender()));
        message.setReceiver(UserMapper.dtoToUser(dto.getReceiver()));
        message.setDate(dto.getDate());

        if (dto.getSessionSender() != null && dto.getSender() != null) {
            User senderUser = UserMapper.dtoToUser(dto.getSender());
            Session sessionSender = SessionMapper.dtoToSession(dto.getSessionSender(), senderUser);
            message.setSessionSender(sessionSender);
        }

        if (dto.getSessionsReceived() != null && dto.getReceiver() != null) {
            User receiverUser = UserMapper.dtoToUser(dto.getReceiver());
            List<Session> sessions = dto.getSessionsReceived()
                    .stream()
                    .map(sessionDTO -> SessionMapper.dtoToSession(sessionDTO, receiverUser))
                    .collect(Collectors.toList());
            message.setSessionsReceived(sessions);
        }

        if ("TEXT".equals(dto.getType())) {
            TextContent textContent = new TextContent();
            textContent.setText(dto.getTextContent());
            message.setContent(textContent);
        } else if ("FILE".equals(dto.getType())) {
            FileContent fileContent = new FileContent();
            fileContent.setName(dto.getFileName());
            fileContent.setData(dto.getFileData());
            message.setContent(fileContent);
        }

        return message;
    }
}
