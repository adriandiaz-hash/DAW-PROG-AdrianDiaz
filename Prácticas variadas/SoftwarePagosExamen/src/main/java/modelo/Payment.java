package modelo;

import java.sql.Date;

/**
 * Clase que representa la entidad Payment (Pago).
 * Almacena la información de una transacción específica vinculada a un cliente.
 * @autor [T] Adrián Díaz García
 */
public class Payment {
    
    private String checkNumber;
    private Date paymentDate;
    private double amount;

    public Payment(String checkNumber, Date paymentDate, double amount) {
        this.checkNumber = checkNumber;
        this.paymentDate = paymentDate;
        this.amount = amount;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public double getAmount() {
        return amount;
    }
}