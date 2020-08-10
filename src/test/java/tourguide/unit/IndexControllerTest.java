package tourguide.unit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(tourguide.controller.IndexController.class)
public class IndexControllerTest {

    @Autowired
    protected MockMvc mockMvc;


    @Test
    public void getIndexController() throws Exception {
        this.mockMvc.perform(post("/")
        )
                .andExpect(status().isOk());
    }

}
