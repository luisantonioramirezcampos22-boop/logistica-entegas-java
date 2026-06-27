public class Moto extends Vehiculo {
    public Moto() {
        // Viaja rápido, pero tiene poca capacidad (máximo 3 paquetes)
        super("Moto", 3, "Rápido");
    }

    @Override
    public double calcularTiempoViaje(int distanciaKm) {
        // Supongamos que la moto va a una velocidad promedio de 60 km/h (1 km por minuto)
        return distanciaKm * 1.0; 
    }
}
