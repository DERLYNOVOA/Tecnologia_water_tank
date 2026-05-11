## InputProvider.java y OutputProvider.java

La idea viene de la arquitectura hexagonal (también llamada Ports & Adapters). En esa arquitectura, los "puertos de salida" (outbound) son contratos que la aplicación define para comunicarse con el mundo exterior: la UI, la consola, archivos, redes. La aplicación no depende de System.out.println, depende de OutputProvider. La implementación concreta de cómo se escribe en consola vive en infrastructure/external.


Antes Console.java tenia un Scanner creado directamente dentro suyo. Eso significa que Console estaba atada para siempre a System.in. Si mañana quisieras probar el menú automáticamente sin tocar el teclado, o si quisieras que el input viniera de un archivo de texto, tendrías que entrar a Console y modificarla. Eso viola OCP.
La solución es que Console no sepa de dónde viene el input ni a dónde va el output. Solo sabe que tiene algo que lee y algo que escribe, y esas cosas le llegan por constructor.

## Input

Define el contrato de lectura. Cualquier clase que necesite leer input del usuario depende de esta interfaz, no de Scanner.

Define un único contrato: dame la siguiente línea de texto que el usuario escribió. No dice cómo, no dice de dónde. Console llama a input.readLine() cada vez que necesita saber qué eligió el usuario, y no tiene idea de que por debajo hay un Scanner apuntando a System.in.


## Output

Define el contrato de escritura. Cubre los tres casos que usa la consola: imprimir sin salto, con salto, y con formato.

Tres métodos porque Console usa los tres estilos de escritura. print escribe sin salto de línea, lo usa para el prompt ❯. println escribe con salto, lo usa para las filas del panel. printf escribe con formato, lo usa para los valores numéricos como "%.1f cm". Separar los tres evita tener que hacer String.format manualmente antes de llamar al provider.
