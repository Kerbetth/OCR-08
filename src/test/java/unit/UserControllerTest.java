package unit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import tourGuide.clients.UserClient;
import tourGuide.controller.UserController;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockBean
    private UserClient userClient;

    @Autowired
    protected MockMvc mockMvc;

}
