package com.mycompany.concessionari;

// IMPORTACIONES: Tus herramientas de trabajo.
// java.io.* importa todo lo necesario para leer/escribir archivos (tu almacenamiento persistente).
// Scanner es tu interfaz con el usuario (el teclado).
// InputMismatchException es el escudo que levantamos cuando el usuario ataca con un String donde va un Int.
import java.io.*;
import java.util.Scanner;
import java.time.LocalDate;
import java.util.InputMismatchException;

/**
 * CLASE PRINCIPAL: El centro de mando. 
 * Aquí no hay lógica de negocio pura (eso está en Vehicles.java), 
 * aquí gestionamos la entrada/salida y las bofetadas del usuario.
 * * @autor [T] Adrián Díaz García
 */
public class ConcessionariAdrianDiaz {

    // CONSTANTES GLOBALES (static final):
    // 'static' significa que pertenecen a la clase, no hace falta instanciarla para usarlas.
    // 'final' significa que son inmutables. Son tus puntos de anclaje, tus "Hogueras" a las que volver.
    // Usar esto evita "cadenas mágicas" perdidas por el código que luego son imposibles de mantener.
    private static final String FITXER_DADES = "datos_vehiculos_adrian_diaz.dat";
    private static final String FITXER_TEXT = "informacion_vehiculo_adrian_diaz.txt";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        // Fase 1: Carga de memoria. Antes de que el usuario haga nada, 
        // levantamos los datos del disco duro (si los hay).
        Vehicles gestor = cargarDatos(); 

        int opcion = -1; // Inicializamos a -1 para asegurar que entramos al bucle.
        
