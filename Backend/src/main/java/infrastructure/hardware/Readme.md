infrastructure

Esta capa contiene todo lo que toca el mundo exterior: hardware físico, archivos del sistema operativo, seguridad, y la consola. Nada de aquí debería conocerse desde el dominio ni desde la aplicación.

# infrastructure/hardware

## Pump.java

Implementa IPump del dominio. Cuando alguien llama turnOn(), hace dos cosas: cambia su estado interno a true y le manda el string "1" al Arduino a través de ArduinoSerial. Cuando llama turnOff(), pone false y manda "0". El Arduino tiene programado que al recibir "1" abre el relé de la bomba, y "0" lo cierra.
El guard if (conexion != null) existe para poder probar la bomba sin Arduino conectado. Si conexion es null, el estado interno cambia pero no se manda nada por el puerto serial.


## ArduinoSerial.java

Es el Adaptador (patrón Adapter) entre el mundo físico y el dominio. El Arduino habla a través de un puerto serial mandando números como texto plano (ej. "23.5"). El dominio espera floats en un WaterLevelSensor. Esta clase hace la traducción.

Tiene tres responsabilidades que en una arquitectura más estricta podrían separarse, pero para este proyecto están bien:
iniciarConexion() abre el puerto serial, espera 2 segundos para que el Arduino termine de arrancar (los Arduinos se reinician cuando se abre la conexión serial), y lanza un hilo separado (Thread) que escucha continuamente el puerto sin bloquear el resto del programa. Ese hilo lee línea por línea, intenta convertir cada línea a float, y si lo logra llama sensorDelDominio.setWaterLevel(distanciaLeida). Ese setWaterLevel activa la cadena de eventos del Observer.
cerrarConexion() cierra el puerto cuando el programa termina.


enviarComando(String) manda texto al Arduino, lo usa Pump para encender y apagar el relé.
Problema activo que aún no se corrigió: ArduinoSerial depende directamente de WaterLevelSensor (una clase concreta del dominio). Si mañana conectas un segundo sensor o cambias de sensor, hay que modificar esta clase. La solución que se propuso antes es reemplazar esa dependencia con un Consumerfloat inyectado desde Main, así ArduinoSerial no sabe nada del dominio.
