/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import modelos.Formasdepagos;

/**
 *
 * @author KEVIN
 */
@Stateless
public class FormasdepagosFacade extends AbstractFacade<Formasdepagos> {

    @PersistenceContext(unitName = "pos2PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FormasdepagosFacade() {
        super(Formasdepagos.class);
    }
    
}
