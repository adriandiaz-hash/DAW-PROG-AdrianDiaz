package modelo;

import java.io.Serializable;

/**
 * Clase que representa una tarea pendiente dentro de la aplicación GestCourse.
 * Gestiona la información básica, prioridades y fechas de entrega de las asignaturas.
 * 
 * @autor Adrián Díaz García
 */
public class Tarea implements Serializable{
    
    private final int codigo;
    private final String titulo;
    private final String prioridad;
    private final String modulo;
    private final String fechaCreacion;
    private final String fechaEntrega;
    private String estado;

    public Tarea(int codigo, String titulo, String prioridad, String modulo, String fechaCreacion, String fechaEntrega) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.prioridad = prioridad;
        this.modulo = modulo;
        this.fechaCreacion = fechaCreacion;
        this.fechaEntrega = fechaEntrega;
        this.estado = "Pendiente";
    }

    public int getCodigo() {
        return codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public String getModulo() {
        return modulo;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Tarea [" + codigo + "] " + titulo + " | Módulo: " + modulo + " | Estado: " + estado;
    }
}