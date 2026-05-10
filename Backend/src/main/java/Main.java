import domain.event.EventHandler;
import domain.model.WaterLevelSensor;
import domain.model.WaterTank;
import domain.service.*;
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

        // 3. Hardware (Creamos la conexión PRIMERO)
        ArduinoSerial conexion = new ArduinoSerial("COM3", miSensor);

        // 4. Actuadores (La bomba ahora recibe la conexión para prender el LED)
        IPump miBomba = new Pump(conexion);

        // 5. El Cerebro (El Manager)
        WaterLevelManager manager = new WaterLevelManager(miBomba, miSensor, miTanque, handler, logger);

        // 6. Comandos y Contexto Global
        // Contextos separados por responsabilidad
        AppContext context = new AppContext(authenticator, handler, logger);
        CommandHandler cmdHandler = new CommandHandler(context);
        cmdHandler.registerCommand("login", new LoginCommand());
        cmdHandler.registerCommand("prender_bomba", new PumpOnCommand(miBomba));
        cmdHandler.registerCommand("ver_nivel", new ViewLevelCommand(manager));
        cmdHandler.registerCommand("logs", new ViewLogsCommand());
        cmdHandler.registerCommand("apagar_bomba", new PumpOffCommand(miBomba));

        // 7. Arrancamos la lectura de datos
        conexion.iniciarConexion();

        // 8. Lanzar la Interfaz de Usuario (Ahora le pasamos también el contexto)
        Console ui = new Console(cmdHandler, context, manager);
        ui.start();

        // 9. Apagado seguro
        conexion.cerrarConexion();
        logger.saveLog("Sistema apagado por el usuario de forma segura.");
        System.out.println("✅ Sistema finalizado.");
        System.exit(0);
    }

}
