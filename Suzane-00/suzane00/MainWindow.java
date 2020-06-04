/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import suzane00.inventory.form.FormArea;
import suzane00.inventory.form.FormBrand;
import suzane00.inventory.form.FormItem;
import suzane00.inventory.form.FormProductType;
import suzane00.inventory.form.FormSellPriceType;
import suzane00.inventory.form.FormUnit;
import suzane00.inventory.form.FormViewArea;
import suzane00.inventory.form.FormViewBrand;
import suzane00.inventory.form.FormViewItem;
import suzane00.inventory.form.FormViewProductType;
import suzane00.inventory.form.FormViewSellPriceType;
import suzane00.inventory.form.FormViewUnit;
import suzane00.purchase.order_receival.form.FormOrderReceival;
import suzane00.purchase.order_receival.form.FormViewOrderReceival;
import suzane00.purchase.purchase_order.form.FormPurchaseOrder;
import suzane00.purchase.purchase_order.form.FormViewPurchaseOrder;
import suzane00.purchase.purchase_retur.form.FormPurchaseRetur;
import suzane00.purchase.purchase_retur.form.FormViewPurchaseRetur;
import suzane00.purchase.vendor_invoice.form.FormVendorInvoice;
import suzane00.purchase.vendor_invoice.form.FormViewVendorInvoice;
import suzane00.purchase.vendor_payment.form.FormVendorPayment;
import suzane00.purchase.vendor_payment.form.FormViewVendorPayment;
import suzane00.sale.delivery_order.form.FormDeliveryOrder;
import suzane00.sale.delivery_order.form.FormViewDeliveryOrder;
import suzane00.sale.sales_invoice.form.FormSalesInvoice;
import suzane00.sale.sales_invoice.form.FormViewSalesInvoice;
import suzane00.sale.sales_order.form.FormSalesOrder;
import suzane00.sale.sales_order.form.FormViewSalesOrder;
import suzane00.sale.sales_payment.form.FormSalesPayment;
import suzane00.sale.sales_payment.form.FormViewSalesPayment;
import suzane00.sale.sales_retur.form.FormSalesRetur;
import suzane00.sale.sales_retur.form.FormViewSalesRetur;
import suzane00.sale.stock_reservation.form.FormStockReservation;
import suzane00.sale.stock_reservation.form.FormViewStockReservation;
import suzane00.source.form.FormCity;
import suzane00.source.form.FormCountry;
import suzane00.source.form.FormCustomer;
import suzane00.source.form.FormCustomerGroup;
import suzane00.source.form.FormSupplier;
import suzane00.source.form.FormSupplierGroup;
import suzane00.source.form.FormViewCity;
import suzane00.source.form.FormViewCountry;
import suzane00.source.form.FormViewCustomer;
import suzane00.source.form.FormViewCustomerGroup;
import suzane00.source.form.FormViewSupplier;
import suzane00.source.form.FormViewSupplierGroup;
import suzane00.warehouse.stock_adjustment.form.FormStockAdjustment;
import suzane00.warehouse.stock_adjustment.form.FormViewStockAdjustment;
import suzane00.warehouse.transfer_stock.form.FormTransferStock;
import suzane00.warehouse.transfer_stock.form.FormViewTransferStock;

/**
 *
 * @author Usere
 */
public class MainWindow extends Stage {
    protected BorderPane areaMain;
    protected Scene v_scene = null;
    
    //-------------------------------------------------- MENU COMPONENTS ---------------------------------------------
    protected MenuBar mb_main = new MenuBar();
    
    /* level 1 menu */
    
    protected Menu menu_file = new Menu("File");
    
    /* level 2 menu */
    
    // menu_file
    protected Menu menu_newFile = new Menu("New File");
    protected Menu menu_openFile = new Menu("Open File");
    protected Menu menu_quit = new Menu("Quit");
    
    /* level 3 menu */
    
    // menu_file -> menu_newFile
    protected Menu menu_newPurchase = new Menu("Purchasing");
    protected Menu menu_newSale = new Menu("Sale");
    protected Menu menu_newInventory = new Menu("Inventory");
    protected Menu menu_newMaster = new Menu("Master data");
    
    // menu_file -> menu_openFile
    protected Menu menu_openPurchase = new Menu("Purchasing");
    protected Menu menu_openSale = new Menu("Sale");
    protected Menu menu_openInventory = new Menu("Inventory");
    protected Menu menu_openMaster = new Menu("Master data");
    
    /* level 4 menu */
    
