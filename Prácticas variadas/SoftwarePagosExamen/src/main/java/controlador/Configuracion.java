package controlador;

/**
 * Almacén central de constantes del sistema.
 * @autor [T] Adrián Díaz García
 */
public class Configuracion {
    // El uso de 'public static final' crea una constante global (Tema 5)
    public static final String BD_URL = "jdbc:mysql://localhost:3306/classicmodels";
    public static final String BD_USER = "Adrian";
    public static final String BD_PASS = "15Octubre*";
    
    public static final String FIRMA_SISTEMA = "[T] Sistema Táctico Casa Díaz - Operativo";
    public static final String ERROR_CRITICO = "[-] Brecha detectada: ";
}