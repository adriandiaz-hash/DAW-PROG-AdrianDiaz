package controlador;

import java.io.*;
import java.util.ArrayList;
import modelo.Tarea;

/**
 * Clase Controlador: Gestiona la lógica de negocio y el almacenamiento 
 * de las tareas, separando los datos de la interfaz visual.
 * @autor [T] Adrián Díaz García
 */
public class GestorTareas {
    
    private ArrayList<Tarea> listaTareas;
    private final String ARCHIVO_DATOS = "tareas.dat";

    public GestorTareas() {
        cargarDatos();
    }

    public void registrarTarea(Tarea nuevaTarea) {
        listaTareas.add(nuevaTarea);
        guardarDatos();
    }

    public ArrayList<Tarea> obtenerTodasLasTareas() {
        return listaTareas;
    }    

    private void guardarDatos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_DATOS))) {
            oos.writeObject(listaTareas);
        } catch (Exception e) {
            System.err.println("Fallo al guardar la memoria: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void cargarDatos() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_DATOS))) {
            listaTareas = (ArrayList<Tarea>) ois.readObject();
        } catch (Exception e) {

            listaTareas = new ArrayList<>();
        }
    }

    public void actualizarMemoria() {
        guardarDatos();
    }
    
    public boolean eliminarTarea(int codigo) {
        boolean eliminada = false;
        int i = 0;
        while (i < listaTareas.size() && !eliminada) {
            if (listaTareas.get(i).getCodigo() == codigo) {
                listaTareas.remove(i);
                guardarDatos();
                eliminada = true;
            }
            i++;
        }
        return eliminada;
    }
}