package in.techutils.poc.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@RunWith(SpringRunner.class)
@WebMvcTest(RESTEndpoint.class)
public class RESTEndpointTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void given_whenNameInRequest_ThenShouldPrintNameWithHelloWorld() {
        try {

            mvc.perform(
                    MockMvcRequestBuilders
                            .get("/Vineet"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string("Hello World, Vineet"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void given_whenNoNameInRequest_ThenShouldFailWithStatus404() {
        try {

            mvc.perform(
                    MockMvcRequestBuilders
                            .get("/"))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}