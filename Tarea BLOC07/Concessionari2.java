package com.mycompany.concessionari;

/**
 * Concessionari2 - Ús d'un array de Vehicles
 * @author avf i dsb
 */
public class Concessionari2 {

    public static void main(String[] args) {

        final int MAXIM_VEHICLES = 50;
        
        ArrayVehicles arrayVehicles = new ArrayVehicles(MAXIM_VEHICLES);
        introduirVehicles(arrayVehicles);

        System.out.println("Número de BMWs: " + arrayVehicles.comptarVehiclesDeMarca("BMW"));
        
        System.out.println("Canviant la marca BMW per Opel...");
        arrayVehicles.canviarMarca("BMW", "Opel");

        System.out.println("Número de BMWs: " + arrayVehicles.comptarVehiclesDeMarca("BMW"));

    }

    public static void introduirVehicles(ArrayVehicles arrayVehicles) {
        Vehicle vehicle1 = new Vehicle("BMW", 136, 0, 210);
        if (arrayVehicles.afegirVehicle(vehicle1)) {
            System.out.println("vehicle1 afegit: " + vehicle1.toString());
        } else {
            System.out.println("No hi caben més vehicles.");
        }
        Vehicle vehicle2 = new Vehicle("Mercedes", 194, 0, 340);
        if (arrayVehicles.afegirVehicle(vehicle2)) {
            System.out.println("vehicle2 afegit: " + vehicle2.toString());
        } else {
            System.out.println("No hi caben més vehicles.");
        }
        Vehicle vehicle3 = new Vehicle("BMW", 850, 0, 220);
        if (arrayVehicles.afegirVehicle(vehicle3)) {
            System.out.println("vehicle3 afegit: " + vehicle1.toString());
        } else {
            System.out.println("No hi caben més vehicles.");
        }
    }

}
