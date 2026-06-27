import java.util.ArrayList;
import java.util.List;

public class Repartidor {
    private String nombre;
    private Vehiculo vehiculo; // Ahora es un objeto especializado (Moto o Camión)
    private String zonaActual;
    private List<String> paquetesAsignados;
    private boolean disponible;

    public Repartidor(String nombre, Vehiculo vehiculo, String zonaActual) {
        this.nombre = nombre;
        this.vehiculo = vehiculo;
        this.zonaActual = zonaActual;
        this.paquetesAsignados = new ArrayList<>();
        this.disponible = true;
    }

    public String getNombre() { return nombre; }
    public Vehiculo getVehiculo() { return vehiculo; }
    public String getZonaActual() { return zonaActual; }
    public void setZonaActual(String zonaActual) { this.zonaActual = zonaActual; }
    public List<String> getPaquetesAsignados() { return paquetesAsignados; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public void asignarPaquete(String codigoPaquete) {
        this.paquetesAsignados.add(codigoPaquete);
        // Si alcanza el límite de su vehículo, deja de estar disponible
        if (this.paquetesAsignados.size() >= vehiculo.getCapacidadMaximaPaquetes()) {
            this.disponible = false;
        }
    }
}
