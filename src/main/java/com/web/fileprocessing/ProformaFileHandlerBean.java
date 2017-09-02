package com.web.fileprocessing;

import com.business.entity.WalmartProduct;
import com.business.parser.WalmartDataProcesser;
import com.business.service.WalmartProductService;
import com.web.view.ProductCount;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

@Named
@SessionScoped
public class ProformaFileHandlerBean implements Serializable {

    private final static Logger LOGGER = Logger.getLogger(ProformaFileHandlerBean.class.getName());
    private static final long serialVersionUID = 1L;
    private static final int BUFFER_SIZE = 6124;
//    private static final String PATH_TO_RESOURCES = "C:\\Users\\tyler durden\\OneDrive\\education\\Workspace\\projects"
//            + "\\ItrKickOff\\src\\main\\webapp\\resources\\itr\\proformas";

    private static final String PATH_TO_RESOURCES = "C:\\GitHub\\Leo\\files";
    private List<String> proformaFiles;

    @Inject
    private ProductCount productCount;
    
    @Inject
    private ProgressBarBean progressBarViewBean;

    @EJB
    private WalmartProductService productService;

    public ProformaFileHandlerBean() {
        System.out.println("================================ CREATE PROFORMA LIST =============================");
        proformaFiles = new ArrayList<>();
        System.out.println("======================================== DONE =====================================");
    }

    public List<String> getProformaFiles() {
        return proformaFiles;
    }

    public void setProformaFiles(List<String> proformaFiles) {
        this.proformaFiles = proformaFiles;
    }

    public void uploadProforma(FileUploadEvent event) throws FileNotFoundException, IOException {

        LOGGER.info("handleFileUpload starts...");

        FacesContext facesContext = FacesContext.getCurrentInstance();

        if (event.getFile() != null) {
            Path path = Paths.get(PATH_TO_RESOURCES);

            Files.createDirectories(path);
            LOGGER.log(Level.INFO, "file path is: {0}", path);
            FileOutputStream fileOutputStream;
            InputStream inputStream;
            try {
                String fn = event.getFile().getFileName();
                fileOutputStream = new FileOutputStream(path.toString() + "\\" + fn);

                byte[] buffer = new byte[BUFFER_SIZE];
                int bulk;

                inputStream = event.getFile().getInputstream();
                while (true) {
                    bulk = inputStream.read(buffer);
                    if (bulk < 0) {
                        break;
                    }
                    fileOutputStream.write(buffer, 0, bulk);
                    fileOutputStream.flush();
                }

                fileOutputStream.close();
                inputStream.close();

                proformaFiles.add(fn);
                System.out.println("================================");
                System.out.println(fn + " was uploaded and added in the list!");
                System.out.println("Current list: " + proformaFiles);
                System.out.println("================================");

                LOGGER.log(Level.INFO, "file...{0} is uploaded", fn);
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "File(s) were successfully uploaded !", null));
            } catch (FileNotFoundException ex) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File "
                        + event.getFile().getFileName() + " cannot be found!", null));
                Logger.getLogger(ProformaFileHandlerBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File "
                        + event.getFile().getFileName() + " cannot be uploaded!", null));
                Logger.getLogger(ProformaFileHandlerBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File does not exist!", null));
            Logger.getLogger(ProformaFileHandlerBean.class.getName()).log(Level.SEVERE, "File does not exist!", "File does not exist!");
        }
    }

    public void processProforma() throws InterruptedException {

        System.out.println("--------------------------------------");
        System.out.println("processProforma method was invoked ... files to process: " + proformaFiles);
        System.out.println("--------------------------------------");

        if (proformaFiles.size() > 0) {

            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(proformaFiles.size());
            List<Future<List<WalmartProduct>>> resultList = new ArrayList<>();
            CountDownLatch latch = new CountDownLatch(proformaFiles.size());

            proformaFiles.forEach((f) -> {
                System.out.println("Processing: " + (PATH_TO_RESOURCES + "\\" + f));
                try {
                    WalmartDataProcesser walmartDataProcesser
                            = new WalmartDataProcesser(PATH_TO_RESOURCES + "\\" + f, latch, progressBarViewBean.getBarIncrementator());
                    Future<List<WalmartProduct>> result = executor.submit(walmartDataProcesser);
                    resultList.add(result);
                } catch (IOException ex) {
                    errorHandle(executor, ex);
                }
            });

            resultList.forEach((future) -> {
                try {
                    System.out.println("Future result is: " + " - " + future.get() + "; And Task done is " + future.isDone());
                } catch (InterruptedException | ExecutionException ex) {
                    errorHandle(executor, ex);
                }
            });

            System.out.println("--------------------------------------");
            System.out.println("ALL FILES WAS PROCESSED ..." + resultList.size());
            System.out.println("--------------------------------------");

            proformaFiles.clear();
            executor.shutdown();

            // save to db
            productService.persistUploadedData(resultList);
            filesProcessed();
        } else {
            noFileToProcess();
        }
    }

    private void filesProcessed() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('processButton').enable();");
        enableUploadChooseButton();
        productCount.setCounts(null);

        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Process successfully complete!", null));
    }

    private void noFileToProcess() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('pbAjax').cancel();");
        context.execute("PF('processButton').enable();");
        enableUploadChooseButton();

        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "There is no file to process!", null));

    }

    private void errorHandle(ThreadPoolExecutor executor, Exception e) {
        executor.shutdownNow();
        proformaFiles.clear();

        LOGGER.log(Level.SEVERE, "ERROR: {0}", e);

        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('pbAjax').cancel();");
        context.execute("PF('processButton').enable();");
        enableUploadChooseButton();
        context.execute("PF('exceptionDialog').show();");

        throw new RuntimeException("Error during processing files !");
    }

    private void enableUploadChooseButton() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("if (!PF('uploadWV').files.length) {\n"
                + "PF('uploadWV').enableButton(PF('uploadWV').chooseButton);\n"
                + "PF('uploadWV').chooseButton.find('input[type=\"file\"]').removeAttr('disabled');\n"
                + "}");
    }
}
