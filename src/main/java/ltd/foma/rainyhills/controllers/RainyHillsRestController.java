package ltd.foma.rainyhills.controllers;

import ltd.foma.rainyhills.data.RainCalculationData;
import ltd.foma.rainyhills.service.IRainCalculationService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * REST API controller
 */
@RestController
@RequestMapping("/rainyhills/rest/volume")
public class RainyHillsRestController {
    private static final Logger log = getLogger( MethodHandles.lookup().lookupClass() );

    private final IRainCalculationService calculationService;

    @Autowired
    public RainyHillsRestController(IRainCalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @GetMapping
    public RainCalculationData getVolume(@RequestParam(value = "map") int[] heightMap) {
        log.info("Received map: {}", heightMap);

        return calculationService.calculate(heightMap);
    }
}
