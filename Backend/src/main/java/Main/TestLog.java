package Main;

import Repository.FileLogRepository;
import Repository.IRepositoryLog;

public class TestLog {
    public static void main(String[] args) {
        IRepositoryLog log = new FileLogRepository();

        log.saveLog("🟢 Sistema iniciado");
        log.saveLog("💧 Bomba encendida - Nivel 12%");
        log.saveLog("🔴 Bomba apagada - Nivel 95%");

        System.out.println("✅ Revisa el archivo historial_tanque.txt en la raíz del proyecto");
    }
}