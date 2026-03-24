import com.mycompany.concessionari.Vehicle;

/**
 * @author Adrián Díaz García
 * Objecte Fill de Vehicle.
 */
public class Cotxe extends Vehicle {

    private int nombrePortes;
    
    public Cotxe(String marca, int potencia, int velocitat, int velocitatMaxima, double consumBase, int nombrePortes) {
        super(marca, potencia, velocitat, velocitatMaxima, consumBase);

        this.nombrePortes = nombrePortes;
    }

    @Override
    public double retornaConsum() {

        double consumFinal = getConsumBase();

        if (getPotencia() > 110) {
            consumFinal += getConsumBase() * 0.05;
        } if (nombrePortes == 5) {
            consumFinal += getConsumBase() * 0.01;
        }
        return consumFinal;
    }
    @Override
    public Vehicle copiar() {
        return new Cotxe(this); // Llama a su propio constructor copia
    }
}