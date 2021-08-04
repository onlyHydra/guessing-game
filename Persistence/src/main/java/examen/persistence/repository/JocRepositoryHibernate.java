package examen.persistence.repository;

import domain.Joc;
import examen.persistence.JocRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.List;

public class JocRepositoryHibernate implements JocRepository {
    @Override
    public Joc findOne(Integer integer) throws IllegalArgumentException {
        initialize();
        try(Session session= sessionFactory.openSession()){
            session.beginTransaction();
            Query query=session.createQuery("from Joc where id=:id");
            query.setParameter("id", integer);
            Joc joc= (Joc) query.uniqueResult();
            session.getTransaction().commit();
            close();
            return  joc;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            close();
        }
        return null;
    }

    @Override
    public Iterable<Joc> findByJocId(Integer integer){
        initialize();
        try(Session session= sessionFactory.openSession()){
            session.beginTransaction();
            Query query=session.createQuery("from Joc where idJoc=:id");
            query.setParameter("id", integer);
            List<Joc> jocuri= query.list();
            session.getTransaction().commit();
            close();
            return  jocuri;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            close();
        }
        return null;
    }

    @Override
    public Joc findByUsernameAndJocId(String username, Integer integer){
        initialize();
        try(Session session= sessionFactory.openSession()){
            session.beginTransaction();
            Query query=session.createQuery("from Joc where idJoc=:id and username=:username");
            query.setParameter("username", username);
            query.setParameter("id", integer);
            Joc joc= (Joc) query.uniqueResult();
            session.getTransaction().commit();
            close();
            return  joc;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            close();
        }
        return null;
    }

    @Override
    public Iterable<Joc> findAll() {
        initialize();
        try(Session session= sessionFactory.openSession()){
            session.beginTransaction();
            List<Joc> jocuri =
                    session.createQuery("from Joc", Joc.class)
                            .list();
            session.getTransaction().commit();
            close();
            return  jocuri;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            close();
        }
        return null;
    }

    @Override
    public void save(Joc entity) {
        initialize();
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Joc joc = new Joc(entity.getIdJoc(), entity.getUsername(), entity.getCuvantPropus());
                session.save(joc);
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }

    }

    static SessionFactory sessionFactory;
    static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.err.println("Exception "+e);
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    static void close(){
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }

    }
}

