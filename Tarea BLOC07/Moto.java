package com.mycompany.concessionari;

/**
 * SUBCLASE: Igual que Cotxe, pero con su propia personalidad.
 * * @autor [T] Adrián Díaz García
 */
public class Moto extends Vehicle {

    // ATRIBUTO ESPECÍFICO: Inmutable ('final').
    private final boolean maleter;

    public Moto(String marca, int potencia, int velocitat, int velocitatMaxima, double consumBase, boolean maleter) {
        // Pasa el trabajo pesado al padre.
        super(marca, potencia, velocitat, velocitatMaxima, consumBase);
        this.maleter = maleter;
    }
    
    public Moto(Moto m) {
        super(m); 
        this.maleter = m.maleter; 
    }

    @Override
    public double retornaConsum() {
        double consumFinal = getConsumBase();

        // Lógica específica de la moto. No le importan las puertas.
        if (maleter) {
            consumFinal += getConsumBase() * 0.04;
        }
        return consumFinal;
    }

    @Override
    public Vehicle copiar() {
        return new Moto(this); 
    }
    
    @Override
    public String toString() {
        // Operador ternario: (maleter ? "Sí" : "No"). 
        // Si maleter es true, imprime "Sí", si no, imprime "No". Mucho más elegante que un if-else.
        return "[MOTO]  Marca: " + getMarca() + 
               " | Potència: " + getPotencia() + " CV" +
               " | Vel: " + getVelocitat() + "/" + getVelocitatMaxima() + " km/h" +
               " | Consum base: " + getConsumBase() + 
               " | Maleter: " + (maleter ? "Sí" : "No");
    }
}