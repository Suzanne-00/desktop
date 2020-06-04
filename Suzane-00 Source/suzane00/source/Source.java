/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.source;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import suzane00.global.Environment;
import suzane00.global.PropertyObject;

/**
 *
 * @author Usere
 */
@Entity
@Table(name = "source")
@Inheritance(strategy = InheritanceType.JOINED)
public class Source implements Serializable, PropertyObject {
    private static final long serialVersionUID = 1L;
    private static Background v_masterBackground;
    static {
        try
        {
            InputStream is = Environment.class.getResource(
                    "/suzane00/global/resource/Blue Gradient 2.jpg").openStream();
            BackgroundImage img= new BackgroundImage(new Image(is,32,32,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(0, 0, false, false, false, true));
            v_masterBackground = new Background(img);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    
    
    @TableGenerator(name = "source_gen", allocationSize = 1)
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "source_gen")
    private Long v_id;
    @Transient
    private SimpleLongProperty v_idProperty = new SimpleLongProperty();
    
    @OneToMany( mappedBy = "v_source", cascade = CascadeType.ALL)
    private List<Address> v_addresses;
    @Transient
    private ObservableList<SimpleObjectProperty> v_addressesProperty = FXCollections.observableArrayList();
    
    @Column(name = "name")
    private String v_name;
    @Transient
    private SimpleStringProperty v_nameProperty = new SimpleStringProperty();
    
    @Column(name = "note")
    private String v_note;
    @Transient
    private SimpleStringProperty v_noteProperty = new SimpleStringProperty();
    
    public static Background getMasterBackground() {
        return v_masterBackground;
    }

    public Long getId() {
        return v_id;
    }

    public void setId(Long _id) {
        this.v_id = _id;
        v_idProperty.setValue(_id);
    }
    
    public SimpleLongProperty idProperty() {
        return v_idProperty;
    }
    
    public ObservableList<Address> getAddresses() {
        return FXCollections.observableArrayList(v_addresses);
    }
    
    public void setAddresses(ObservableList<Address> _addrs) {
        v_addresses = _addrs;
        for (Address _addr : _addrs) {
            v_addressesProperty.add(new SimpleObjectProperty<Address>(_addr));
        }
    }
    
    public ObservableList<SimpleObjectProperty> addressesProperty() {
        return v_addressesProperty;
    }
    
    public String getName() {
        return v_name;
    }

    public void setName(String _name) {
        this.v_name = _name;
        v_nameProperty.setValue(_name);
    }
    
     public SimpleStringProperty nameProperty() {
        return v_nameProperty;
    }
     
    public String getNote() {
        return v_note;
    }

    public void setNote(String _note) {
        this.v_note = _note;
        v_noteProperty.setValue(_note);
    }
    
    public SimpleStringProperty noteProperty() {
        return v_noteProperty;
    }
    
    @Override
    public void refreshProperty() {
        v_idProperty.setValue(v_id);
        v_nameProperty.setValue(v_name);
        v_noteProperty.setValue(v_note);
        v_addressesProperty = FXCollections.observableArrayList();
        for (Address addr : v_addresses) {
            v_addressesProperty.add(new SimpleObjectProperty<Address>(addr));
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (v_id != null ? v_id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the v_id fields are not set
        if (!(object instanceof Source)) {
            return false;
        }
        Source other = (Source) object;
        if ((this.v_id == null && other.v_id != null) || (this.v_id != null && !this.v_id.equals(other.v_id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return v_name ;
    }
    
}
