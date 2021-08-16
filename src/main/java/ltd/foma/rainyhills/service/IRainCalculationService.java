package ltd.foma.rainyhills.service;

import ltd.foma.rainyhills.data.RainCalculationData;

public interface IRainCalculationService {
    RainCalculationData calculate(int[] heightMap);
}
