//package com.itr.helpers;
//
//import java.io.File;
//import javax.faces.context.ExternalContext;
//import javax.faces.context.FacesContext;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import javax.servlet.ServletContext;
//
//public class ProformaDir {
//
//    private static String dirPath;
//
//    public static Path getDirPath() {
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        ExternalContext externalContext = facesContext.getExternalContext();
//        Path dirPath = Paths.get(((ServletContext) externalContext.getContext()).
//                getRealPath(File.separator + "resources" + File.pathSeparator + "itr/proformas"));
//        return dirPath;
//    }
//
//    
//
//}
