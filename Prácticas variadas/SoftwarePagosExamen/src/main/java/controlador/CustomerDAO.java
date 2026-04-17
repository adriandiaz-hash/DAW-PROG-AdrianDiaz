package controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import modelo.Customer;
import controlador.Configuracion;

/**
 * Clase encargada de gestionar las operaciones de la entidad Customer contra la BBDD.
 * @autor [T] Adrián Díaz García
 */
public class CustomerDAO {
    
    private String url;
    private String user;
    private String password;

    public CustomerDAO(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    /**
     * Obtiene los clientes con más pagos registrados.
     */
    public ArrayList<Customer> obtenerTopClientes(int limite) throws SQLException {
        ArrayList<Customer> listaClientes = new ArrayList<>();
        
        String sql = "SELECT c.customerNumber, c.customerName, COUNT(p.checkNumber) AS numPagaments, SUM(p.amount) AS totalPagat " +
                     "FROM customers c " +
                     "LEFT JOIN payments p ON c.customerNumber = p.customerNumber " +
                     "GROUP BY c.customerNumber " +
                     "ORDER BY totalPagat DESC " +
                     "LIMIT ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limite);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Customer c = new Customer(
                        rs.getInt("customerNumber"), 
                        rs.getString("customerName"), 
                        rs.getInt("numPagaments"), 
                        rs.getDouble("totalPagat")
                    );
                    listaClientes.add(c);
                }
            }
        }
        return listaClientes;
    }

    /**
     * Busca un cliente específico por su ID para la cabecera del informe.
     */
    public Customer obtenerClientePorId(int customerNumber) throws SQLException {
        Customer cliente = null;
        String sql = "SELECT customerNumber, customerName FROM customers WHERE customerNumber = ?";
        
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, customerNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    cliente = new Customer(rs.getInt("customerNumber"), rs.getString("customerName"));
                }
            }
        }
        return cliente;
    }
}