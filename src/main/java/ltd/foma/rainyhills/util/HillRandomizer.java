package ltd.foma.rainyhills.util;

import java.util.Random;

public class HillRandomizer {
    private static final int minLength = 3;
    private static final int maxLength = 50;

    private static final int minHeight = -3;
    private static final int maxHeight = 15;
    private static final Random RANDOM = new Random();

    /**
     * Generates a random height map for hills as int array
     */
    public static int[] getRandomMapInt(){
        int length = RANDOM.nextInt(maxLength - minLength) + minLength;

        int[] map = new int[length];

        for (int i = 0; i < length; i++) {
            int height = RANDOM.nextInt(maxHeight - minHeight) + minHeight;
            map[i] = height;
        }
        return map;
    }

    /**
     * Generates a random height map for hills as string
     */
    public static String getRandomMapString(){
        StringBuilder sb = new StringBuilder();

        int length = RANDOM.nextInt(maxLength - minLength) + minLength;
        for (int i = 0; i < length; i++) {
            if (i > 0){
                sb.append(',');
            }
            int height = RANDOM.nextInt(maxHeight - minHeight) + minHeight;
            sb.append(height);
        }
        return sb.toString();
    }
}
