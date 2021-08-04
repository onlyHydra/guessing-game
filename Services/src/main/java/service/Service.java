package service;

import domain.Jucator;

public interface Service {
    public void login(Jucator user, Observer client);
    void logout(Jucator user, Observer client);
    public void start(Jucator player, String cuvant);
    public void joacaRunda(Jucator player, String selectat, String litera);
}