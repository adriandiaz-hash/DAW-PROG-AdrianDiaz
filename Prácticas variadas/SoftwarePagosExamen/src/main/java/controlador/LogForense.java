package controlador;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Motor de persistencia para registrar eventos del sistema.
 * @autor [T] Adrián Díaz García
 */
public class LogForense {

    // Archivo donde se guardarán las pruebas físicas
    private static final String ARCHIVO_LOG = "auditoria_sistema.log";

    /**
     * Escribe un evento en el archivo de registro.
     * @param nivel "INFO", "ALERTA" o "CRÍTICO"
     * @param mensaje Detalle forense del evento
     */
    public static void registrarEvento(String nivel, String mensaje) {
        // Tema 9: Uso de FileWriter con el parámetro 'true' para modo APPEND (Añadir al final)
        // El bloque try-with-resources asegura que el archivo se libere y cierre automáticamente
        try (FileWriter fw = new FileWriter(ARCHIVO_LOG, true);
             PrintWriter pw = new PrintWriter(fw)) {
             
            String marcaTemporal = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            
            // Escribimos la línea en el disco físico
            pw.println("[" + marcaTemporal + "] [" + nivel + "] " + mensaje);
            
        } catch (IOException e) {
            // Si el disco falla (falta de permisos, disco lleno), mostramos el error
            System.out.println(Configuracion.ERROR_CRITICO + "Fallo de persistencia en disco: " + e.getMessage());
        }
    }
}