/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.transaction.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import suzane00.global.Utility;
import suzane00.inventory.Cost;
import suzane00.inventory.Unit;

/**
 *
 * @author Usere
 */
public class PaymentFileRow {

    private SimpleLongProperty v_idProperty = new SimpleLongProperty();
    private SimpleDoubleProperty v_totalProperty = new SimpleDoubleProperty();
    private SimpleDoubleProperty v_amountProperty = new SimpleDoubleProperty();
    private SimpleDoubleProperty v_paidProperty = new SimpleDoubleProperty();
    private SimpleDoubleProperty v_leftAmountProperty = new SimpleDoubleProperty();
    private SimpleStringProperty v_issueDateProperty = new SimpleStringProperty();
    private SimpleStringProperty v_dueDateProperty = new SimpleStringProperty();
    private SimpleStringProperty v_invoiceNumberProperty = new SimpleStringProperty();
    private SimpleStringProperty v_noteProperty = new SimpleStringProperty();
    
    public PaymentFileRow() {        
        v_amountProperty.addListener(o -> calculateTotal());
        v_totalProperty.addListener(o -> calculateTotal());
        v_paidProperty.addListener(o -> calculateTotal());
    }
    
    
    
    public long getId() {
      return v_idProperty.getValue();
    }
    
    public void setId(long _id) {
       v_idProperty.setValue(_id);
    }
    
    public SimpleLongProperty idProperty() {
        return v_idProperty;
    }
    
    public double getTotal() {
        return v_totalProperty.getValue();
    }
    
    public void setTotal(double _total) {
        v_totalProperty.setValue(_total);
    }
    
    public SimpleDoubleProperty totalProperty() {
        return v_totalProperty;
    }
    
    public double getAmount() {
        return v_amountProperty.getValue();
    }
    
    public void setAmount(double _amount) {
        v_amountProperty.setValue(_amount);
    }
    
    public SimpleDoubleProperty amountProperty() {
        return v_amountProperty;
    }
    
    public double getPaidProperty() {
        return v_paidProperty.getValue();
    }
    
    public void setPaid(double _paid) {
        v_paidProperty.setValue(_paid);
    }
    
    public SimpleDoubleProperty paidProperty() {
        return v_paidProperty;
    }
    
    public double getLeftAmount() {
        return v_amountProperty.getValue();
    }
    
    public void setLeftAmount(double _amount) {
        v_leftAmountProperty.setValue(_amount);
    }
    
    public SimpleDoubleProperty leftAmountProperty() {
        return v_leftAmountProperty;
    }
    
    public String getInvoiceNumber() {
        return v_invoiceNumberProperty.getValue();
    }
    
    public void setInvoiceNumber(String _numb) {
        v_invoiceNumberProperty.setValue(_numb);
    }
    
    public SimpleStringProperty invoiceNumberProperty() {
        return v_invoiceNumberProperty;
    }
    
    public String getNote() {
        return v_noteProperty.getValue();
    }
    
    public void setNote(String _note) {
        v_noteProperty.setValue(_note);
    }
    
    public SimpleStringProperty noteProperty() {
        return v_noteProperty;
    }
    
    public Calendar getIssueDate() {
        if(v_issueDateProperty.getValue() != null) {
            try {
                Calendar cal = Calendar.getInstance();
                Date date = (new SimpleDateFormat("dd-MM-yyyy")).parse(v_issueDateProperty.getValue());
                cal.setTime(date);
                return cal;
            }
            catch(ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
        else
            return null;
    }
    
    public void setIssueDate(Calendar _date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = _date.getTime();
        v_issueDateProperty.setValue(sdf.format(date));
    }
    
    public SimpleStringProperty issueDateProperty() {
        return v_issueDateProperty;
    }
    
    public Calendar getDueDate() {
        if(v_dueDateProperty.getValue() != null) {
            try {
                Calendar cal = Calendar.getInstance();
                Date date = (new SimpleDateFormat("dd-MM-yyyy")).parse(v_dueDateProperty.getValue());
                cal.setTime(date);
                return cal;
            }
            catch(ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
        else
            return null;
    }
    
    public void setDueDate(Calendar _date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = _date.getTime();
        v_dueDateProperty.setValue(sdf.format(date));
    }
    
    public SimpleStringProperty dueDateProperty() {
        return v_dueDateProperty;
    }
    
    protected void calculateTotal() {
        double total = v_totalProperty.getValue();
        double paid = v_paidProperty.getValue();
        double amount = v_amountProperty.getValue();
        v_leftAmountProperty.setValue(total - (paid + amount));
    }
    
}
