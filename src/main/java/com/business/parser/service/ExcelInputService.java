package com.business.parser.service;

import com.business.entity.WalmartProduct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class ExcelInputService {

    private final AtomicInteger totalRows = new AtomicInteger(0);
    private final AtomicInteger actualRow = new AtomicInteger(1);
    XSSFSheet excelData;

    public ExcelInputService(XSSFSheet excelData) {
        this.excelData = excelData;
    }

    public int calculateProductsInFile() {
        Iterator<Row> rowIterator = excelData.iterator();
        while (rowIterator.hasNext()) {
            Row currentRow = rowIterator.next();

            if (currentRow.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue() == null
                    || "".equals(currentRow.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue())) {
                continue;
            }

            totalRows.incrementAndGet();
        }
        return totalRows.get();
    }

    public List<WalmartProduct> getAllProducts() {
        List<WalmartProduct> products = new ArrayList<>();
        Iterator<Row> rowIterator = excelData.iterator();
        rowIterator.next();
        while (rowIterator.hasNext()) {
            Row inputRow = rowIterator.next();

            inputRow = formatCellToStringType(inputRow);

            WalmartProduct product = new WalmartProduct()
                    .setProductName2(inputRow.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue())
                    .setImportedUpc2(inputRow.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue())
                    .setSalePrice2(inputRow.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue())
                    .setRetailPrice2(inputRow.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue())
                    .setProductNameFromProforma2(inputRow.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue())
                    .setQuantity2(inputRow.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue())
                    .setReturnReason2(inputRow.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue())
                    .setWeight2(inputRow.getCell(13, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue())
                    .setPalletId2(inputRow.getCell(15, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue())
                    .setPalletName2(inputRow.getCell(14, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue())
                    .setPalletSize2(inputRow.getCell(16, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue())
                    .setDimensionX2(inputRow.getCell(10, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue())
                    .setDimensionY2(inputRow.getCell(11, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue())
                    .setDimensionZ2(inputRow.getCell(12, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue())
                    .setSubcategory2(inputRow.getCell(9, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue())
                    .setDepartment2(inputRow.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue())
                    .setVendor2(inputRow.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());

            products.add(product);
            System.out.println("product: " + product.toString());
        }
        return products;
    }

    private Row formatCellToStringType(Row unformatted) {
        Iterator<Cell> freshRows = unformatted.iterator();
        while (freshRows.hasNext()) {
            Cell cell = freshRows.next();
            cell.setCellType(CellType.STRING);
        }
        return unformatted;
    }

    public AtomicInteger getTotalRows() {
        return totalRows;
    }

    public AtomicInteger getActualRow() {
        return actualRow;
    }
}
