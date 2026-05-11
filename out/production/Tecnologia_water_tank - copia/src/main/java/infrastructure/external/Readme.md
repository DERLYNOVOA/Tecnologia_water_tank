## ConsoleInputProvider

única clase del sistema que tiene un Scanner. Encapsula toda la lectura de consola. Si mañana quieres leer de un archivo de pruebas en lugar de teclado, creas FileInputProvider implements InputProvider y no tocas nada más.

Esta es la única clase del sistema que tiene un Scanner. Cuando alguien llama readLine(), internamente hace scanner.nextLine() que bloquea el hilo hasta que el usuario presiona Enter y devuelve lo que escribió. El Scanner se crea una sola vez y vive todo el tiempo que viva este objeto. Nadie más en el sistema sabe que existe un Scanner.


## ConsoleOutputProvider

Encapsula todo System.out. Si en el futuro quieres que la salida vaya a un archivo de log o a una interfaz gráfica, cambias la implementación sin tocar Console.java.

Cada método delega directamente a System.out. El valor no es que haga algo distinto, sino que centraliza ese System.out aquí. Si mañana quieres que todo lo que se muestra en pantalla también se guarde en un archivo de log, solo modificas esta clase y agregas un fileWriter.write(message) junto al System.out. El resto del sistema no se entera del cambio.
