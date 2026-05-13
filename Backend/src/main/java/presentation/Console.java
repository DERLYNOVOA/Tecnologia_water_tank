package presentation;

import application.port.outbound.InputProvider;
import application.port.outbound.OutputProvider;
import domain.service.IWaterSystemStatus;
import domain.model.TemperatureSensor;
import application.service.AppContext;
import application.command.CommandHandler;
import infrastructure.hardware.ArduinoSerial;

import java.util.ArrayList;
import java.util.List;

public class Console {


    private static final String R  = "\u001B[0m", B = "\u001B[1m", CY = "\u001B[36m", GR = "\u001B[32m";
    private static final String RD = "\u001B[31m", YE = "\u001B[33m", BL = "\u001B[34m", WH = "\u001B[97m", DM = "\u001B[90m";

    private final CommandHandler commandHandler;
    private final AppContext context;
    private final IWaterSystemStatus status;
    private final TemperatureSensor tempSensor;
    private final ArduinoSerial hardware; // Para los comandos debug
    private final InputProvider input;
    private final OutputProvider output;


    private boolean debugMode = false;
    private boolean autoClear = true;

    public Console(CommandHandler ch, AppContext ctx, IWaterSystemStatus st, TemperatureSensor ts,
                   ArduinoSerial hw, InputProvider in, OutputProvider out) {
        this.commandHandler = ch; this.context = ctx; this.status = st;
        this.tempSensor = ts; this.hardware = hw; this.input = in; this.output = out;
    }

    public void start() {
        while (true) {
            if (autoClear) clearScreen();
            printDashboard();
            output.print(CY + B + (debugMode ? " [DEBUG]" : "") + "  WaterTank" + R + " > ");

            String rawInput = input.readLine().trim().toLowerCase();
            if (rawInput.isEmpty()) continue;


            if (rawInput.equals("debug")) {
                debugMode = !debugMode;
                continue;
            }


            if (debugMode && handleDebugCommands(rawInput)) continue;

            if (rawInput.equals("exit")) break;
            executeStandardCommand(rawInput);
        }
    }

    private boolean handleDebugCommands(String cmd) {
        switch (cmd) {
            case "refresh": autoClear = !autoClear; return true;
            case "led_t_on":  hardware.enviarComando("Y1"); return true;
            case "led_t_off": hardware.enviarComando("Y0"); return true;
            case "led_e_on":  hardware.enviarComando("E1"); return true;
            case "led_e_off": hardware.enviarComando("E0"); return true;
            case "led_a_on":  hardware.enviarComando("B1"); return true;
            case "led_a_off": hardware.enviarComando("B0"); return true;
            default: return false;
        }
    }

    private void executeStandardCommand(String rawInput) {
        if (rawInput.equals("logout")) {
            context.getAuth().logout();
            return;
        }
        try {
            String command = resolveInput(rawInput);
            String[] parts = command.split(" ", 2);
            commandHandler.execute(parts[0], parts.length > 1 ? parts[1] : "");
        } catch (Exception e) { printAlert(RD, "X", "Error."); }
    }

    private void printDashboard() {
        int LW = 38, RW = 46;
        output.println("\n" + CY + "  ╔" + "═".repeat(LW) + "╦" + "═".repeat(RW) + "╗" + R);
        printDualRow(B + WH + center("ESTADO", LW) + R, B + WH + center("MENÚ", RW) + R, LW, RW);
        output.println(CY + "  ╠" + "═".repeat(LW) + "╬" + "═".repeat(RW) + "╣" + R);

        List<String> L = new ArrayList<>(), R_CONTENT = new ArrayList<>();
        fillStatus(L);
        fillMenu(R_CONTENT);

        for (int i = 0; i < Math.max(L.size(), R_CONTENT.size()); i++) {
            printDualRow(" " + (i < L.size() ? L.get(i) : ""), " " + (i < R_CONTENT.size() ? R_CONTENT.get(i) : ""), LW, RW);
        }
        output.println(CY + "  ╚" + "═".repeat(LW) + "╩" + "═".repeat(RW) + "╝" + R);
    }

    private void fillStatus(List<String> l) {
        if (context.getAuth().getCurrentUser() == null) {
            l.add(RD + B + "BLOQUEADO" + R); return;
        }
        l.add(DM + "REFRESH   :" + R + (autoClear ? GR + " AUTO" : YE + " MANUAL") + R);
        l.add(DM + "NIVEL     :" + R + String.format(" %.1f%%", status.getCurrentPercentage()));
        l.add("  " + buildBar(status.getCurrentPercentage(), 26));
        l.add(DM + "TEMP      :" + R + String.format(" %.1f°C", tempSensor.getTemperature()));
    }

    private void fillMenu(List<String> r) {
        boolean logged = context.getAuth().getCurrentUser() != null;
        if (!logged) {
            r.add(opt(1, "login", "Entrar")); r.add(opt(2, "exit", "Salir"));
        } else {
            r.add(opt(1, "ver_nivel", "Estado"));
            r.add(opt(2, "logs", "Historial"));
            r.add(opt(3, "logout", "Cerrar Sesion"));
            if (debugMode) {
                r.add(CY + "  --- MODO DEBUG ---" + R);
                r.add(opt(9, "refresh", "Toggle Limpiar Pantalla"));
                r.add(opt(10, "led_t_on/off", "Control Led Temp (Y)"));
                r.add(opt(11, "led_e_on/off", "Control Led Error (E)"));
                r.add(opt(12, "led_a_on/off", "Control Led Agua (B)"));
            }
        }
    }

    private String opt(int n, String c, String d) {
        return DM + "[" + YE + n + DM + "]" + CY + String.format(" %-12s ", c) + WH + d + R;
    }

    private void printDualRow(String left, String right, int lw, int rw) {
        output.println(CY + "  ║" + R + pad(left, lw) + CY + "║" + R + pad(right, rw) + CY + "║" + R);
    }

    private String buildBar(float p, int w) {
        int f = (int)((p/100)*w);
        return BL + "█".repeat(f) + DM + "░".repeat(w-f) + R;
    }

    private void clearScreen() { output.print("\033[H\033[2J"); System.out.flush(); }
    private String pad(String t, int w) {
        int vis = t.replaceAll("\u001B\\[[;\\d]*m", "").length();
        return t + " ".repeat(Math.max(0, w - vis));
    }
    private String center(String t, int w) {
        int vis = t.replaceAll("\u001B\\[[;\\d]*m", "").length();
        int p = (w - vis) / 2;
        return " ".repeat(p) + t + " ".repeat(w - vis - p);
    }
    private void printAlert(String c, String i, String m) { output.println("\n  " + c + "[" + i + "] " + m + R); }
    private String resolveInput(String r) {
        if (r.equals("1")) return "ver_nivel"; if (r.equals("2")) return "logs"; if (r.equals("3")) return "logout";
        return r;
    }
}