package com.mycompany.concessionari;

/**
 * @author Adrián Díaz García
 * Objecte Pare Vehicle.
 */
public abstract class Vehicle implements java.io.Serializable {

    private String marca;
    private int potencia;
    private int velocitat;
    private int velocitatMaxima;
    private double consumBase;

    public abstract double retornaConsum();
    public abstract Vehicle copiar();

    public Vehicle() {
    }

    public Vehicle(String marca, int potencia, int velocitat, int velocitatMaxima, double consumBase) {
        this.marca = marca;
        this.potencia = potencia;
        this.velocitat = velocitat;
        this.velocitatMaxima = velocitatMaxima;
        this.consumBase = consumBase;
    }

    public Vehicle(Vehicle vehicle) { //CONSTRUCTOR CÒPIA
        this.marca = vehicle.marca;
        this.potencia = vehicle.potencia;
        this.velocitat = vehicle.velocitat;
        this.velocitatMaxima = vehicle.velocitatMaxima;
        this.consumBase = vehicle.consumBase;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getPotencia() {
        return potencia;
    }

    public void setPotencia(int potencia) {
        this.potencia = potencia;
    }

    public int getVelocitat() {
        return velocitat;
    }

    public void setVelocitat(int velocitat) {
        this.velocitat = velocitat;
    }

    public int getVelocitatMaxima() {
        return velocitatMaxima;
    }

    public void setVelocitatMaxima(int velocitatMaxima) {
        this.velocitatMaxima = velocitatMaxima;
    }

    public double getConsumBase() {
        return consumBase;
    }

    public void setConsumBase(double consumBase) {
        this.consumBase = consumBase;
    }

    public void augmentarVelocitat(int increment) {
        this.velocitat = this.velocitat + increment;
    }

    public void reduirVelocitat(int decrement) {
        this.velocitat = this.velocitat - decrement;
    }

    @Override
    public String toString() {
        return "Vehicle{" + "marca=" + marca + ", potencia=" + potencia + ", velocitat=" + velocitat + ", velocitatMaxima=" + velocitatMaxima + '}';
    }

}