    // menu_file -> menu_newFile -> menu_newPurchase
    protected MenuItem mit_purchaseOrder = new MenuItem("Purchase Order");
    protected MenuItem mit_vendorInvoice = new MenuItem("Vendor Invoice");
    protected MenuItem mit_orderReceival = new MenuItem("Order Receival");
    protected MenuItem mit_purchaseRetur = new MenuItem("Purchase Retur");
    protected MenuItem mit_vendorPayment = new MenuItem("Vendor Payment");
    
    // menu_file -> menu_newFile -> menu_newSale
    protected MenuItem mit_salesOrder = new MenuItem("Sales Order");
    protected MenuItem mit_salesInvoice = new MenuItem("Sales Invoice");
    protected MenuItem mit_stockReservation = new MenuItem("Stock Reservation");
    protected MenuItem mit_deliveryOrder = new MenuItem("Delivery Order");
    protected MenuItem mit_salesRetur = new MenuItem("Sales Retur");
    protected MenuItem mit_salesPayment = new MenuItem("Sales Payment");
    
    //menu_file ->menu_newFile -> menu_newInventory
    protected MenuItem mit_stockAdjustment = new MenuItem("Stock Adjustment");
    protected MenuItem mit_transferStock = new MenuItem("Transfer Stock");
    
    // menu_file -> menu_newFile -> menu_newMaster
    protected Menu menu_newMasterPurchase = new Menu("Purchase");
    protected Menu menu_newMasterSale = new Menu("Sale");
    protected Menu menu_newMasterInventory = new Menu("Inventory");
    protected MenuItem mit_city = new MenuItem("City");
    protected MenuItem mit_country = new MenuItem("Country");
    
    // menu_file -> menu_openFile -> menu_openPurchase
    protected MenuItem mit_poList = new MenuItem("Purchase Order List");
    protected MenuItem mit_viList = new MenuItem("Vendor Invoice List");
    protected MenuItem mit_orList = new MenuItem("Order Receival List");
    protected MenuItem mit_prList = new MenuItem("Purchase Retur List");
    protected MenuItem mit_vpList = new MenuItem("Vendor Payment List");
    
    // menu_file -> menu_openFile -> menu_openPO
    protected MenuItem mit_soList = new MenuItem("Sales Order List");
    protected MenuItem mit_siList = new MenuItem("Sales Invoice List");
    protected MenuItem mit_srList = new MenuItem("Stock Reservation List");
    protected MenuItem mit_doList = new MenuItem("Delivery Order List");
    protected MenuItem mit_stList = new MenuItem("Sales Retur List");
    protected MenuItem mit_spList = new MenuItem("Sales Payment List");
    
    // menu_file -> menu_openFile -> menu_openInventory
    protected MenuItem mit_saList = new MenuItem("Stock Adjustment List");
    protected MenuItem mit_tsList = new MenuItem("Transfer Stock List");
    
     // menu_file -> menu_openFile -> menu_openMaster
    protected Menu menu_openMasterPurchase = new Menu("Purchase");
    protected Menu menu_openMasterSale = new Menu("Sale");
    protected Menu menu_openMasterInventory = new Menu("Inventory");
    protected MenuItem mit_cityList = new MenuItem("City");
    protected MenuItem mit_countryList = new MenuItem("Country");
    
    /* level 5 menu */
    
    // menu_file -> menu_newFile -> menu_newMaster -> menu_newMasterPurchase
    protected MenuItem mit_supplier = new MenuItem("Supplier");
    protected MenuItem mit_supplierGroup = new MenuItem("Supplier group");
    
    // menu_file -> menu_newFile -> menu_newMaster -> menu_newMasterSale
    protected MenuItem mit_customer = new MenuItem("Customer");
    protected MenuItem mit_customerGroup = new MenuItem("Customer group");
    
    // menu_file -> menu_newFile -> menu_newMaster -> menu_newMasterInventory
    protected MenuItem mit_item = new MenuItem("Item");
    protected MenuItem mit_productType = new MenuItem("Product type");
    protected MenuItem mit_brand = new MenuItem("Brand");
    protected MenuItem mit_area = new MenuItem("Area");
    protected MenuItem mit_unit = new MenuItem("Unit");
    protected MenuItem mit_sellPriceType = new MenuItem("Sell price type");
    
     // menu_file -> menu_openFile -> menu_openMaster -> menu_openMasterPurchase
    protected MenuItem mit_supplierList = new MenuItem("Supplier");
    protected MenuItem mit_sgList = new MenuItem("Supplier group");
    
