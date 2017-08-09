//package com.itr.datamodel;
//
//import com.itr.boundary.WalmartProductService;
//import java.io.Serializable;
//import java.util.List;
//import java.util.logging.Logger;
//import javax.ejb.EJB;
//import javax.faces.model.ListDataModel;
//import org.primefaces.model.SelectableDataModel;
//
//public class ProductDataModel<Product> extends ListDataModel<Product> implements SelectableDataModel<Product>, Serializable {
//
//    private final static Logger LOGGER = Logger.getLogger(WalmartProductService.class.getName());
//    private static final long serialVersionUID = 1L;
//
//    @EJB
//    private WalmartProductService productService;
//    private SelectableDataModel<Product> selectedProducts;
//    private com.itr.entity.Product product;
//
//    public ProductDataModel(List<Product> data) {
//        super(data);
//    }
//
//    public WalmartProductService getProductService() {
//        return productService;
//    }
//
//    public void setProductService(WalmartProductService productService) {
//        this.productService = productService;
//    }
//
//    public SelectableDataModel<Product> getSelectedProducts() {
//        return selectedProducts;
//    }
//
//    public void setSelectedProducts(SelectableDataModel<Product> selectedProducts) {
//        this.selectedProducts = selectedProducts;
//    }
//
//    @Override
//    public Product getRowData(String rowKey) {
//        List<Product> products = (List<Product>) getWrappedData();
//        
//        for (Product product : products) {
//           // if (String.valueOf(product.getId()).equals(rowKey)) {
//                System.out.println("String.valueOf(p.getId().equals(rowKey): " + rowKey);
//                
//                return product;
//           // }
//        }
//        return null;
//    }
//
//    @Override
//    public Object getRowKey(Product product) {
////        LOGGER.log(Level.INFO, "product id is : {0}", product.getId());
////        return product.getId();
//        return null;
//    }
//    
//    
//    
//    
// 
//
//}
