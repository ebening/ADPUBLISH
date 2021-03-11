package com.adinfi.formateador.bos;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Guillermo Trejo
 */
public class DocumentBO {

    int documentId = 0;    
    List<TemplateSectionBO> sections = null;
    boolean collaborative=false;
    String collaborativeType=null;
    
    MarketBO marketBO;
    DocumentTypeBO tipoBO;
    SubjectBO subjectBO;
    
    
    

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    String authorName;
    int idAuthor;
    String documentName = null;
    String document_Name = null;
    String fileName=null;
    String file_Name=null;
    String layoutType = null;
    short widht = 0;
    int numHojas = 0;
    Map<Integer, HojaBO> mapHojas = null;
    Map<String, HojaBO> mapHojasDelete = null;
    short height = 0;
    String status = null;
    int idMarket;
    int idDocType;
    int idDocument_Type;
    int idLanguage;
    int idSubject;
    String guid;
    int docVersionId;
    int version;
    ObjectInfoBO firstImage;
    List<Integer> orden;
    
    boolean favorite;
    int idPublish;
    String publishName;

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
    
    public String getFile_Name() {
        return file_Name;
    }

    public void setFile_Name(String file_Name) {
        this.file_Name = file_Name;
    }
    
    public int getIdDocument_Type() {
        return idDocument_Type;
    }

    public void setIdDocument_Type(int idDocument_Type) {
        this.idDocument_Type = idDocument_Type;
    }
    

    public String getDocument_Name() {
        return document_Name;
    }

    public void setDocument_Name(String document_Name) {
        this.document_Name = document_Name;
    }
    
    

    public int getIdSubject() {
        return idSubject;
    }

    public void setIdSubject(int idSubject) {
        this.idSubject = idSubject;
    }
    
    

    public Map<String, HojaBO> getMapHojasDelete() {
        return mapHojasDelete;
    }

    public void setMapHojasDelete(Map<String, HojaBO> mapHojasDelete) {
        this.mapHojasDelete = mapHojasDelete;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    private Date fecha;

    public DocumentBO() {
        mapHojas = new TreeMap<>();
    }

    public Map<Integer, HojaBO> getMapHojas() {
        return mapHojas;
    }

    public void setMapHojas(Map<Integer, HojaBO> mapHojas) {
        this.mapHojas = mapHojas;
    }

    public String getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(String layoutType) {
        this.layoutType = layoutType;
    }

    public short getWidht() {
        return widht;
    }

    public void setWidht(short widht) {
        this.widht = widht;
    }

    public short getHeight() {
        return height;
    }

    public void setHeight(short height) {
        this.height = height;
    }

    public String getStatus() {
        return status;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setTemplateName(String documentName) {
        this.documentName = documentName;
    }

    public List<TemplateSectionBO> getSections() {
        return sections;
    }

    public void setSections(List<TemplateSectionBO> sections) {
        this.sections = sections;
    }

    @Override
    public String toString() {
        return this.documentName;
    }

    public int getNumHojas() {
        if (this.mapHojas == null) {
            return 0;
        }
        return this.mapHojas.size();
    }

    /**
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getIdMarket() {
        return idMarket;
    }

    public void setIdMarket(int idMarket) {
        this.idMarket = idMarket;
    }

    public int getIdDocType() {
        return idDocType;
    }

    public void setIdDocType(int idDocType) {
        this.idDocType = idDocType;
    }

    public int getIdLanguage() {
        return idLanguage;
    }

    public void setIdLanguage(int idLanguage) {
        this.idLanguage = idLanguage;
    }

    public int getDocVersionId() {
        return docVersionId;
    }

    public void setDocVersionId(int docVersionId) {
        this.docVersionId = docVersionId;
    }



    public boolean isCollaborative() {
        return collaborative;
    }

    public void setCollaborative(boolean collaborative) {
        this.collaborative = collaborative;
    }

    public MarketBO getMarketBO() {
        return marketBO;
    }

    public void setMarketBO(MarketBO marketBO) {
        this.marketBO = marketBO;
    }

    public DocumentTypeBO getTipoBO() {
        return tipoBO;
    }

    public void setTipoBO(DocumentTypeBO tipoBO) {
        this.tipoBO = tipoBO;
    }

    public SubjectBO getSubjectBO() {
        return subjectBO;
    }

    public void setSubjectBO(SubjectBO subjectBO) {
        this.subjectBO = subjectBO;
    }

    public ObjectInfoBO getFirstImage() {
        return firstImage;
    }

    public void setFirstImage(ObjectInfoBO firstImage) {
        this.firstImage = firstImage;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getCollaborativeType() {
        return collaborativeType;
    }

    public void setCollaborativeType(String collaborativeType) {
        this.collaborativeType = collaborativeType;
    }

    public List<Integer> getOrden() {
        return orden;
    }

    public void setOrden(List<Integer> orden) {
        this.orden = orden;
    }

    public int getIdPublish() {
        return idPublish;
    }

    public void setIdPublish(int idPublish) {
        this.idPublish = idPublish;
    }

    public String getPublishName() {
        return publishName;
    }

    public void setPublishName(String publishName) {
        this.publishName = publishName;
    }
    
    List<DocumentCollabItemBO> lstDocumentCollab=null;

    public List<DocumentCollabItemBO> getLstDocumentCollab() {
        return lstDocumentCollab;
    }

    public void setLstDocumentCollab(List<DocumentCollabItemBO> lstDocumentCollab) {
        this.lstDocumentCollab = lstDocumentCollab;
    }

    public int getIdAuthor() {
        return idAuthor;
    }

    public void setIdAuthor(int idAuthor) {
        this.idAuthor = idAuthor;
    }
    
}