     // menu_file -> menu_openFile -> menu_openMaster -> menu_openMasterSale
    protected MenuItem mit_customerList = new MenuItem("Customer");
    protected MenuItem mit_cgList = new MenuItem("Customer group");
    
     // menu_file -> menu_openFile -> menu_openMaster -> menu_openMasterInventory
    protected MenuItem mit_itemList = new MenuItem("Item");
    protected MenuItem mit_productTypeList = new MenuItem("Product type");
    protected MenuItem mit_areaList = new MenuItem("Area");
    protected MenuItem mit_brandList = new MenuItem("Brand");
    protected MenuItem mit_unitList = new MenuItem("Unit");
    protected MenuItem mit_sptList = new MenuItem("Sell price type");
    
    
    public MainWindow() {
        setAppearance();
        setActionPerformed();
        setScene();
    }
    
    protected void setAppearance() {
        setMenuBar();
        setMainArea();
        setTitle("SUZANE-00");
    }
    
    protected void setActionPerformed() {
        setPurchaseMenusActionPerformed();
        setSaleMenusActionPerformed();
        setInventoryMenusActionPerformed();
        setMasterDataMenusActionPerformed();
    }
    
    protected void setMenuBar() {
        
        mb_main.getMenus().addAll(menu_file);

        menu_file.getItems().addAll(menu_newFile, menu_openFile, menu_quit);
       
        menu_newFile.getItems().addAll(menu_newPurchase, menu_newSale, menu_newInventory, menu_newMaster);
        menu_openFile.getItems().addAll(menu_openPurchase, menu_openSale, menu_openInventory, menu_openMaster);
        
        menu_newPurchase.getItems().addAll(mit_purchaseOrder, mit_vendorInvoice, mit_orderReceival, 
                mit_purchaseRetur, mit_vendorPayment);
        menu_newSale.getItems().addAll(mit_salesOrder, mit_salesInvoice, mit_stockReservation, 
                mit_deliveryOrder, mit_salesRetur, mit_salesPayment);
        menu_newInventory.getItems().addAll(mit_stockAdjustment, mit_transferStock);
        menu_newMaster.getItems().addAll(menu_newMasterPurchase, menu_newMasterSale, menu_newMasterInventory);
        menu_newMaster.getItems().addAll(mit_country, mit_city);
        
        menu_openPurchase.getItems().addAll(mit_poList, mit_viList, mit_orList, mit_prList, mit_vpList);
        menu_openSale.getItems().addAll(mit_soList, mit_siList, mit_srList, mit_doList, mit_stList,
                mit_spList);
        menu_openInventory.getItems().addAll(mit_saList, mit_tsList);
        menu_openMaster.getItems().addAll(menu_openMasterPurchase, menu_openMasterSale, menu_openMasterInventory); 
        menu_openMaster.getItems().addAll(mit_cityList, mit_countryList);
        
        menu_newMasterPurchase.getItems().addAll(mit_supplier, mit_supplierGroup);
        menu_newMasterSale.getItems().addAll(mit_customer, mit_customerGroup);
        menu_newMasterInventory.getItems().addAll(mit_item, mit_productType, 
                mit_unit, mit_sellPriceType, mit_brand, mit_area);
        
        menu_openMasterPurchase.getItems().addAll(mit_supplierList, mit_sgList);
        menu_openMasterSale.getItems().addAll(mit_customerList, mit_cgList);
        menu_openMasterInventory.getItems().addAll(mit_itemList, mit_productTypeList, 
                mit_unitList, mit_sptList, mit_brandList, mit_areaList);
        
        setBackgroundMenu();
        
    }
    
    protected void setMainArea() {
        areaMain = new BorderPane();
        areaMain.setTop(mb_main);
        areaMain.setPrefSize(800, 650);
    }    
    
