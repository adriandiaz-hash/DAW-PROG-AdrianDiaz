package vista;

// [CONCEPTO: IMPORTACIONES] Traemos herramientas de otras bibliotecas.
// ArrayList y HashMap para estructuras de datos, Scanner para leer teclado.
// Clases de java.io para escribir archivos (nuestro reporte HTML).
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

    // [CONCEPTO: PUNTO DE ENTRADA] main es donde el compilador empieza a ejecutar la vida del programa.
    public static void main(String[] args) {
        
        // 1. INICIALIZACIÓN DE LOS AGENTES TÁCTICOS (DAOs)
        // [CONCEPTO: INSTANCIACIÓN DE OBJETOS] Creamos objetos de nuestras clases DAO pasándole las constantes.
        CustomerDAO customerDAO = new CustomerDAO(Configuracion.BD_URL, Configuracion.BD_USER, Configuracion.BD_PASS);
        PaymentDAO paymentDAO = new PaymentDAO(Configuracion.BD_URL, Configuracion.BD_USER, Configuracion.BD_PASS);

        // [CONCEPTO: LECTURA DE TECLADO] Scanner lee lo que el usuario aporrea en la consola.
        Scanner scanner = new Scanner(System.in);
        int opcion = 0;

        System.out.println(Configuracion.FIRMA_SISTEMA);

        // 3. BUCLE PRINCIPAL DEL MENÚ
        // [CONCEPTO: BUCLE DO-WHILE] Se ejecuta SIEMPRE al menos una vez, y luego comprueba la condición al final.
        // Ideal para menús: primero muestras las opciones, luego preguntas si quiere salir.
        do {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Top Clientes con más pagos");
            System.out.println("2. Generar informe HTML de cliente");
            System.out.println("3. Modificar pago");
            System.out.println("4. Auditoría por Matriz de Riesgos");
            System.out.println("5. Agrupar Pagos por Hash (Análisis de Volumen)");
            System.out.println("6. Salir");
            System.out.print("Seleccione una directiva: ");

            // Leemos como String primero para evitar el clásico bug de que el Scanner se salte líneas si leemos un int directamente.
            String entradaOpcion = scanner.nextLine();
            
            // [CONCEPTO: EXPRESIONES REGULARES - REGEX] (Tema 6)
            // matches("[1-6]") significa: "¿Es este String exactamente un solo carácter numérico entre el 1 y el 6?"
            // Si el usuario pone "Hola" o "7", entra en el if.
            if (!entradaOpcion.matches("[1-6]")) {
                System.out.println(Configuracion.ERROR_CRITICO + "Opción inválida. Usa la lógica.");
                // [CONCEPTO: CONTINUE] Corta la iteración actual del bucle aquí mismo y vuelve arriba (al 'do').
                continue; 
            }
            
            // [CONCEPTO: PARSEO] Convertimos el texto (String) validado a número entero (int) para usarlo en el switch.
            opcion = Integer.parseInt(entradaOpcion);
            
            // [CONCEPTO: MANEJO DE EXCEPCIONES] try-catch atrapa cualquier explosión (error) dentro del bloque try.
            // Si algo falla (ej. base de datos caída), no se cierra el programa de golpe, salta al catch.
            try{
                // [CONCEPTO: ESTRUCTURA DE CONTROL SWITCH] Evalúa una variable y salta al 'case' correspondiente.
                // Es más limpio y eficiente que poner veinte if-else encadenados.
                switch (opcion) {
                    case 1: // TOP CLIENTES
                        System.out.print("¿Cuántos clientes desea en el ranking?: ");
                        int limite = scanner.nextInt();
                        scanner.nextLine(); // [CONCEPTO: LIMPIEZA DE BUFFER] Consumimos el "Intro" que dejó nextInt()

                        // [CONCEPTO: ARRAYLIST] Estructura de datos dinámica. A diferencia de un Array normal, 
                        // puede crecer o encogerse. Aquí guarda objetos de tipo 'Customer'.
                        ArrayList<Customer> topClientes = customerDAO.obtenerTopClientes(limite);
                        
                        System.out.println("\n--- RANKING TOP " + limite + " ---");
                        // [CONCEPTO: SALIDA FORMATEADA] printf formatea el texto. %-10s = String alineado a la izquierda ocupando 10 espacios.
                        System.out.printf("%-10s %-30s %-10s %-15s\n", "ID", "NOMBRE", "PAGOS", "TOTAL (€)");
                        System.out.println("-------------------------------------------------------------------");
                        
                        // [CONCEPTO: BUCLE FOR-EACH] Recorre el ArrayList de principio a fin automáticamente.
                        // Se lee: "Por cada Customer 'c' en 'topClientes'..."
                        for (Customer c : topClientes) {
                            // %d es para enteros (decimal integer), %f para decimales (float/double). .2f limita a 2 decimales.
                            System.out.printf("%-10d %-30s %-10d %-15.2f\n", 
                                    c.getCustomerNumber(), c.getCustomerName(), 
                                    c.getNumberOfPayments(), c.getTotalPaid());
                        }
                        break; // [CONCEPTO: BREAK] Evita que la ejecución caiga en el 'case 2'.

                    case 2: // GENERAR INFORME HTML
                        System.out.print("Introduzca el ID del cliente (ej. 141): ");
                        String inputId = scanner.nextLine();

                        // [CONCEPTO: REGEX CUANTIFICADORES] "\\d{1,5}" = Busca de 1 a 5 dígitos consecutivos.
                        // \\d significa dígito. Sirve como escudo contra inyección de letras.
                        if (!inputId.matches("\\d{1,5}")) {
                            System.out.println(Configuracion.ERROR_CRITICO + "ID con formato incorrecto.");
                            break; 
                        }
                        
                        int customerNumber = Integer.parseInt(inputId);
                        Customer cliente = customerDAO.obtenerClientePorId(customerNumber);
                        
                        // [CONCEPTO: COMPROBACIÓN DE NULOS (NULL CHECK)] Evita el famoso NullPointerException
                        if (cliente == null) {
                            System.out.println(Configuracion.ERROR_CRITICO + "El cliente " + customerNumber + " no existe.");
                            break;
                        }

                        ArrayList<Payment> pagos = paymentDAO.obtenerPagosCliente(customerNumber);
                        double totalPagos = paymentDAO.obtenerSumaPagosCliente(customerNumber);

                        // Llamada a método privado interno.
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

                        // [CONCEPTO: PREVENCIÓN DE INYECCIÓN SQL (Oculto en el DAO)]
                        // Aunque pasamos Strings libremente aquí, la protección está en paymentDAO.modificarPago() 
                        // porque usa PreparedStatement en lugar de concatenar cadenas directamente en la query.
                        boolean modificado = paymentDAO.modificarPago(idC, check, variacion);
                        if (modificado) {
                            System.out.println("[+] Operación exitosa. Pago actualizado en el servidor.");
                        } else {
                            System.out.println("[-] Error de actualización: Verifica que el cliente y el cheque existan.");
                        }
                        break;
                        
                    case 4:
                        ejecutarAuditoriaMatriz(); // Arrays multidimensionales
                        break;
                        
                    case 5:
                        ejecutarAnalisisHashMap(paymentDAO, scanner); // Mapas clave-valor
                        break;    

                    case 6: // SALIR
                        System.out.println("Cerrando sesión.[T].");
                        break;

                    default:
                        // Si por algún milagro escapan al Regex, caen aquí.
                        System.out.println("[!] Comando no reconocido.");
                    }

                } catch (Exception e) {
                // [CONCEPTO: CAPTURA DE EXCEPCIONES] Entramos aquí si, por ejemplo, la BBDD explota o falla la red.
                String error = "Caída del sistema: " + e.getMessage();
                System.out.println(Configuracion.ERROR_CRITICO + error);
                
                // Llamamos a un método estático (no requiere instanciar LogForense) para guardar el error.
                controlador.LogForense.registrarEvento("CRÍTICO", error);
            }

            } while (opcion != 6); // [CONCEPTO: CONDICIÓN DEL DO-WHILE] Repetir mientras no se elija salir.
            
            System.out.println("[+] Conexiones cerradas. Praise the Sun.");
            scanner.close(); // [CONCEPTO: CIERRE DE RECURSOS] Liberamos memoria cerrando el lector de teclado.
        }

    /**
     * [CONCEPTO: ESCRITURA DE ARCHIVOS (I/O)]
     * Genera el archivo HTML.
     */
    private static void generarInformeHTML(Customer cliente, ArrayList<Payment> pagos, double total) {
        String nombreArchivo = "informeAdrianDiaz.html";
        
        // [CONCEPTO: TRY-WITH-RESOURCES] (Tema 9) Declaramos FileWriter y PrintWriter dentro del paréntesis del try.
        // Esto garantiza que el archivo se cierre automáticamente al terminar, pase lo que pase, evitando archivos corruptos.
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
            
            // Escritura secuencial. Todo esto es texto plano que el navegador luego interpreta.
            writer.println("<!DOCTYPE html>");
            writer.println("<html lang='es'>");
            // ... (Me salto el CSS por brevedad en la chuleta, es puro println) ...
            writer.println("<body><div class='container'>");
            writer.println("<h1>Informe Confidencial de Cliente</h1>");
            writer.println("<p><strong>ID Cliente:</strong> " + cliente.getCustomerNumber() + "</p>");
            
            writer.println("<table>");
            writer.println("<tr><th>Check Number</th><th>Payment Date</th><th>Amount (€)</th></tr>");
            
            // [CONCEPTO: BUCLE PARA GENERAR CÓDIGO HTML DINÁMICO]
            for (Payment p : pagos) {
                writer.println("<tr>");
                writer.println("<td>" + p.getCheckNumber() + "</td>");
                writer.println("<td>" + p.getPaymentDate() + "</td>");
                writer.println("<td>" + String.format("%.2f", p.getAmount()) + "</td>");
                writer.println("</tr>");
            }
            writer.println("</table>");
            // ... (Cierre de etiquetas) ...
            
            System.out.println("[+] Informe compilado y guardado en la raíz del proyecto como: " + nombreArchivo);
            
        } catch (IOException e) {
            // Entra si hay problemas de disco: no hay permisos de escritura, disco lleno, etc.
            System.out.println("[-] Error crítico al escribir el archivo en el disco: " + e.getMessage());
        }
    }
    
    private static void ejecutarAuditoriaMatriz() {
        System.out.println("\n[*] Escaneando matriz de transacciones...");
        // [CONCEPTO: MATRIZ / ARRAY BIDIMENSIONAL] Es como una hoja de cálculo (filas y columnas).
        double[][] matriz = {
            {20.5, 500.0, 10.0},
            {6000.5, 120.0, 45.0},
            {15.0, 8000.0, 99.0}
        };
        
        int f = 0;
        int alertas = 0;
        // [CONCEPTO: BUCLES ANIDADOS] Un bucle dentro de otro para recorrer las dos dimensiones (X e Y).
        while (f < matriz.length) { // Recorre Filas
            for (int c = 0; c < matriz[f].length; c++) { // Recorre Columnas de la fila actual
                if (matriz[f][c] > 1000) { // Lógica de negocio (detección de pagos altos)
                    System.out.println("    [!] ALERTA en ["+f+"]["+c+"]: " + matriz[f][c] + "€ detectados.");
                    alertas++;
                }
            }
            f++; // Avance del bucle while para evitar un bucle infinito.
        }
        System.out.println("[+] Escaneo completo. Alertas totales: " + alertas);
    }
    
    private static void ejecutarAnalisisHashMap(controlador.PaymentDAO dao, java.util.Scanner scanner) throws java.sql.SQLException {
        System.out.print("[*] Introduzca ID cliente para análisis de volumen: ");
        
        // [CONCEPTO: SANITIZACIÓN BÁSICA] trim() borra espacios en blanco al principio y final.
        String inputId = scanner.nextLine().trim(); 
        
        // [CONCEPTO: PROGRAMACIÓN DEFENSIVA (Guard Clauses)]
        if (inputId.isEmpty() || !inputId.matches("\\d+")) { // "\\d+" = uno o más dígitos numéricos.
            System.out.println(Configuracion.ERROR_CRITICO + "Entrada inválida.");
            return; // [CONCEPTO: RETURN TEMPRANO] Expulsa al usuario del método inmediatamente sin hacer el análisis.
        }
        
        int id = Integer.parseInt(inputId);
        java.util.ArrayList<modelo.Payment> pagos = dao.obtenerPagosCliente(id);
        
        if (pagos.isEmpty()) {
            return;
        }

        // [CONCEPTO: HASHMAP] Colección que almacena pares Clave-Valor. En lugar de índices (0,1,2...),
        // usamos una clave (ej. el String "2003") para guardar/recuperar su valor (el recuento).
        java.util.HashMap<String, Integer> contadorPorAnio = new java.util.HashMap<>();
        
        for (modelo.Payment p : pagos) {
            // Extrae los primeros 4 caracteres de la fecha (el año).
            String anio = p.getPaymentDate().toString().substring(0, 4); 
            
            // [CONCEPTO: LÓGICA DE CONTEO EN MAPAS]
            // getOrDefault busca si el año ya está. Si está, devuelve su conteo actual. Si no, devuelve 0.
            // Luego le suma 1 y vuelve a guardarlo (put) en esa misma clave.
            contadorPorAnio.put(anio, contadorPorAnio.getOrDefault(anio, 0) + 1);
        }
        
        // [CONCEPTO: RECORRIDO DE HASHMAP] keySet() obtiene todas las claves ("2003", "2004", etc.)
        for (String anio : contadorPorAnio.keySet()) { 
            System.out.println("    - Año " + anio + ": " + contadorPorAnio.get(anio) + " transacciones.");
        }
    }
}