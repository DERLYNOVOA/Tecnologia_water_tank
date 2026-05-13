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
import infrastructure.hardware.Pump;
import infrastructure.persistence.FileLogRepository;
import infrastructure.persistence.InMemoryUserRepository;
import infrastructure.security.SimplePasswordHasher;
import presentation.Console;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando Motor de Sistema");

        UserRepository userRepo = new InMemoryUserRepository();
        PasswordHasher hasher = new SimplePasswordHasher();
        AuthenticationService authService = new AuthenticationService(userRepo, hasher);
        AuthorizationService authzService = new AuthorizationService();
        Authenticator authenticator = new Authenticator(authService, authzService);
        RepositoryLog logger = new FileLogRepository();

        EventHandler handler = new EventHandler();
        WaterTank miTanque = new WaterTank(15.0f);

        WaterLevelSensor miSensor = new WaterLevelSensor(handler);
        miSensor.setActive(true);

        TemperatureSensor tempSensor = new TemperatureSensor(handler);
        tempSensor.setActive(true);

        ArduinoSerial conexion = new ArduinoSerial("COM3", miSensor, tempSensor);

        IPump miBomba = new Pump(conexion);
        domain.service.IPumpStrategy pumpStrategy = new domain.service.IPumpStrategy() {
            @Override
            public boolean shouldTurnOff(float porcentaje) {
                return porcentaje >= 80.0f;
            }

            @Override
            public boolean shouldTurnOn(float porcentaje) {
                return porcentaje <= 20.0f;
            }
        };

        WaterLevelManager manager = new WaterLevelManager(miBomba, miSensor, miTanque, handler, logger, pumpStrategy);

        // Pasamos el ArduinoSerial a la alarma
        Alarm alarma = new Alarm(conexion);

        TemperatureManager tempManager = new TemperatureManager(tempSensor, manager, alarma, 15.0f, 35.0f);
        handler.subscribe(tempManager);

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

        InputProvider input  = new ConsoleInputProvider();
        OutputProvider output = new ConsoleOutputProvider();

        // <-- AQUI: Inyectamos 'tempSensor' y la 'conexion' (ArduinoSerial) a Console para el modo DEBUG
        Console ui = new Console(cmdHandler, context, manager, tempSensor, conexion, input, output);

        conexion.iniciarConexion();

        try {
            ui.start();
        } catch (Exception e) {
            System.err.println("Error fatal: " + e.getMessage());
            logger.saveLog("[ERROR FATAL] " + e.getMessage());
        }

        conexion.cerrarConexion();
        System.out.println(" Sistema finalizado.");
        System.exit(0);
    }
}