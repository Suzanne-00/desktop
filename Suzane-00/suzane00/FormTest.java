package suzane00;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Map;
import java.util.WeakHashMap;
import javafx.application.Application;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import suzane00.inventory.form.FormItem;
import suzane00.source.form.FormCity;
import suzane00.source.form.FormSupplierGroup;
import suzane00.source.form.FormSupplier;
import suzane00.global.Environment;
import suzane00.inventory.form.FormSellPriceType;
import suzane00.inventory.form.FormBrand;
import suzane00.inventory.form.FormPickItem;
import suzane00.purchase.purchase_order.form.FormPurchaseOrder;
import suzane00.purchase.purchase_order.form.FormPurchaseOrderAbstract;

/**
 *
 * @author Usere
 */
public class FormTest extends Application {
    
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        try {
            Class.forName("suzane00.global.Environment");
        } catch (ClassNotFoundException e) {
            System.out.println("Can't load environment class");
            e.printStackTrace();
            return;
        }
        
//        FormRegisterItem form = new FormRegisterItem();
        FormPurchaseOrder form = new FormPurchaseOrder();
//        FormRegisterCountry form = new FormRegisterCountry();
//        FormAddCity form = new FormAddCity();        
//        FormRegisterSupplier form = new FormRegisterSupplier();
//        FormPickItem form = new FormPickItem();
//        FormRegisterUnit form = new FormRegisterUnit() ;
//        FormRegisterSellPriceType form = new FormRegisterSellPriceType();

        form.show();
    }   
}
