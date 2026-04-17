package vista;

import controlador.CustomerDAO;
import controlador.PaymentDAO;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import modelo.Customer;
import modelo.Payment;
import controlador.Configuracion;

/**
 * Clase principal que gestiona la Vista (interfaz de usuario) y el menú de la aplicación.
 * @autor [T] Adrián Díaz García
 */
public class SoftwarePagosExamen {

    public static void main(String[] args) {
        
        // 1. INICIALIZACIÓN DE LOS AGENTES TÁCTICOS (DAOs)
        CustomerDAO customerDAO = new CustomerDAO(Configuracion.BD_URL, Configuracion.BD_USER, Configuracion.BD_PASS);
        PaymentDAO paymentDAO = new PaymentDAO(Configuracion.BD_URL, Configuracion.BD_USER, Configuracion.BD_PASS);

        Scanner scanner = new Scanner(System.in);
        int opcion = 0;

        System.out.println(Configuracion.FIRMA_SISTEMA);

        // 3. BUCLE PRINCIPAL DEL MENÚ
        do {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Top Clientes con más pagos");
            System.out.println("2. Generar informe HTML de cliente");
            System.out.println("3. Modificar pago");
            System.out.println("4. Auditoría por Matriz de Riesgos");
            System.out.println("5. Agrupar Pagos por Hash (Análisis de Volumen)");
            System.out.println("6. Salir");
            System.out.print("Seleccione una directiva: ");

            String entradaOpcion = scanner.nextLine();
            
            // VALIDACIÓN REGEX (Tema 6): Asegura que solo se introduce un número del 1 al 4
            if (!entradaOpcion.matches("[1-6]")) {
                System.out.println(Configuracion.ERROR_CRITICO + "Opción inválida. Usa la lógica.");
                continue; // Salta el resto del código y vuelve a iniciar el do-while
            }
            
            opcion = Integer.parseInt(entradaOpcion);
            
            try{
                switch (opcion) {
                    case 1: // TOP CLIENTES
                        System.out.print("¿Cuántos clientes desea en el ranking?: ");
                        int limite = scanner.nextInt();
                        scanner.nextLine();

                        // Llamada al DAO. Si falla, el catch de abajo lo atrapa.
                        ArrayList<Customer> topClientes = customerDAO.obtenerTopClientes(limite);
                        
                        System.out.println("\n--- RANKING TOP " + limite + " ---");
                        System.out.printf("%-10s %-30s %-10s %-15s\n", "ID", "NOMBRE", "PAGOS", "TOTAL (€)");
                        System.out.println("-------------------------------------------------------------------");
                        for (Customer c : topClientes) {
                            System.out.printf("%-10d %-30s %-10d %-15.2f\n", 
                                    c.getCustomerNumber(), c.getCustomerName(), 
                                    c.getNumberOfPayments(), c.getTotalPaid());
                        }
                        break;

                    case 2: // GENERAR INFORME HTML
                        System.out.print("Introduzca el ID del cliente (ej. 141): ");
                        String inputId = scanner.nextLine();

                        // OTRA VALIDACIÓN REGEX: Solo permite dígitos de 1 a 5 caracteres
                        if (!inputId.matches("\\d{1,5}")) {
                            System.out.println(Configuracion.ERROR_CRITICO + "ID con formato incorrecto. Se requiere identificador numérico.");
                            break; // Sale del switch
                        }
                        
                        int customerNumber = Integer.parseInt(inputId);
                        
                        Customer cliente = customerDAO.obtenerClientePorId(customerNumber);
                        
                        // Pequeño escudo extra: ¿Y si el cliente no existe en la BBDD?
                        if (cliente == null) {
                            System.out.println(Configuracion.ERROR_CRITICO + "El cliente " + customerNumber + " no existe en los registros.");
                            break;
                        }

                        ArrayList<Payment> pagos = paymentDAO.obtenerPagosCliente(customerNumber);
                        double totalPagos = paymentDAO.obtenerSumaPagosCliente(customerNumber);

                        generarInformeHTML(cliente, pagos, totalPagos);
                        break;

                    case 3: // MODIFICAR PAGO
                        System.out.print("Introduzca el ID del cliente: ");
                        int idC = scanner.nextInt();
                        scanner.nextLine();
                        
                        System.out.print("Introduzca el número de cheque (ej. IN446258): ");
                        String check = scanner.nextLine();
                        
                        System.out.print("Introduzca la variación de importe (use negativo para restar): ");
                        double variacion = scanner.nextDouble();
                        scanner.nextLine();

                        boolean modificado = paymentDAO.modificarPago(idC, check, variacion);
                        if (modificado) {
                            System.out.println("[+] Operación exitosa. Pago actualizado en el servidor.");
                        } else {
                            System.out.println("[-] Error de actualización: Verifica que el cliente y el cheque existan.");
                        }
                        break;
                        
                    case 4:
                        ejecutarAuditoriaMatriz(); // Tema 6: Arrays y Matrices
                        break;
                        
                    case 5:
                        ejecutarAnalisisHashMap(paymentDAO, scanner); // Tema 8: HashMap
                        break;    

                    case 6: // SALIR
                        System.out.println("Cerrando sesión.[T].");
                        break;

                    default:
                        System.out.println("[!] Comando no reconocido.");
                    }

                } catch (Exception e) {
                String error = "Caída del sistema: " + e.getMessage();
                System.out.println(Configuracion.ERROR_CRITICO + error);
                
                // [NUEVO] Escribimos el error crítico en la Caja Negra
                controlador.LogForense.registrarEvento("CRÍTICO", error);
            }

            } while (opcion != 6);
            
            System.out.println("[+] Conexiones cerradas. Praise the Sun.");
            scanner.close();
        }

