package dk.cngroup.cnu.entitiesService.controller;

import dk.cngroup.cnu.entitiesService.model.XmlEntity;
import dk.cngroup.cnu.entitiesService.service.XmlEntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class XmlEntityController {

    private final XmlEntityService service;

    public XmlEntityController(XmlEntityService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/entities")
    public List<XmlEntity> getEntities() {
        return service.getEntities();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/entities/{id}")
    public ResponseEntity<XmlEntity> getEntity(@PathVariable("id") Long id) {
        Optional<XmlEntity> entity = service.getEntity(id);
        if (entity.isPresent()) {
            return ResponseEntity.ok(entity.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/entities/{id}")
    public ResponseEntity<String> deleteEntity(@PathVariable("id") Long id) {
        boolean deletedSuccessfully = service.deleteEntity(id);
        if (deletedSuccessfully) {
            return ResponseEntity.ok("");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "/entities")
    public ResponseEntity<XmlEntity> addEntity(@RequestBody XmlEntity entity) {
        Optional<XmlEntity> xmlEntity = service.saveEntity(entity);
        if (xmlEntity.isPresent()) {
            return ResponseEntity.ok(xmlEntity.get());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}

