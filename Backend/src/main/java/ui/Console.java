package ui;

import Domain.IWaterSystemStatus;
import Services.AppContext;
import Services.CommandHandler;
import java.util.Scanner;

public class Console {

    // ── ANSI ──────────────────────────────────────────────
    private static final String R  = "\u001B[0m";
    private static final String B  = "\u001B[1m";
    private static final String CY = "\u001B[36m";
    private static final String GR = "\u001B[32m";
    private static final String RD = "\u001B[31m";
    private static final String YE = "\u001B[33m";
    private static final String BL = "\u001B[34m";
    private static final String WH = "\u001B[97m";
    private static final String DM = "\u001B[90m";  // gris oscuro

    private static final int INNER = 46;

    // ── Mapeo numérico por rol ─────────────────────────────
    // índice 0 = opción "1", etc.
    private static final String[] GUEST_CMDS  = {"login",  "exit"};
    private static final String[] GUEST_DESC  = {"Iniciar sesión", "Salir del sistema"};

    private static final String[] ADMIN_CMDS = {
            "ver_nivel", "prender_bomba", "apagar_bomba", "logs", "logout", "exit"
    };
    private static final String[] ADMIN_DESC = {
            "Ver estado del sensor",
            "Activar bomba manualmente",
            "Desactivar bomba manualmente",   // ← nuevo
            "Historial de eventos",
            "Cerrar sesión",
            "Salir del sistema"
    };

    private final CommandHandler     commandHandler;
    private final AppContext         context;
    private final IWaterSystemStatus status;
    private final Scanner            scanner;

    public Console(CommandHandler commandHandler, AppContext context, IWaterSystemStatus status) {
        this.commandHandler = commandHandler;
        this.context        = context;
        this.status         = status;
        this.scanner        = new Scanner(System.in);
    }

