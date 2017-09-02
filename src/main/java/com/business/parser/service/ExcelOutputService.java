package com.business.parser.service;

import com.business.entity.WalmartProduct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelOutputService {

    private static final String DATA_FROM_WALMART = "FETCHED_INFO";
    private AtomicInteger actualRow = new AtomicInteger(0);
    private XSSFWorkbook excel;
    private XSSFSheet sheet;

    public ExcelOutputService(XSSFWorkbook excelData) {
        this.excel = excelData;

        try {
            sheet = excel.createSheet(DATA_FROM_WALMART);

            this.prepareOutputSheet();
        } catch (IllegalArgumentException e) {
            excel.removeSheetAt(excel.getSheetIndex(DATA_FROM_WALMART));
            sheet = excel.createSheet(DATA_FROM_WALMART);
        }
    }

    //isnt this writing into FETCHED ? this is writing the headers into new fetched sheet right ?? Yes, but data is written phyy just after
    //  continue !!sicall
    public void writeProduct(WalmartProduct product) {

        Row currentRow = sheet.createRow(actualRow.getAndIncrement());

        currentRow.createCell(0).setCellValue(product.getProductId());
        currentRow.createCell(1).setCellValue(product.getUpc());
        currentRow.createCell(2).setCellValue(product.getImportedUpc());
        currentRow.createCell(3).setCellValue(product.getProductNameFromProforma());
        currentRow.createCell(4).setCellValue(product.getProductName());
        currentRow.createCell(5).setCellValue(product.getProductDescription());
        currentRow.createCell(6).setCellValue(product.getBrandName());
        currentRow.createCell(7).setCellValue(product.getSalePrice());
        currentRow.createCell(8).setCellValue(product.getRetailPrice());
        currentRow.createCell(9).setCellValue(product.getOfferType());
        currentRow.createCell(10).setCellValue(product.getModelNumber());
        currentRow.createCell(11).setCellValue(product.getColor());
        currentRow.createCell(12).setCellValue(product.getCategory());
        currentRow.createCell(13).setCellValue(product.getStock());
        currentRow.createCell(14).setCellValue(product.getStandardShipRate());
        currentRow.createCell(15).setCellValue(product.getShipToStore());
        currentRow.createCell(16).setCellValue(product.getFreeShipToStore());
        currentRow.createCell(17).setCellValue(product.getSize());
        currentRow.createCell(18).setCellValue(product.getWeight());
        currentRow.createCell(19).setCellValue(product.getPalletId());
        currentRow.createCell(20).setCellValue(product.getPalletName());
        currentRow.createCell(21).setCellValue(product.getPalletSize());
        currentRow.createCell(22).setCellValue(product.getSellerInfo());
        currentRow.createCell(23).setCellValue(product.getMarketplace());
        currentRow.createCell(24).setCellValue(product.getCustomerRating());
        currentRow.createCell(25).setCellValue(product.getCustomerReviews());
        currentRow.createCell(26).setCellValue(product.getReturnReason());
        currentRow.createCell(27).setCellValue(product.getQuantity());
    }

    public void prepareOutputSheet() {

        Row header = sheet.createRow(actualRow.getAndIncrement());

        header.createCell(0).setCellValue("Product_ID");
        header.createCell(1).setCellValue("UPC");
        header.createCell(2).setCellValue("Imported_UPC");
        header.createCell(3).setCellValue("Selected_by");
        header.createCell(4).setCellValue("Product_Name");
        header.createCell(5).setCellValue("Product_Description");
        header.createCell(6).setCellValue("Brand_Name");
        header.createCell(7).setCellValue("Sell_Price");
        header.createCell(8).setCellValue("Retail_Price");
        header.createCell(9).setCellValue("Offer_Type");
        header.createCell(10).setCellValue("Model_Number");
        header.createCell(11).setCellValue("Color");
        header.createCell(12).setCellValue("Category_Full");
        header.createCell(13).setCellValue("Stock");
        header.createCell(14).setCellValue("Standard_Ship_Rate");
        header.createCell(15).setCellValue("Ship_To_Store");
        header.createCell(16).setCellValue("Free_Ship_To_Store");
        header.createCell(17).setCellValue("Size");
        header.createCell(18).setCellValue("Ship_Weight");
        header.createCell(19).setCellValue("Pallet_ID");
        header.createCell(20).setCellValue("Pallet_Name");
        header.createCell(21).setCellValue("Pallet_Size");
        header.createCell(22).setCellValue("Seller_Info");
        header.createCell(23).setCellValue("Marketplace");
        header.createCell(24).setCellValue("Customer_Rating");
        header.createCell(25).setCellValue("Customer_Reviews");
        header.createCell(26).setCellValue("Reason");
        header.createCell(27).setCellValue("Quantity");
    }

    public void writeChanges(File output) {
        try {
            excel.write(new FileOutputStream(output));
            excel.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    public AtomicInteger getActualRow() {
        return actualRow;
    }
}
