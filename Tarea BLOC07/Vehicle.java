package com.mycompany.concessionari;

/**
 * CLASE ABSTRACTA (El Plano Eléctrico Base):
 * Al ser 'abstract', le estás diciendo a Java: "Sé lo que es un vehículo en teoría, 
 * pero es un concepto demasiado genérico para existir en la realidad".
 * No puedes hacer un 'new Vehicle()', igual que no puedes instalar un "electrodoméstico genérico",
 * instalas una nevera o un horno.
 * * @autor [T] Adrián Díaz García
 */
public abstract class Vehicle implements java.io.Serializable {

    // ATRIBUTOS BASE: Todos privados. Ocultación de datos pura. 
    // Ninguna clase externa, ni siquiera sus hijos (Cotxe/Moto), pueden tocarlos directamente.
    private String marca;
    private int potencia;
    private int velocitat;
    private int velocitatMaxima;
    private double consumBase;

    // MÉTODOS ABSTRACTOS (Los Contratos):
    // No tienen llaves { }, solo punto y coma. 
    // Obligan a cualquier hijo (Cotxe o Moto) a escribir su propia versión de estos métodos.
    // Es tu forma de decir: "No me importa cómo lo hagas, pero si heredas de mí, 
    // TIENES que saber calcular tu consumo y hacer una copia de ti mismo".
    public abstract double retornaConsum();
    public abstract Vehicle copiar();

    // CONSTRUCTOR VACÍO:
    // Necesario muchas veces para que ciertas librerías o frameworks puedan inicializar el objeto.
    public Vehicle() {
    }

    // CONSTRUCTOR COMPLETO:
    // El punto de entrada de datos. Inicializa el "cableado base" del objeto.
    public Vehicle(String marca, int potencia, int velocitat, int velocitatMaxima, double consumBase) {
        this.marca = marca;
        this.potencia = potencia;
        this.velocitat = velocitat;
        this.velocitatMaxima = velocitatMaxima;
        this.consumBase = consumBase;
    }

    // CONSTRUCTOR CÒPIA (El clonador forense base):
    // Coge un vehículo ya existente y copia sus valores uno a uno en un vehículo nuevo.
    // Esto es vital para no alterar la evidencia original en la memoria RAM.
    public Vehicle(Vehicle vehicle) { 
        this.marca = vehicle.marca;
        this.potencia = vehicle.potencia;
        this.velocitat = vehicle.velocitat;
        this.velocitatMaxima = vehicle.velocitatMaxima;
        this.consumBase = vehicle.consumBase;
    }

    // GETTERS Y SETTERS: 
    // Las únicas puertas de acceso controladas a los atributos privados.
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public int getPotencia() { return potencia; }
    public void setPotencia(int potencia) { this.potencia = potencia; }
    public int getVelocitat() { return velocitat; }
    public void setVelocitat(int velocitat) { this.velocitat = velocitat; }
    public int getVelocitatMaxima() { return velocitatMaxima; }
    public void setVelocitatMaxima(int velocitatMaxima) { this.velocitatMaxima = velocitatMaxima; }
    public double getConsumBase() { return consumBase; }
    public void setConsumBase(double consumBase) { this.consumBase = consumBase; }

    // MÉTODOS DE COMPORTAMIENTO COMÚN:
    public void augmentarVelocitat(int increment) {
        this.velocitat = this.velocitat + increment;
    }

    public void reduirVelocitat(int decrement) {
        this.velocitat = this.velocitat - decrement;
    }

    // @OVERRIDE: Sobrescribe el método toString