    protected void setScene() {
        try {
            v_scene = new Scene(areaMain);
            InputStream is = this.getClass().getResource(
                    "/suzane00/global/resource/Blue Vector Gradient.jpg").openStream();
            BackgroundImage myBI= new BackgroundImage(new Image(is,626,626,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(0, 0, false, false, false, true));
            areaMain.setBackground(new Background(myBI));
            setScene(v_scene);
        }
        catch(IOException e) {
            e.printStackTrace();;
        }
    }
    
     protected void setBackgroundMenu() {
        try {
            InputStream is = this.getClass().getResource(
                    "/suzane00/global/resource/Light Silver Gradient.jpg").openStream();
            BackgroundImage myBI= new BackgroundImage(new Image(is,32,32,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(0, 0, false, false, false, true));
            mb_main.setBackground(new Background(myBI));
        }
        catch(IOException e) {
            e.printStackTrace();;
        }
    }
    
    protected void setPurchaseMenusActionPerformed() {
        mit_purchaseOrder.setOnAction(
                e -> {
                    FormPurchaseOrder form = new FormPurchaseOrder();
                    form.setTitle("New Purchase Order");
                    form.show();
                }
        );
        
        mit_vendorInvoice.setOnAction(
                e -> {
                    FormVendorInvoice form = new FormVendorInvoice();
                    form.setTitle("New Vendor invoice");
                    form.show();
                }
        );
        
        mit_orderReceival.setOnAction(
                e -> {
                    FormOrderReceival form = new FormOrderReceival();
                    form.setTitle("New Order Receival");
                    form.show();
                }
        );
        
        mit_purchaseRetur.setOnAction(
                e -> {
                    FormPurchaseRetur form = new FormPurchaseRetur();
                    form.setTitle("New Purchase Retur");
                    form.show();
                }
        );
        
        mit_vendorPayment.setOnAction(
                e -> {
                    FormVendorPayment form = new FormVendorPayment();
                    form.setTitle("New Vendor Payment");
                    form.show();
                }
        );
        
        mit_supplier.setOnAction(e -> {
                    FormSupplier form = new FormSupplier();
                    form.setTitle("New Supplier");
                    form.show();
                }
        );
        
        mit_supplierGroup.setOnAction(
                e -> {
                    FormSupplierGroup form = new FormSupplierGroup();
                    form.setTitle("New Supplier Group");
                    form.show();
                }
        );
        
        mit_poList.setOnAction(
                e -> {
                    FormViewPurchaseOrder form = new FormViewPurchaseOrder();
                    form.show();
                }
        );
        
        mit_viList.setOnAction(
                e -> {
                    FormViewVendorInvoice form = new FormViewVendorInvoice();
                    form.show();
                }
        );
        
        mit_orList.setOnAction(
                e -> {
                    FormViewOrderReceival form = new FormViewOrderReceival();
                    form.show();
                }
        );
        
        mit_prList.setOnAction(
                e -> {
                    FormViewPurchaseRetur form = new FormViewPurchaseRetur();
                    form.show();
                }
        );
        
        mit_prList.setOnAction(
                e -> {
                    FormViewPurchaseRetur form = new FormViewPurchaseRetur();
                    form.show();
                }
        );
        
        mit_supplierList.setOnAction(
                e -> {
                    FormViewSupplier form = new FormViewSupplier();
                    form.show();
                }
        );
        
        mit_vpList.setOnAction(
                e -> {
                    FormViewVendorPayment form = new FormViewVendorPayment();
                    form.show();
                }
        );
    }
    
    protected void setSaleMenusActionPerformed() {
       
        mit_salesOrder.setOnAction(
                e -> {
                    FormSalesOrder form = new FormSalesOrder();
                    form.setTitle("New Sales Order");
                    form.show();
                }
        );
        
        mit_salesInvoice.setOnAction(
                e -> {
                    FormSalesInvoice form = new FormSalesInvoice();
                    form.setTitle("New Sales Invoice");
                    form.show();
                }
        );
        
        mit_stockReservation.setOnAction(
                e -> {
                    FormStockReservation form = new FormStockReservation();
                    form.setTitle("New Stock Reservation");
                    form.show();
                }
        );
        
        mit_deliveryOrder.setOnAction(
                e -> {
                    FormDeliveryOrder form = new FormDeliveryOrder();
                    form.setTitle("New Delivery Order");
                    form.show();
                }
        );
        
        mit_salesRetur.setOnAction(
                e -> {
                    FormSalesRetur form = new FormSalesRetur();
                    form.setTitle("New Sales retur");
                    form.show();
                }
        );
        
        mit_salesPayment.setOnAction(
                e -> {
                    FormSalesPayment form = new FormSalesPayment();
                    form.setTitle("New Sales Payment");
                    form.show();
                }
        );
        
        mit_customer.setOnAction(e -> {
                    FormCustomer form = new FormCustomer();
                    form.setTitle("New Customer");
                    form.show();
                }
        );
        
        mit_customerGroup.setOnAction(
                e -> {
                    FormCustomerGroup form = new FormCustomerGroup();
                    form.setTitle("New Customer Group");
                    form.show();
                }
        );
        
        mit_soList.setOnAction(
                e -> {
                    FormViewSalesOrder form = new FormViewSalesOrder();
                    form.show();
                }
        );
        
        mit_siList.setOnAction(
                e -> {
                    FormViewSalesInvoice form = new FormViewSalesInvoice();
                    form.show();
                }
        );
        
        mit_srList.setOnAction(
                e -> {
                    FormViewStockReservation form = new FormViewStockReservation();
                    form.show();
                }
        );
        
        mit_stList.setOnAction(
                e -> {
                    FormViewSalesRetur form = new FormViewSalesRetur();
                    form.show();
                }
        );
        
        mit_spList.setOnAction(
                e -> {
                    FormViewSalesPayment form = new FormViewSalesPayment();
                    form.show();
                }
        );
        
        mit_doList.setOnAction(
                e -> {
                    FormViewDeliveryOrder form = new FormViewDeliveryOrder();
                    form.show();
                }
        );
        
        mit_customerList.setOnAction(
                e -> {
                    FormViewCustomer form = new FormViewCustomer();
                    form.show();
                }
        );
        
        mit_cgList.setOnAction(
                e -> {
                    FormViewCustomerGroup form = new FormViewCustomerGroup();
                    form.show();
                }
        );
        
    }
    
    protected void setInventoryMenusActionPerformed() {
       
        mit_stockAdjustment.setOnAction(
                e -> {
                    FormStockAdjustment form = new FormStockAdjustment();
                    form.setTitle("New Stock Adjustment");
                    form.show();
                }
        );
        
         mit_transferStock.setOnAction(
                e -> {
                    FormTransferStock form = new FormTransferStock();
                    form.setTitle("New Transfer Stock");
                    form.show();
                }
        );
        
        mit_item.setOnAction(
                e -> {
                    FormItem form = new FormItem();
                    form.setTitle("New Itemr");
                    form.show();
                }
        );
        
        mit_productType.setOnAction(
                e -> {
                    FormProductType form = new FormProductType();
                    form.setTitle("New Product Type");
                    form.show();
                }
        );
        
        mit_brand.setOnAction(e -> {
                    FormBrand form = new FormBrand();
                    form.setTitle("New Brand");
                    form.show();
                }
        );
        
        mit_area.setOnAction(e -> {
                    FormArea form = new FormArea();
                    form.setTitle("New Arear");
                    form.show();
                }
        );
        
        mit_unit.setOnAction(e -> {
                    FormUnit form = new FormUnit();
                    form.setTitle("New Unit");
                    form.show();
                }
        );
        
        mit_sellPriceType.setOnAction(e -> {
                    FormSellPriceType form = new FormSellPriceType();
                    form.setTitle("New Sell Price Type");
                    form.show();
                }
        );
        
        mit_saList.setOnAction(
                e -> {
                    FormViewStockAdjustment form = new FormViewStockAdjustment();
                    form.show();
                }
        );
        
        mit_tsList.setOnAction(
                e -> {
                    FormViewTransferStock form = new FormViewTransferStock();
                    form.show();
                }
        );
        
        mit_itemList.setOnAction(
                e -> {
                    FormViewItem form = new FormViewItem();
                    form.show();
                }
        );
        
        mit_productTypeList.setOnAction(
                e -> {
                    FormViewProductType form = new FormViewProductType();
                    form.show();
                }
        );
        
        mit_areaList.setOnAction(e -> {
                    FormViewArea form = new FormViewArea();
                    form.show();
                }
        );
        
        mit_unitList.setOnAction(
                e -> {
                    FormViewUnit form = new FormViewUnit();
                    form.show();
                }
        );
        
        mit_brandList.setOnAction(e -> {
                    FormViewBrand form = new FormViewBrand();
                    form.show();
                }
        );
        
        mit_sptList.setOnAction(e -> {
                    FormViewSellPriceType form = new FormViewSellPriceType();
                    form.show();
                }
        );
    }
    
    protected void setMasterDataMenusActionPerformed() {
        
        mit_city.setOnAction(
                e -> {
                    FormCity form = new FormCity();
                    form.setTitle("New City");
                    form.show();
                }
        );
        
        mit_country.setOnAction(
                e -> {
                    FormCountry form = new FormCountry();
                    form.setTitle("New Countrt");
                    form.show();
                }
        );
        
        mit_cityList.setOnAction(
                e -> {
                    FormViewCity form = new FormViewCity();
                    form.show();
                }
        );
        
        mit_countryList.setOnAction(
                e -> {
                    FormViewCountry form = new FormViewCountry();
                    form.show();
                }
        );
    }
}
