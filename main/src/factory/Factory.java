package factory;

import java.util.logging.Logger;
import production.ProductionStation;
import util.CircularBuffer;

public class Factory {
    private static final int MAX_STOCK = 500;                // Capacidade máxima do estoque de peças
    private CircularBuffer<model.Car> storage;               // Buffer circular para armazenar os carros produzidos
    @SuppressWarnings("unused")
    private Logger logger;                                   // Logger para registrar atividades da fábrica

    // Construtor da classe Factory
    public Factory() {
        this.storage = new CircularBuffer<>(40);             // Inicializa o buffer circular de armazenamento com capacidade 40
        this.logger = Logger.getLogger("logs_da_Fabrica");       // Inicializa o logger com o nome "logs_da_Fabrica"
    }

    // Método para iniciar a produção na fábrica
    public void startProduction() {
        ProductionStation[] stations = new ProductionStation[4];   // Array para armazenar as estações de produção
        for (int i = 0; i < 4; i++) {
            int stationId = i;
            // Inicializa e configura cada estação de produção
            stations[i] = new ProductionStation(stationId, storage, Logger.getLogger("ProductionStation" + stationId), this);
            // Inicia uma nova thread para cada estação de produção
            new Thread(() -> {
                while (true) {
                    try {
                        // Inicia a produção de um carro na estação
                        stations[stationId].produceCar();
                        Thread.sleep((long) (Math.random() * 1000));   // Simula o tempo de produção
                    } catch (InterruptedException e) {
                        e.printStackTrace();    // Trata a exceção caso o thread seja interrompido
                    }
                }
            }).start();   // Inicia a thread
        }
    }

    // Método para obter o número de peças restantes no estoque
    public int getRemainingStock() {
        return MAX_STOCK - storage.getCurrentSize();   // Calcula e retorna o número de peças restantes no estoque
    }

    // Método principal (main) para iniciar a produção na fábrica
    public static void main(String[] args) {
        Factory factory = new Factory();   // Cria uma instância da fábrica
        factory.startProduction();         // Inicia a produção na fábrica

        try {
            Thread.sleep(30000);    // Aguarda 30 segundos 
        } catch (InterruptedException e) {
            e.printStackTrace();    // Trata a exceção caso o thread seja interrompido
        }

        @SuppressWarnings("unused")
        int remainingStock = factory.getRemainingStock();   // Obtém o número de peças restantes no estoque
        // System.out.println("Peças restantes: " + remainingStock);   // Imprime o número de peças restantes no estoque
    }
}
