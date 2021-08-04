package server;


import domain.Joc;
import domain.Jucator;
import domain.Runda;
import examen.persistence.JocRepository;
import examen.persistence.JucatorRepository;
import examen.persistence.RundaRepository;
import service.ExamenException;
import service.Observer;
import service.Service;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServicesImpl implements Service {
    private JocRepository jocRepository;
    private RundaRepository rundaRepository;
    private JucatorRepository jucatorRepository;
    private Map<String, Observer> loggedClients;
    private List<Jucator> readyToPlay = new ArrayList<>(); //players that clicked "play"
    private boolean hasStarted = false;
    private int gameId;
    private int roundNr=1;
    Map<String, String> cuvintePropuse=new HashMap<>();
    private Map<String, List<String>> situatie=new HashMap<>();
    private List<Jucator> players = new ArrayList<>();
    private List<String> litereAlese=new ArrayList<>();
    private List<Jucator> jucatoriAlesi=new ArrayList<>();





    public ServicesImpl(JocRepository jocRepository, RundaRepository rundaRepository, JucatorRepository jucatorRepository) {
        this.jocRepository=jocRepository;
        this.rundaRepository=rundaRepository;
        this.jucatorRepository = jucatorRepository;
        loggedClients=new ConcurrentHashMap<>();
        gameId=getNextGameId();
    }

    @Override
    public void login(Jucator user, service.Observer client){
        Jucator jucator = jucatorRepository.findOne(user.getId());
        if(jucator==null)
            throw new ExamenException("Niciun jucator cu acest username!");
        else if(loggedClients.get(user.getId()) != null)
            throw new ExamenException("Jucator deja logat!");
        else if(!jucator.getParola().equals(user.getParola()))
            throw new ExamenException("Parola gresita!");
        else {
            loggedClients.put(jucator.getId(), client);
        }
    }

    @Override
    public void logout(Jucator user, service.Observer client) {
        Observer localClient=loggedClients.remove(user.getId());
        if (localClient==null)
            throw new ExamenException("User "+user.getId()+" is not logged in.");
    }

    public List<Jucator> getLoggedPlayers() {
        List<Jucator> jucatori=new ArrayList<>();
        for (Jucator jucator : jucatorRepository.findAll()){
            if (loggedClients.containsKey(jucator.getId())){//the player is logged in
                jucatori.add(jucator);
            }
        }
        System.out.println("Size "+jucatori.size());
        return jucatori;
    }

    private final int defaultThreadsNo=5;
    private void notifyNewGame(Jucator jucator, String str) {
        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);
        for(Jucator jucator1 : readyToPlay) {
            if (jucator1.getId().compareTo(jucator.getId()) == 0) {
                //System.out.println(str);
                Observer iObserver = loggedClients.get(jucator1.getId());
                if (iObserver != null)
                    executor.execute(() -> {
                        try {
                            System.out.println("Notifying " + jucator1.getId() + " new game");
                            iObserver.notifyNewGame(readyToPlay,str);
                        } catch (RemoteException | ExamenException e) {
                            System.err.println("Error notifying " + e);
                        }
                    });
            }
        }
        executor.shutdown();
    }



    public void start(Jucator player, String cuvant){
        if(readyToPlay.indexOf(player)==-1 && !hasStarted) {
            readyToPlay.add(player);
            cuvintePropuse.put(player.getId(), cuvant);
            Joc joc = new Joc();
            joc.setIdJoc(gameId);
            joc.setCuvantPropus(cuvant);
            joc.setUsername(player.getId());
            jocRepository.save(joc);
        }
        if(readyToPlay.size()==3) { //ready to play
            for(Jucator jucator: readyToPlay) {
                List<String> str=new ArrayList<>();
                for(int i=0;i<cuvintePropuse.get(jucator.getId()).length();i++)
                    str.add("_");
                situatie.put(jucator.getId(),str);
            }
            String toSend="";
            for(int i=0;i<3;i++){
                String sit="";
                for(String l: situatie.get(readyToPlay.get(i).getId())){
                    sit=sit+l+" ";
                }
                System.out.println(sit);
                toSend=toSend + readyToPlay.get(i).getId() +", " +  sit + "\n";
            }
            for(Jucator jucator:readyToPlay){
                notifyNewGame(jucator,toSend);
            }

        }
    }

    @Override
    public void joacaRunda(Jucator player, String selectat, String litera) {
        players.add(player);
        jucatoriAlesi.add(jucatorRepository.findOne(selectat));
        litereAlese.add(litera);
        if (players.size() == 3) { //all have sent
            System.out.println("READY");
            String primite = "";
            for (int i = 0; i < 3; i++) { //for all users
                List<String> situatie = this.situatie.get(jucatoriAlesi.get(i).getId());
                System.out.println("SITUATIE JUCATOR "+situatie);
                int punctaj = 0;
                String poz = "";
                //int index=0;
                System.out.println("PT JUCATORUL " + readyToPlay.get(i).getId());
                Joc joc = jocRepository.findByUsernameAndJocId(jucatoriAlesi.get(i).getId(), gameId);
                System.out.println("CUVANTUL BUN "+ joc);
                int index = joc.getCuvantPropus().indexOf(litereAlese.get(i));
                while(index >= 0) {
                    situatie.set(index, litereAlese.get(i));
                    punctaj += 1;
                    System.out.println(index);
                    index = joc.getCuvantPropus().indexOf(litereAlese.get(i), index+1);
                }
//                for(int j=0;j<joc.getCuvantPropus().length();j++)
//                    if(joc.getCuvantPropus().indexOf(litereAlese.get(i),index)==j) {
//                        System.out.println("AM GASIT LITERA LA INDEX " + j);
//                        situatie.set(j, litereAlese.get(i));
//                        punctaj += 1;
//                        index=joc.getCuvantPropus().indexOf(litereAlese.get(i),index);
//                    }
                System.out.println("CREEZ RUNDA");
                Runda runda = new Runda();
                runda.setNrRunda(roundNr);
                runda.setLiteraAleasa(litereAlese.get(i));
                runda.setIdJoc(gameId);
                runda.setUsername(players.get(i).getId());
                runda.setNrPuncteCastigate(punctaj);
                runda.setUsernameSelectat(jucatoriAlesi.get(i).getId());
                rundaRepository.save(runda);
                this.situatie.put(jucatoriAlesi.get(i).getId(),situatie);
                primite = primite + jucatoriAlesi.get(i).getId() + "," + situatie + "\n";
            }
            notifyWinner(primite);
            players.clear();
            jucatoriAlesi.clear();
            litereAlese.clear();

            if(roundNr%3==0) {//game is over
                Map<String, Integer> rezultate=new HashMap<>();
                for(Jucator j:readyToPlay)
                    rezultate.put(j.getId(),rundaRepository.nrPuncteCastigate(gameId,j.getId()));
                LinkedHashMap<String, Integer> reverseSortedMap = new LinkedHashMap<>();

                rezultate.entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));
                String s = "";
                for (String c : reverseSortedMap.keySet()) {
                    s += c + " a castigat in total " + rezultate.get(c) + " puncte\n";
                }
                notifyEndGame(s);
                roundNr = 1;
                gameId=getNextGameId();

            }
            else
                roundNr+=1;
        }


    }
    private void notifyEndGame(String s) {
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for (Jucator jucator1 : readyToPlay) {
            Observer iObserver = loggedClients.get(jucator1.getId());
            if (iObserver != null)
                executor.execute(() -> {
                    try {
                        System.out.println("Notifying " + jucator1.getId() + "end game");
                        iObserver.notifyEndGame(s);
                    } catch (RemoteException | ExamenException e) {
                        System.err.println("Error notifying " + e);
                    }
                });
        }
    }




    private int getNextGameId(){
        int maxId=0;
        Iterable<Joc> games = jocRepository.findAll();
        for (Joc joc: games)
            if(joc.getId()>maxId)
                maxId = joc.getId();
        gameId=maxId+1;
        return maxId+1;
    }


    private void notifyWinner(String a) {
        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);
        for(Jucator jucator1 : readyToPlay){
            //System.out.println(str);
            Observer iObserver = loggedClients.get(jucator1.getId());
            if (iObserver != null)
                executor.execute(() -> {
                    try {
                        System.out.println("Notifying " + jucator1.getId() + " winner");
                        iObserver.notifyWinner(a);
                    } catch (RemoteException | ExamenException e) {
                        System.err.println("Error notifying " + e);
                    }
                });
        }
        executor.shutdown();
    }





}
