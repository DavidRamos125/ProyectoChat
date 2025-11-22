package com.proyect.factory;

import com.proyect.domain.FileContent;
import com.proyect.domain.Message;
import com.proyect.domain.Session;
import com.proyect.domain.User;
import com.proyect.persistence.DAO.*;
import com.proyect.persistence.GeneralConection;
import com.proyect.persistence.Conection;
import com.proyect.util.Config;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Date;

public class InternalFactory {
    public static Connection getConnection() {
        Conection conection= GeneralConection.getInstance();
        return conection.conect();
    }

    public static User getUser(int id, String username, String password, boolean aceepted) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setAccepted(aceepted);
        return user;
    }

    public static Session getSession(String id, User user, String ip, Timestamp conectionTime) {
        Session session = new Session();
        session.setId(id);
        session.setUser(user);
        session.setIp(ip);
        session.setConnectionTime(conectionTime);

        return session;
    }

    public static Session getSession(String id, User user, String ip, Timestamp conectionTime, Timestamp disconectionTime) {
        Session session = new Session();
        session.setId(id);
        session.setUser(user);
        session.setIp(ip);
        session.setConnectionTime(conectionTime);
        session.setDisconnectionTime(disconectionTime);

        return session;
    }

    public static Message getMessage(){
        Message message = new Message();
        return message;
    }

    public static SessionDAO getSessionDAO() {
        return new SessionDAO();
    }

    public static FileContent getFileContent(String id, String name, byte[] data) {
        return new FileContent(id, name, data);
    }

    public static FileContentDAO getFileContentDAO() {
        return new FileContentDAO();
    }

    public static TextContentDAO getTextContentDAO() {
        return new TextContentDAO();
    }

    public static UserDAO getUserDAO() {
        return new UserDAO();
    }

    public static MessageDAO getMessageDAO() {
        return new MessageDAO();
    }

    public static Timestamp getTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}


