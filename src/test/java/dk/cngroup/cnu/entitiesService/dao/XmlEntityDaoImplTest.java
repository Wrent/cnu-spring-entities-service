package dk.cngroup.cnu.entitiesService.dao;

import dk.cngroup.cnu.entitiesService.model.XmlEntity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.ws.soap.Addressing;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class XmlEntityDaoImplTest {

    @Autowired
    private XmlEntityDAO dao;

    @Autowired
    private JdbcTemplate template;

    @Before
    public void setUp() throws Exception {
        List<Object[]> data = new ArrayList<>();
        addObject("car", "brm brm", data);
        addObject("dog", "woof woof", data);
        addObject("cat", "mew mew", data);
        template.batchUpdate("INSERT INTO XmlEntities(id, title, content) VALUES(?, ?, ?)", data);
    }

    private void addObject(String title, String content, List<Object[]> data) {
        Object[] arr = new Object[3];
        arr[0] = data.size();
        arr[1] = title;
        arr[2] = content;
        data.add(arr);
    }

    @After
    public void tearDown() throws Exception {
        template.execute("DELETE FROM XmlEntities");
    }

    @Test
    public void testGetAll() {
        List<XmlEntity> all = dao.getAll();
        assertEquals(all.size(), 3);
        assertEquals(all.get(0).getTitle(), "car");
    }

    @Test
    public void testGet() {
        XmlEntity xmlEntity = dao.get(0L);
        assertEquals(xmlEntity.getTitle(), "car");

        xmlEntity = dao.get(123L);
        assertNull(xmlEntity);
    }

    @Test
    public void testDelete() {
        boolean deleted = dao.delete(123L);
        assertFalse(deleted);
        assertEquals(dao.getAll().size(), 3);

        deleted = dao.delete(0L);
        assertTrue(deleted);
        assertEquals(dao.getAll().size(), 2);
    }

    @Test
    public void testInsert() {
        XmlEntity xmlEntity = new XmlEntity("plane", "whoosh whoosh");
        XmlEntity inserted = dao.insert(xmlEntity);
        assertEquals(inserted.getId(), (Long) 10L);
        assertEquals(inserted.getTitle(), xmlEntity.getTitle());

        assertEquals(dao.getAll().size(), 4);
    }
}
