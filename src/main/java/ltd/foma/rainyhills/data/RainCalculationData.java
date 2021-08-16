package ltd.foma.rainyhills.data;

import java.beans.Transient;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * All relevant data produced during volume calculation
 */
public class RainCalculationData implements Serializable {
    private static final long serialVersionUID = 12_08_2021L;

    private List<Integer> heightMap;
    private int[] waterMap;

    private long totalVolume;
    private Set<GapData> gaps;

    public RainCalculationData() {}

    /**
     * @return total water volume
     */
    public long getTotalVolume() {
        return totalVolume;
    }
    public void setTotalVolume(long totalVolume) {
        this.totalVolume = totalVolume;
    }

    /**
     * @return set of water filled gaps found in the height map
     */
    @Transient
    public Set<GapData> getGaps() {
        return gaps;
    }
    public void setGaps(Set<GapData> gapSet) {
        this.gaps = gapSet;
    }

    /**
     * @return total number of water gaps in the map
     */
    public Integer getTotalGaps() {
        return gaps.size();
    }

    /**
     * @return original height map provided in request
     */
    @Transient
    public List<Integer> getHeightMap() {
        return heightMap;
    }
    public void setHeightMap(List<Integer> heightMap) {
        this.heightMap = heightMap;
    }

    /**
     * @return height map of water cells only
     */
    @Transient
    public int[] getWaterMap() {
        return waterMap;
    }

    public void setWaterMap(int[] waterMap) {
        this.waterMap = waterMap;
    }
}
