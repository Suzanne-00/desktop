/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.global;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.WeakHashMap;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Region;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Usere
 */
public class Environment {
    
    private static Connection v_connection ;
    private static EntityManager v_entityManager ;
    
    static {
        try
        {
            v_connection = DriverManager.getConnection(
                                            "jdbc:postgresql://127.0.0.1:5432/TestDB", "dbexerphi_dba",
                                            "admin");
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("Suzane-00PU", getPersistenceAttributes());
        
            v_entityManager = emf.createEntityManager();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static EntityManager getEntityManager() {
        return v_entityManager;
    }
    
    public static void setEntityManager(EntityManager _em) {
        v_entityManager = _em;
    }
    
    public static Connection getConnection() {
        return v_connection;
    }
    
    public static void setConnection(Connection _con) {
        v_connection = _con;
    }
    
    
    
    private static Map<String, String> getPersistenceAttributes ()
    {
        WeakHashMap retval = new WeakHashMap ();

        // set property hostname berdasarkan m_dbIPaddr.
        retval.put ("javax.persistence.jdbc.password", "admin");
        retval.put ("javax.persistence.jdbc.driver", "org.postgresql.Driver");
        retval.put ("javax.persistence.jdbc.user", "dbexerphi_dba");
        String url = "jdbc:postgresql://localhost:5432/TestDB";
        retval.put ("javax.persistence.jdbc.url", url);

        return retval;
    }
}
