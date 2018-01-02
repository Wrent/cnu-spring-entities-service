package dk.cngroup.cnu.entitiesService.dao;

public class IdGenerator {
    private static long id = 10;

    public synchronized static Long generate() {
        return id++;
    }
}
