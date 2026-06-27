public class Camion extends Vehiculo {
    public Camion() {
        // Viaja lento, pero tiene alta capacidad (máximo 15 paquetes)
        super("Camión", 15, "Lento");
    }

    @Override
    public double calcularTiempoViaje(int distanciaKm) {
        // Supongamos que el camión va más lento debido al tráfico, a 30 km/h (2 minutos por km)
        return distanciaKm * 2.0;
    }
}
