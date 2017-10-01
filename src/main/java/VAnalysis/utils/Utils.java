package VAnalysis.utils;

import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @author kissx on 2017/10/1.
 */
public class Utils {

    public static <T> void saveObj(Session session, Transaction transaction, T obj) {
        try {
            session.save(obj);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
        }
    }

}
