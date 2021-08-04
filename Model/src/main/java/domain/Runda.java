package domain;


import java.io.Serializable;

public class Runda extends Entity<Integer> implements Serializable {
    private int nrRunda;
    private int idJoc;
    private String username;
    private String literaAleasa;
    private int nrPuncteCastigate;
    private String usernameSelectat;

    public Runda() {
    }

    public int getNrRunda() {
        return nrRunda;
    }

    public void setNrRunda(int nrRunda) {
        this.nrRunda = nrRunda;
    }

    @Override
    public String toString() {
        return "Runda{" +
                "nrRunda=" + nrRunda +
                ", idJoc=" + idJoc +
                ", username='" + username + '\'' +
                ", literaAleasa=" + literaAleasa +
                ", nrPuncteCastigate=" + nrPuncteCastigate +
                ", usernameSelectat='" + usernameSelectat + '\'' +
                '}';
    }

    public int getIdJoc() {
        return idJoc;
    }

    public void setIdJoc(int idJoc) {
        this.idJoc = idJoc;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLiteraAleasa() {
        return literaAleasa;
    }

    public void setLiteraAleasa(String literaAleasa) {
        this.literaAleasa = literaAleasa;
    }

    public int getNrPuncteCastigate() {
        return nrPuncteCastigate;
    }

    public void setNrPuncteCastigate(int nrPuncteCastigate) {
        this.nrPuncteCastigate = nrPuncteCastigate;
    }

    public String getUsernameSelectat() {
        return usernameSelectat;
    }

    public void setUsernameSelectat(String usernameSelectat) {
        this.usernameSelectat = usernameSelectat;
    }

    public Runda(int nrRunda, int idJoc, String username, String literaAleasa, int nrPuncteCastigate, String usernameSelectat) {
        this.nrRunda = nrRunda;
        this.idJoc = idJoc;
        this.username = username;
        this.literaAleasa = literaAleasa;
        this.nrPuncteCastigate = nrPuncteCastigate;
        this.usernameSelectat = usernameSelectat;
    }
}
