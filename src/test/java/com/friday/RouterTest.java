package com.friday;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.friday.persist.Perister;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(Router.class)
public class RouterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Perister perister;

    @Test
    public void testRoot() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())  // Expect HTTP 200 OK
                .andExpect(content().string("see available <A HREF=\"swagger-ui/index.html\">API endpoints</A>"));  // Expect specific response content
    }

    @Test
    public void testGetAllEmpty() throws Exception {

        Mockito.when(perister.getAll()).thenReturn(List.of());
        mockMvc.perform(get("/api/getAll"))
                .andExpect(status().isPartialContent())  // Expect HTTP 200 OK
                .andExpect(jsonPath("$.message", is("No users registered")));  // Expect specific response content
    }

    @Test
    public void testGetByEmail() throws Exception {

        Mockito.when(perister.getByEmail(Mockito.any())).thenReturn(new User("Mr.", "James", "J", "Bond", "Jr.", "james@bond.com", "123456789"));
        mockMvc.perform(get("/api/getByEmail?email=james@bond.com"))
                .andExpect(status().isOk())  // Expect HTTP 200 OK
                .andExpect(jsonPath("$.firstName", is("James")));  // Expect specific response content
    }

    @Test
    public void testGetAll() throws Exception {

        Mockito.when(perister.getAll()).thenReturn(List.of(new User("Mr.", "James", "J", "Bond", "Jr.", "james@bond.com", "123456789")));
        mockMvc.perform(get("/api/getAll"))
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"prefix\":\"Mr.\",\"firstName\":\"James\",\"middleName\":\"J\",\"lastName\":\"Bond\",\"suffix\":\"Jr.\",\"email\":\"james@bond.com\",\"phone\":\"123456789\"}]"));  // Expect specific response content
    }

    @Test
    public void testAdd() throws Exception {

        User user = new User("Mr.", "James", "J", "Bond", "Jr.", "james2@bond.com", "123456789");
        Mockito.when(perister.add(Mockito.any())).thenReturn(1);
        mockMvc.perform(post("/api/add").content(new ObjectMapper().writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @Test
    public void testAddFailedOnPersister() throws Exception {

        User user = new User("Mr.", "James", "J", "Bond", "Jr.", "james2@bond.com", "123456789");
        Mockito.when(perister.add(Mockito.any())).thenReturn(0);
        mockMvc.perform(post("/api/add").content(new ObjectMapper().writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isPartialContent())
                .andExpect(jsonPath("$.message", is("User james2@bond.com not added")));
    }


    @Test
    public void testAddBadEmail() throws Exception {

        User user = new User("Mr.", "James", "J", "Bond", "Jr.", "asd", "123456789");
        Mockito.when(perister.add(Mockito.any())).thenReturn(1);
        mockMvc.perform(post("/api/add").content(new ObjectMapper().writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message.email", is("Invalid email address")));
    }

    @Test
    public void testupdate() throws Exception {

        User user = new User("Mr.", "James", "J", "Bond", "Jr.", "007@bond.com", "123456789");
        Mockito.when(perister.update(Mockito.any())).thenReturn(1);
        mockMvc.perform(post("/api/update").content(new ObjectMapper().writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is("User 007@bond.com updated successfully")));
    }
    @Test
    public void testDelete() throws Exception {
        Mockito.when(perister.delete(Mockito.any())).thenReturn(1);
        mockMvc.perform(post("/api/delete").content(new ObjectMapper().writeValueAsString(new UserLocator("james@bond.com")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is("User james@bond.com deleted successfully")));
    }

    @Test
    public void testDeleteZeroDeleted() throws Exception {
        Mockito.when(perister.delete(Mockito.any())).thenReturn(0);
        mockMvc.perform(post("/api/delete").content(new ObjectMapper().writeValueAsString(new UserLocator("james@bond.com")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isPartialContent())
                .andExpect(jsonPath("$.message", is("User james@bond.com not deleted")));
    }
    //TODO: more test for the rest of api calls
}
