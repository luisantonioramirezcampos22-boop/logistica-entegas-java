import java.util.*;

public class Grafo {
    // Clase interna para almacenar los datos de cada conexión (Arista)
    private static class Conexion {
        int distancia;
        int trafico; // Escala de 0 a 10
        boolean estaCerrada; // RETO 2: Control de calle cerrada

        Conexion(int distancia) {
            this.distancia = distancia;
            this.trafico = 0; 
            this.estaCerrada = false; // Por defecto la calle está abierta
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
        if (zona != null) {
            adjacencia.putIfAbsent(zona.toLowerCase().trim(), new HashMap<>());
        }
    }

    public void agregarConexion(String origen, String destino, int distancia) {
        if (origen == null || destino == null) return;
        
        String o = origen.toLowerCase().trim();
        String d = destino.toLowerCase().trim();
        
        agregarZona(o);
        agregarZona(d);
        
        adjacencia.get(o).put(d, new Conexion(distancia));
        adjacencia.get(d).put(o, new Conexion(distancia)); // Grafo no dirigido
    }

    // RETO 1: Permite actualizar el tráfico de una calle de forma dinámica
    public void actualizarTrafico(String origen, String destino, int nivelTrafico) {
        String o = origen.toLowerCase().trim();
        String d = destino.toLowerCase().trim();
        
        if (adjacencia.containsKey(o) && adjacencia.get(o).containsKey(d)) {
            adjacencia.get(o).get(d).trafico = nivelTrafico;
            adjacencia.get(d).get(o).trafico = nivelTrafico;
            System.out.printf("TRÁFICO: Tráfico actualizado a (+%d) entre '%s' y '%s'.\n", nivelTrafico, origen, destino);
        } else {
            System.out.println(">>> Error: No existe una conexión entre esas zonas.");
        }
    }

    // RETO 2: Cambiar el estado de una calle (Cerrar / Abrir)
    public void setEstadoCalle(String origen, String destino, boolean cerrar) {
        String o = origen.toLowerCase().trim();
        String d = destino.toLowerCase().trim();
        
        if (adjacencia.containsKey(o) && adjacencia.get(o).containsKey(d)) {
            adjacencia.get(o).get(d).estaCerrada = cerrar;
            adjacencia.get(d).get(o).estaCerrada = cerrar;
            String estado = cerrar ? "CERRADA temporalmente" : "ABIERTA nuevamente";
            System.out.printf("La calle entre '%s' y '%s' ahora está %s.\n", origen, destino, estado);
        } else {
            System.out.println(">>> Error: No existe una conexión entre esas zonas.");
        }
    }

    // Búsqueda de rutas mediante DFS 
    public boolean buscarRutaDFS(String actual, String destino, Set<String> visitados, List<String> ruta) {
        String act = actual.toLowerCase().trim();
        String dest = destino.toLowerCase().trim();
        
        visitados.add(act);
        // Aqui se guarda el nombre formateado estéticamente
        String nombreFormateado = act.substring(0, 1).toUpperCase() + act.substring(1);
        ruta.add(nombreFormateado); 

        if (act.equals(dest)) {
            return true;
        }

        if (adjacencia.get(act) != null) {
            for (Map.Entry<String, Conexion> entrada : adjacencia.get(act).entrySet()) {
                String vecino = entrada.getKey();
                Conexion conexion = entrada.getValue();

                // RETO 2: Si la calle está cerrada, el DFS la ignora por completo
                if (!visitados.contains(vecino) && !conexion.estaCerrada) {
                    if (buscarRutaDFS(vecino, dest, visitados, ruta)) {
                        return true;
                    }
                }
            }
        }

        ruta.remove(ruta.size() - 1);
        return false;
    }

    // Aqui se calcula el costo considerando la fórmula del Reto 1
    public int calcularDistanciaRuta(String actual, String destino) {
        if (actual == null || destino == null) return Integer.MAX_VALUE;
        
        List<String> ruta = new ArrayList<>();
        Set<String> visitados = new HashSet<>();
        
        String inicio = actual.toLowerCase().trim();
        String fin = destino.toLowerCase().trim();
        
        if (buscarRutaDFS(inicio, fin, visitados, ruta)) {
            int pesoTotal = 0;
            for (int i = 0; i < ruta.size() - 1; i++) {
                String nodoA = ruta.get(i).toLowerCase().trim();
                String nodoB = ruta.get(i + 1).toLowerCase().trim();
                pesoTotal += adjacencia.get(nodoA).get(nodoB).getPesoTotal();
            }
            return pesoTotal;
        }
        return Integer.MAX_VALUE; // Retorna infinito si no hay ruta disponible (o si está bloqueada)
    }
}
