package examen.persistence;

import domain.Joc;

public interface JocRepository extends CrudRepository<Integer, Joc>{
    public Iterable<Joc> findByJocId(Integer integer);
    public Joc findByUsernameAndJocId(String username, Integer integer);
}
