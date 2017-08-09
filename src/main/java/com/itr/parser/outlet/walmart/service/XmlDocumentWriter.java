package com.itr.parser.outlet.walmart.service;

import com.itr.outlet.walmart.control.ProformaFileHandlerBean;
//import com.itr.helpers.ProformaDir;
import com.itr.entity.WalmartProduct;
import com.itr.parser.outlet.walmart.model.WalmartProductList;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class XmlDocumentWriter {

    private final static Logger LOGGER = Logger.getLogger(ProformaFileHandlerBean.class.getName());
    private static final String INVENTORY_PRODUCTS = "inventory\\products";
    private static final String PATH_TO_RESOURCES = "C:\\Users\\tyler durden\\OneDrive\\education\\Workspace\\projects"
            + "\\ItrKickOff\\src\\main\\webapp\\resources\\itr\\proformas";
    
//    private static final String PATH_TO_RESOURCES = "D:\\files";
    
    private WalmartProduct product;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    String date = sdf.format(new Date());

    public XmlDocumentWriter(WalmartProduct product) {
        LOGGER.info(this.getClass().getName());
        this.product = product;
    }

    public XmlDocumentWriter() {
        LOGGER.info(this.getClass().getName());
    }

    public File createDirectory() {
        LOGGER.info(this.getClass().getName());

        File dir = new File(PATH_TO_RESOURCES + File.separator + INVENTORY_PRODUCTS + File.separator + date + File.separator
                + product.getProductNameFromProforma().replaceAll("[-+.^:'\",/]", "").replaceAll(" ", "_"));

        System.out.println("directory where products are placed " + dir);
        dir.mkdirs();
        return dir;
    }

    public void createXmlDocumentForProduct() {
        try {
            File file = new File(PATH_TO_RESOURCES + File.separator + INVENTORY_PRODUCTS + File.separator + date + File.separator
                    + product.getProductNameFromProforma().replaceAll("[-+.^:'\",/]", "").replaceAll(" ", "_") + "/document.xml");

            JAXBContext jaxbContext = JAXBContext.newInstance(WalmartProduct.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(product, file);
        } catch (JAXBException e) {
        }
    }

    public void createXmlDocumentForAllProducts(List<WalmartProduct> products) {
        try {
            File file = new File(PATH_TO_RESOURCES + File.separator + INVENTORY_PRODUCTS + File.separator + "allProducts.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(WalmartProductList.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(new WalmartProductList().setProducts(products), file);
        } catch (JAXBException e) {
        }
    }
}
