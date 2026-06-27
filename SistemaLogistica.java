import java.io.*;
import java.util.*;

public class SistemaLogistica {
    private Map<String, Cliente> clientes = new HashMap<>();
    private Map<String, Paquete> paquetes = new HashMap<>();
    private List<Repartidor> repartidores = new ArrayList<>();
    private Grafo mapaCiudad = new Grafo();

    public void registrarCliente(Cliente c) { clientes.put(c.getId(), c); }
    public void registrarPaquete(Paquete p) { 
        paquetes.put(p.getCodigo(), p); 
        if (clientes.containsKey(p.getClienteId())) {
            clientes.get(p.getClienteId()).agregarPedido(p.getCodigo());
        }
    }
    public void registrarRepartidor(Repartidor r) { repartidores.add(r); }
    public Grafo getMapaCiudad() { return mapaCiudad; }

    // Ordenamiento Bubble Sort por peso (Fase 2)
    public List<Paquete> ordenarPaquetesPorPeso() {
        List<Paquete> listaPaquetes = new ArrayList<>(paquetes.values());
        int n = listaPaquetes.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (listaPaquetes.get(j).getPeso() > listaPaquetes.get(j + 1).getPeso()) {
                    Paquete temp = listaPaquetes.get(j);
                    listaPaquetes.set(j, listaPaquetes.get(j + 1));
                    listaPaquetes.set(j + 1, temp);
                }
            }
        }
        return listaPaquetes;
    }

    // Inteligencia de Asignación (Fase 3)
    public Repartidor buscarRepartidorOptimo(Paquete paquete) {
        Repartidor mejorRepartidor = null;
        int menorPesoRuta = Integer.MAX_VALUE;
        int menorCargaPaquetes = Integer.MAX_VALUE;

        for (Repartidor r : repartidores) {
            if (!r.isDisponible()) continue;

            // Calcula el peso de la ruta (distancia + tráfico debido al Reto 1)
            int pesoRuta = mapaCiudad.calcularDistanciaRuta(r.getZonaActual(), paquete.getDestino());
            if (pesoRuta == Integer.MAX_VALUE) continue;

            if (pesoRuta < menorPesoRuta) {
                menorPesoRuta = pesoRuta;
                menorCargaPaquetes = r.getPaquetesAsignados().size();
                mejorRepartidor = r;
            } else if (pesoRuta == menorPesoRuta) {
                if (r.getPaquetesAsignados().size() < menorCargaPaquetes) {
                    menorCargaPaquetes = r.getPaquetesAsignados().size();
                    mejorRepartidor = r;
                }
            }
        }
        return mejorRepartidor;
    }

    public void procesarAsignacionInteligente(Paquete paquete) {
        Repartidor optimo = buscarRepartidorOptimo(paquete);
        if (optimo != null) {
            optimo.asignarPaquete(paquete.getCodigo());
            paquete.setEstado("En ruta");
            System.out.printf(">>> [ASIGNADO] Paquete %s asignado a %s (%s). Costo de Ruta actual: %d unidades.\n", 
                    paquete.getCodigo(), optimo.getNombre(), optimo.getVehiculo().getTipo(), 
                    mapaCiudad.calcularDistanciaRuta(optimo.getZonaActual(), paquete.getDestino()));
        } else {
            System.out.printf(">>> [ALERTA] Sin repartidores disponibles para el destino: %s\n", paquete.getDestino());
        }
    }

    // Persistencia JSON en texto plano (Fase 1)
    public void guardarDatos() {
        try (PrintWriter out = new PrintWriter(new FileWriter("clientes.json"))) {
            out.println("[");
            int i = 0;
            for (Cliente c : clientes.values()) {
                out.printf("  {\"id\":\"%s\", \"nombre\":\"%s\", \"direccion\":\"%s\"}%s\n",
                        c.getId(), c.getNombre(), c.getDireccion(), (++i < clientes.size() ? "," : ""));
            }
            out.println("]");
            System.out.println(">>> Base de datos guardada en 'clientes.json'.");
        } catch (IOException e) {
            System.out.println("Error al guardar persistencia: " + e.getMessage());
        }
    }

    // MENÚ PRINCIPAL INTERACTIVO
    public static void main(String[] args) {
        SistemaLogistica sistema = new SistemaLogistica();
        Scanner scanner = new Scanner(System.in);

        // Inicialización de la Ciudad por defecto
        sistema.getMapaCiudad().agregarConexion("Centro", "Norte", 5);
        sistema.getMapaCiudad().agregarConexion("Centro", "Sur", 4);
        sistema.getMapaCiudad().agregarConexion("Sur", "Este", 3);

        // Inicialización de Repartidores (Moto vs Camión)
        sistema.registrarRepartidor(new Repartidor("Marcos", new Moto(), "Centro"));
        sistema.registrarRepartidor(new Repartidor("Elena", new Camion(), "Sur"));

        int opcion = 0;
        do {
            System.out.println("Ssistema De Logistica De Entregas Inteligente");
            System.out.println("1. Registrar nuevo Cliente");
            System.out.println("2. Registrar Paquete (Asignación Inteligente)");
            System.out.println("3. Actualizar Tráfico de la Ciudad (Reto 1)");
            System.out.println("4. Ver Reporte de Paquetes (Ordenados por Peso)");
            System.out.println("5. Guardar y Salir");
            System.out.print("Seleccione una opción: ");
            
            try {
                opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar buffer
            } catch (Exception e) {
                System.out.println("Por favor, ingrese un número válido.");
                scanner.nextLine();
                continue;
            }

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese ID del Cliente (ej. CLI01): ");
                    String id = scanner.nextLine();
                    System.out.print("Ingrese Nombre: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Ingrese Dirección / Zona de entrega: ");
                    String direccion = scanner.nextLine();
                    
                    sistema.registrarCliente(new Cliente(id, nombre, direccion));
                    System.out.println(">>> Cliente registrado con éxito.");
                    break;

                case 2:
                    System.out.print("Ingrese código de paquete: ");
                    String cod = scanner.nextLine();
                    System.out.print("Ingrese peso en kg: ");
                    double peso = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Ingrese Zona Destino (Centro/Norte/Sur/Este): ");
                    String dest = scanner.nextLine();
                    System.out.print("Ingrese ID del Cliente asociado: ");
                    String cliId = scanner.nextLine();

                    Paquete nuevoPaquete = new Paquete(cod, peso, dest, cliId);
                    sistema.registrarPaquete(nuevoPaquete);
                    sistema.procesarAsignacionInteligente(nuevoPaquete);
                    break;

                case 3:
                    System.out.print("Ingrese Zona Origen de la calle: ");
                    String zo = scanner.nextLine();
                    System.out.print("Ingrese Zona Destino de la calle: ");
                    String zd = scanner.nextLine();
                    System.out.print("Ingrese nivel de tráfico extra (0 a 10): ");
                    int trafico = scanner.nextInt();
                    
                    sistema.getMapaCiudad().actualizarTrafico(zo, zd, trafico);
                    break;

                case 4:
                    System.out.println("\n--- REPORTE DE INVENTARIO POR PESO ---");
                    List<Paquete> ordenados = sistema.ordenarPaquetesPorPeso();
                    if(ordenados.isEmpty()) System.out.println("No hay paquetes registrados.");
                    for (Paquete p : ordenados) {
                        System.out.printf("- Código: %s | Peso: %.2f kg | Destino: %s | Estado: %s\n",
                                p.getCodigo(), p.getPeso(), p.getDestino(), p.getEstado());
                    }
                    break;

                case 5:
                    sistema.guardarDatos();
                    System.out.println("Saliendo del sistema de entregas. ¡Buen viaje!");
                    break;

                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 5);

        scanner.close();
    }
}
