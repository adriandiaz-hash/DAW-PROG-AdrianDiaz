package modelo;

/**
 * Entidad que hereda de Customer. Representa un cliente con privilegios
 * o bajo vigilancia especial.
 * @autor [T] Adrián Díaz García
 */
public class CustomerVIP extends Customer { // La palabra 'extends' es la clave de la herencia (Tema 7)
    
    private String nivelPrioridad; // Atributo exclusivo de los VIP

    // El constructor debe llamar obligatoriamente al constructor del padre usando super()
    public CustomerVIP(int customerNumber, String customerName, int numberOfPayments, double totalPaid, String nivelPrioridad) {
        // super() inyecta los datos en la clase Customer original
        super(customerNumber, customerName, numberOfPayments, totalPaid);
        this.nivelPrioridad = nivelPrioridad;
    }

    public String getNivelPrioridad() {
        return nivelPrioridad;
    }

    // Sobreescritura de métodos (Polimorfismo). Si intentamos imprimir este cliente, 
    // mostrará su etiqueta VIP automáticamente.
    @Override
    public String toString() {
        return super.getCustomerName() + " [VIP - Prioridad: " + nivelPrioridad + "]";
    }
}