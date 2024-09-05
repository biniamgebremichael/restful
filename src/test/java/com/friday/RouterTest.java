package com.friday;
import com.friday.persist.Perister;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(Router.class)
public class RouterTest {

    @Autowired
    private MockMvc mockMvc; // MockMvc allows us to simulate HTTP requests

    @MockBean
    private Perister perister;

    @Test
    public void testRoute() throws Exception {
        // Perform GET request and verify the response
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())  // Expect HTTP 200 OK
                .andExpect(content().string("see available <A HREF=\"swagger-ui/index.html\">API usage</A>"));  // Expect specific response content
    }

    @Test
    public void testGetAll() throws Exception {

        Mockito.when(perister.get()).thenReturn(Arrays.asList(new User("Mr.", "James", "J", "Bond", "Jr.","james@bond.com","123456789")));
        // Perform GET request and verify the response
        mockMvc.perform(get("/api/getAll"))
                .andExpect(status().isOk())  // Expect HTTP 200 OK
                .andExpect(content().string("[{\"prefix\":\"Mr.\",\"firstName\":\"James\",\"middleName\":\"J\",\"lastName\":\"Bond\",\"suffix\":\"Jr.\",\"email\":\"james@bond.com\",\"phone\":\"123456789\"}]"));  // Expect specific response content
    }

    @Test
    public void testGetByEmail() throws Exception {

        Mockito.when(perister.getByEmail(Mockito.any())).thenReturn(new User("Mr.", "James", "J", "Bond", "Jr.","james@bond.com","123456789"));
        // Perform GET request and verify the response
        mockMvc.perform(get("/api/getByEmail?email=james@bond.com"))
                .andExpect(status().isOk())  // Expect HTTP 200 OK
                .andExpect(jsonPath("$.firstName",is("James")));  // Expect specific response content
    }

}