        // BUCLE PRINCIPAL (do-while): 
        // Garantiza que el menú se muestre AL MENOS UNA VEZ antes de comprobar la condición de salida.
        do {
            System.out.println("\n--- CONCESSIONARI ADRIÁN DÍAZ ---");
            System.out.println("1. Afegir Cotxe");
            System.out.println("2. Afegir Moto");
            System.out.println("3. Mostrar Vehicles");
            System.out.println("4. Cercar Vehicle Consum Mínim");
            System.out.println("0. Sortir i Guardar");
            System.out.print("Selecciona una opció: ");
            
            // EL GRAN TRY-CATCH: Tu red de seguridad.
            // Si cualquier cosa dentro del 'try' lanza una InputMismatchException (ej. meter letras),
            // el código salta inmediatamente al 'catch' en lugar de colapsar y matar el programa.
            try {
                opcion = sc.nextInt(); // Intenta leer un entero.
                
                // LA TRAMPA DEL SCANNER: 
                // sc.nextInt() lee el número, pero deja el salto de línea (Enter) flotando en el buffer.
                // Si no ponemos este sc.nextLine(), la próxima vez que leamos un String, se tragará ese Enter y fallará.
                sc.nextLine(); 

                // SWITCH: Enrutador de opciones. Más limpio que mil 'if-else' anidados.
                switch (opcion) {
                    case 1: {
                        introduirVehicles(1, sc, gestor);
                        break; // Si no pones break, ejecutará el caso 2 también (efecto cascada/fall-through).
                    }
                    case 2: {
                        introduirVehicles(2, sc, gestor);
                        break;
                    }
                    case 3: {
                        System.out.println("\n--- LLISTAT DE VEHICLES ---");
                        // Obtenemos la lista. OJO: Vehicles.java devuelve una COPIA de los objetos,
                        // manteniendo la encapsulación intacta. Nadie puede modificar tus datos desde aquí.
                        java.util.ArrayList<Vehicle> llista = gestor.getVehicles();
                        
                        if (llista.isEmpty()) {
                            System.out.println("No hi ha cap vehicle al concessionari.");
                        } else {
                            // Bucle For-Each: "Para cada 'Vehicle v' dentro de la 'llista'..."
                            for (Vehicle v : llista) {
                                // Polimorfismo en acción: v.toString() y v.retornaConsum() se ejecutarán
                                // según si 'v' es en realidad una Moto o un Cotxe. La magia de la herencia.
                                System.out.print(v.toString());
                                System.out.println(" | Consum real: " + v.retornaConsum() + " L/100km");
                            }
                        }
                        break;
                    }
                    case 4: {
                        System.out.println("\n--- CERCAR VEHICLE AMB CONSUM MÍNIM ---");
                        System.out.print("Introdueix la potència (CV) a cercar: ");
                        int potBusqueda = sc.nextInt();
                        sc.nextLine(); 

                        // Try anidado: La clase Vehicles lanza un Exception genérico si no encuentra la potencia.
                        // Lo capturamos aquí para imprimir el mensaje sin romper la ejecución.
                        try {
                            Vehicle vMinim = gestor.retornaVehicleConsumMinim(potBusqueda);
                            System.out.println("Vehicle trobat amb el consum més baix:");
                            System.out.println(vMinim.toString() + " | Consum real: " + vMinim.retornaConsum() + " L/100km");
                        } catch (Exception e) {
                            // e.getMessage() saca el texto exacto que pusiste en el 'throw new Exception' de Vehicles.java
                            System.out.println("Avís del sistema: " + e.getMessage());
                        }
                        break;
                    }
                    case 0:
                        // Ruta de salida. Generamos el registro forense en texto y volcamos la RAM al disco (.dat).
                        generarInformeText(gestor, sc); 
                        guardarDatos(gestor);           
                        System.out.println("Dades guardades. Fins aviat!");
                        break;
                    default:
                        // Si mete un número como 8 o 99.
                        System.out.println("Avís: Opció no vàlida. Introdueix un número del 0 al 4.");
                }
            } catch (InputMismatchException e) {
                // Si el usuario mete un String (ej: "hola") en sc.nextInt().
                System.out.println("Error crític evitat: Has introduït un caràcter no vàlid. S'esperava un número.");
                // VITAL: Vaciamos el buffer del teclado corrupto. Si no lo hacemos, el bucle do-while
                // girará infinitamente leyendo ese mismo error sin parar.
                sc.nextLine(); 
            }
        } while (opcion != 0); // Condición de permanencia: mientras no pulse 0, seguimos dentro.
    }

    // =========================================================================
    // MÉTODOS DE PERSISTENCIA (I/O BINARIA)
    // Aquí usamos flujos (Streams) para convertir objetos vivos de RAM a bytes muertos en disco.
    // =========================================================================

    private static Vehicles cargarDatos() {
        File f = new File(FITXER_DADES); // Apuntamos al archivo.
        
        if (f.exists()) {
            // TRY WITH RESOURCES (Try con recursos):
            // Al declarar el InputStream dentro de los paréntesis del try(...), Java se encarga
            // de cerrar el archivo automáticamente al terminar, pase lo que pase. Evita fugas de memoria.
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
                // Leemos el archivo y lo "casteamos" (forzamos) a que sea de tipo Vehicles.
                // Esto solo funciona porque Vehicles implementa java.io.Serializable.
                Vehicles v = (Vehicles) ois.readObject();
                System.out.println("Fitxer " + FITXER_DADES + " carregat correctament.");
                
                return v; // Devolvemos el objeto resucitado.
            } catch (Exception e) {
                System.out.println("Error al carregar les dades: " + e.getMessage());
            }
        } 
        // Si el archivo no existe o hay error, devolvemos un gestor vacío y limpio.
        return new Vehicles(); 
    }

    private static void guardarDatos(Vehicles gestor) {
        // ObjectOutputStream escribe objetos enteros en binario. Ininteligible para humanos, perfecto para Java.
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FITXER_DADES))) {
            oos.writeObject(gestor); // Volcamos toda la clase gestora (y sus arrays) de golpe.
        } catch (IOException e) {
            System.out.println("Error en guardar les dades: " + e.getMessage());
        }
    }

    // =========================================================================
    // LÓGICA DE INTERFAZ Y RECOPILACIÓN DE DATOS
    // =========================================================================

    private static void introduirVehicles(int tipus, Scanner sc, Vehicles gestor) {
        // Operador ternario (condición ? si_verdadero : si_falso). Un if-else compactado en una línea.
        String nomTipus = (tipus == 1) ? "cotxes" : "motos";
        System.out.print("\nQuants " + nomTipus + " vols afegir a la llista?: ");
        
        try {
            int quantitat = sc.nextInt();
            sc.nextLine(); // Limpieza de buffer post-int.

            for (int i = 0; i < quantitat; i++) {
                // Recopilación secuencial de datos comunes a la clase padre (Vehicle).
                System.out.print("Marca: ");
                String marca = sc.nextLine();
                System.out.print("Potència (CV): ");
                int potencia = sc.nextInt();
                System.out.print("Velocitat actual: ");
                int velocitat = sc.nextInt();
                System.out.print("Velocitat màxima: ");
                int velocitatMaxima = sc.nextInt();
                System.out.print("Consum base (ex: 5,5): "); // Ojo: double en Scanner español suele pedir coma, no punto.
                double consumBase = sc.nextDouble();
                
                if (tipus == 1) { 
                    // Atributos exclusivos del HIJO 1 (Cotxe)
                    System.out.print("Nombre de portes (3 o 5): ");
                    int portes = sc.nextInt();
                    sc.nextLine(); 
                    // Instanciamos y añadimos. Al enviarlo a 'gestor', este internamente 
                    // le hará una copia de seguridad gracias al método .copiar()
                    Cotxe nouCotxe = new Cotxe(marca, potencia, velocitat, velocitatMaxima, consumBase, portes);
                    gestor.afegirVehicle(nouCotxe);
                } else { 
                    // Atributos exclusivos del HIJO 2 (Moto)
                    System.out.print("Te Maleter (true o false): ");
                    boolean maleter = sc.nextBoolean();
                    sc.nextLine(); 
                    Moto novaMoto = new Moto(marca, potencia, velocitat, velocitatMaxima, consumBase, maleter);
                    gestor.afegirVehicle(novaMoto);
                }
            }
        } catch (InputMismatchException e) {
             System.out.println("Error de format: Les dades introduïdes no són vàlides. Operació cancel·lada.");
             sc.nextLine(); // Purgamos el buffer.
        }
    }

    // =========================================================================
    // GENERACIÓN DEL LOG FORENSE (I/O TEXTO)
    // Aquí escribimos datos legibles por humanos, no binarios.
    // =========================================================================

    private static void generarInformeText(Vehicles gestor, Scanner sc) {
        System.out.print("Introdueix una potència (CV) per buscar i generar l'informe: ");
        
        try {
            int potencia = sc.nextInt();
            sc.nextLine(); 

            // PrintWriter sobre FileWriter: 
            // FileWriter abre el archivo, PrintWriter nos da métodos cómodos como println() 
            // para escribir como si fuera un System.out, pero directo al disco.
            try (PrintWriter pw = new PrintWriter(new FileWriter(FITXER_TEXT))) {
                pw.println("Potència sol·licitada al sistema: " + potencia + " CV");
                
                try {
                    Vehicle vMinim = gestor.retornaVehicleConsumMinim(potencia);
                    pw.println("Vehicle de consum mínim: Marca=" + vMinim.getMarca() + 
                               ", Consum real=" + vMinim.retornaConsum() + " L/100km");
                    
                    // LocalDate.now() extrae la fecha actual del sistema.
                    pw.println("Investigador: Adrián Díaz García - Data: " + LocalDate.now());
                    
                } catch (Exception e) {
                    // Si Vehicles.java lanza su excepción porque no encuentra la potencia,
                    // documentamos el fallo ordenadamente en el archivo de texto.
                    pw.println("No s'han trobat vehicles amb la potència sol·licitada.");
                    pw.println("Investigador: Adrián Díaz García - Data: " + LocalDate.now());
                }
            } catch (IOException e) {
                System.out.println("Error crític al crear el fitxer de text: " + e.getMessage());
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Has d'introduir un número per a la potència.");
            sc.nextLine();
        }
    }
}