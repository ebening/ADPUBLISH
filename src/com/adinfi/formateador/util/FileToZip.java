package com.adinfi.formateador.util;
        
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileToZip {
	 		
	    List<String> fileList;
	  
            /**
            private String OUTPUT_ZIP_FILE = "C:\\\\Users\\Desktop\\File.zip";
	    private String SOURCE_FOLDER = "C:\\\\Users\\Desktop\\SearchViernes";
            **/
            
	    private String output_zip_file = "";
	    private String source_folder = "";
	    
            FileToZip(){
		this.fileList = new ArrayList<>();
	    }
            
	    FileToZip(List<String> fileList, String output_zip_file){
		this.fileList = fileList;
                this.output_zip_file = output_zip_file;
	    }
	 
	    public void zipFile()
	    {
	    	FileToZip fileToZip = new FileToZip();
	    	fileToZip.generateFileList(new File(getSource_folder()));
	    	fileToZip.zipIt(getOutput_zip_file());
	    }
            
            public void zipFileByMyList()
	    {
	    	//FileToZip fileToZip = new FileToZip();
	    	//fileToZip.generateFileList(new File(getSource_folder()));
	    	zipIt(getOutput_zip_file());
	    }
	 
	    /**
	     * Zip it
	     * @param zipFile output ZIP file location
	     */
	    public void zipIt(String zipFile){
	 
	     byte[] buffer = new byte[1024];
	 
	     try{
	 
	    	FileOutputStream fos = new FileOutputStream(zipFile);
	    	ZipOutputStream zos = new ZipOutputStream(fos);	 	    	
	 
	    	for(String file : this.fileList){
	 	    		
	    		ZipEntry ze= new ZipEntry(file);
	        	zos.putNextEntry(ze);
	 
	        	FileInputStream in = 
	                       new FileInputStream(file);
	 
	        	int len;
	        	while ((len = in.read(buffer)) > 0) {
	        		zos.write(buffer, 0, len);
	        	}
	 
	        	in.close();
	    	}	
                fos.flush();
	    	zos.closeEntry();
	    	//remember close it
	    	zos.close();
	 	    	
	    }catch(IOException ex){	        
               Utilerias.logger(getClass()).error(ex.getMessage());
	    }
	   }
	 
	    /**
	     * Traverse a directory and get all files,
	     * and add the file into fileList  
	     * @param node file or directory
	     */
	    public void generateFileList(File node){
	 
	    	//add file only
		if(node.isFile()){			
			fileList.add(generateZipEntry(node.getAbsoluteFile().toString()));
		}
	 
		if(node.isDirectory()){
			String[] subNote = node.list();
			for(String filename : subNote){
				generateFileList(new File(node, filename));
			}
		}	 
	    }
	 
	    /**
	     * Format the file path for zip
	     * @param file file path
	     * @return Formatted file path
	     */
	    private String generateZipEntry(String file){
	    	return file.substring(source_folder.length(), file.length());
	    }

    public void zipFiles(){

        FileOutputStream fos = null;
        ZipOutputStream zipOut = null;
        FileInputStream fis = null;
        try {
            fos = new FileOutputStream(output_zip_file);
            zipOut = new ZipOutputStream(new BufferedOutputStream(fos));
            for(String filePath : this.fileList){
                File input = new File(filePath);
                fis = new FileInputStream(input);
                ZipEntry ze = new ZipEntry(input.getName());
                System.out.println("Zipping the file: "+input.getName());
                zipOut.putNextEntry(ze);
                byte[] tmp = new byte[4*1024];
                int size = 0;
                while((size = fis.read(tmp)) != -1){
                    zipOut.write(tmp, 0, size);
                }
                zipOut.flush();
                fis.close();
            }
            zipOut.close();
            System.out.println("Done... Zipped the files...");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            try{
                if(fos != null) fos.close();
            } catch(Exception ex){

            }
        }
    }

            
    /**
     * @return the output_zip_file
     */
    public String getOutput_zip_file() {
        return output_zip_file;
    }

    /**
     * @param output_zip_file the output_zip_file to set
     */
    public void setOutput_zip_file(String output_zip_file) {
        this.output_zip_file = output_zip_file;
    }

    /**
     * @return the source_folder
     */
    public String getSource_folder() {
        return source_folder;
    }

    /**
     * @param source_folder the source_folder to set
     */
    public void setSource_folder(String source_folder) {
        this.source_folder = source_folder;
    }
	}