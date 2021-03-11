package com.adinfi.formateador.bos;

import java.awt.image.BufferedImage;

/**
 *
 * @author Guillermo Trejo
 */
public class ObjectInfoBO {
    private int objectId;
    private int objType;
    private String file;
    private int templateId;
    private int sectionRootId;
    private int sectionId;
    private int moduleOrderId;
    
    private int categoryId;
    private int nameId;
    
    private short width;
    private short height;
    private String url;
    boolean delete;
    private String titulo;
    private String subTitulo;
    private String comentarios;
    private String data;
    private String plain_text;
    private String htmlVideo;
    private String thumbnailUrl;
    private String rangoIni;
    private String rangoFin;
    private String file2;
    private BufferedImage image;
    private BufferedImage imageThumb;
    private int orderId;
    
    //vectormedia o youtube
    private String tipoVideo;
    private String idVideo;
    private String idVideoDb;
    
    private boolean header;
    private boolean section;
    private int idLinkedExcel;

    public String getFile2() {
        return file2;
    }

    public void setFile2(String file2) {
        this.file2 = file2;
    }

    
    public String getRangoIni() {
        return rangoIni;
    }

    public void setRangoIni(String rangoIni) {
        this.rangoIni = rangoIni;
    }

    public String getRangoFin() {
        return rangoFin;
    }

    public void setRangoFin(String rangoFin) {
        this.rangoFin = rangoFin;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
    
    public String getHtmlVideo() {
        return htmlVideo;
    }

    public void setHtmlVideo(String htmlVideo) {
        this.htmlVideo = htmlVideo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPlain_text() {
        return plain_text;
    }

    public void setPlain_text(String plain_text) {
        this.plain_text = plain_text;
    }
    
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSubTitulo() {
        return subTitulo;
    }

    public void setSubTitulo(String subTitulo) {
        this.subTitulo = subTitulo;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public int getSectionRootId() {
        return sectionRootId;
    }

    public void setSectionRootId(int sectionRootId) {
        this.sectionRootId = sectionRootId;
    }

    public int getModuleOrderId() {
        return moduleOrderId;
    }

    public void setModuleOrderId(int moduleOrderId) {
        this.moduleOrderId = moduleOrderId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public short getWidth() {
        return width;
    }

    public void setWidth(short width) {
        this.width = width;
    }

    public short getHeight() {
        return height;
    }

    public void setHeight(short height) {
        this.height = height;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public int getObjType() {
        return objType;
    }

    public void setObjType(int objType) {
        this.objType = objType;
    }

    /**
     * @return the image
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    /**
     * @return the imageThumb
     */
    public BufferedImage getImageThumb() {
        return imageThumb;
    }

    /**
     * @param imageThumb the imageThumb to set
     */
    public void setImageThumb(BufferedImage imageThumb) {
        this.imageThumb = imageThumb;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getNameId() {
        return nameId;
    }

    public void setNameId(int nameId) {
        this.nameId = nameId;
    }

    public String getTipoVideo() {
        return tipoVideo;
    }

    public void setTipoVideo(String tipoVideo) {
        this.tipoVideo = tipoVideo;
    }

    public String getIdVideo() {
        return idVideo;
    }

    public void setIdVideo(String idVideo) {
        this.idVideo = idVideo;
    }

    public String getIdVideoDb() {
        return idVideoDb;
    }

    public void setIdVideoDb(String idVideoDb) {
        this.idVideoDb = idVideoDb;
    }
    
    

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public boolean isHeader() {
        return header;
    }

    public void setHeader(boolean header) {
        this.header = header;
    }

    public boolean isSection() {
        return section;
    }

    public void setSection(boolean section) {
        this.section = section;
    }

    public int getIdLinkedExcel() {
        return idLinkedExcel;
    }

    public void setIdLinkedExcel(int idLinkedExcel) {
        this.idLinkedExcel = idLinkedExcel;
    }
}
