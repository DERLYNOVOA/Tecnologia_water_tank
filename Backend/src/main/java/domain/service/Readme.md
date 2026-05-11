# domain/service

Esta carpeta contiene únicamente interfaces. Ninguna implementación vive aquí. Esa es exactamente su razón de existir: define los contratos que el dominio necesita, sin importarle quién los cumple ni cómo.

Es la aplicación directa del principio de Inversión de Dependencias: el dominio no depende de infraestructura, la infraestructura depende del dominio.


## EventListener.java

Es la mitad suscriptora del patrón Observer. Cualquier clase que quiera enterarse de eventos del sistema implementa esta interfaz y define qué hace cuando llega uno.
EventHandler mantiene una lista de EventListener. Cuando un sensor emite algo, EventHandler llama a onEvent() en cada uno. El sensor no sabe quién escucha, el listener no sabe quién emite. Eso es exactamente el desacoplamiento que busca Observer.


## IAlarm.java

Contrato de la alarma. Alarm en domain/model la implementa, y cualquier clase que necesite usar la alarma depende de esta interfaz, no de Alarm directamente.
Vive aquí y no en model porque es una abstracción de comportamiento, no un modelo de datos. Define qué puede hacer una alarma, no qué es.


## IPump.java

Mismo rol que IAlarm pero para la bomba. La implementación real (Pump.java) vive en infrastructure/hardware porque interactúa con Arduino. Los servicios que controlan la bomba solo conocen IPump, nunca a Pump directamente.
getStatus() devuelve si está encendida o apagada, lo cual permite que los managers consulten el estado sin encenderla ni apagarla.


## IWaterSystemStatus.java

Interfaz de solo lectura del estado del sistema de agua. Expone cuatro cosas:

* getCurrentDistance() → la distancia cruda del sensor ultrasónico
* getCurrentPercentage() → el porcentaje calculado del tanque
* isPumpActive() → si la bomba está corriendo
* hasData() → si el sensor ya tiene una lectura válida

La implementa alguna clase que centraliza ese estado, probablemente WaterLevelManager. La UI la usa para dibujar las barras de nivel sin tener acceso directo al sensor ni al tanque.


## PasswordHasher.java

Interfaz del patrón Strategy para el hasheo de contraseñas. AuthenticationService depende de esta interfaz. La implementación concreta (SimplePasswordHasher con SHA-256) vive en infrastructure/security.

Si mañana decides cambiar a BCrypt, creas BcryptPasswordHasher implements PasswordHasher y cambias una línea en Main. El resto del sistema no se entera.

verifyPassword está bien aquí porque la verificación es parte de la misma estrategia de hasheo: quien sabe cómo hashear, sabe cómo comparar.


## RepositoryLog.java


Esta interfaz define el contrato de persistencia de logs. Tiene dos responsabilidades:
saveLog(String message) → alguien le dice "guarda esto" y ella lo guarda donde sea que viva la implementación.
readAllLogs() → alguien le dice "dame todo lo que tienes guardado" y ella devuelve la lista.
El que llama a estos métodos nunca sabe si hay un archivo, una base de datos, o simplemente un ArrayList en memoria. Eso es exactamente lo que busca el patrón Repository: esconder dónde y cómo se guardan los datos.


## UserRepository.java

Contrato del repositorio de usuarios. Define las cuatro operaciones que el sistema necesita sobre usuarios: guardar, buscar por ID, buscar por nombre, y listar todos.

Optional<user) en los métodos de búsqueda es la forma correcta de manejar "puede que no exista": evita devolver null y obliga a quien usa el repositorio a manejar el caso de ausencia explícitamente.
InMemoryUserRepository implementa esto en infrastructure/persistence.
