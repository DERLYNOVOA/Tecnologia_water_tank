package infrastructure.hardware;

import domain.model.WaterLevelSensor;
import com.fazecast.jSerialComm.SerialPort;
import java.util.Scanner;

public class ArduinoSerial {

    private final SerialPort puertoArduino;
    private final WaterLevelSensor sensorDelDominio;

    // Constructor: Le pasamos el nombre del puerto (ej. "COM3") y el sensor que vamos a actualizar
    public ArduinoSerial(String nombrePuerto, WaterLevelSensor sensor) {
        this.sensorDelDominio = sensor;
        this.puertoArduino = SerialPort.getCommPort(nombrePuerto);
        this.puertoArduino.setBaudRate(9600);
        // Configuramos para leer como si fuera texto continuo
        this.puertoArduino.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
    }

    public void iniciarConexion() {
        if (puertoArduino.openPort()) {
            System.out.println("🔌 CONEXIÓN SERIAL: Arduino conectado exitosamente.");

            //Darle 2 segundos al Arduino para que se reinicie bien
            try {
                System.out.println("⏳ Esperando a que el Arduino despierte...");
                Thread.sleep(2000);
            } catch (InterruptedException e) {}

        } else {
            System.out.println("ERROR: No se pudo abrir el puerto del Arduino.");
            return;
        }

        Thread hiloEscucha = new Thread(() -> {
            try (Scanner scanner = new Scanner(puertoArduino.getInputStream())) {
                while (scanner.hasNextLine()) {
                    // Leemos la línea cruda
                    String lineaRecibida = scanner.nextLine().trim();

                    // Ignoramos si llega una línea en blanco
                    if (lineaRecibida.isEmpty()) continue;

                    try {
                        //System.out.println("📡 RAW: [" + lineaRecibida + "]");

                        float distanciaLeida = Float.parseFloat(lineaRecibida);
                        sensorDelDominio.setWaterLevel(distanciaLeida);

                    } catch (NumberFormatException e) {
                        System.err.println("⚠Error convirtiendo a número. Basura recibida: [" + lineaRecibida + "]");
                    }
                }
            }
        });

        hiloEscucha.start();
    }

    //Método para apagar la conexión cuando se cierre el programa
    public void cerrarConexion() {
        if (puertoArduino.isOpen()) {
            puertoArduino.closePort();
            System.out.println("Conexión serial cerrada.");
        }
    }

    // Método para enviarle órdenes al Arduino
    public void enviarComando(String comando) {
        if (puertoArduino != null && puertoArduino.isOpen()) {
            byte[] bytes = comando.getBytes();
            puertoArduino.writeBytes(bytes, bytes.length);
        }
    }
}
