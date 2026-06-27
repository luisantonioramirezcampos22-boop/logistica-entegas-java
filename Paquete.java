public class Paquete {
    private String codigo;
    private double peso;
    private String destino;
    private String estado; // Pendiente, En ruta, Entregado, Cancelado
    private String clienteId; // Relación con el ID del Cliente

    public Paquete(String codigo, double peso, String destino, String clienteId) {
        this.codigo = codigo;
        this.peso = peso;
        this.destino = destino;
        this.estado = "Pendiente";
        this.clienteId = clienteId;
    }

    // Getters y Setters
    public String getCodigo() { return codigo; }
    public double getPeso() { return peso; }
    public String getDestino() { return destino; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getClienteId() { return clienteId; }
}
