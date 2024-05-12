package model;

public class Car {
    private static int nextId = 1;     // Variável estática para gerar IDs únicos para os carros
    private int id=0;                     // ID do carro
    private String color;               // Cor do carro
    private String type;                // Tipo do carro

    // Construtor da classe Car que recebe a cor e o tipo do carro como parâmetros
    public Car(String color, String type) {
        this.id = nextId++;     // Atribui um ID único ao carro e incrementa o valor de nextId para o próximo carro
        this.color = color;     // Define a cor do carro
        this.type = type;       // Define o tipo do carro
    }

    // Método para obter o ID do carro
    public int getId() {
        return id;    // Retorna o ID do carro
    }

    // Método para obter a cor do carro
    public String getColor() {
        return color;    // Retorna a cor do carro
    }

    // Método para obter o tipo do carro
    public String getType() {
        return type;    // Retorna o tipo do carro
    }
}
