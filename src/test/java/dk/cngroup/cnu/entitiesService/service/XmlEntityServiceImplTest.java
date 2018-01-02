package dk.cngroup.cnu.entitiesService.service;


import dk.cngroup.cnu.entitiesService.dao.XmlEntityDAO;
import dk.cngroup.cnu.entitiesService.model.XmlEntity;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class XmlEntityServiceImplTest {
    private XmlEntityService service;
    private XmlEntityDAO mockDao;

    private List<XmlEntity> entities;

    @Before
    public void setUp() throws Exception {
        mockDao = EasyMock.createMock(XmlEntityDAO.class);
        List<String> forbiddenWords = Arrays.asList("php");
        service = new XmlEntityServiceImpl(mockDao, forbiddenWords);

        entities = Arrays.asList(new XmlEntity("entity1", "content"), new XmlEntity("entity2", "content2"));
    }

    @Test
    public void testGetEntities() {
        expect(mockDao.getAll()).andReturn(entities).times(1);

        replay(mockDao);

        List<XmlEntity> entities = service.getEntities();
        assertEquals(entities.size(), 2);
        assertTrue(entities.get(0).getTitle().equals("entity1"));

        verify(mockDao);
    }

    @Test
    public void testGetEntity() {
        expect(mockDao.get(EasyMock.eq(1L))).andReturn(entities.get(0)).times(1);

        replay(mockDao);

        Optional<XmlEntity> entity = service.getEntity(1L);
        assertTrue(entity.isPresent());
        assertTrue(entity.get().getTitle().equals("entity1"));

        verify(mockDao);
    }

    @Test
    public void testGetNonExistentEntity() {
        expect(mockDao.get(EasyMock.eq(100L))).andReturn(null).times(1);

        replay(mockDao);

        Optional<XmlEntity> entity = service.getEntity(100L);
        assertFalse(entity.isPresent());

        verify(mockDao);
    }

    @Test
    public void testDeleteEntity() {
        expect(mockDao.delete(EasyMock.eq(1L))).andReturn(true).times(1);

        replay(mockDao);

        assertTrue(service.deleteEntity(1L));

        verify(mockDao);
    }

    @Test
    public void testDeleteNonExistentEntity() {
        expect(mockDao.delete(EasyMock.eq(100L))).andReturn(false).times(1);

        replay(mockDao);

        assertFalse(service.deleteEntity(100L));

        verify(mockDao);
    }

    @Test
    public void testSaveValidEntity() {
        expect(mockDao.insert(anyObject(XmlEntity.class))).andReturn(entities.get(1)).times(1);

        replay(mockDao);

        XmlEntity toBeSaved = new XmlEntity("entity2", "content2");
        Optional<XmlEntity> xmlEntity = service.saveEntity(toBeSaved);
        assertTrue(xmlEntity.isPresent());
        assertTrue(xmlEntity.get().getTitle().equals("entity2"));

        verify(mockDao);
    }


    @Test
    public void testSaveInvalidEntityWithForbiddenWord() {
        replay(mockDao);

        XmlEntity toBeSaved = new XmlEntity("entity2", "php content");
        Optional<XmlEntity> xmlEntity = service.saveEntity(toBeSaved);
        assertFalse(xmlEntity.isPresent());

        verify(mockDao);
    }

    @Test
    public void testSaveInvalidEntityWithNullFields() {
        replay(mockDao);

        XmlEntity toBeSaved = new XmlEntity(null, "content");
        Optional<XmlEntity> xmlEntity = service.saveEntity(toBeSaved);
        assertFalse(xmlEntity.isPresent());

        toBeSaved = new XmlEntity("title", null);
        xmlEntity = service.saveEntity(toBeSaved);
        assertFalse(xmlEntity.isPresent());

        verify(mockDao);
    }

    @Test
    public void testSaveInvalidEntityWithEmptyFields() {
        replay(mockDao);

        XmlEntity toBeSaved = new XmlEntity("", "content");
        Optional<XmlEntity> xmlEntity = service.saveEntity(toBeSaved);
        assertFalse(xmlEntity.isPresent());

        toBeSaved = new XmlEntity("title", "");
        xmlEntity = service.saveEntity(toBeSaved);
        assertFalse(xmlEntity.isPresent());

        verify(mockDao);
    }

    @Test
    public void testSaveInvalidEntityTooLongContent() {
        replay(mockDao);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12000; i++) {
            sb.append("c");
        }

        XmlEntity toBeSaved = new XmlEntity("title", sb.toString());
        Optional<XmlEntity> xmlEntity = service.saveEntity(toBeSaved);
        assertFalse(xmlEntity.isPresent());

        verify(mockDao);
    }


}
