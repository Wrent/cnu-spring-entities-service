package dk.cngroup.cnu.entitiesService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.cngroup.cnu.entitiesService.model.XmlEntity;
import dk.cngroup.cnu.entitiesService.service.XmlEntityService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
public class XmlEntityControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @MockBean
    private XmlEntityService service;
    private List<XmlEntity> entities;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        entities = Arrays.asList(new XmlEntity("entity1", "content"), new XmlEntity("entity2", "content2"));

        when(service.getEntities()).thenReturn(entities);

        when(service.getEntity(1L)).thenReturn(Optional.ofNullable(entities.get(0)));
        when(service.getEntity(123L)).thenReturn(Optional.empty());

        when(service.deleteEntity(1L)).thenReturn(true);
        when(service.deleteEntity(123L)).thenReturn(false);
    }

    @Test
    public void testGetEntities() throws Exception {
        mockMvc.perform(get("/entities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("entity1")));

        verify(service, times(1)).getEntities();
    }

    @Test
    public void testGetEntity() throws Exception {
        mockMvc.perform(get("/entities/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("entity1")));

        mockMvc.perform(get("/entities/123"))
                .andExpect(status().isNotFound());

        verify(service, times(2)).getEntity(anyLong());
    }

    @Test
    public void testDeleteEntity() throws Exception {
        mockMvc.perform(delete("/entities/1"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/entities/123"))
                .andExpect(status().isNotFound());

        verify(service, times(2)).deleteEntity(anyLong());
    }

    @Test
    public void testAddEntity() throws Exception {
        XmlEntity entity = new XmlEntity();
        ObjectMapper mapper = new ObjectMapper();

        String entityString = mapper.writeValueAsString(entity);

        when(service.saveEntity(any(XmlEntity.class))).thenReturn(Optional.ofNullable(entities.get(0)));
        mockMvc.perform(post("/entities")
                .content(entityString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("entity1")));

        when(service.saveEntity(any(XmlEntity.class))).thenReturn(Optional.empty());
        mockMvc.perform(post("/entities")
                .content(entityString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
