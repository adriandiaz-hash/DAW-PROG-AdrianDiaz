package vista;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import controlador.FirmaCabalaDetectadaException;
import modelo.RegistroCabala;
import modelo.RegistroForense;

java.util.Scanner;
java.io.File;

/** * Clase principal de la aplicación Operación Eclipse Digital.
 * @autor Adrián
 */
public class OperacionEclipseDigital {

    public static void main(String[] args) {
        ArrayList<RegistroForense> registros = new ArrayList<>();

        DateTimeFormatter formatoFechas = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("[T] Iniciando escaneo de logs...");

        try (Scanner lector = new Scanner(new File("raw.logs.txt"))) {
            while (lector.hasNextLine()) {
                String linea = lector.nextLine();
                
                String[] partes = linea.split(";");

                int id = Integer.parseInt(partes[0]);
                String ipOrigen = partes[1];
                LocalDateTime fechaHora = LocalDateTime.parse(partes[2], formatoFechas);
                String accion = partes[3];
                String firma = partes[4];

                if (firma.equals("NONE")) {
                    listaRegistros.add(new RegistroForense(id, ip, fecha, accion));
                } else {
                    listaRegistros.add(new RegistroCabala(id, ipOrigen, fecha, accion, firma));

                    if (firma.equals("MERCER_ECHO")) {
                        try{
                            throw new FirmaCabalaDetectadaException("ALERTA CRÍTICA: Firma de cábala detectada en el registro ID ");
                        } catch (FirmaCabalaDetectadaException e) {
                        System.out.println(e.getMessage() + id);
                        }
                    }
                }
            }
            System.out.println("[+] Logs procesados con éxito. Total registros: " + listaRegistros.size());

        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado. ");
        }
    }
}