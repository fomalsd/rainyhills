package ltd.foma.rainyhills.integration;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ltd.foma.rainyhills.controllers.RainyHillsRestController;
import ltd.foma.rainyhills.controllers.RainyHillsWebController;
import ltd.foma.rainyhills.controllers.WelcomeController;
import ltd.foma.rainyhills.service.RainCalculationServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {
        RainyHillsRestController.class,
        RainyHillsWebController.class,
        WelcomeController.class,
        RainCalculationServiceImpl.class
})
@WebMvcTest
class RainyHillsIntegrationTests {

    public static final String REST_URL = "/rainyhills/rest/volume";
    public static final String WEB_URL = "/rainyhills";
    String mapNormal = "-1,1,5,2,3,0";
    String mapGibberish = "-1,1,5sldkfjl,2,3,0";
    String mapSkipped = "-1,1,5,,2,3,0";

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void welcome_shouldRedirect() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(redirectedUrl(WEB_URL))
                .andReturn();

        String resultResponse = result.getResponse().getContentAsString();
        assertNotNull(resultResponse);
    }


    @Test
    public void web_shouldCalculateValidRequest() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(WEB_URL)
                .param("map", mapNormal)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Total volume: 1")))
                .andReturn();

        String resultResponse = result.getResponse().getContentAsString();
        assertNotNull(resultResponse);
    }

    @Test
    public void web_shouldRedirectIfNoParam() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(WEB_URL))
                .andExpect(redirectedUrlPattern(WEB_URL+"?map=*"))
                .andReturn();
        String resultResponse = result.getResponse().getContentAsString();
        assertNotNull(resultResponse);
    }


    @Test
    public void rest_shouldCalculateValidRequest() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .param("map", mapNormal)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String resultResponse = result.getResponse().getContentAsString();
        assertNotNull(resultResponse);
        assertEquals("{\"totalVolume\":1,\"totalGaps\":1}", resultResponse);
    }

    @Test
    public void rest_shouldReturnZeroOnEmptyParam() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .param("map", "")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String resultResponse = result.getResponse().getContentAsString();
        assertNotNull(resultResponse);
    }

    @Test
    public void rest_shouldFailOnGibberishParam() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .param("map", mapGibberish)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

        String resultResponse = result.getResponse().getContentAsString();
        assertNotNull(resultResponse);
    }

    @Test
    public void rest_shouldFailOnSkippedMapEntry() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .param("map", mapSkipped)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

        String resultResponse = result.getResponse().getContentAsString();
        assertNotNull(resultResponse);
    }

    @Test
    public void rest_shouldFailOnNoParam() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

        String resultResponse = result.getResponse().getContentAsString();
        assertNotNull(resultResponse);
    }
}
