package com.mycompany.concessionari;
import java.util.ArrayList;
import java.io.Serializable;

/**
 * Classe que gestiona un ArrayList de Vehicles de la classe Vehicle
 * * @author [T] Adrián Díaz García.
 */
public class Vehicles implements Serializable {

    ArrayList<Vehicle> vehicles = new ArrayList<>();

    public ArrayList<Vehicle> getVehicles() {
        ArrayList<Vehicle> nou = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            nou.add(vehicle.copiar()); // Retorna una còpia de cada vehicle per evitar que es modifiquin
        }
        return nou;
    }
    
    public void afegirVehicle(Vehicle nou) {
        vehicles.add(nou.copiar());
    }

    public int comptarVehiclesDeMarca(String marca) {
        int comptador = 0;
        for (int i = 0; i < vehicles.size(); i++) {
            if (vehicles.get(i).getMarca().equals(marca)) {
                comptador++;
            }
        }
        return comptador;
    }

    public void canviarMarca(String marca, String marcaNova) {
        for (int i = 0; i < vehicles.size(); i++) {
            if (vehicles.get(i).getMarca().equals(marca)) {
                vehicles.get(i).setMarca(marcaNova);
            }
        }
    }

    public Vehicle retornaVehicleConsumMinim(int potencia) throws Exception {
        Vehicle vehicleMinim = null;

        for (Vehicle v : vehicles) {
            if (v.getPotencia() == potencia) {
                if (vehicleMinim == null || v.retornaConsum() < vehicleMinim.retornaConsum()) {
                    vehicleMinim = v;
                }
            }
        }
        if (vehicleMinim == null) {
            throw new Exception("No s'ha trobat cap vehicle amb la potencia de " + potencia);
        }
        return vehicleMinim.copiar(); // Retorna una còpia del vehicle per evitar que es modifiqui l'original
    }
}