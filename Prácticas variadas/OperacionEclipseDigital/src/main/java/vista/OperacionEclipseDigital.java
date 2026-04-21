package vista;

import java.io.FileNotFoundException;

java.util.Scanner;
java.io.File;

/** * Clase principal de la aplicación Operación Eclipse Digital.
 * @autor Adrián
 */
public class OperacionEclipseDigital {

    public static void main(String[] args) {
        System.out.println("Hello World!");

        Scanner scanner = new Scanner(System.in);

        try (Scanner lector = new Scanner(new File("raw.logs.txt"))) {
            while (lector.hasNextLine()) {
                String linea = lector.nextLine();
                System.out.println(linea);
                String[] partes = linea.split(";");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado. ");
        }
    }
}
