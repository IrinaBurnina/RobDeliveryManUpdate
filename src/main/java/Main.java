import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class Main {
    static final String LETTERS = "RLRHR";
    static final int LENGTH = 100;
    static final int NUMBER_OF_THREADS = 1000;
    public static Map<Integer, Integer> statMap = new TreeMap<>();

    public static void main(String[] args) {
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
                }
            }).start();
        }
        synchronized (statMap) {
            Map.Entry<Integer, Integer> max = statMap
                    .entrySet()
                    .stream().max(Map.Entry.comparingByValue())
                    .get();
            System.out.println("Самое частое количество повторений: " + max.getKey() + " встретилось "
                    + max.getValue() + " раз");
            System.out.println("Другие размеры:");
            for (Integer key : statMap.keySet()) {
                System.out.println(" -" + key + "  (" + statMap.get(key) + " раз)");
            }
        }
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
