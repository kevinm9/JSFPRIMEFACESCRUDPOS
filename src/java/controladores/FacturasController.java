package controladores;

import modelos.Facturas;
import controladores.util.JsfUtil;
import controladores.util.JsfUtil.PersistAction;
import servicios.FacturasFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import modelos.DetallesFacturas;
import modelos.Productos;

@Named("facturasController")
@SessionScoped
public class FacturasController implements Serializable {

    @EJB
    private servicios.FacturasFacade ejbFacade;
    @EJB
    private servicios.ProductosFacade ejbFacadeProductos;

    private List<Facturas> items = null;
    private List<Productos> itemsproductos = null;
    private DetallesFacturas selecteddf;
    private DetallesFacturas selectedproductodf;
    private Facturas selected;
    private Double subtotal = 00.00, total = 00.00;
    private ArrayList<DetallesFacturas> listadf;

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public DetallesFacturas getSelectedproductodf() {
        return selectedproductodf;
    }

    public void setSelectedproductodf(DetallesFacturas selectedproductodf) {
        this.selectedproductodf = selectedproductodf;
    }

    public ArrayList<DetallesFacturas> getListadf() {
        return listadf;
    }

    public void setListadf(ArrayList<DetallesFacturas> listadf) {
        this.listadf = listadf;
    }

    public DetallesFacturas getSelecteddf() {
        return selecteddf;
    }

    public void setSelecteddf(DetallesFacturas selecteddf) {
        this.selecteddf = selecteddf;
    }

    @PostConstruct
    public void init() {
        selected = new Facturas();
        selecteddf = new DetallesFacturas();
        selectedproductodf = new DetallesFacturas();
        listadf = new ArrayList<DetallesFacturas>();
    }

    public void agregar() {

        for (DetallesFacturas item : listadf) {
            if (item.getProductosId().getId() == selecteddf.getProductosId().getId()) {
                JsfUtil.addSuccessMessage("ya esta agregado");
                return;
            }
        }

        if (selecteddf.getProductosId().getStock() < selecteddf.getCantidad()) {
            JsfUtil.addSuccessMessage("supera el stock");
            return;
        }

        DetallesFacturas dtfactura = new DetallesFacturas(
                selecteddf.getCantidad(),
                selected,
                selecteddf.getProductosId()
        );
        dtfactura.setTotal(selecteddf.getProductosId().getPrecio() * selecteddf.getCantidad());
        listadf.add(dtfactura);
        calcularprecios();
        selecteddf.setCantidad(0);
        JsfUtil.addSuccessMessage("agregado");

    }

    public void calcularprecios() {
        this.subtotal = 00.00;
        this.total = 00.00;

        Double subtotal = 00.00;
        for (DetallesFacturas item : listadf) {
            subtotal += item.getTotal();
        }
        this.subtotal = subtotal;
        this.total = subtotal + (subtotal * 0.12);
    }

    public void eliminarproducto() {
        listadf.remove(selectedproductodf);
        calcularprecios();
        selecteddf.setCantidad(0);
        JsfUtil.addSuccessMessage("eliminado");
    }

    public List<Productos> getItemsproductos() {
        itemsproductos = ejbFacadeProductos.buscarporfactura(selected);
        return itemsproductos;
    }

    public void setItemsproductos(List<Productos> itemsproductos) {
        this.itemsproductos = itemsproductos;
    }

    public FacturasController() {
    }

    public Facturas getSelected() {
        return selected;
    }

    public void setSelected(Facturas selected) {
        this.selected = selected;
    }

    private FacturasFacade getFacade() {
        return ejbFacade;
    }

    public Facturas prepareCreate() {
        selected = new Facturas();

        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("FacturasCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("FacturasUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("FacturasDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Facturas> getItems() {

        items = getFacade().findAll();

        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Facturas getFacturas(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Facturas> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Facturas> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public void nuevafactura() {
        subtotal = 00.00;
        total = 00.00;
        selected = new Facturas();
        selecteddf = new DetallesFacturas();
        selectedproductodf = new DetallesFacturas();
        listadf = new ArrayList<DetallesFacturas>();
    }

    public void guardarfactura() {
        try {
            selected.setDetallesFacturasList(listadf);
            selected.setTotal(total);
            getFacade().crearfactura(selected);
            Facturas entidadPersistida = getFacade().find(selected.getId());

            if (entidadPersistida == null) {
                JsfUtil.addSuccessMessage("Factura no creada");
                return;
            }

            JsfUtil.addSuccessMessage("Factura creada");
            nuevafactura();

        } catch (EJBException ex) {
            String msg = "";
            Throwable cause = ex.getCause();
            if (cause != null) {
                msg = cause.getLocalizedMessage();
            }
            if (msg.length() > 0) {
                JsfUtil.addErrorMessage(msg);
            } else {
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }

    }

    @FacesConverter(forClass = Facturas.class)
    public static class FacturasControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            FacturasController controller = (FacturasController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "facturasController");
            return controller.getFacturas(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Facturas) {
                Facturas o = (Facturas) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Facturas.class.getName()});
                return null;
            }
        }

    }

}
