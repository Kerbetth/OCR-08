package tourguide.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import tourguide.clients.UserClient;
import tourguide.clients.dto.CreateUser;
import tourguide.clients.dto.SetUserPreferences;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ExtendWith(SpringExtension.class)
public class UserControllerTest {

    @MockBean
    private UserClient userClient;

    @Autowired
    protected MockMvc mockMvc;

    @Test
    public void getSetUserPreferences() throws Exception {
        SetUserPreferences setUserPreferences = new SetUserPreferences(1,"USD", 0,1000,1,1,1,1);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(setUserPreferences);
        this.mockMvc.perform(get("/setUserPreferences")
                .param("userId", body)
        )
                .andExpect(status().isOk());
    }

    @Test
    public void getErrorIfSetUserPreferencesWthoutPreferences() throws Exception {
        this.mockMvc.perform(get("/setUserPreferences")
        )
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void getAddUser() throws Exception {
        CreateUser createUser = new CreateUser("aName", "000","an@Email.com");
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(createUser);
        this.mockMvc.perform(get("/addUser")
                .param("userId", body)
        )
                .andExpect(status().isOk());
    }
}
