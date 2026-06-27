import java.util.*;

public class Grafo {
    // Clase interna para almacenar los datos de cada conexión (Arista)
    private static class Conexion {
        int distancia;
        int trafico; // Escala de 0 (libre) a 10 (embotellamiento)

        Conexion(int distancia) {
            this.distancia = distancia;
            this.trafico = 0; // Por defecto inicia sin tráfico
        }

        // RETO 1: Fórmula de Peso Dinámico
        int getPesoTotal() {
            return this.distancia + this.trafico;
        }
    }

    // Grafo: Nodo origen -> Nodo destino -> Datos de la Conexión
    private Map<String, Map<String, Conexion>> adjacencia;

    public Grafo() {
        this.adjacencia = new HashMap<>();
    }

    public void agregarZona(String zona) {
        adjacencia.putIfAbsent(zona.toLowerCase(), new HashMap<>());
    }

    public void agregarConexion(String origen, String destino, int distancia) {
        String o = origen.toLowerCase();
        String d = destino.toLowerCase();
        agregarZona(o);
        agregarZona(d);
        adjacencia.get(o).put(d, new Conexion(distancia));
        adjacencia.get(d).put(o, new Conexion(distancia)); // Grafo no dirigido
    }

    // Permite actualizar el tráfico de una calle de forma dinámica
    public void actualizarTrafico(String origen, String destino, int nivelTrafico) {
        String o = origen.toLowerCase();
        String d = destino.toLowerCase();
        if (adjacencia.containsKey(o) && adjacencia.get(o).containsKey(d)) {
            adjacencia.get(o).get(d).trafico = nivelTrafico;
            adjacencia.get(d).get(o).trafico = nivelTrafico;
            System.out.println(">>> Tráfico actualizado entre " + origen + " y " + destino + ".");
        } else {
            System.out.println(">>> Error: No existe una conexión entre esas zonas.");
        }
    }

    // Búsqueda de rutas mediante DFS
    public boolean buscarRutaDFS(String actual, String destino, Set<String> visitados, List<String> ruta) {
        String act = actual.toLowerCase();
        String dest = destino.toLowerCase();
        
        visitados.add(act);
        ruta.add(actual); // Guardamos el nombre original para la vista del usuario

        if (act.equals(dest)) {
            return true;
        }

        if (adjacencia.get(act) != null) {
            for (String vecino : adjacencia.get(act).keySet()) {
                if (!visitados.contains(vecino)) {
                    // Restauramos capitalización simple para la ruta visual
                    String nombreOriginalVecino = vecino.substring(0, 1).toUpperCase() + vecino.substring(1);
                    if (buscarRutaDFS(vecino, dest, visitados, ruta)) {
                        return true;
                    }
                }
            }
        }

        ruta.remove(ruta.size() - 1);
        return false;
    }

    // Calcula el costo considerando la fórmula del Reto 1
    public int calcularDistanciaRuta(String actual, String destino) {
        List<String> ruta = new ArrayList<>();
        Set<String> visitados = new HashSet<>();
        
        if (buscarRutaDFS(actual.toLowerCase(), destino.toLowerCase(), visitados, ruta)) {
            int pesoTotal = 0;
            for (int i = 0; i < ruta.size() - 1; i++) {
                String nodoA = ruta.get(i).toLowerCase();
                String nodoB = ruta.get(i + 1).toLowerCase();
                pesoTotal += adjacencia.get(nodoA).get(nodoB).getPesoTotal();
            }
            return pesoTotal;
        }
        return Integer.MAX_VALUE;
    }
}