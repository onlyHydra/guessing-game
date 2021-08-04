package examen.persistence.repository;

import domain.Joc;
import examen.persistence.JocRepository;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class JocRepositoryJDBC implements JocRepository {
    private JdbcUtils dbUtils;

    public JocRepositoryJDBC(Properties props) {
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Joc findOne(Integer id) throws IllegalArgumentException {
        Connection con = dbUtils.getConnection();
        List<Joc> jocList = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Jocuri where id=?")) {
            preStmt.setInt(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id1 = result.getInt("id");
                    String cuvant= result.getString("cuvantPropus");
                    int idJoc = result.getInt("idJoc");
                    String username=result.getString("username");
                    Joc joc = new Joc(idJoc,username,cuvant);
                    joc.setId(id1);
                    jocList.add(joc);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB " + e);
        }
        return jocList.get(0);
    }

    @Override
    public Iterable<Joc> findAll() {
        Connection con = dbUtils.getConnection();
        List<Joc> jocList = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Jocuri")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id1 = result.getInt("id");
                    String cuvant= result.getString("cuvantPropus");
                    int idJoc = result.getInt("idJoc");
                    String username=result.getString("username");
                    Joc joc = new Joc(idJoc,username,cuvant);
                    joc.setId(id1);
                    jocList.add(joc);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB " + e);
        }
        return jocList;
    }

    @Override
    public void save(Joc entity) {
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement("insert into Jocuri (idJoc, cuvantPropus, username) values (?, ?)")) {
            preStmt.setInt(1,entity.getIdJoc());
            preStmt.setString(2,entity.getCuvantPropus());
            preStmt.setString(3, entity.getUsername());
            int result = preStmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error DB " + ex);
        }
    }


    @Override
    public Iterable<Joc> findByJocId(Integer integer) {
        Connection con = dbUtils.getConnection();
        List<Joc> jocList = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Jocuri where idJoc=?")) {
            preStmt.setInt(1, integer);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id1 = result.getInt("id");
                    String cuvant= result.getString("cuvantPropus");
                    int idJoc = result.getInt("idJoc");
                    String username=result.getString("username");
                    Joc joc = new Joc(idJoc,username,cuvant);
                    joc.setId(id1);
                    jocList.add(joc);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB " + e);
        }
        return jocList;
    }

    @Override
    public Joc findByUsernameAndJocId(String username, Integer integer) {
        return null;
    }
}

