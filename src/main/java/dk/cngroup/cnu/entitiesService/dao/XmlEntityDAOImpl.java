package dk.cngroup.cnu.entitiesService.dao;

import dk.cngroup.cnu.entitiesService.model.XmlEntity;
import dk.cngroup.cnu.entitiesService.model.mapper.XmlEntityRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.List;

@Component
public class XmlEntityDAOImpl implements XmlEntityDAO {

    private final JdbcTemplate jdbcTemplate;

    public XmlEntityDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<XmlEntity> getAll() {
        return jdbcTemplate.query("SELECT * FROM XmlEntities", new Object[]{}, new XmlEntityRowMapper());
    }

    @Override
    public XmlEntity get(Long id) {
        List<XmlEntity> result = jdbcTemplate.query("SELECT * FROM XmlEntities WHERE id = ?", new Object[]{id}, new XmlEntityRowMapper());
        if (result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }

    @Override
    public XmlEntity insert(XmlEntity entity) {
        entity.setId(IdGenerator.generate());
        Object[] params = { entity.getId(), entity.getTitle(), entity.getContent() };
        int[] types = {Types.BIGINT, Types.VARCHAR, Types.LONGVARCHAR };
		jdbcTemplate.update("INSERT INTO XmlEntities(id, title, content) VALUES(?, ?, ?)", params, types);

		return get(entity.getId());
    }

    @Override
    public boolean delete(Long id) {
        if (get(id) == null) {
            return false;
        } else {
            Object[] params = { id };
            int[] types = {Types.BIGINT};
            jdbcTemplate.update("DELETE FROM XmlEntities WHERE id = ?", params, types);
            return true;
        }
    }
}
