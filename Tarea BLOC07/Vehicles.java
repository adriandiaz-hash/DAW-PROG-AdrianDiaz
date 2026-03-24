package com.mycompany.concessionari;

// IMPORTACIONES CORE:
// ArrayList: Tu matriz dinámica. A diferencia de un array normal (Vehicle[]), 
// este crece y decrece automáticamente. No tienes que decirle el tamaño inicial.
// Serializable: La etiqueta mágica que le dice a Java "este objeto se puede convertir en bytes".
import java.util.ArrayList;
import java.io.Serializable;

/**
 * CLASE GESTORA (El Búnker):
 * Aquí almacenamos la flota. El objetivo principal de esta clase no es solo guardar datos,
 * sino PROTEGERLOS. Por eso aplicamos el principio de "Ocultación de la información".
 * Nunca confíes en la clase main(), trátala como un entorno hostil.
 * * @autor [T] Adrián Díaz García
 */
public class Vehicles implements Serializable {

    // EL ALMACÉN (ArrayList):
    // Sin modificador de acceso explícito (es 'package-private', aunque en un entorno más estricto sería 'private').
    // Contiene objetos que heredan de Vehicle (pueden ser Cotxe o Moto gracias al polimorfismo).
    ArrayList<Vehicle> vehicles = new ArrayList<>();

    // =========================================================================
    // GETTERS Y PRESERVACIÓN DE LA OCULTACIÓN (Defensa Activa)
    // =========================================================================

    /**
     * Devuelve la lista de vehículos. 
     * ATENCIÓN: No devolvemos el 'ArrayList vehicles' original. Si lo hiciéramos, 
     * alguien desde el main() podría borrar un coche usando gestor.getVehicles().clear() 
     * y destruiría nuestra base de datos interna.
     */
    public ArrayList<Vehicle> getVehicles() {
        // 1. Creamos una lista temporal totalmente nueva.
        ArrayList<Vehicle> nou = new ArrayList<>();
        
        // 2. Iteramos sobre nuestra lista original.
        for (Vehicle vehicle : vehicles) {
            // 3. CLONACIÓN: No metemos el vehículo original en la lista nueva.
            // Usamos el método abstracto .copiar() (que cada hijo implementa) para 
            // meter un clon exacto. Así, si el main() modifica el clon, el original sigue intacto.
            nou.add(vehicle.copiar()); 
        }
        // 4. Entregamos la lista de clones. Seguridad de grado DFIR superada.
        return nou;
    }
    
    // =========================================================================
    //