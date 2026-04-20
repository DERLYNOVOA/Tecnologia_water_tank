package Repository;

/**
 * Interfaz para persistencia de logs del sistema.
 * Aplicando Bajo Acoplamiento: los Managers no conocen
 * cómo se guarda el log (archivo, BD, etc.)
 */
public interface IRepositoryLog {
    /**
     * Guarda un mensaje de log con timestamp
     * @param message Mensaje a persistir (ej. "Bomba encendida - Nivel 15%")
     */
    void saveLog(String message);
}