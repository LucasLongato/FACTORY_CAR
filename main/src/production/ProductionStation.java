package production;

import java.util.concurrent.Semaphore;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import model.Car;
import util.CircularBuffer;
import factory.Factory;

public class ProductionStation {
    private int id;                             // ID da estação de produção
    private Semaphore leftTool;                 // Semáforo para a ferramenta esquerda
    private Semaphore rightTool;                // Semáforo para a ferramenta direita
    private Semaphore assemblyLine;             // Semáforo para a linha de montagem
    private CircularBuffer<Car> storage;        // Buffer circular para armazenar os carros produzidos
    private Logger logger;                      // Logger para registrar as atividades da estação de produção
    private Factory factory;                    // Referência para a fábrica para acessar o estoque de peças
    private int carsProduced;                   // Contador para acompanhar o número de carros produzidos

    // Construtor da classe ProductionStation
    public ProductionStation(int id, CircularBuffer<Car> storage, Logger logger, Factory factory) {
        this.id = id;                                       // Define o ID da estação
        this.leftTool = new Semaphore(1);                   // Inicializa o semáforo para a ferramenta esquerda
        this.rightTool = new Semaphore(1);                  // Inicializa o semáforo para a ferramenta direita
        this.assemblyLine = new Semaphore(5);               // Inicializa o semáforo para a linha de montagem
        this.storage = storage;                             // Define o buffer circular de armazenamento
        this.logger = logger;                               // Define o logger para registrar as atividades
        this.factory = factory;                             // Define a referência para a fábrica
       
        // Configura o formato do logger
        Formatter formatter = new Formatter() {
            @Override
            public String format(LogRecord record) {
                return record.getMessage() + "\n";
            }
        };
        
        // Inicializa o FileHandler para registrar as atividades em um arquivo
        FileHandler fh;
        try {
            fh = new FileHandler("logs_da_Fabrica.txt", true);
            fh.setFormatter(formatter);
            logger.addHandler(fh);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para produzir um carro
    public void produceCar() throws InterruptedException {
        // Registra que o funcionário pegou as ferramentas para iniciar a produção
        logger.info(String.format("Funcionário da ESTAÇÃO ID: %d pegou as ferramentas para iniciar a produção do carro.", id ));
        
        // Adquire os semáforos para a linha de montagem e as ferramentas esquerda e direita
        assemblyLine.acquire();
        leftTool.acquire();
        rightTool.acquire();

        // Cria um novo carro com cor e tipo aleatórios
        Car car = new Car(getRandomColor(), getRandomType());
        
        // Armazena o carro no buffer circular
        storage.put(car);
        
        // Registra a produção do carro
        logProduction(car);

        // Libera os semáforos para a linha de montagem e as ferramentas esquerda e direita
        leftTool.release();
        rightTool.release();
        assemblyLine.release();
    }

    // Método privado para gerar uma cor aleatória para o carro
    private String getRandomColor() {
        String[] colors = {"RED", "GREEN", "BLUE"};
        return colors[(int) (Math.random() * colors.length)];
    }

    // Método privado para gerar um tipo aleatório para o carro
    private String getRandomType() {
        String[] types = {"SEDAN", "SUV"};
        return types[(int) (Math.random() * types.length)];
    }

    // Método privado para registrar a produção do carro
    private void logProduction(Car car) {
        carsProduced++;   // Incrementa o contador de carros produzidos
        // Registra as informações do carro produzido e o número de peças restantes na fábrica
        logger.info(String.format("Carro ID: %d | Cor: %s | Tipo: %s | ESTAÇÃO ID: %d | Peças restantes: %d",
                car.getId(), car.getColor(), car.getType(), id, factory.getRemainingStock()));
    }
}
