/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.purchase.purchase_order;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import suzane00.global.Environment;
import suzane00.global.PropertyObject;
import suzane00.global.Utility;
import suzane00.inventory.Area;
import suzane00.inventory.Cost;
import suzane00.inventory.PackedItem;
import suzane00.inventory.PackedItemPK;
import suzane00.transaction.model.ItemFileRow;
import suzane00.source.Supplier;
import suzane00.transaction.Transaction;

/**
 *
 * @author Usere
 */
@Entity
@Table(name = "purchase_order")
//@NamedQueries (
//    {
//        @NamedQuery(name="PurchaseOrder.update",
//        query="SELECT i FROM ItemPurchasePrice i"),
//    }
//)
public class PurchaseOrder extends Transaction implements Serializable {
    private static final long serialVersionUID = 1L;

    @OneToMany(mappedBy = "v_purchaseOrder", cascade = CascadeType.ALL)
    private List<POFile> v_poFiles ;
    @Transient
    private ObservableList<SimpleObjectProperty> v_filesProperty = FXCollections.observableArrayList();
    
    @OneToMany(mappedBy = "v_purchaseOrder", cascade = CascadeType.ALL)
    private List<POMiscCharge> v_miscCharges;
    @Transient
    private ObservableList<SimpleObjectProperty> v_miscChargesProperty = FXCollections.observableArrayList();
    
    @OneToMany(mappedBy = "v_purchaseOrder", cascade = CascadeType.ALL)
    private List<PODiscount> v_poDiscounts;
    @Transient
    private ObservableList<SimpleObjectProperty> v_discountsProperty = FXCollections.observableArrayList();
    
    
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier v_supplier;
    @Transient
    private SimpleObjectProperty v_supplierProperty = new SimpleObjectProperty();
    
    @ManyToOne
    @JoinColumn(name = "area_id")
    private Area v_area;
    @Transient
    private SimpleObjectProperty v_areaProperty = new SimpleObjectProperty();
    
    
    public static ObservableList<PurchaseOrder> getPOSByAttributes(String _numb, Supplier _supplier, Area _area, String _note, Calendar _issueDateFrom, Calendar _issueDateTo, Calendar _dueDateFrom, Calendar _dueDateTo) {
        String query = "SELECT p FROM PurchaseOrder p WHERE p.v_id != 0";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (_numb != null && _numb.length() > 0) {
            query += " AND p.v_transactionNumber = " + "'" + _numb + "'";
        }
        if (_supplier != null) {
            query += " AND p.v_supplier.v_id = " + _supplier.getId();
        }
        if (_area != null) {
            query += " AND p.v_area.v_id = " + _area.getId() ;
        }
        if (_note != null && _note.length() > 0) {
            query += " AND p.v_note = " + "'" + _note + "'";
        }
        if (_issueDateFrom != null) {
            Date issueDateFrom = new Date(_issueDateFrom.getTimeInMillis());
            query += " AND p.v_issueDate >= '" + sdf.format(issueDateFrom) + "'";
        }
        if (_issueDateTo != null) {
            Date issueDateTo = new Date(_issueDateTo.getTimeInMillis());
            query += " AND p.v_issueDate <= '" + sdf.format(issueDateTo) + "'";
        }
        if (_dueDateFrom != null) {
            Date dueDateFrom = new Date(_dueDateFrom.getTimeInMillis());
            query += " AND p.v_dueDate >= '" + sdf.format(dueDateFrom) + "'";
        }
        if (_dueDateTo != null) {
            Date dueDateTo = new Date(_dueDateTo.getTimeInMillis());
            query += " AND p.v_dueDate <= '" + sdf.format(dueDateTo) + "'";
        }
        ObservableList<PurchaseOrder> pos = FXCollections.observableArrayList
                                                (Environment.getEntityManager()
                                                            .createQuery(query, PurchaseOrder.class)
                                                            .getResultList());
        
        return PropertyObject.refreshProperies(pos);
    }
    
    public static void savePurchaseOrder(PurchaseOrder _po, ObservableList<POFile> _files, ObservableList<Cost> _discs) {
        for (POFile _file : _files) {
            _file.setPurchaseOrder(_po);
        }
        _po.setFiles(_files);
        setPODiscounts(_po, _discs);
        Environment.getEntityManager().persist(_po);
    }
    
    public static void savePurchaseOrderByRow(PurchaseOrder _po, ObservableList<ItemFileRow> _rows, ObservableList<Cost> _discs, ObservableList<Cost> _miscs) {
        buildPurchaseOrder(_po, _rows, _discs, _miscs);
        Environment.getEntityManager().persist(_po);
        for (ItemFileRow _row : _rows) {
            if(_row.getQuantity() < Utility.DOUBLE_TOLERANCE)
                continue;
            ItemPurchasePrice.saveItemPurchasePrice(_po.getSupplier(), _row);
        }
    }
    
    public static void editPurchaseOrderByRow(PurchaseOrder _po, ObservableList<ItemFileRow> _rows, ObservableList<Cost> _discs, ObservableList<Cost> _miscs) {
//         Query query = Environment.getEntityManager().createQuery(
//         "UPDATE PurchaseOrder SET population = 0, area = 0");
//         int updateCount = em.executeUpdate(); 
        for (POFile _file : _po.getFiles()) {
            Environment.getEntityManager().remove(_file);
        }
        for (PODiscount _disc : _po.getDiscounts()) {
            Environment.getEntityManager().remove(_disc);
        }
        for (POMiscCharge _misc : _po.getMiscCharges()) {
            Environment.getEntityManager().remove(_misc);
        }
        _po.setDiscounts(null);
        _po.setFiles(null);
        _po.setMiscCharges(null);
        
        Environment.getEntityManager().flush();
        
//        String query = "SELECT P.v_poFiles FROM PurchaseOrder P WHERE P = :po"; 
//        ObservableList<POFile> files = FXCollections.observableArrayList
//                                                (Environment.getEntityManager()
//                                                            .createQuery(query, POFile.class)
//                                                            .setParameter("po", _po)
//                                                            .getResultList());
//        System.out.println("COUNT: " + files.size() + files.get(0)/*.getPackedItem().getItem().getName()*/);
//        
        buildPurchaseOrder(_po, _rows, _discs, _miscs);
    }
    
