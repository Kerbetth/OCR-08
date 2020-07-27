package tourguide.unit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import tourguide.clients.UserClient;
import tourguide.controller.UserController;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockBean
    private UserClient userClient;

    @Autowired
    protected MockMvc mockMvc;

}
