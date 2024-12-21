/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SolucaoParalela;

/**
 *
 * @author luizg
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BuscaPrimosThreads {

    //Classe representando uma thread para encontrar números primos
    static class EncontraPrimosThread extends Thread {
        private final int start; //Inicio do intervalo
        private final int end; //Fim do intervalo
        private final String name; //Nome da thread
        private final List<Integer> primos; //Lista de primos encontrados

        public EncontraPrimosThread(String name, int start, int end) {
            this.name = name;
            this.start = start;
            this.end = end;
            this.primos = new ArrayList<>();
        }

        @Override
        public void run() { //Metodo executado pela thread
            System.out.println(name + " começou a buscar primos no intervalo: " + start + " ate " + end);
            for (int i = start; i <= end; i++) {
                if (ehPrimo(i)) { //Verifica se o numero eh primo
                    primos.add(i); //Adiciona o numero a lista de primos
                }
            }
            System.out.println(name + " finalizada");
        }

        //Metodo para verificar se um numero eh primo
        private boolean ehPrimo(int num) {
            if (num < 2) return false;
            for (int i = 2; i <= Math.sqrt(num); i++) {
                if (num % i == 0) return false;
            }
            return true;
        }

        //Retorna a lista de primos
        public List<Integer> getPrimos() {
            return primos;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);

        //Solicita o intervalo
        System.out.println("Indique o intervalo para buscar os primos");
        System.out.print("Inicio: ");
        int inicio = scanner.nextInt();
        System.out.print("Fim: ");
        int fim = scanner.nextInt();

        //Solicita o numero de threads
        System.out.print("Indique o numero de threads: ");
        int numThreads = scanner.nextInt();
        
        //Receber tempo inicial da execucao
        long tempoInicial = System.nanoTime();

        //Divide o intervalo entre as threads
        int rangePorThread = (fim - inicio + 1) / numThreads;
        List<EncontraPrimosThread> threads = new ArrayList<>();

        //Cria e inicia as threads
        for (int i = 0; i < numThreads; i++) {
            int rangeInicio = inicio + i * rangePorThread;
            int rangeFim = (i == numThreads - 1) ? fim : rangeInicio + rangePorThread - 1; //Ultima thread cobre ate o fim do intervalo
            EncontraPrimosThread thread = new EncontraPrimosThread("Thread-" + (i + 1), rangeInicio, rangeFim);
            threads.add(thread);
            thread.start();
        }

        //Espera a thread terminar
        for (EncontraPrimosThread thread : threads) {
            thread.join();
        }

        //Combinar os resultados
        List<Integer> totalPrimos = new ArrayList<>();
        for (EncontraPrimosThread thread : threads) {
            totalPrimos.addAll(thread.getPrimos());
        }
        
        //Receber tempo final da execucao
        long tempoFinal = System.nanoTime();
        double totalTempo = (tempoFinal - tempoInicial) / 1_000_000; //Calculo para obter tempo em ms

        System.out.println("Total de primos encontrados: " + totalPrimos.size());
        System.out.println("Tempo de execucao: " + totalTempo + " ms");
    }
}

