package com.mycompany.concessionari;

import java.io.*;
import java.util.Scanner;

public class ConcessionariAdrianDiaz {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Vehicles gestor = cargarDatos(); // Intentamos cargar al empezar

        int opcion;
        do {
            System.out.println("\n--- CONCESSIONARI ADRIÁN DÍAZ ---");
            System.out.println("1. Afegir Cotxe");
            System.out.println("2. Afegir Moto");
            System.out.println("3. Mostrar Vehicles");
            System.out.println("4. Cercar Vehicle Consum Mínim");
            System.out.println("0. Sortir i Guardar");
            System.out.print("Selecciona una opció: ");
            opcion = sc.nextInt();
            sc.nextLine(); // Limpiar buffer

            switch (opcion) {
                case 1: {
                    System.out.println("\n--- DADES DEL NOU COTXE ---");
                    System.out.print("Marca: ");
                    String marca = sc.nextLine();
                    
                    System.out.print("Potència (CV): ");
                    int potencia = sc.nextInt();
                    
                    System.out.print("Velocitat actual: ");
                    int velocitat = sc.nextInt();
                    
                    System.out.print("Velocitat màxima: ");
                    int velocitatMaxima = sc.nextInt();
                    
                    System.out.print("Consum base (ex: 5,5): ");
                    double consumBase = sc.nextDouble();
                    
                    System.out.print("Nombre de portes (3 o 5): ");
                    int portes = sc.nextInt();
                    sc.nextLine(); // ¡Vital! Limpiamos el salto de línea residual del buffer
                    
                    // Ensamblamos el vehículo con las piezas recogidas
                    Cotxe nouCotxe = new Cotxe(marca, potencia, velocitat, velocitatMaxima, consumBase, portes);
                    
                    // Lo guardamos en el garaje
                    gestor.afegirVehicle(nouCotxe);
                    System.out.println("Cotxe afegit a la base de dades amb èxit.");
                    break;
                }
                case 2: {
                    System.out.println("\n--- DADES DE LA NOVA MOTO ---");
                    System.out.print("Marca: ");
                    String marca = sc.nextLine();
                    
                    System.out.print("Potència (CV): ");
                    int potencia = sc.nextInt();
                    
                    System.out.print("Velocitat actual: ");
                    int velocitat = sc.nextInt();
                    
                    System.out.print("Velocitat màxima: ");
                    int velocitatMaxima = sc.nextInt();
                    
                    System.out.print("Consum base (ex: 5,5): ");
                    double consumBase = sc.nextDouble();
                    
                    System.out.print("Te Maleter (true o false): ");
                    boolean maleter = sc.nextBoolean();
                    sc.nextLine(); // ¡Vital! Limpiamos el salto de línea residual del buffer
                    
                    // Ensamblamos el vehículo con las piezas recogidas
                    Moto novaMoto = new Moto(marca, potencia, velocitat, velocitatMaxima, consumBase, maleter);
                    
                    // Lo guardamos en el garaje
                    gestor.afegirVehicle(novaMoto);
                    System.out.println("Moto afegida a la base de dades amb èxit.");
                    break;
                }
                case 3: {
                    System.out.println("\n--- LLISTAT DE VEHICLES ---");
                    // 1. Obtenemos la lista segura (las copias)
                    java.util.ArrayList<Vehicle> llista = gestor.getVehicles();
                    
                    // 2. Comprobamos si el concesionario está vacío
                    if (llista.isEmpty()) {
                        System.out.println("No hi ha cap vehicle al concessionari.");
                    } else {
                        // 3. Recorremos la lista
                        for (Vehicle v : llista) {
                            // Imprimimos la información básica (usa el toString() de Vehicle)
                            System.out.print(v.toString());
                            // Añadimos el cálculo dinámico del consumo
                            System.out.println(" | Consum real: " + v.retornaConsum() + " L/100km");
                        }
                    }
                    break;
                }
                case 4: {
                    System.out.println("\n--- CERCAR VEHICLE AMB CONSUM MÍNIM ---");
                    System.out.print("Introdueix la potència (CV) a cercar: ");
                    int potBusqueda = sc.nextInt();
                    sc.nextLine(); // Escudo deflector para limpiar el buffer

                    try {
                        // Intentamos buscar el vehículo
                        Vehicle vMinim = gestor.retornaVehicleConsumMinim(potBusqueda);
                        
                        System.out.println("Vehicle trobat amb el consum més baix:");
                        System.out.println(vMinim.toString() + " | Consum real: " + vMinim.retornaConsum() + " L/100km");
                        
                    } catch (Exception e) {
                        // Si el método lanza el 'throw new Exception', lo capturamos aquí y evitamos que el programa explote
                        System.out.println("Avís del sistema: " + e.getMessage());
                    }
                    break;
                }
                case 0:
                    guardarDatos(gestor);
                    System.out.println("Dades guardades. Fins aviat!");
                    break;
            }
        } while (opcion != 0);
    }

    // MÉTODO PARA LEER EL ARCHIVO (DESERIALIZACIÓN)
    private static Vehicles cargarDatos() {
        File f = new File("concessionari.dat");
        if (f.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
                return (Vehicles) ois.readObject();
            } catch (Exception e) {
                System.out.println("Error al carregar les dades: " + e.getMessage());
            }
        }
        return new Vehicles(); // Si no existe, devolvemos uno nuevo vacío
    }

    // MÉTODO PARA GUARDAR EL ARCHIVO (SERIALIZACIÓN)
    private static void guardarDatos(Vehicles gestor) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("concessionari.dat"))) {
            oos.writeObject(gestor);
        } catch (IOException e) {
            System.out.println("Error en guardar les dades: " + e.getMessage());
        }
    }
}