# domain/model

Esta carpeta contiene las entidades y objetos del mundo real que el sistema modela. No hay lógica de infraestructura, no hay base de datos, no hay consola. Solo las cosas que existen en el dominio del problema: un tanque, un sensor, un usuario, una alarma.


## Alarm.java

Representa la alarma del sistema. Implementa IAlarm (su interfaz en domain.service), lo cual está bien aplicado: el dominio define la interfaz, y aquí vive la implementación concreta del objeto de dominio.

Tiene dos responsabilidades: saber si está activa y tener un volumen. El constructor la inicializa apagada y en volumen 0, que es el estado lógico de arranque.


## Credential.java

Es un Value Object, no una entidad. La diferencia importante es que no tiene identidad propia (no tiene UUID), solo encapsula datos sensibles de autenticación: el hash de la contraseña, el salt, y cuándo fue cambiada por última vez.

Separar esto de User es una decisión de diseño muy buena. User sabe tu nombre y tu rol. Credential sabe tu contraseña hasheada. Si alguien accede a User no necesariamente necesita ver los datos de seguridad.

Observación pendiente del análisis: el salt existe en el objeto pero SimplePasswordHasher no lo está usando al momento de hashear. En producción eso sería un problema de seguridad real, aunque para el proyecto académico está bien.


## Event.java

Es el mensaje que viaja por el sistema cuando algo ocurre. Tiene cuatro datos:

* id → UUID único para identificar cada evento irrepetible
* type → qué tipo de evento es (usando el enum SensorTypeEvent)
* detail → texto libre con el valor leído, ej. "Temperature: 37.5"
* timestamp → cuándo ocurrió exactamente

Es inmutable en la práctica (los setters existen pero no deberían usarse después de construirlo). Es el sobre que los sensores le entregan al EventHandler para que lo reparta.



## Sensor.java

Es la clase abstracta base de todos los sensores. Define qué tienen en común todos los sensores del sistema sin importar qué miden.
Todo sensor tiene:

* isActive → si está encendido y debe emitir eventos
* handler → el EventHandler al que le reporta cuando detecta algo
* handleSensor() → abstracto, cada sensor hijo define cómo construye y emite su evento

Punto positivo respecto al análisis anterior: isActive y handler ahora son private con getters, lo cual protege el encapsulamiento. Las clases hijas acceden a ellos correctamente por los métodos heredados.



## TemperatureSensor.java y WaterLevelSensor.java

Ambos extienden Sensor y siguen la misma estructura: guardan su valor actual, cuando ese valor se actualiza via setter llaman a handleSensor(), y handleSensor() construye un Event y lo emite por el EventHandler.

Problema activo: el handleSensor() de ambos es prácticamente idéntico, solo cambia el SensorTypeEvent y el texto del detalle. El análisis recomendó el patrón Template Method para eliminar esta repetición. Aún no se ha aplicado. Te lo marco al final.

Observación en WaterLevelSensor: el método hasData() hace return waterLevel >= 0, pero waterLevel se inicializa en 0.0f, entonces siempre devuelve true desde el inicio aunque el sensor no haya recibido ningún dato real. Probablemente debería ser waterLevel > 0.


## User.java

Entidad principal del sistema de autenticación. Tiene identidad propia con UUID, nombre de usuario, rol, estado activo, y su Credential.
La composición User → Credential está correcta. 


## WaterTank.java

Clase con una sola responsabilidad muy clara: dado que conoce la altura total del tanque, puede calcular qué porcentaje está lleno a partir de la distancia que reporta el sensor ultrasónico.

La lógica es: si la distancia medida es grande, hay poco agua. Si es pequeña, hay mucha. (alturaTotal - distanciaActual) / alturaTotal * 100.
