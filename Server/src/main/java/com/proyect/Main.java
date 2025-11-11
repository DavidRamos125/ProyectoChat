package com.proyect;

import com.proyect.GUI.VentanaPrincipal;
import com.proyect.factory.ExternalFactory;


public class Main {

    public static void main(String[] args) {
        VentanaPrincipal ventanaPrincipal = ExternalFactory.getVentanaPrincipal();
        ventanaPrincipal.setVisible(true);
    }
}