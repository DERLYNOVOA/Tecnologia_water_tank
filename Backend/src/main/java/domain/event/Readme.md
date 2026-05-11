# **domain/event**

Todo lo que vive aquí define el lenguaje de los eventos del sistema. Es el vocabulario que usan los sensores para comunicarse con el resto del sistema sin conocerse entre sí. No tiene lógica de negocio, no tiene hardware, no tiene base de datos, solo *define cómo se hablan las piezas.*


## EventHandler.java

***(controlador de eventos)***

Es el centro de mensajes del sistema. Piénsalo como un tablero de anuncios: alguien publica algo, y todos los que están suscritos se enteran.
Tiene tres responsabilidades únicas:

* subscribe() → alguien dice "quiero enterarme cuando pase algo"
* unsubscribe() → alguien dice "ya no me avises"
* emitEvent() → alguien publica un evento y EventHandler lo reparte a todos los suscritos

La razón de usar CopyOnWriteArrayList en lugar de un ArrayList normal es que los sensores corren en hilos separados (threads). Si un sensor emite un evento mientras otro hilo modifica la lista de listeners al mismo tiempo, con ArrayList el programa crashea. CopyOnWriteArrayList hace una copia de la lista cada vez que se modifica, entonces el hilo que está emitiendo siempre lee una lista estable.


## RoleType.java

***(tipo de rol)***

Define qué tipos de usuario existen en el sistema. Actualmente ADMIN y USER.

Vive aquí como enum porque un rol es parte del vocabulario del dominio, es una regla de negocio: el sistema distingue entre quién puede hacer qué. No es infraestructura, no es UI, es una definición pura de dominio.

La razón de ser enum y no un String es seguridad en tiempo de compilación. Si escribes RoleType.ADMON el compilador te lo dice de inmediato. Si fuera un String "ADMON" el error solo aparece en ejecución.



## SensorTypeEvent.java

***(Evento tipo sensor)***

Define qué tipos de eventos pueden existir en el sistema. Actualmente TemperatureEvent, WaterLevelEvent, y NullEvent.

NullEvent es interesante: es un valor seguro para cuando no hay evento real que reportar, evita trabajar con null y que el sistema explote con un NullPointerException.

Igual que RoleType, vive aquí porque es vocabulario del dominio. Cuando un sensor crea un Event, necesita decir de qué tipo es, y este enum es el catálogo oficial de tipos posibles. Si mañana agregas un sensor de pH, solo añades PhEvent aquí y el resto del sistema puede reaccionar a él sin modificar nada más.
