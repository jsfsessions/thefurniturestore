package com.itr.entity;

import com.itr.parser.outlet.walmart.model.Selectable;
import com.itr.parser.outlet.walmart.model.Selector;
import com.itr.parser.outlet.walmart.model.WalmartImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(propOrder = {"productId", "upc", "productNameFromProforma", "productDescription", "brandName", "retailPrice", "salePrice", "modelNumber",
    "color", "category", "standardShipRate", "walmartImage", "palletSize", "weight", "palletId", "palletName", "sellerInfo", "customerRating",
    "customerReviews", "vendor", "department", "subcategory", "dimensionX", "dimensionY", "dimensionZ", "returnReason", "dataSource"})
@Selector("items item")
@Entity
@Table
public class WalmartProduct implements Selectable, Serializable {

    private static final long serialVersionUID = 42L;

    @Id
    @Column(name = "ID", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @XmlElement
    @Selector("item itemId")
    @Column
    private String productId;

    @Column(name = "imageDirectory", length = 1000)
    private String imageDirectory;

    @Column(name = "imgUrl", length = 5000)
    private String imgUrl;

    @XmlElement
    @Selector("item upc")
    @Column
    private String upc;

    @XmlElement
    @Column
    private String productNameFromProforma;
    private String importedUpc;

    @Selector("item > name")
    @Column
    private String productName;

    @XmlElement
    @Selector("item longDescription")
    @Column(length = 5000)
    private String productDescription;

    @XmlElement
    @Selector("item brandName")
    @Column
    private String brandName;

    @XmlElement
    @Selector("item salePrice")
    @Column
    private String salePrice;

    @XmlElement
    @Selector("item msrp")
    @Column
    private String retailPrice;

    @Selector("item offerType")
    @Column
    private String offerType;

    @XmlElement
    @Selector("item modelNumber")
    @Column
    private String modelNumber;

    @XmlElement
    @Selector("item color")
    @Column
    private String color;

    @XmlElement(name = "categoryFull")
    @Selector("item categoryPath")
    @Column
    private String category;

    @Selector("item stock")
    @Column
    private String stock;

    @XmlElement
    @Selector("item standardShipRate")
    @Column
    private String standardShipRate;

    @Selector("item shipToStore")
    @Column
    private String shipToStore;

    @Selector("item freeShipToStore")
    @Column
    private String freeShipToStore;

    @Selector("item size")
    @Column
    private String size;

    @Transient
    @Selector(value = "item largeImage", isCollection = true)
    private List<String> images = new ArrayList<>();

    @Transient
    @XmlElement(name = "image")
    @XmlElementWrapper(name = "images")
    private List<WalmartImage> walmartImage = new ArrayList<>();

    @XmlElement
    @Column
    private String vendor;

    @XmlElement
    @Column
    private String weight;

    @XmlElement
    @Column
    private String palletId;

    @XmlElement
    @Column
    private String palletName;

    @XmlElement
    @Column
    private String palletSize;

    @XmlElement
    @Selector("item sellerInfo")
    @Column
    private String sellerInfo;

    @Selector("item marketplace")
    @Column
    private String marketplace;

    @XmlElement
    @Selector("item customerRating")
    @Column
    private String customerRating;

    @XmlElement
    @Selector("item numReviews")
    @Column
    private String customerReviews;

    @XmlElement
    @Column
    private String returnReason;

    @XmlElement
    @Column
    private String department;

    @XmlElement
    @Column
    private String subcategory;

    @XmlElement
    @Column
    private String dimensionX;

    @XmlElement
    @Column
    private String dimensionY;

    @XmlElement
    @Column
    private String dimensionZ;

    @XmlElement
    @Column
    private String dataSource;

    @Column
    private String quantity;

    //    this property shows total quantity in all stores
    @Column(name = "total_qty")
    private Integer totalQty;

    //    <===========================================================================>    
//    need to create a cdi bean validation to make sure the product is not placed in a bin that is full; this bin could be in any locale
    @Column(name = "bin_location")
    private String binLocation;

    //    <===========================================================================>    
//    product to be sent to a store location cdi built as follows TXDAL75247 or OKOKC73107 ...  state + city + zipcode
    @Column(name = "store_location")
    private String storeLocation;

    @Column(name = "store_address")
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "streetAddress", column = @Column(name = "street_address"))
        ,
        @AttributeOverride(name = "city", column = @Column(name = "city"))
        ,
        @AttributeOverride(name = "state", column = @Column(name = "state"))
        ,
        @AttributeOverride(name = "zipCode", column = @Column(name = "zipcode"))
    })
    private Address storeAddress;

    //    <===========================================================================>    
