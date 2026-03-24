package com.mycompany.concessionari;
import java.util.ArrayList;

/**
 * Classe que gestiona un ArrayList de Vehicles de la classe Vehicle
 * * @author [T] Adrián Díaz García.
 */
public class Vehicles {

    ArrayList<Vehicle> vehicles = new ArrayList<>();

    public ArrayList<Vehicle> getVehicles() {
        ArrayList<Vehicle> nou = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            nou.add(new Vehicle(vehicle)); // Retorna una còpia de cada vehicle per evitar que es modifiquin
        }
        return nou;
    }
    
    public void afegirVehicle(Vehicle nou) {
        vehicles.add(new Vehicle(nou));
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
}