import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private String id;
    private String nombre;
    private String direccion;
    private List<String> historialPedidos; // Guarda los códigos de los paquetes

    public Cliente(String id, String nombre, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.historialPedidos = new ArrayList<>();
    }

    // Getters y Setters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public List<String> getHistorialPedidos() { return historialPedidos; }
    
    public void agregarPedido(String codigoPaquete) {
        this.historialPedidos.add(codigoPaquete);
    }
}
