public class RegistroCabala extends RegistroForense {
    private String firmaOculta;

    public RegistroCabala(int id, String ipOrigen, LocalDateTime fechaHora, String accion, String firmaOculta) {
        super(id, ipOrigen, fechaHora, accion);
        this.firmaOculta = firmaOculta;
    }

    @Override
    public double analizarRiesgo() {
        double riesgo = 99.0;
        return riesgo;
    }
}