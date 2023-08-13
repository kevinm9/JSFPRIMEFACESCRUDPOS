/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 *
 * @author KEVIN
 */
@Entity
@Table(name = "detalles_facturas")
@NamedQueries({
    @NamedQuery(name = "DetallesFacturas.findAll", query = "SELECT d FROM DetallesFacturas d")
    , @NamedQuery(name = "DetallesFacturas.findById", query = "SELECT d FROM DetallesFacturas d WHERE d.id = :id")
    , @NamedQuery(name = "DetallesFacturas.findByCantidad", query = "SELECT d FROM DetallesFacturas d WHERE d.cantidad = :cantidad")})
public class DetallesFacturas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad")
    private int cantidad;
    @JoinColumn(name = "facturas_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Facturas facturasId;
    @JoinColumn(name = "productos_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Productos productosId;
    
    @Transient
    private double total;

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }


    
    
    public DetallesFacturas() {
    }

    public DetallesFacturas(int cantidad, Facturas facturasId, Productos productosId) {
        this.cantidad = cantidad;
        this.facturasId = facturasId;
        this.productosId = productosId;
    }

    public DetallesFacturas(int cantidad, Productos productosId) {
        this.cantidad = cantidad;
        this.productosId = productosId;
    }
    
    

    public DetallesFacturas(Long id) {
        this.id = id;
    }

    public DetallesFacturas(Long id, int cantidad) {
        this.id = id;
        this.cantidad = cantidad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Facturas getFacturasId() {
        return facturasId;
    }

    public void setFacturasId(Facturas facturasId) {
        this.facturasId = facturasId;
    }

    public Productos getProductosId() {
        return productosId;
    }

    public void setProductosId(Productos productosId) {
        this.productosId = productosId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetallesFacturas)) {
            return false;
        }
        DetallesFacturas other = (DetallesFacturas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelos.DetallesFacturas[ id=" + id + " ]";
    }
    
}
