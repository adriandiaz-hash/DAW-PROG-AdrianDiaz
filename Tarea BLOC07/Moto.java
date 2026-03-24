import com.mycompany.concessionari.Vehicle;

/**
 * @author Adrián Díaz García
 * Objecte Fill de Vehicle.
 */
public class Moto extends Vehicle {

    private boolean maleter;

    public Moto(String marca, int potencia, int velocitat, int velocitatMaxima, double consumBase, boolean maleter) {
        super(marca, potencia, velocitat, velocitatMaxima, consumBase);
        this.maleter = maleter;
    }

    @Override
    public double retornaConsum() {

        double consumFinal = getConsumBase();

        if (maleter) {
            consumFinal += getConsumBase() * 0.04;
        }
        return consumFinal;
    }
    @Override
    public Vehicle copiar() {
        return new Moto(this); // Llama a su propio constructor copia
    }
}