    /**
     * Genera el archivo HTML estructurado según las especificaciones de la Opción A.
     */
    private static void generarInformeHTML(Customer cliente, ArrayList<Payment> pagos, double total) {
        String nombreArchivo = "informeAdrianDiaz.html";
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
            
            // Cabecera HTML y CSS incrustado
            writer.println("<!DOCTYPE html>");
            writer.println("<html lang='es'>");
            writer.println("<head><meta charset='UTF-8'><title>Informe de Pagos - " + cliente.getCustomerName() + "</title>");
            writer.println("<style>");
            writer.println("body { font-family: 'Consolas', monospace; background-color: #f4f4f4; color: #333; padding: 20px; }");
            writer.println(".container { background-color: #fff; padding: 30px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); max-width: 800px; margin: auto; }");
            writer.println("h1 { color: #2c3e50; border-bottom: 2px solid #e74c3c; padding-bottom: 10px; }");
            writer.println("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
            writer.println("th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }");
            writer.println("th { background-color: #2c3e50; color: white; }");
            writer.println(".total { font-weight: bold; font-size: 1.2em; background-color: #e8f8f5; }");
            writer.println(".footer { margin-top: 40px; font-size: 0.9em; color: #7f8c8d; text-align: right; border-top: 1px solid #ccc; padding-top: 10px; }");
            writer.println("</style></head>");
            
            // Cuerpo del documento
            writer.println("<body><div class='container'>");
            writer.println("<h1>Informe Confidencial de Cliente</h1>");
            writer.println("<p><strong>ID Cliente:</strong> " + cliente.getCustomerNumber() + "</p>");
            writer.println("<p><strong>Nombre Corporativo:</strong> " + cliente.getCustomerName() + "</p>");
            
            // Tabla de pagos
            writer.println("<table>");
            writer.println("<tr><th>Check Number</th><th>Payment Date</th><th>Amount (€)</th></tr>");
            for (Payment p : pagos) {
                writer.println("<tr>");
                writer.println("<td>" + p.getCheckNumber() + "</td>");
                writer.println("<td>" + p.getPaymentDate() + "</td>");
                writer.println("<td>" + String.format("%.2f", p.getAmount()) + "</td>");
                writer.println("</tr>");
            }
            
            // Fila de Suma Total (Última línea)
            writer.println("<tr class='total'><td colspan='2' style='text-align: right;'>SUMA TOTAL:</td>");
            writer.println("<td>" + String.format("%.2f", total) + "</td></tr>");
            writer.println("</table>");
            
            // Pie de página (Nombre y Hora temporal)
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            writer.println("<div class='footer'>");
            writer.println("<p>Generado por: Adrián Díaz García [T]</p>");
            writer.println("<p>Fecha de emisión: " + dtf.format(now) + "</p>");
            writer.println("</div>");
            
            // Cierre
            writer.println("</div></body></html>");
            
            System.out.println("[+] Informe compilado y guardado en la raíz del proyecto como: " + nombreArchivo);
            
        } catch (IOException e) {
            System.out.println("[-] Error crítico al escribir el archivo en el disco: " + e.getMessage());
        }
    }
    
