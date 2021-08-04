package examen.persistence;

import domain.Runda;

public interface RundaRepository extends CrudRepository<Integer, Runda> {
    public Iterable<Runda> findByGameIdAndUser(int idJoc, String username);
    public int nrPuncteCastigate(int idJoc, String username);
}
