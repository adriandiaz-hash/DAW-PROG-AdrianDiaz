package controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import modelo.Payment;
import controlador.Configuracion;

/**
 * Clase encargada de gestionar las operaciones de la entidad Payment contra la BBDD.
 * @autor [T] Adrián Díaz García
 */
public class PaymentDAO {
    
    private String url;
    private String user;
    private String password;

    public PaymentDAO(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    /**
     * Obtiene el listado de pagos de un cliente concreto.
     */
    public ArrayList<Payment> obtenerPagosCliente(int customerNumber) throws SQLException {
        ArrayList<Payment> pagos = new ArrayList<>();
        String sql = "SELECT checkNumber, paymentDate, amount FROM payments WHERE customerNumber = ?";
        
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, customerNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // Creación directa mediante constructor, sin setters superfluos.
                    pagos.add(new Payment(
                        rs.getString("checkNumber"),
                        rs.getDate("paymentDate"),
                        rs.getDouble("amount")
                    ));
                }
            }
        }
        return pagos;
    }

    /**
     * Obtiene la suma total de los pagos de un cliente.
     */
    public double obtenerSumaPagosCliente(int customerNumber) throws SQLException {
        double total = 0;
        String sql = "SELECT SUM(amount) AS total FROM payments WHERE customerNumber = ?";
        
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, customerNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    total = rs.getDouble("total");
                }
            }
        }
        return total;
    }

    /**
     * Modifica el importe de un pago existente (suma o resta).
     */
    public boolean modificarPago(int customerNumber, String checkNumber, double variacion) throws SQLException {
        String sql = "UPDATE payments SET amount = amount + ? WHERE customerNumber = ? AND checkNumber = ?";
        
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, variacion);
            pstmt.setInt(2, customerNumber);
            pstmt.setString(3, checkNumber);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        }
    }
}