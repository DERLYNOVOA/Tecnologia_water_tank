# application/service


Aquí vive la lógica de aplicación: qué hace el sistema, cómo coordina sus piezas, cómo autentica, cómo reacciona a los eventos de los sensores. No tiene UI, no tiene hardware, no lee archivos directamente. Es el cerebro del sistema.



## AppContext.java

Es una Fachada (Facade). En lugar de inyectar Authenticator, EventHandler y RepositoryLog por separado en cada comando, se agrupan en un solo objeto que viaja con todos los comandos. Esto reduce la cantidad de parámetros y centraliza las dependencias transversales del sistema.


## AuthenticationService.java

Implementa la lógica pura de autenticación. Recibe por constructor UserRepository y PasswordHasher (ambas interfaces), nunca implementaciones concretas. Eso es Inversión de Dependencias (DIP) aplicada correctamente.
El flujo es claro: busca el usuario, verifica que esté activo, verifica que la contraseña coincida con el hash. Si todo pasa, devuelve el usuario dentro de un Optional.


## Authenticator.java

Gestiona la sesión activa. Mientras AuthenticationService solo verifica credenciales sin guardar estado, Authenticator recuerda quién está logueado en currentUser. Separa las dos responsabilidades correctamente: verificar identidad vs. mantener sesión.


## AuthorizationService.java

Verifica si un usuario tiene permiso para hacer algo.


## SensorLevelManager.java

Es la clase padre de todos los gestores de sensores del sistema. Existe porque WaterLevelManager y TemperatureManager comparten cosas en común: ambos tienen un nivel mínimo y máximo que definen cuándo algo está fuera de rango, y ambos tienen un estado activo/inactivo.

En lugar de que cada uno tenga esos tres atributos repetidos, el padre los centraliza. Así, si mañana agregas un tercer gestor (por ejemplo para pH), también hereda esos atributos sin repetir código.

El método run() es abstracto porque el padre no sabe qué debe pasar cuando el nivel sale de rango. Eso depende de cada hijo: el de agua enciende la bomba, el de temperatura activa la alarma. El padre solo establece que todos los hijos deben tener ese método.


## TemperatureManager.java

Extiende SensorLevelManager y también implementa EventListener. Eso significa que está suscrito al EventHandler y reacciona automáticamente cuando llega un evento.

La comunicación completa es: Arduino manda una temperatura → ArduinoSerial la recibe → se la pasa a TemperatureSensor → TemperatureSensor emite un Event por el EventHandler → EventHandler llama a onEvent() en todos sus suscriptores → TemperatureManager.onEvent() recibe el evento y llama a run().

Dentro de run() pasan dos cosas importantes. Primero pregunta al IWaterSystemStatus (que es WaterLevelManager) cuánta agua hay. Si no hay agua, no tiene sentido vigilar la temperatura del agua porque no existe. Segundo, si hay poca agua (menos del 30%), el umbral máximo baja 5 grados porque poca agua se calienta más rápido con el calentador de uñas. Si la temperatura supera ese umbral, activa la alarma a través de IAlarm. Si vuelve a rango normal, la apaga.

*Nota importante: TemperatureManager no se suscribe automáticamente al EventHandler en su constructor como sí lo hace WaterLevelManager. Eso significa que hay que suscribirlo manualmente en Main.java con handler.subscribe(temperatureManager).*


## WaterLevelManager.java

Es la clase más completa del sistema. Hace tres cosas a la vez gracias a las interfaces que implementa:
Como SensorLevelManager tiene los umbrales de nivel (20% mínimo, 80% máximo heredados del padre, aunque internamente usa 10% y 90% en la lógica de run()).

Como EventListener está suscrito al EventHandler. Cuando el sensor ultrasónico manda una nueva distancia, WaterLevelSensor emite un evento, EventHandler lo distribuye, y WaterLevelManager.onEvent() recibe ese llamado y ejecuta run().

Como IWaterSystemStatus expone el estado del agua de solo lectura hacia afuera. La UI y TemperatureManager llaman getCurrentPercentage(), isPumpActive() y hasData() sin tener acceso directo al sensor ni al tanque.

Dentro de run() la lógica es: verifica que haya datos reales del sensor, calcula el porcentaje con el tanque, y actúa solo si la condición cambia realmente. Si el porcentaje llega al 90% y la bomba estaba encendida, la apaga y lo registra en el log. Si baja al 10% y la bomba estaba apagada, la enciende y lo registra. No actúa si la bomba ya está en el estado correcto, para evitar comandos redundantes al Arduino.

El constructor recibe el EventHandler y llama handler.subscribe(this) inmediatamente, así desde el momento en que existe ya está escuchando eventos.
