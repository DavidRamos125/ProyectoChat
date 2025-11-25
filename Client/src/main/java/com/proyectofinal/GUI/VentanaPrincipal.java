package com.proyectofinal.GUI;

import com.proyectofinal.DTO.UserDTO;
import com.proyectofinal.controller.CommunicationController;
import com.proyectofinal.factory.ExternalFactory;
import com.proyectofinal.util.Encoder;

import java.util.Arrays;
import javax.swing.JOptionPane;

public class VentanaPrincipal extends javax.swing.JFrame {
    
    public static VentanaPrincipal getInstance(){
        if(instance == null){
            instance = new VentanaPrincipal();
            instance.setVisible(true);
        }
        return instance;
    }

    private VentanaPrincipal() {
        initComponents();
        communicationController = ExternalFactory.getSocketController();
        disabled();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        conect = new javax.swing.JButton();
        disconect = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        registroPanel = new javax.swing.JPanel();
        registrarUsuario = new javax.swing.JButton();
        usernameRegistro = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        passwordRegistro1 = new javax.swing.JPasswordField();
        passwordRegistro2 = new javax.swing.JPasswordField();
        loginPanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        usernameLogin = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        passwordLogin = new javax.swing.JPasswordField();
        loginUsuario = new javax.swing.JButton();
        chatPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();
        enviarMensajeTexto = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        enviarArchivo = new javax.swing.JButton();
        descargarArchivo = new javax.swing.JButton();
        status = new javax.swing.JTextField();
        jTextField1 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("CLIENTE");

        conect.setText("Conectar");
        conect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conectActionPerformed(evt);
            }
        });

        disconect.setText("Desconectar");
        disconect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disconectActionPerformed(evt);
            }
        });

        registrarUsuario.setText("Registrar");
        registrarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registrarUsuarioActionPerformed(evt);
            }
        });

        jLabel2.setText("Usuario");

        jLabel3.setText("Contraseña");

        jLabel4.setText("Confirmar Contraseña");

        javax.swing.GroupLayout registroPanelLayout = new javax.swing.GroupLayout(registroPanel);
        registroPanel.setLayout(registroPanelLayout);
        registroPanelLayout.setHorizontalGroup(
            registroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(registroPanelLayout.createSequentialGroup()
                .addGap(192, 192, 192)
                .addGroup(registroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(30, 30, 30)
                .addGroup(registroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(usernameRegistro)
                    .addGroup(registroPanelLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(registrarUsuario))
                    .addComponent(passwordRegistro1)
                    .addComponent(passwordRegistro2, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(259, Short.MAX_VALUE))
        );
        registroPanelLayout.setVerticalGroup(
            registroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(registroPanelLayout.createSequentialGroup()
                .addGap(92, 92, 92)
                .addGroup(registroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(registroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(passwordRegistro1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(registroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(passwordRegistro2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addComponent(registrarUsuario)
                .addContainerGap(158, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Registro", registroPanel);

        jLabel5.setText("Usuario");

        jLabel6.setText("Contraseña");

        loginUsuario.setText("Login");
        loginUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginUsuarioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout loginPanelLayout = new javax.swing.GroupLayout(loginPanel);
        loginPanel.setLayout(loginPanelLayout);
        loginPanelLayout.setHorizontalGroup(
            loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addGap(249, 249, 249)
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(30, 30, 30)
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(usernameLogin, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                    .addComponent(passwordLogin)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loginPanelLayout.createSequentialGroup()
                        .addComponent(loginUsuario)
                        .addGap(40, 40, 40)))
                .addContainerGap(259, Short.MAX_VALUE))
        );
        loginPanelLayout.setVerticalGroup(
            loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addGap(92, 92, 92)
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(passwordLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addComponent(loginUsuario)
                .addContainerGap(194, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Login", loginPanel);

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        jList2.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList2);

        enviarMensajeTexto.setText("Enviar Mensaje");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane3.setViewportView(jTextArea1);

        enviarArchivo.setText("Enviar Archivo");

        descargarArchivo.setText("<html>Descargar archivo seleccionado</html>\n");

        javax.swing.GroupLayout chatPanelLayout = new javax.swing.GroupLayout(chatPanel);
        chatPanel.setLayout(chatPanelLayout);
        chatPanelLayout.setHorizontalGroup(
            chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chatPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(chatPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(enviarMensajeTexto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(enviarArchivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(chatPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 486, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(descargarArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 13, Short.MAX_VALUE)))
                .addContainerGap())
        );
        chatPanelLayout.setVerticalGroup(
            chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, chatPanelLayout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addGroup(chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addGroup(chatPanelLayout.createSequentialGroup()
                        .addGroup(chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(chatPanelLayout.createSequentialGroup()
                                .addGap(69, 69, 69)
                                .addComponent(descargarArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(chatPanelLayout.createSequentialGroup()
                                .addComponent(enviarMensajeTexto)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(enviarArchivo))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Chat", chatPanel);

        status.setEditable(false);
        status.setBackground(new java.awt.Color(102, 255, 102));
        status.setText("Desconectado");

        jTextField1.setEditable(false);
        jTextField1.setText("No loggeado");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(107, 107, 107)
                        .addComponent(conect, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(disconect)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(status, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 756, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(121, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(conect)
                    .addComponent(disconect)
                    .addComponent(jLabel1)
                    .addComponent(status, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 437, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void conectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_conectActionPerformed
        communicationController.connect();
        if (communicationController.isConnected()) {
            status.setText("Conectado");
            enabled();
        }
    }//GEN-LAST:event_conectActionPerformed

    private void registrarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registrarUsuarioActionPerformed

        char[] password1 = passwordRegistro1.getPassword();
        char[] password2 = passwordRegistro2.getPassword();
        String username = usernameRegistro.getText();
        if (Arrays.toString(password1).equals(Arrays.toString(password2))) {
            String passwordEncode =  Encoder.encode(Arrays.toString(password1));
            communicationController.sendRegister(username, passwordEncode);
        } else {
            JOptionPane.showMessageDialog(null, "Las contraseñas son distintas", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_registrarUsuarioActionPerformed

    private void disconectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disconectActionPerformed
        //communicationController.disconnect();
        disabled();
    }//GEN-LAST:event_disconectActionPerformed

    private void loginUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginUsuarioActionPerformed
        char[] password1 = passwordRegistro1.getPassword();
        String username = usernameRegistro.getText();

            String passwordEncode =  Encoder.encode(Arrays.toString(password1));
            communicationController.sendLogin(username, passwordEncode);
    }//GEN-LAST:event_loginUsuarioActionPerformed

    private void enabled() {
        loginUsuario.setEnabled(true);
        registrarUsuario.setEnabled(true);
        conect.setEnabled(false);
        disconect.setEnabled(true);
    }

    private void disabled() {
        loginUsuario.setEnabled(false);
        registrarUsuario.setEnabled(false);
        conect.setEnabled(true);
        disconect.setEnabled(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel chatPanel;
    private javax.swing.JButton conect;
    private javax.swing.JButton descargarArchivo;
    private javax.swing.JButton disconect;
    private javax.swing.JButton enviarArchivo;
    private javax.swing.JButton enviarMensajeTexto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JList<String> jList1;
    private javax.swing.JList<String> jList2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JPanel loginPanel;
    private javax.swing.JButton loginUsuario;
    private javax.swing.JPasswordField passwordLogin;
    private javax.swing.JPasswordField passwordRegistro1;
    private javax.swing.JPasswordField passwordRegistro2;
    private javax.swing.JButton registrarUsuario;
    private javax.swing.JPanel registroPanel;
    private javax.swing.JTextField status;
    private javax.swing.JTextField usernameLogin;
    private javax.swing.JTextField usernameRegistro;
    // End of variables declaration//GEN-END:variables
    private CommunicationController communicationController;
    private static VentanaPrincipal instance;
}
