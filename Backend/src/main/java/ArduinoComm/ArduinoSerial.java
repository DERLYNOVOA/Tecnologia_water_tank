package ArduinoComm;

import Domain.WaterLevelSensor;
import com.fazecast.jSerialComm.SerialPort;
import java.util.Scanner;

public class ArduinoSerial {

    private SerialPort puertoArduino;
    private WaterLevelSensor sensorDelDominio;

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

            // 1. EL TRUCO: Darle 2 segundos al Arduino para que se reinicie bien
            try {
                System.out.println("⏳ Esperando a que el Arduino despierte...");
                Thread.sleep(2000);
            } catch (InterruptedException e) {}

        } else {
            System.out.println("❌ ERROR: No se pudo abrir el puerto del Arduino.");
            return;
        }

        Thread hiloEscucha = new Thread(() -> {
            Scanner scanner = new Scanner(puertoArduino.getInputStream());

            while (scanner.hasNextLine()) {
                // 2. Leemos la línea cruda
                String lineaRecibida = scanner.nextLine().trim();

                // Ignoramos si llega una línea en blanco
                if (lineaRecibida.isEmpty()) continue;

                try {
                    // 3. IMPRIMIMOS LO QUE LLEGA (Para saber que no estamos ciegos)
                    System.out.println("📡 Dato crudo recibido del Arduino: [" + lineaRecibida + "]");

                    float distanciaLeida = Float.parseFloat(lineaRecibida);
                    sensorDelDominio.setWaterLevel(distanciaLeida);

                } catch (NumberFormatException e) {
                    // 4. SI HAY ERROR, QUE NOS LO DIGA EN ROJO
                    System.err.println("⚠️ Error convirtiendo a número. Basura recibida: [" + lineaRecibida + "]");
                }
            }
            scanner.close();
        });

        hiloEscucha.start();
    }

    // Buena práctica: Método para apagar la conexión cuando se cierre el programa
    public void cerrarConexion() {
        if (puertoArduino.isOpen()) {
            puertoArduino.closePort();
            System.out.println("Conexión serial cerrada.");
        }
    }
}
