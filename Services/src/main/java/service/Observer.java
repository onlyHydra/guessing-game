package service;

import domain.Jucator;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Observer extends Remote {
    void notifyNewGame(List<Jucator> readyToPlay, String cuv) throws RemoteException;
    void notifyWinner(String a) throws RemoteException;
    public void notifyEndGame(String a) throws RemoteException;
}
