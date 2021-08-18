package ltd.foma.rainyhills.service;

import ltd.foma.rainyhills.data.GapData;
import ltd.foma.rainyhills.data.RainCalculationData;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Rain water calculation logic goes here
 */
@Service
@CacheConfig(cacheNames = "calculations")
public class RainCalculationServiceImpl implements IRainCalculationService {
    private static final Logger log = getLogger( MethodHandles.lookup().lookupClass() );
    private Set<RainCalculationData> calculations = new HashSet<>();

    @Value("${spring.profiles.active:}")
    private String activeProfile;

    /**
     * Main calculation method
     */
    @Override
    @Cacheable
    public RainCalculationData calculate(int[] heightMap) {
        if (heightMap == null){
            heightMap = new int[0];
        }
        List<Integer> map = Arrays.stream(heightMap)
                .boxed()
                .collect(Collectors.toList());

        int[] waterMap = new int[heightMap.length];

        long totalVolume = 0L;

        Set<GapData> gapSet = findGaps(map);

        for (GapData gapData : gapSet) {
            totalVolume += volumeForGap(map, gapData, waterMap);
        }

        if(activeProfile.equals("dev")){
            try {
                log.info("simulating long ops");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                log.error("Oops",e);
            }
        }

        RainCalculationData rainCalculationData = new RainCalculationData();
        rainCalculationData.setHeightMap(map);
        rainCalculationData.setWaterMap(waterMap);
        rainCalculationData.setGaps(gapSet);
        rainCalculationData.setTotalVolume(totalVolume);
        calculations.add(rainCalculationData);
        return rainCalculationData;
    }

    private Set<GapData> findGaps(List<Integer> heightMap){
        Set<GapData> gapSet = new LinkedHashSet<>();
        for (int i = 0; i < heightMap.size(); ) {
            Optional<GapData> optionalGap = checkForGap(heightMap, i);

            if (optionalGap.isPresent()) {
                GapData foundGap = optionalGap.get();
                gapSet.add(foundGap);
                i = foundGap.end;
            } else i++;
        }
        return gapSet;
    }

    /**
     * Returns first gap data starting from provided index, if it exists
     */
    private Optional<GapData> checkForGap(List<Integer> heightMap, int startOffset){
        Optional<GapData> gapData = Optional.empty();
        int maxHeight = heightMap.get(startOffset);
        int maxHeightIndex = startOffset;

        int secondMaxHeight = Integer.MIN_VALUE;
        int secondMaxHeightIndex = startOffset;

        for (int i = startOffset+1; i < heightMap.size(); i++) {

            int currentHeight = heightMap.get(i);

            //easy case: right border is equal or higher than the left border
            if (currentHeight >= maxHeight){

                maxHeight = currentHeight;

                int distanceBetweenPeaks = i - maxHeightIndex;
                if (distanceBetweenPeaks <= 1){
                    //it's not a gap if it's adjacent
                    maxHeightIndex = i;
                } else {
                    //we found a gap end border that's at least as high as start, ending prematurely
                    gapData = Optional.of(new GapData(maxHeightIndex, i));
                    break;
                }
            }

            //if right border is lower we'll have to keep checking till the end
            if (currentHeight >= secondMaxHeight){

                secondMaxHeight = currentHeight;
                secondMaxHeightIndex = i;
            }
        }

        if (!gapData.isPresent()
                && maxHeight > secondMaxHeight
                && secondMaxHeightIndex > (startOffset+1)){
            gapData = Optional.of(new GapData(maxHeightIndex, secondMaxHeightIndex));
        }

        return gapData;
    }

    /**
     * Calculates volume for provided gap
     */
    private long volumeForGap(List<Integer> heightMap, GapData gap, int[] waterMap){
        long totalVolume = 0L;
        int waterLevel = getWaterLevel(heightMap, gap);

        for (int i = gap.start+1; i < gap.end; i++) {
            int currentLevel = heightMap.get(i);
            if (currentLevel <= waterLevel){
                int volumeForPos = waterLevel - currentLevel;
                waterMap[i] = volumeForPos;
                totalVolume += volumeForPos;
            }
        }
        return totalVolume;
    }

    private int getWaterLevel(List<Integer> heightMap, GapData gap){
        return Math.min(heightMap.get(gap.start), heightMap.get(gap.end));
    }
}
