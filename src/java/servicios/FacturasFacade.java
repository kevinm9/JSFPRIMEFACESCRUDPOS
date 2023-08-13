/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import modelos.DetallesFacturas;
import modelos.Facturas;
import modelos.Productos;

/**
 *
 * @author KEVIN
 */
@Stateless
public class FacturasFacade extends AbstractFacade<Facturas> {

    @EJB
    private ProductosFacade ejbFacadeProductos;
    
    @PersistenceContext(unitName = "pos2PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FacturasFacade() {
        super(Facturas.class);
    }

    public void crearfactura(Facturas factura) {
        for (DetallesFacturas dt : factura.getDetallesFacturasList()) {
            Productos p = dt.getProductosId();
            p.setStock(p.getStock() - dt.getCantidad());
            ejbFacadeProductos.edit(p);
        }
        getEntityManager().persist(factura);
    }

}
