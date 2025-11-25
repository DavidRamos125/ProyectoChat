package com.proyectofinal;

import com.proyectofinal.GUI.VentanaPrincipal;
import com.proyectofinal.factory.ExternalFactory;

public class Main {
    public static void main(String[] args) {
        VentanaPrincipal ventanaPrincipal = ExternalFactory.getVentanaPrincipal();
    }
}