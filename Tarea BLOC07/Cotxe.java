package com.mycompany.concessionari;

/**
 * SUBCLASE (La Implementación):
 * Al usar 'extends Vehicle', Cotxe hereda automáticamente todos los atributos (la marca, potencia, etc.) 
 * y métodos de Vehicle. Es como añadirle un módulo extra a la placa base.
 * * @autor [T] Adrián Díaz García
 */
public class Cotxe extends Vehicle {

    // ATRIBUTO ESPECÍFICO: 
    // Declarado como 'final' porque un coche nace con un número de puertas y no se le instalan más después.
    private final int nombrePortes;
    
    // CONSTRUCTOR COMPLETO:
    public Cotxe(String marca, int potencia, int velocitat, int velocitatMaxima, double consumBase, int nombrePortes) {
        // 'super()' llama al constructor de la clase padre (Vehicle).
        // Le pasa los primeros 5 cables (parámetros) para que el padre los conecte.
        super(marca, potencia, velocitat, velocitatMaxima, consumBase);
        // Y el hijo (Cotxe) se encarga exclusivamente de su propio cable adicional.
        this.nombrePortes = nombrePortes;
    }
    
    // CONSTRUCTOR COPIA DE COTXE:
    public Cotxe(Cotxe c) {
        // Le pasamos el objeto 'c' al constructor copia del padre para que clone la base.
        super(c); 
        // Y aquí clonamos el atributo específico.
        this.nombrePortes = c.nombrePortes; 
    }

    // CUMPLIENDO EL CONTRATO: 
    // Como Vehicle obligaba a crear retornaConsum() y copiar(), aquí están (@Override).
    @Override
    public double retornaConsum() {
        // Usamos getters para acceder a la potencia y consumo base, 
        // porque en el padre son 'private' y no podemos tocarlos directamente.
        double consumFinal = getConsumBase();

        // Condiciones acumulativas e independientes. 
        if (getPotencia() > 110) {
            consumFinal += getConsumBase() * 0.05;
        } 
        if (nombrePortes == 5) {
            consumFinal += getConsumBase() * 0.01;
        }
        return consumFinal;
    }

    @Override
    public Vehicle copiar() {
        // Crea una instancia nueva de sí mismo llamando al constructor copia.
        // El 'this' significa "pásate a ti mismo como molde para la copia".
        return new Cotxe(this); 
    }
    
    @Override
    public String toString() {
        // Construye su propia etiqueta legible.
        return "[COTXE] Marca: " + getMarca() + 
               " | Potència: " + getPotencia() + " CV" +
               " | Vel: " + getVelocitat() + "/" + getVelocitatMaxima() + " km/h" +
               " | Consum base: " + getConsumBase() + 
               " | Portes: " + nombrePortes;
    }
}