package dk.cngroup.cnu.entitiesService.dao;

import dk.cngroup.cnu.entitiesService.model.XmlEntity;

import java.util.List;

public interface XmlEntityDAO {
    List<XmlEntity> getAll();
    XmlEntity get(Long id);
    XmlEntity insert(XmlEntity entity);
    boolean delete(Long id);
}
