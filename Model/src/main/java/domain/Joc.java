package domain;


import java.io.Serializable;

public class Joc extends Entity<Integer> implements Serializable {
    private Integer idJoc;
    private String username;
    private String cuvantPropus;

    public Integer getIdJoc() {
        return idJoc;
    }

    public Joc() {
    }

    public void setIdJoc(Integer idJoc) {
        this.idJoc = idJoc;
    }

    @Override
    public String toString() {
        return "Joc{" +
                "idJoc=" + idJoc +
                ", username='" + username + '\'' +
                ", cuvantPropus='" + cuvantPropus + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCuvantPropus() {
        return cuvantPropus;
    }

    public void setCuvantPropus(String cuvantPropus) {
        this.cuvantPropus = cuvantPropus;
    }

    public Joc(Integer idJoc, String username, String cuvantPropus) {
        this.idJoc = idJoc;
        this.username = username;
        this.cuvantPropus = cuvantPropus;
    }
}
