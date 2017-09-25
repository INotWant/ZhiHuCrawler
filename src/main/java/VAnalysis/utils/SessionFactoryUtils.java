package VAnalysis.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * @author kissx on 2017/9/25.
 */
public class SessionFactoryUtils {
    private static final Configuration CONFIGURATION;
    private static final SessionFactory SESSION_FACTORY;

    static {
        CONFIGURATION = new Configuration();
        CONFIGURATION.configure("VAnalysis/hibernate.cfg.xml");
        SESSION_FACTORY =  CONFIGURATION.buildSessionFactory();
    }

    public static SessionFactory getSessionFactory(){
        return SESSION_FACTORY;
    }

    public static Session getCurrentSession(){
        return SESSION_FACTORY.getCurrentSession();
    }

}
