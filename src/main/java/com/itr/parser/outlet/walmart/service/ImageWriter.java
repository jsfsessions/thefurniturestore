package com.itr.parser.outlet.walmart.service;

import com.itr.outlet.walmart.control.ProformaFileHandlerBean;
import com.itr.entity.WalmartProduct;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Logger;

public class ImageWriter {

    private final static Logger LOGGER = Logger.getLogger(ProformaFileHandlerBean.class.getName());
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    private String date = sdf.format(new Date());
    private WalmartProduct product;

    private static final String INVENTORY_PRODUCTS = "inventory\\products";
    private static final String PATH_TO_RESOURCES = "C:\\Users\\tyler durden\\OneDrive\\education\\Workspace\\projects"
            + "\\ItrKickOff\\src\\main\\webapp\\resources\\itr\\proformas";
    // C:\Users\tyler durden\OneDrive\education\Workspace\projects\ItrKickOff\src\main\webapp\resources\itr\proformas

    public ImageWriter(WalmartProduct product) {
        this.product = product;
    }
    
    public File createDirectory() {  
        
        LOGGER.info(this.getClass().getName());

        File dir = new File(PATH_TO_RESOURCES + File.separator + INVENTORY_PRODUCTS + File.separator + date + File.separator
                + product.getProductNameFromProforma().replaceAll("[-+.^:'\",/]", "").replaceAll(" ", "_"));

        System.out.println("directory where products are placed " + dir);
        dir.mkdirs();
        return dir;
    }

    public ImageWriter loadImages() {
        
        Iterator<String> links = product.getImages().iterator();
        int count = 1;
        FileOutputStream fos = null;

        String date = sdf.format(new Date());
        while (links.hasNext()) {
            try (InputStream in = new BufferedInputStream(new URL(links.next()).openStream());
                    ByteArrayOutputStream out = new ByteArrayOutputStream()) {

                File image = new File(PATH_TO_RESOURCES + File.separator + INVENTORY_PRODUCTS + File.separator + 
                        date + File.separator + product.getProductNameFromProforma().replaceAll("[-+.^:'\",/]", "").replaceAll(" ", "_") + 
                        "/" + count++ + ".jpg");

                byte[] buf = new byte[1024];
                int n = 0;
                while (-1 != (n = in.read(buf))) {
                    out.write(buf, 0, n);
                }

                byte[] response = out.toByteArray();

                fos = new FileOutputStream(image);
                fos.write(response);
                fos.close();
            } catch (IOException e) {
            }
        }
        return this;
    }
}
