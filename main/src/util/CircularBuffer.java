package util;

import model.Car;

public class CircularBuffer<T> {
    private T[] buffer;           // Armazena os elementos do buffer
    private int capacity;         // Capacidade máxima do buffer
    private int size;             // Tamanho atual do buffer
    private int writeIndex;       // Índice de escrita
    private int readIndex;        // Índice de leitura

    @SuppressWarnings("unchecked")
    public CircularBuffer(int capacity) {
        this.capacity = capacity;   // Define a capacidade máxima do buffer
        this.buffer = (T[]) new Object[capacity];  // Inicializa o buffer com o tamanho especificado
        this.size = 0;              // Inicializa o tamanho atual como zero
        this.writeIndex = 0;        // Inicializa o índice de escrita como zero
        this.readIndex = 0;         // Inicializa o índice de leitura como zero
    }

    // Método para adicionar um item ao buffer
    public synchronized void put(T item) {
        // Aguarda até que haja espaço disponível no buffer
        while (size == capacity) {
            try {
                wait();  // Aguarda até que outro thread notifique sobre a disponibilidade de espaço
            } catch (InterruptedException e) {
                e.printStackTrace();  // Trata a interrupção do thread
            }
        }
        buffer[writeIndex] = item;       // Adiciona o item ao buffer na posição de escrita
        writeIndex = (writeIndex + 1) % capacity;  // Atualiza o índice de escrita considerando o limite do buffer circular
        size++;                          // Incrementa o tamanho atual do buffer
        notifyAll();                     // Notifica todos os threads que estavam esperando
    }

    // Método para remover e retornar um item do buffer
    public synchronized T take() {
        // Aguarda até que haja pelo menos um item no buffer
        while (size == 0) {
            try {
                wait();   // Aguarda até que outro thread notifique sobre a disponibilidade de itens
            } catch (InterruptedException e) {
                e.printStackTrace();  // Trata a interrupção do thread
            }
        }
        T item = buffer[readIndex];      // Obtém o item do buffer na posição de leitura
        readIndex = (readIndex + 1) % capacity;  // Atualiza o índice de leitura considerando o limite do buffer circular
        size--;                          // Decrementa o tamanho atual do buffer
        notifyAll();                     // Notifica todos os threads que estavam esperando
        return item;                     // Retorna o item removido do buffer
    }

    // Método para obter o tamanho atual do buffer
    public synchronized int getCurrentSize() {
        return size;    // Retorna o tamanho atual do buffer
    }

    public void colocarCarro(Car car) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'colocarCarro'");
    }

    public Object getTamanho() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTamanho'");
    }
}
