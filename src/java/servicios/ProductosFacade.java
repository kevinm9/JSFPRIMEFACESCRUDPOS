/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios;

import dto.Actors;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import modelos.Facturas;
import modelos.Productos;

/**
 *
 * @author KEVIN
 */
@Stateless
public class ProductosFacade extends AbstractFacade<Productos> {

    @PersistenceContext(unitName = "pos2PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProductosFacade() {
        super(Productos.class);
    }

    public List<Productos> buscarporfactura(Facturas factura) {
        return getEntityManager()
                .createNamedQuery("Productos.productosporfactura")
                .setParameter("facturasId", factura)
                .getResultList();
    }

}
