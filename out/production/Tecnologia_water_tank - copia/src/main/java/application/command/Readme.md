# application/command

Aquí vive el patrón Command completo. Separa qué se puede hacer en el sistema de quién lo pide y cómo se ejecuta. La UI solo sabe que existe un comando llamado "login" o "ver_nivel", no sabe nada de sensores ni bases de datos. Cada acción del sistema es una clase independiente.


## Command.java

Es el contrato del patrón Command. Define que todo comando recibe un AppContext (para acceder a servicios) y un String arg (para recibir parámetros de la UI). Cualquier acción nueva del sistema solo necesita implementar esta interfaz.


## CommandHandler.java

Es el Invocador del patrón Command. Mantiene un mapa de nombre → comando, y cuando la UI dice execute("login", ""), él busca el comando correcto y lo dispara. No sabe qué hace cada comando, solo los registra y los llama.
El uso de HashMap permite agregar nuevos comandos sin tocar esta clase, lo que cumple el principio Abierto/Cerrado (OCP).


## PumpOffCommand.java y PumpOnCommand.java

Ambos están bien diseñados. Dependen de IPump (la interfaz), nunca de la implementación concreta de Arduino. Registran la acción en el log con el nombre del usuario que la ejecutó. PumpOffCommand tiene la validación extra de verificar si ya estaba apagada antes de actuar, lo cual es correcto.


## ViewLevelCommand.java

Muestra el estado del tanque usando IWaterSystemStatus, que es una interfaz de solo lectura. Correcto en cuanto a dependencias.
Observación menor: la lógica del ASCII art (los bordes ╔══╗) vive aquí dentro del comando. En una arquitectura estricta, el comando devolvería datos y la UI los dibujaría. Para el alcance de este proyecto es aceptable, pero es algo para tener en mente.


## ViewLogsCommand.java

Cuando el usuario elige "ver historial" en el menú, este comando es el que responde. Su trabajo es pedirle los logs al sistema y mostrarlos en pantalla.

La comunicación funciona así: el comando recibe el AppContext, le pregunta context.getLogger() que le devuelve un objeto que implementa RepositoryLog, y sobre ese objeto llama readAllLogs() que devuelve una lista de strings. El comando no sabe si esos strings vienen de un archivo de texto, de una base de datos, o de la memoria RAM. Solo sabe que llegaron y los imprime uno por uno.

Antes leía el archivo directamente con Files.readAllLines, lo cual rompía la arquitectura porque la capa de aplicación sabía que existía un archivo llamado historial_tanque.txt. Con esta corrección, eso ya no le importa.
