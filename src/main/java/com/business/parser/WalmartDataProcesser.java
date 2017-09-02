package com.business.parser;

import com.business.entity.WalmartProduct;
import com.business.parser.service.ExcelInputService;
import com.business.parser.service.ExcelOutputService;
import com.business.parser.service.ResponseParseService;
import com.web.fileprocessing.BarIncrementator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WalmartDataProcesser implements Callable<List<WalmartProduct>> {

    private final File inputFile;
    private final XSSFWorkbook excel;
    private final CountDownLatch latch;
    private final BarIncrementator barIncrementator;

    public WalmartDataProcesser(final String path, final CountDownLatch latch, final BarIncrementator barIncrementator)
            throws FileNotFoundException, IOException {
        this.latch = latch;
        this.barIncrementator = barIncrementator;
        this.inputFile = new File(path);
        this.excel = new XSSFWorkbook(new FileInputStream(inputFile));
    }

    @Override
    public List<WalmartProduct> call() throws InterruptedException, IOException {

        List<WalmartProduct> processedProducts = new ArrayList<>();
        ExcelInputService input = new ExcelInputService(excel.getSheetAt(0));
        ResponseParseService parser = new ResponseParseService();
        ExcelOutputService output = new ExcelOutputService(excel);

        barIncrementator.setRows(input.getAllProducts().size());

        latch.countDown();
        latch.await(1, TimeUnit.MINUTES);

        excel.close();

        input.getAllProducts().iterator().forEachRemaining(I -> {
            try {
                I = parser.getDataForSingleProduct(I);
                //output.writeProduct(I);
                //Get product (with some data) from list -> form a URL, which is also a call to API. ->
                processedProducts.add(I);
                barIncrementator.incProduct();
            } catch (UnknownHostException ex) {
                Logger.getLogger(WalmartDataProcesser.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex);
            }
        });

        //remove do not need data written to excel
        //output.writeChanges(inputFile);
        return processedProducts;
    }
}
