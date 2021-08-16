package ltd.foma.rainyhills.util;

import ltd.foma.rainyhills.data.RainCalculationData;
import org.slf4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * A supplementary tool for visualising height map as a string
 */
public class HillVisualizer {
    private static final Logger log = getLogger( MethodHandles.lookup().lookupClass() );

    public static void main(String[] args) {
        RainCalculationData data = new RainCalculationData();

        ArrayList<Integer> heightMap = new ArrayList<>();
        heightMap.add(-2);
        heightMap.add(-3);
        heightMap.add(-2);
        data.setHeightMap(heightMap);

        int[] waterMap = new int[]{0,1,0};
        data.setWaterMap(waterMap);

        log.info("{}",drawWetMap(data));
    }

    /**
     * Draw hills with calculated water added
     */
    public static String drawWetMap(RainCalculationData calculationResult){
        var heightMap = calculationResult.getHeightMap();
        var waterMap = calculationResult.getWaterMap();

        //get minimal height in the height map if it's < 0
        //to avoid map cropping with less-than-zero values
        Integer minHeight = Math.min(0, heightMap.stream()
                .mapToInt(v -> v)
                .min()
                .orElseThrow(NoSuchElementException::new));

        StringBuilder sb = new StringBuilder();

        //below-zero meter
        if (minHeight < 0){
            sb.append("| ".repeat(Math.abs(minHeight)-1))
                .append("0")
                .append("\n")
                .append("\n");
        }

        for (int i = 0; i < heightMap.size(); i++) {
            int heightValue = heightMap.get(i) - minHeight;
            sb.append("X ".repeat(heightValue));
            sb.append(". ".repeat(waterMap[i]));
            sb.append("\n");
        }

        return sb.toString();
    }

}