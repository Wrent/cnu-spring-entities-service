package dk.cngroup.cnu.entitiesService.service;

import dk.cngroup.cnu.entitiesService.dao.XmlEntityDAO;
import dk.cngroup.cnu.entitiesService.model.XmlEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class XmlEntityServiceImpl implements XmlEntityService {

    private final XmlEntityDAO dao;
    private final List<String> forbiddenWords;

    public XmlEntityServiceImpl(XmlEntityDAO dao,
                                @Value("${forbiddenWords}")List<String> forbiddenWords) {
        this.dao = dao;
        this.forbiddenWords = forbiddenWords;
    }

    @Override
    public List<XmlEntity> getEntities() {
        return dao.getAll();
    }

    @Override
    public Optional<XmlEntity> getEntity(Long id) {
        return Optional.ofNullable(dao.get(id));
    }

    @Override
    public Optional<XmlEntity> saveEntity(XmlEntity entity) {
        if (isValid(entity)) {
            return Optional.ofNullable(dao.insert(entity));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteEntity(Long id) {
        return dao.delete(id);
    }

    private boolean isValid(XmlEntity entity) {
        if (entity.getContent() == null || entity.getTitle() == null) {
            return false;
        }
        if (entity.getTitle().length() == 0 || entity.getContent().length() == 0) {
            return false;
        }
        if (entity.getContent().length() > 10000) {
            return false;
        }
        return forbiddenWords.stream().noneMatch(w -> entity.getContent().contains(w));
    }
}