    private static void ejecutarAuditoriaMatriz() {
        System.out.println("\n[*] Escaneando matriz de transacciones...");
        // Matriz estática (Tema 6)
        double[][] matriz = {
            {20.5, 500.0, 10.0},
            {6000.5, 120.0, 45.0},
            {15.0, 8000.0, 99.0}
        };
        
        int f = 0;
        int alertas = 0;
        while (f < matriz.length) { // Recorrido de matriz [cite: 644]
            for (int c = 0; c < matriz[f].length; c++) {
                if (matriz[f][c] > 1000) {
                    System.out.println("    [!] ALERTA en ["+f+"]["+c+"]: " + matriz[f][c] + "€ detectados.");
                    alertas++;
                    
                    controlador.LogForense.registrarEvento("ALERTA", "Pago anómalo de " + matriz[f][c] + "€ en el sector ["+f+"]["+c+"]");
                    alertas++;
                }
            }
            f++;
        }
        System.out.println("[+] Escaneo completo. Alertas totales: " + alertas);
    }
    
    private static void ejecutarAnalisisHashMap(controlador.PaymentDAO dao, java.util.Scanner scanner) throws java.sql.SQLException {
        System.out.print("[*] Introduzca ID cliente para análisis de volumen: ");
        
        // Leemos la línea y usamos trim() para limpiar saltos de línea basura
        String inputId = scanner.nextLine().trim(); 
        
        // Escudo 1: ¿Se coló un texto vacío?
        if (inputId.isEmpty()) {
            System.out.println(Configuracion.ERROR_CRITICO + "Entrada vacía. Análisis abortado para proteger el sistema.");
            return; // Corta la ejecución del método y vuelve al menú
        }
        
        // Escudo 2: ¿Son letras en lugar de números? (Regex Tema 6)
        if (!inputId.matches("\\d+")) {
            System.out.println(Configuracion.ERROR_CRITICO + "El ID debe ser estrictamente numérico.");
            return;
        }
        
        // Si superamos los escudos, es 100% seguro convertir a int
        int id = Integer.parseInt(inputId);
        
        java.util.ArrayList<modelo.Payment> pagos = dao.obtenerPagosCliente(id);
        
        if (pagos.isEmpty()) {
            System.out.println("[-] No hay pagos registrados para el cliente " + id);
            return;
        }

        // Creamos un HashMap para agrupar pagos por año (Tema 8)
        java.util.HashMap<String, Integer> contadorPorAnio = new java.util.HashMap<>();
        
        for (modelo.Payment p : pagos) {
            String anio = p.getPaymentDate().toString().substring(0, 4); 
            contadorPorAnio.put(anio, contadorPorAnio.getOrDefault(anio, 0) + 1);
        }
        
        System.out.println("[+] Resultado del análisis de volumen:");
        for (String anio : contadorPorAnio.keySet()) { 
            System.out.println("    - Año " + anio + ": " + contadorPorAnio.get(anio) + " transacciones.");
        }
    }
}