    public static void deletePurchaseOrder(PurchaseOrder _po) {
        Environment.getEntityManager().remove(_po);
    }
    
    
    public List<POFile> getFiles() {
        return FXCollections.observableArrayList(v_poFiles);
    }
    
    public void setFiles(ObservableList<POFile> _files) {
        v_poFiles = _files;
        if(_files == null)
            return;
        for (POFile _file : _files) {
            v_filesProperty.add(new SimpleObjectProperty<POFile>(_file));
        }
    }
    
    public ObservableList<SimpleObjectProperty> poFilesProperty() {
        return v_filesProperty;
    }
    
    public List<POMiscCharge> getMiscCharges() {
        return FXCollections.observableArrayList(v_miscCharges);
    }
    
    public void setMiscCharges(ObservableList<POMiscCharge> _miscs) {
        v_miscCharges = _miscs;
        if(_miscs == null)
            return;
        for (POMiscCharge _misc : _miscs) {
            v_miscChargesProperty.add(new SimpleObjectProperty<POMiscCharge>(_misc));
        }
    }
    
   public ObservableList<SimpleObjectProperty> miscChargesProperty() {
        return v_miscChargesProperty;
    }
    
    public List<PODiscount> getDiscounts() {
        return FXCollections.observableArrayList(v_poDiscounts);
    }
    
    public void setDiscounts(ObservableList<PODiscount> _discounts) {
        v_poDiscounts = _discounts;
        if(_discounts == null)
            return;
        for (PODiscount _discount : _discounts) {
            v_discountsProperty.add(new SimpleObjectProperty<PODiscount>(_discount));
        }
    }
    
    public ObservableList<SimpleObjectProperty> discountsProperty() {
        return v_discountsProperty;
    }
        
    public Supplier getSupplier() {
        return v_supplier;
    }

    public void setSupplier(Supplier _supplier) {
        this.v_supplier = _supplier;
        v_supplierProperty.setValue(v_supplier);
    }
    
    public SimpleObjectProperty supplierProperty() {
        return v_supplierProperty;
    }
    
    public Area getArea() {
        return v_area;
    }

    public void setArea(Area _area) {
        this.v_area = _area;
        v_areaProperty.setValue(v_area);
    }
    
    @Override
    public void refreshProperty() {
        super.refreshProperty();
        v_areaProperty.setValue(v_area);
        v_supplierProperty.setValue(v_supplier);
        v_filesProperty = FXCollections.observableArrayList();
        for (POFile poFile : v_poFiles) {
            v_filesProperty.add(new SimpleObjectProperty<POFile>(poFile));
        }
        v_discountsProperty = FXCollections.observableArrayList();
        for (PODiscount poDisc : v_poDiscounts) {
            v_discountsProperty.add(new SimpleObjectProperty<PODiscount>(poDisc));
        }
        v_miscChargesProperty = FXCollections.observableArrayList();
        for (POMiscCharge charge : v_miscCharges) {
            v_miscChargesProperty.add(new SimpleObjectProperty<POMiscCharge>(charge));
        }
    }
    
    private SimpleObjectProperty areaProperty() {
       return v_areaProperty;
    }
    
    private static void buildPurchaseOrder(PurchaseOrder _po, ObservableList<ItemFileRow> _rows, ObservableList<Cost> _discs, ObservableList<Cost> _miscs) {
        setPOFiles(_po, _rows);
        setPODiscounts(_po, _discs);
        setMiscCharges(_po, _miscs);
    }

    private static void setPODiscounts(PurchaseOrder _po, ObservableList<Cost> _discs) {
        ObservableList<PODiscount> discs = FXCollections.observableArrayList();
        if (_discs == null) {
            return;
        }
        for (Cost disc : _discs) {
            discs.add(new PODiscount(_po, disc));
        }
        _po.setDiscounts(discs);
    }
    
    private static void setPOFiles(PurchaseOrder _po, List<ItemFileRow> _rows) {
        ObservableList<POFile> files = FXCollections.observableArrayList();
        for (ItemFileRow row : _rows) {
            if(row.getQuantity() < Utility.DOUBLE_TOLERANCE)
                continue;
            POFile file = new POFile();
            ObservableList<POFileDiscount> discs = FXCollections.observableArrayList();
            file.setPackedItem(Environment.getEntityManager().find(PackedItem.class, new PackedItemPK(row.getId(), row.getUnit().getId())));
            file.setQuantity(row.getQuantity());
            file.setPrice(row.getPrice());
            if (row.getDiscounts() != null) {
                for (Cost _disc : row.getDiscounts()) {
                    discs.add(new POFileDiscount(file, _disc));
                }
                file.setDiscounts(discs);
            }
            file.setPurchaseOrder(_po); 
            files.add(file);
        }
        _po.setFiles(files);
    }
    
    private static void setMiscCharges(PurchaseOrder _po, ObservableList<Cost> _miscs) {
        ObservableList<POMiscCharge> miscs = FXCollections.observableArrayList();
        if (_miscs == null) {
            return;
        }
        for (Cost misc : _miscs) {
            miscs.add(new POMiscCharge(_po, misc));
        }
        _po.setMiscCharges(miscs);
    }
}
