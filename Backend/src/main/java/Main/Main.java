package Main;

import Domain.*;
import Services.*;
import ArduinoComm.ArduinoSerial;

public class Main {
    public static void main(String[] args) {
        System.out.println("🚀 INICIANDO PRUEBA EN VIVO CON ARDUINO...");

        // 1. El motor de eventos
        EventHandler handler = new EventHandler();

        // 2. El Dominio (Ajusta el 20.0f a la altura real de tu tanque en cm si es diferente)
        WaterTank miTanque = new WaterTank(20.0f);
        WaterLevelSensor miSensor = new WaterLevelSensor(handler);

        // 3. El Hardware y Actuadores
        IPump miBomba = new MockPump(); // Usamos la de mentiras porque no tienes el relé aún

        // 🔌 CONEXIÓN REAL AL ARDUINO
        // ¡IMPORTANTE! Cambia "COM3" por el puerto exacto en el que está conectado tu Arduino
        ArduinoSerial conexion = new ArduinoSerial("COM3", miSensor);

        // 4. El Cerebro (El Manager)
        WaterLevelManager manager = new WaterLevelManager(miBomba, miSensor, miTanque, handler);

        // 5. ¡A darle energía!
        conexion.iniciarConexion();

        System.out.println("✅ Sistema escuchando el puerto USB...");
        System.out.println("👉 Mueve tu mano arriba y abajo sobre el sensor.");
    }
}