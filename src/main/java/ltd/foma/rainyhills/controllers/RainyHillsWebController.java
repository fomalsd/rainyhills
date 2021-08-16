package ltd.foma.rainyhills.controllers;

import ltd.foma.rainyhills.data.RainCalculationData;
import ltd.foma.rainyhills.service.IRainCalculationService;
import ltd.foma.rainyhills.util.HillRandomizer;
import ltd.foma.rainyhills.util.HillVisualizer;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.invoke.MethodHandles;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Main webpage controller.
 * Will generate a random heightmap if none is provided
 */
@Controller
@RequestMapping("/rainyhills")
public class RainyHillsWebController {
    private static final Logger log = getLogger( MethodHandles.lookup().lookupClass() );

    private final IRainCalculationService calculationService;

    @Autowired
    public RainyHillsWebController(IRainCalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @GetMapping
    public String getVolume(@RequestParam(value = "map",required = false) int[] heightMap, Model model) {

        if (heightMap == null || heightMap.length == 0){
            String randomMap = HillRandomizer.getRandomMapString();
            //randomize map and redirect;
            //we want parameters to be seen in the URL
            return "redirect:/rainyhills?map="+randomMap;
        }
        log.info("Received map: {}", heightMap);

        RainCalculationData calculationData = calculationService.calculate(heightMap);

        model.addAttribute("volume",    calculationData.getTotalVolume());
        model.addAttribute("gaps",      calculationData.getTotalGaps());
        model.addAttribute("heightmap", calculationData.getHeightMap());
        model.addAttribute("wetmap",    HillVisualizer.drawWetMap(calculationData));

        return "rainyhills";
    }
}
