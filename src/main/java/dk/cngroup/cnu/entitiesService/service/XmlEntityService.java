package dk.cngroup.cnu.entitiesService.service;

import dk.cngroup.cnu.entitiesService.model.XmlEntity;

import java.util.List;
import java.util.Optional;

public interface XmlEntityService {
    List<XmlEntity> getEntities();
    Optional<XmlEntity> getEntity(Long id);
    Optional<XmlEntity> saveEntity(XmlEntity entity);
    boolean deleteEntity(Long id);
}
