import application.port.outbound.InputProvider;
import application.port.outbound.OutputProvider;
import domain.event.EventHandler;
import domain.model.Alarm;
import domain.model.TemperatureSensor;
import domain.model.WaterLevelSensor;
import domain.model.WaterTank;
import domain.service.*;
import infrastructure.external.ConsoleInputProvider;
import infrastructure.external.ConsoleOutputProvider;
import infrastructure.hardware.ArduinoSerial;
import application.command.*;
import application.service.*;
import application.command.CommandHandler;
import infrastructure.hardware.Pump;
import infrastructure.persistence.FileLogRepository;
import infrastructure.persistence.InMemoryUserRepository;
import infrastructure.security.SimplePasswordHasher;
import presentation.Console;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando Motor de Sistema");

        // 1. Seguridad y Persistencia
        UserRepository userRepo = new InMemoryUserRepository();
        PasswordHasher hasher = new SimplePasswordHasher();
        AuthenticationService authService = new AuthenticationService(userRepo, hasher);
        AuthorizationService authzService = new AuthorizationService();
        Authenticator authenticator = new Authenticator(authService, authzService);
        RepositoryLog logger = new FileLogRepository();

        // 2. Dominio y Eventos
        EventHandler handler = new EventHandler();
        WaterTank miTanque = new WaterTank(15.0f); //rango real de prueba
        WaterLevelSensor miSensor = new WaterLevelSensor(handler);
        miSensor.setActive(true);

        ArduinoSerial conexion = new ArduinoSerial("COM3", miSensor);

        IPump miBomba = new Pump(conexion);

        // 5. El Cerebro (El Manager)
        WaterLevelManager manager = new WaterLevelManager(miBomba, miSensor, miTanque, handler, logger);

        // Alarma y sensor de temperatura
        Alarm alarma = new Alarm();
        TemperatureSensor tempSensor = new TemperatureSensor(handler);
        tempSensor.setActive(true);
        TemperatureManager tempManager = new TemperatureManager(tempSensor, manager, alarma, 15.0f, 35.0f);
        handler.subscribe(tempManager); // WaterLevelManager ya se suscribe solo en su constructor


        // ── 6. Apagado seguro ante cierre forzoso (Ctrl+C, excepción) ────
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n[!] Cerrando sistema...");
            conexion.cerrarConexion();
            logger.saveLog("Sistema apagado de forma segura.");
        }));

        AppContext context = new AppContext(authenticator, handler, logger);
        CommandHandler cmdHandler = new CommandHandler(context);
        cmdHandler.registerCommand("login", new LoginCommand());
        cmdHandler.registerCommand("prender_bomba", new PumpOnCommand(miBomba));
        cmdHandler.registerCommand("ver_nivel", new ViewLevelCommand(manager));
        cmdHandler.registerCommand("logs", new ViewLogsCommand());
        cmdHandler.registerCommand("apagar_bomba", new PumpOffCommand(miBomba));


        // ── 8. UI: proveedores inyectados ────────────────────────────────
        InputProvider input  = new ConsoleInputProvider();
        OutputProvider output = new ConsoleOutputProvider();
        Console ui = new Console(cmdHandler, context, manager, input, output);

        // 7. Arrancamos la lectura de datos
        conexion.iniciarConexion();

        try {
            ui.start(); // bloquea hasta que el usuario escoja "exit"
        } catch (Exception e) {
            System.err.println("Error fatal: " + e.getMessage());
            logger.saveLog("[ERROR FATAL] " + e.getMessage());
        }

        // El ShutdownHook se encarga del cierre, pero si start() termina normal:
        conexion.cerrarConexion();
        logger.saveLog("Sistema apagado por el usuario de forma segura.");
        System.out.println(" Sistema finalizado.");
        System.exit(0);

    }

}


