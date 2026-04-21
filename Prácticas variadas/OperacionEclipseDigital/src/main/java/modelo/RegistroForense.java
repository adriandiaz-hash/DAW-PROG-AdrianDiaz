public class RegistroForense {
    private int id;
    private String ipOrigen;
    private LocalDateTime fechaHora;
    private String accion;

    public RegistroForense(int id, String ipOrigen, LocalDateTime fechaHora, String accion) {
        this.id = id;
        this.ipOrigen = ipOrigen;
        this.fechaHora = fechaHora;
        this.accion = accion;
    }

    public int getId() {
        return id;
    }

    public String getIpOrigen() {
        return ipOrigen;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getAccion() {
        return accion;
    }

    public double analizarRiesgo() {
        double riesgo = 10.0;
        return riesgo;
    }