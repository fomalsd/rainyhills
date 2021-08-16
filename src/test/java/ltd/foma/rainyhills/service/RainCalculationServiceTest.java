package ltd.foma.rainyhills.service;

import ltd.foma.rainyhills.data.RainCalculationData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RainCalculationServiceTest {

    private static IRainCalculationService calculationService;

    @BeforeAll
    static void init() {
        calculationService = new RainCalculationServiceImpl();
    }
    @Test
    void shouldReturnZeroIfFlat(){
        RainCalculationData data = calculationService.calculate(new int[]{1, 1, 1});
        assertEquals(0, data.getTotalVolume());
        assertEquals(0, data.getTotalGaps());
    }

    @Test
    void shouldWorkWithNegativeHeights(){
        RainCalculationData data = calculationService.calculate(new int[]{1, -1, 1});
        assertEquals(2, data.getTotalVolume());
        assertEquals(1, data.getTotalGaps());
    }

    @Test
    void shouldWorkWithComplexMap(){
        RainCalculationData data = calculationService.calculate(new int[]{6,4,3,-4,4,3,2,10,2,3,4,-4,3,4,6});
        assertEquals(48, data.getTotalVolume());
        assertEquals(2, data.getTotalGaps());
    }

    @Test
    void shouldReturnZeroWithNoParam(){
        RainCalculationData data = calculationService.calculate(null);
        assertEquals(0, data.getTotalVolume());
        assertEquals(0, data.getTotalGaps());
    }

    @Test
    void shouldReturnZeroWithReverseLadder(){
        RainCalculationData data = calculationService.calculate(new int[]{10,9,8,7,6,5,4,3,2,1,0});
        assertEquals(0, data.getTotalVolume());
        assertEquals(0, data.getTotalGaps());
    }

}