import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ProblemPlecakowy {

    public static void main(String[] args) {
        String filename = "/path/6.txt";
        try {
            KnapsackProblem problem = readInput(filename);
            long startTime = System.currentTimeMillis();
            Result result = solveKnapsack(problem.capacity, problem.items);
            long endTime = System.currentTimeMillis();
            System.out.println();
            System.out.println("Final result");
            System.out.println("Best characteristic vector: " + result.vector);
            System.out.println("Total weight: " + result.totalWeight);
            System.out.println("Total value: " + result.totalValue);
            System.out.println("Execution time: " + (endTime - startTime) + " ms");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //KnapsackProblem jest pomocniczą klasą używaną do przechowywania danych problemu plecakowego.
    // Jej celem jest zapewnienie wygodnego sposobu na przekazanie danych o pojemności plecaka oraz
    // danych o przedmiotach (ich wagach i wartościach) w jednym obiekcie.
    static class KnapsackProblem {
        int capacity;
        int[][] items; // items[i][0] to waga, items[i][1] to wartosc

        KnapsackProblem(int capacity, int[][] items) {
            this.capacity = capacity;
            this.items = items;
        }
    }

   // Używana jest do przekazywania tych informacji z metody solveKnapsack do głównego programu, gdzie wyniki są wypisywane na ekran.
    static class Result {
        String vector;
        int totalWeight;
        int totalValue;

        Result(String vector, int totalWeight, int totalValue) {
            this.vector = vector;
            this.totalWeight = totalWeight;
            this.totalValue = totalValue;
        }
    }

    //Metoda odczytujaca dane z pliku +
    public static KnapsackProblem readInput(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        int capacity = Integer.parseInt(reader.readLine().trim()); //Odczytuje pierwszą linię pliku, która zawiera pojemność plecaka, przekształca ją na liczbę całkowitą i zapisuje w zmiennej
        String line;
        int itemCount = 0;

        while ((line = reader.readLine()) != null) {
            itemCount++;
        }

        reader.close();

        int[][] items = new int[itemCount][2]; //Tworzy dwuwymiarową tablicę items, gdzie każda linia zawiera dwa elementy: wagę i wartość przedmiotu.
        reader = new BufferedReader(new FileReader(filename));
        reader.readLine(); // Pomijamy 1 linie

        int index = 0;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(" "); //String[] parts = line.split(" ");: Dzieli linię na części przy użyciu spacji jako separatora. parts[0] zawiera wagę przedmiotu, a parts[1] zawiera wartość przedmiotu.
            items[index][0] = Integer.parseInt(parts[0]); // items[index][0] = Integer.parseInt(parts[0]);: Przekształca wagę na liczbę całkowitą i zapisuje w tablicy items w kolumnie 0.
            items[index][1] = Integer.parseInt(parts[1]);
            index++;
        }

        reader.close();
        return new KnapsackProblem(capacity, items);
    }

    public static Result solveKnapsack(int capacity, int[][] items) {
        int n = items.length;
        int bestValue = 0;
        int bestWeight = 0;
        String bestVector = "";

        // Iteracja przez wszystkie możliwe kombinacje przedmiotów
        for (int i = 0; i < (1 << n); i++) { //(1 << n) = 2^n (wszystkie mozliwe kombinacje)
            int totalWeight = 0;
            int totalValue = 0;
            StringBuilder vector = new StringBuilder();

            // Sprawdzanie, które przedmioty są w bieżącej kombinacji
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) > 0) {// if ((i & (1 << j)) > 0): Sprawdza, czy przedmiot j jest w bieżącej kombinacji. Operator bitowy & (AND) sprawdza, czy bit j w liczbie i jest ustawiony na 1.
                    totalWeight += items[j][0];
                    totalValue += items[j][1];
                    vector.append("1"); //Jeśli przedmiot j jest wybrany (bit jest ustawiony na 1):
                } else {
                    vector.append("0"); //Jeśli przedmiot j nie jest wybrany (bit jest ustawiony na 0):
                }
            }

            // Sprawdzenie, czy bieżąca kombinacja jest dopuszczalna
            if (totalWeight <= capacity) {
                // Sprawdzenie, czy bieżąca kombinacja jest najlepsza
                if (totalValue > bestValue) {
                    bestValue = totalValue;
                    bestWeight = totalWeight;
                    bestVector = vector.toString();
                    System.out.println("New best vector found: " + bestVector);
                    System.out.println("Total weight: " + bestWeight);
                    System.out.println("Total value: " + bestValue);
                }
            }
        }

        return new Result(bestVector, bestWeight, bestValue);
    }
}
