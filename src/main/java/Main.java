import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class Main {
    static final String LETTERS = "RLRHR";
    static final int LENGTH = 100;
    static final int NUMBER_OF_THREADS = 1000;
    public static Map<Integer, Integer> statMap = new TreeMap<>();

    public static void main(String[] args) {
        Thread threadPrinter = new Thread(() -> {
            while (!Thread.interrupted()) {
                synchronized (statMap) {
                    try {
                        statMap.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                    printer();
                }
            }
        });
        threadPrinter.start();
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            new Thread(() -> {
                String route = generateRoute(LETTERS, LENGTH);
                int rotatesRight = (int) route.chars().filter(ch -> ch == 'R').count();
                synchronized (statMap) {
                    if (statMap.containsKey(rotatesRight)) {
                        statMap.put(rotatesRight, statMap.get(rotatesRight) + 1);
                    } else {
                        statMap.put(rotatesRight, 1);
                    }
                    statMap.notify();
                }
            }).start();
        }
        threadPrinter.interrupt();
    }

    public static void printer() {
        Map.Entry<Integer, Integer> max = statMap
                .entrySet()
                .stream().max(Map.Entry.comparingByValue())
                .get();
        System.out.println("Текущий лидер: " + max.getKey() + " встретилось "
                + max.getValue() + " раз");
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}