//    create cdi to calculate total qty in store of specific locale
    @Column(name = "store_qty")
    private Integer storeQty;

    @Column(name = "date_shipment_received")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateShipmentReceived;

    @Column(name = "slot_in_bin_location")
    private String slotInBinLocation;

    //ebay sale or amazon
    @Column(name = "sales_channel", length = 15)
    private String salesChannel;

    @Column(name = "status", length = 15)
    private String status;

    @Column(name = "store")
    private String store;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public void setRetailPrice(String retailPrice) {
        this.retailPrice = retailPrice;
    }

    public void setShipToStore(String shipToStore) {
        this.shipToStore = shipToStore;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public void setSellerInfo(String sellerInfo) {
        this.sellerInfo = sellerInfo;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    public void setStandardShipRate(String standardShipRate) {
        this.standardShipRate = standardShipRate;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setWalmartImage(List<WalmartImage> walmartImage) {
        this.walmartImage = walmartImage;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setProductNameFromProforma(String productNameFromProforma) {
        this.productNameFromProforma = productNameFromProforma;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public void setPalletName(String palletName) {
        this.palletName = palletName;
    }

    public void setPalletSize(String palletSize) {
        this.palletSize = palletSize;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setImportedUpc(String importedUpc) {
        this.importedUpc = importedUpc;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public void setPalletId(String palletId) {
        this.palletId = palletId;
    }

    public void setMarketplace(String marketplace) {
        this.marketplace = marketplace;
    }

    public void setFreeShipToStore(String freeShipToStore) {
        this.freeShipToStore = freeShipToStore;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setDimensionX(String dimensionX) {
        this.dimensionX = dimensionX;
    }

    public void setDimensionY(String dimensionY) {
        this.dimensionY = dimensionY;
    }

    public void setDimensionZ(String dimensionZ) {
        this.dimensionZ = dimensionZ;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCustomerRating(String customerRating) {
        this.customerRating = customerRating;
    }

    public void setCustomerReviews(String customerReviews) {
        this.customerReviews = customerReviews;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getProductNameFromProforma() {
        return productNameFromProforma;
    }

    public WalmartProduct setProductNameFromProforma2(String productNameFromProforma) {
        this.productNameFromProforma = productNameFromProforma;
        return this;
    }

    public String getPalletId() {
        return palletId;
    }

    public WalmartProduct setPalletId2(String palletId) {
        this.palletId = palletId;
        return this;
    }

    public String getPalletName() {
        return palletName;
    }

    public WalmartProduct setPalletName2(String palletName) {
        this.palletName = palletName;
        return this;
    }

    public String getPalletSize() {
        return palletSize;
    }

    public WalmartProduct setPalletSize2(String palletSize) {
        this.palletSize = palletSize;
        return this;
    }

    public String getReturnReason() {
        return returnReason;
    }

    public WalmartProduct setReturnReason2(String returnReason) {
        this.returnReason = returnReason;
        return this;
    }

    public String getQuantity() {
        return quantity;
    }

    public WalmartProduct setQuantity2(String quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getImageDirectory() {
        return imageDirectory;
    }

    public void setImageDirectory(String imageDirectory) {
        this.imageDirectory = imageDirectory;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getProductId() {
        return productId;
    }

    public WalmartProduct setProductId2(String productId) {
        this.productId = productId;
        return this;
    }

    public WalmartProduct setProductName2(String productName) {
        this.productName = productName;
        return this;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public WalmartProduct setProductDescription2(String productDescription) {
        this.productDescription = productDescription;
        return this;
    }

    public String getBrandName() {
        return brandName;
    }

    public WalmartProduct setBrandName2(String brandName) {
        this.brandName = brandName;
        return this;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public WalmartProduct setSalePrice2(String salePrice) {
        this.salePrice = salePrice;
        return this;
    }

    public String getRetailPrice() {
        return retailPrice;
    }

    public WalmartProduct setRetailPrice2(String retailPrice) {
        this.retailPrice = retailPrice;
        return this;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public WalmartProduct setModelNumber2(String model) {
        this.modelNumber = model;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public WalmartProduct setCategory2(String category) {
        this.category = category;
        return this;
    }

    public String getWeight() {
        return weight;
    }

    public WalmartProduct setWeight2(String weight) {
        this.weight = weight;
        return this;
    }

    public String getStandardShipRate() {
        return standardShipRate;
    }

    public WalmartProduct setStandardShipRate2(String standardShipRate) {
        this.standardShipRate = standardShipRate;
        return this;
    }

    public String getSize() {
        return size;
    }

    public WalmartProduct setSize2(String size) {
        this.size = size;
        return this;
    }

    public String getShipToStore() {
        return shipToStore;
    }

    public WalmartProduct setShipToStore2(String shipToStore) {
        this.shipToStore = shipToStore;
        return this;
    }

    public String getFreeShipToStore() {
        return freeShipToStore;
    }

    public WalmartProduct setFreeShipToStore2(String freeShipToStore) {
        this.freeShipToStore = freeShipToStore;
        return this;
    }

    public String getColor() {
        return color;
    }

    public WalmartProduct setColor2(String color) {
        this.color = color;
        return this;
    }

    public String getCustomerRating() {
        return customerRating;
    }

    public WalmartProduct setCustomerRating2(String customerRating) {
        this.customerRating = customerRating;
        return this;
    }

    public String getCustomerReviews() {
        return customerReviews;
    }

    public WalmartProduct setCustomerReviews2(String customerReviews) {
        this.customerReviews = customerReviews;
        return this;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public WalmartProduct setStock2(String stock) {
        this.stock = stock;
        return this;
    }

    public String getMarketplace() {
        return marketplace;
    }

    public WalmartProduct setMarketplace2(String marketplace) {
        this.marketplace = marketplace;
        return this;
    }

    public String getSellerInfo() {
        return sellerInfo;
    }

    public WalmartProduct setSellerInfo2(String sellerInfo) {
        this.sellerInfo = sellerInfo;
        return this;
    }

    public String getOfferType() {
        return offerType;
    }

    public WalmartProduct setOfferType2(String offerType) {
        this.offerType = offerType;
        return this;
    }

    public String getVendor() {
        return vendor;
    }

    public WalmartProduct setVendor2(String vendor) {
        this.vendor = vendor;
        return this;
    }

    public String getImportedUpc() {
        return importedUpc;
    }

    public WalmartProduct setImportedUpc2(String importedUpc) {
        this.importedUpc = importedUpc;
        return this;
    }

    public List<String> getImages() {
        return images;
    }

    public WalmartProduct setImages2(List<String> images) {
        this.images = images;
        return this;
    }

    public List<WalmartImage> getWalmartImage() {
        return walmartImage;
    }

    public WalmartProduct setWalmartImage2(List<WalmartImage> walmartImage) {
        this.walmartImage = walmartImage;
        return this;
    }

    public String getDepartment() {
        return department;
    }

    public WalmartProduct setDepartment2(String department) {
        this.department = department;
        return this;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public WalmartProduct setSubcategory2(String subcategory) {
        this.subcategory = subcategory;
        return this;
    }

    public String getDimensionX() {
        return dimensionX;
    }

    public WalmartProduct setDimensionX2(String dimensionX) {
        this.dimensionX = dimensionX;
        return this;
    }

    public String getDimensionY() {
        return dimensionY;
    }

    public WalmartProduct setDimensionY2(String dimensionY) {
        this.dimensionY = dimensionY;
        return this;
    }

    public String getDimensionZ() {
        return dimensionZ;
    }

    public WalmartProduct setDimensionZ2(String dimensionZ) {
        this.dimensionZ = dimensionZ;
        return this;
    }

    public String getDataSource() {
        return dataSource;
    }

    public WalmartProduct setDataSource2(String dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    public Integer getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(Integer totalQty) {
        this.totalQty = totalQty;
    }

    public String getBinLocation() {
        return binLocation;
    }

    public void setBinLocation(String binLocation) {
        this.binLocation = binLocation;
    }

    public String getStoreLocation() {
        return storeLocation;
    }

    public void setStoreLocation(String storeLocation) {
        this.storeLocation = storeLocation;
    }

    public Address getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(Address storeAddress) {
        this.storeAddress = storeAddress;
    }

    public Integer getStoreQty() {
        return storeQty;
    }

    public void setStoreQty(Integer storeQty) {
        this.storeQty = storeQty;
    }

    public Date getDateShipmentReceived() {
        return dateShipmentReceived;
    }

    public void setDateShipmentReceived(Date dateShipmentReceived) {
        this.dateShipmentReceived = dateShipmentReceived;
    }

    public String getSlotInBinLocation() {
        return slotInBinLocation;
    }

    public void setSlotInBinLocation(String slotInBinLocation) {
        this.slotInBinLocation = slotInBinLocation;
    }

    public String getSalesChannel() {
        return salesChannel;
    }

    public void setSalesChannel(String salesChannel) {
        this.salesChannel = salesChannel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    @Override
    public String toString() {
        return "WalmartProduct [productId=" + productId + ", upc=" + upc + ", productNameFromProforma=" + productNameFromProforma
                + ", importedUpc=" + importedUpc + ", productName=" + productName + ", productDescription=" + productDescription
                + ", brandName=" + brandName + ", salePrice=" + salePrice + ", retailPrice=" + retailPrice
                + ", offerType=" + offerType + ", modelNumber=" + modelNumber + ", color=" + color + ", category="
                + category + ", stock=" + stock + ", standardShipRate=" + standardShipRate + ", shipToStore="
                + shipToStore + ", freeShipToStore=" + freeShipToStore + ", size=" + size + ", images=" + images
                + ", walmartImage=" + walmartImage + ", vendor=" + vendor + ", weight=" + weight + ", palletId="
                + palletId + ", palletName=" + palletName + ", palletSize=" + palletSize + ", sellerInfo=" + sellerInfo
                + ", marketplace=" + marketplace + ", customerRating=" + customerRating + ", customerReviews="
                + customerReviews + ", returnReason=" + returnReason + ", department=" + department + ", subcategory="
                + subcategory + ", dimensionX=" + dimensionX + ", dimensionY=" + dimensionY + ", dimensionZ="
                + dimensionZ + ", dataSource=" + dataSource + ", quantity=" + quantity + ", imgUrl= " + imgUrl + "]";
    }

//    public void setProductNotFound() {
//        setProductName("Results not found!").setPalletId(null).setPalletName(null).setPalletSize(null)
//                .setReturnReason(null).setQuantity(null).setWeight(null).setProductId(null).setImportedUpc(null)
//                .setUpc(null).setProductDescription(null).setBrandName(null).setSalePrice(null)
//                .setRetailPrice(null).setModelNumber(null).setCategory(null).setStandardShipRate(null)
//                .setSize(null).setShipToStore(null).setShipToStore(null).setFreeShipToStore(null)
//                .setColor(null).setCustomerRating(null).setCustomerReviews(null).setStock(null)
//                .setMarketplace(null).setSellerInfo(null).setOfferType(null).setVendor(null);
//    }
    public void wrapImages() {
        Iterator<String> imageIterator = images.iterator();
        while (imageIterator.hasNext()) {
            walmartImage.add(new WalmartImage(imageIterator.next()));
        }
        imgUrl = images.stream().collect(Collectors.joining(","));

    }

}
