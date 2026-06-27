public abstract class Vehiculo {
    private String tipo;
    private int capacidadMaximaPaquetes;
    private String velocidadDesempenio;

    public Vehiculo(String tipo, int capacidadMaximaPaquetes, String velocidadDesempenio) {
        this.tipo = tipo;
        this.capacidadMaximaPaquetes = capacidadMaximaPaquetes;
        this.velocidadDesempenio = velocidadDesempenio;
    }

    public String getTipo() { return tipo; }
    public int getCapacidadMaximaPaquetes() { return capacidadMaximaPaquetes; }
    public String getVelocidadDesempenio() { return velocidadDesempenio; }

    // Método abstracto para calcular el tiempo estimado basado en el tipo de vehículo
    public abstract double calcularTiempoViaje(int distanciaKm);
}
