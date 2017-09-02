package com.business.parser.service;

import com.business.entity.WalmartProduct;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ResponseParseService {

    private WalmartQueryService connector;

    public ResponseParseService() {
        connector = new WalmartQueryService();
    }

    public WalmartProduct getDataForSingleProduct(WalmartProduct product) throws UnknownHostException {
        String url = connector.getStringForProduct(product.getProductNameFromProforma());
        try {
            Document document = fetchData(url);
            if (!document.select("searchresponse message").stream().collect(Collectors.toList()).isEmpty()) {
                product.setDataSource2("EXCEL");
                return writeFilesForProduct(product);
            }

            List<Element> elements = document.select("items item:contains(" + product.getProductNameFromProforma() + ")")
                    .stream()
                    .collect(Collectors.toList());
            if (elements.isEmpty()) {
                product.setDataSource2("EXCEL");
                return writeFilesForProduct(product);
            }

            Element currentElement = elements.get(0);
            for (Entry<String, Consumer<Elements>> mapper : product.getSelectors().entrySet()) {
                String query = mapper.getKey();
                Consumer<Elements> setter = mapper.getValue();

                setter.accept(currentElement.select(query));
            }

            product.setProductDescription2(Jsoup.parse(Jsoup.parse(product.getProductDescription()).text()).text()).setDataSource2("WALMART API").wrapImages();
            product = writeFilesForProduct(product);

        } catch (IOException e) {
            if (e instanceof UnknownHostException) {
                throw new UnknownHostException(e.getMessage());
            }
        }

        return product;
    }

    private Document fetchData(String url) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        InputStream stream;
        int read;

        stream = new URL(url).openStream();

        while ((read = stream.read(buffer)) != -1) {
            result.write(buffer, 0, read);
        }

        return Jsoup.parse(result.toString("UTF-8"));
    }

    private WalmartProduct writeFilesForProduct(WalmartProduct product) {

        if (!"EXCEL".equals(product.getDataSource())) {
            ImageWriter writer = new ImageWriter(product);
            writer.createDirectory();
            writer.loadImages();
        }

        return product;
    }
}
