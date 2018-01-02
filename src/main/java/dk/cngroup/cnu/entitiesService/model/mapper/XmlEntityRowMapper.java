package dk.cngroup.cnu.entitiesService.model.mapper;

import dk.cngroup.cnu.entitiesService.model.XmlEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class XmlEntityRowMapper implements RowMapper<XmlEntity> {
    @Override
    public XmlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long id = rs.getLong("id");
        String title = rs.getString("title");
        String content = rs.getString("content");
        return new XmlEntity(id, title, content);
    }
}
