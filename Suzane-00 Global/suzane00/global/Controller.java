/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.global;

import javax.persistence.EntityManager;

/**
 *
 * @author Usere
 */
public class Controller {
    
    protected static EntityManager entityManager = null;
    
    public static EntityManager getEntityManager() {
        return entityManager ;
    }
    
    public static void setEntityManager(EntityManager _em) {
        entityManager = _em;
    }
    
}
