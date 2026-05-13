package infrastructure.hardware;

import domain.model.WaterLevelSensor;
import domain.model.TemperatureSensor;
import com.fazecast.jSerialComm.SerialPort;
import java.util.Scanner;

public class ArduinoSerial {

    private final SerialPort puertoArduino;
    private final WaterLevelSensor sensorDelDominio;
    private final TemperatureSensor sensorTemperatura;

    public ArduinoSerial(String nombrePuerto, WaterLevelSensor sensor, TemperatureSensor sensorTemperatura) {
        this.sensorDelDominio = sensor;
        this.sensorTemperatura = sensorTemperatura;
        this.puertoArduino = SerialPort.getCommPort(nombrePuerto);
        this.puertoArduino.setBaudRate(9600);
        this.puertoArduino.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
    }

    public void iniciarConexion() {
        if (puertoArduino.openPort()) {
            System.out.println("🔌 CONEXIÓN SERIAL: Arduino conectado exitosamente.");
            try {
                System.out.println("⏳ Esperando a que el Arduino despierte...");
                Thread.sleep(2000);
                enviarComando("O1");
            } catch (InterruptedException e) {}
        } else {
            System.out.println("ERROR: No se pudo abrir el puerto del Arduino.");
            return;
        }

        Thread hiloEscucha = new Thread(() -> {
            try (Scanner scanner = new Scanner(puertoArduino.getInputStream())) {
                while (scanner.hasNextLine()) {
                    String lineaRecibida = scanner.nextLine().trim();
                    if (lineaRecibida.isEmpty()) continue;

                    try {
                        if (lineaRecibida.startsWith("D:")) {
                            float distancia = Float.parseFloat(lineaRecibida.substring(2));
                            sensorDelDominio.setWaterLevel(distancia);
                        } else if (lineaRecibida.startsWith("T:")) {
                            float temperatura = Float.parseFloat(lineaRecibida.substring(2));
                            sensorTemperatura.setTemperature(temperatura);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("⚠ Error convirtiendo a número: [" + lineaRecibida + "]");
                    }
                }
            }
        });
        hiloEscucha.start();
    }

    public void cerrarConexion() {
        if (puertoArduino.isOpen()) {
            enviarComando("O0");
            puertoArduino.closePort();
            System.out.println("Conexión serial cerrada.");
        }
    }

    public void enviarComando(String comando) {
        if (puertoArduino != null && puertoArduino.isOpen()) {
            byte[] bytes = (comando + "\n").getBytes();
            puertoArduino.writeBytes(bytes, bytes.length);
        }
    }
}