    // ══════════════════════════════════════════════════════
    // CICLO PRINCIPAL
    // ══════════════════════════════════════════════════════
    public void start() {
        printBanner();
        while (true) {
            printDashboard();
            System.out.print(CY + B + "  WaterTank" + R + " ❯ ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) continue;

            // Resolvemos número → comando real
            String command = resolveInput(input);

            if (command.equalsIgnoreCase("exit")) break;

            if (command.equalsIgnoreCase("logout")) {
                context.getAuth().logout();
                printAlert(YE, "👋", "Sesión cerrada correctamente.");
                pause(600);
                continue;
            }

            String[] parts = command.split(" ", 2);
            String cmd = parts[0];
            String arg = parts.length > 1 ? parts[1] : "";
            try {
                commandHandler.execute(cmd, arg);
            } catch (Exception e) {
                if (!cmd.isEmpty())
                    printAlert(RD, "✖", "Opción inválida. Elige un número del menú.");
            }
        }
    }

    /**
     * Si el input es un número válido del menú actual, devuelve el comando.
     * Si no, lo trata como texto directo (compatibilidad).
     */
    private String resolveInput(String input) {
        boolean loggedIn = context.getAuth().getCurrentUser() != null;
        String[] cmds    = loggedIn ? ADMIN_CMDS : GUEST_CMDS;

        try {
            int idx = Integer.parseInt(input) - 1; // "1" → índice 0
            if (idx >= 0 && idx < cmds.length) {
                return cmds[idx];
            }
        } catch (NumberFormatException ignored) {}

        return input;
    }

    // ══════════════════════════════════════════════════════
    // BANNER
    // ══════════════════════════════════════════════════════
    private void printBanner() {
        System.out.println();
        System.out.println(BL + B +
                "  ██╗    ██╗ █████╗ ████████╗███████╗██████╗ \n" +
                "  ██║    ██║██╔══██╗╚══██╔══╝██╔════╝██╔══██╗\n" +
                "  ██║ █╗ ██║███████║   ██║   █████╗  ██████╔╝\n" +
                "  ██║███╗██║██╔══██║   ██║   ██╔══╝  ██╔══██╗\n" +
                "  ╚███╔███╔╝██║  ██║   ██║   ███████╗██║  ██║\n" +
                "   ╚══╝╚══╝ ╚═╝  ╚═╝   ╚═╝   ╚══════╝╚═╝  ╚═╝" + R);
        System.out.println(CY + B +
                "  ████████╗ █████╗ ███╗   ██╗██╗  ██╗\n" +
                "  ╚══██╔══╝██╔══██╗████╗  ██║██║ ██╔╝\n" +
                "     ██║   ███████║██╔██╗ ██║█████╔╝ \n" +
                "     ██║   ██╔══██║██║╚██╗██║██╔═██╗ \n" +
                "     ██║   ██║  ██║██║ ╚████║██║  ██╗\n" +
                "     ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝" + R);
        System.out.println(WH + "       Water Level Control System  v1.0  🌊" + R);
        System.out.println();
    }

    // ══════════════════════════════════════════════════════
    // DASHBOARD
    // ══════════════════════════════════════════════════════
    private void printDashboard() {
        System.out.println();
        printTop("🌊  WATER TANK  —  PANEL DE CONTROL");
        printSeparator();
        if (context.getAuth().getCurrentUser() != null) {
            printLoggedInPanel();
        } else {
            printGuestPanel();
        }
        printBottom();
        System.out.println();
    }

    private void printLoggedInPanel() {
        float   dist   = status.getCurrentDistance();
        float   pct    = status.getCurrentPercentage();
        boolean active = status.isPumpActive();
        String  user   = context.getAuth().getCurrentUser().getUserName();
        String  role   = context.getAuth().getCurrentUser().getRole().toString();

        printRow("  👤 " + YE + B + user + R + "  [" + WH + role + R + "]");
        printSeparator();

        printRow("  📡 Distancia   :  " + WH + B + String.format("%.1f cm", dist) + R);

        String lvlColor = pct >= 80 ? RD : pct >= 40 ? BL : GR;
        printRow("  💧 Nivel       :  " + lvlColor + B + String.format("%.1f %%", pct) + R);
        printRow("     " + CY + "[" + R + buildBar(pct, 30, lvlColor) + CY + "]" + R);

        String pumpTxt = active
                ? GR + "●  ACTIVA  " + R + GR + "⚡" + R
                : RD + "○  INACTIVA" + R;
        printRow("  🔧 Bomba       :  " + pumpTxt);

        printSeparator();
        printRow("  " + WH + B + "OPCIONES" + R);
        printSeparator();

        // ← numerado
        for (int i = 0; i < ADMIN_CMDS.length; i++) {
            printNumberedCmd(i + 1, ADMIN_CMDS[i], ADMIN_DESC[i]);
        }
    }

    private void printGuestPanel() {
        printRow("  " + RD + "🔒  ACCESO RESTRINGIDO" + R);
        printRow("  Inicie sesión para operar el sistema.");
        printSeparator();
        printRow("  " + WH + B + "OPCIONES" + R);
        printSeparator();

        for (int i = 0; i < GUEST_CMDS.length; i++) {
            printNumberedCmd(i + 1, GUEST_CMDS[i], GUEST_DESC[i]);
        }
    }

    // ══════════════════════════════════════════════════════
    // UTILIDADES DE DIBUJO
    // ══════════════════════════════════════════════════════
    private void printTop(String title) {
        System.out.println(CY + "  ╔" + "═".repeat(INNER) + "╗" + R);
        printRow("  " + B + WH + centerText(title, INNER - 2) + R);
    }

    private void printSeparator() {
        System.out.println(CY + "  ╠" + "═".repeat(INNER) + "╣" + R);
    }

    private void printBottom() {
        System.out.println(CY + "  ╚" + "═".repeat(INNER) + "╝" + R);
    }

    private void printRow(String content) {
        String visible = content.replaceAll("\u001B\\[[;\\d]*m", "");
        int pad = INNER - visible.length() - 1;
        System.out.println(CY + "  ║" + R + content + pad(Math.max(0, pad)) + CY + "║" + R);
    }

    /** Fila numerada: "  [1] ver_nivel      Ver estado..." */
    private void printNumberedCmd(int n, String cmd, String desc) {
        String line = "  " + DM + "[" + R + YE + B + n + R + DM + "]" + R
                + " " + CY + String.format("%-16s", cmd) + R
                + WH + desc + R;
        printRow(line);
    }

    private String buildBar(float pct, int width, String color) {
        int filled = Math.min((int)((pct / 100f) * width), width);
        return color + B + "█".repeat(filled) + R
                + DM + "░".repeat(width - filled) + R;
    }

    private String centerText(String text, int w) {
        int pad  = Math.max(0, w - text.length());
        int left = pad / 2;
        return " ".repeat(left) + text + " ".repeat(pad - left);
    }

    private String pad(int n) { return " ".repeat(n); }

    private void printAlert(String color, String icon, String msg) {
        System.out.println("\n  " + color + icon + "  " + msg + R);
    }

    private void pause(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}

