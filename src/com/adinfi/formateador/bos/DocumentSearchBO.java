package com.adinfi.formateador.bos;

public class DocumentSearchBO {

    private boolean check;
    private int documentId = 0;
    private String documentName = null;
    private int idMarket;
    private String marketName;
    private int idDocType;
    private String docTypeName;
    private int idLanguage;
    private String languageName;
    private int idSubject;
    private String subjectName;
    private int idIndustry;
    private String industryName;
    private int idAuthor;
    private String author;
    private String docCreationDate;
    private String docPublicationDate;
    private String docPublicationDateFrom;
    private String docPublicationDateTo;
    private int idStatus;
    private String status;
    private int version = 0;
    private int docVersionId = 0;
    private String criteria;
    private int idStatus_publish;
    private String document_status;
    private boolean collaborative;
    private String collaborative_type;
    private String fecha_creacion;
    private int idstatus_publish;

    private boolean scheduled;
    private String date_publish;
    private String time;
    private String title_regex;
    private String nomenclature;
    private boolean searchByContent;
    private int idPublish;
    private int enableForDocumentTypeProfile;
    private String fileName;
    private String code_hist;
    private boolean history;
    private String url;

    public DocumentSearchBO() {
    }

    public DocumentSearchBO(boolean check, int idMarket, String marketName, int idDocType,
            String docTypeName, int idLanguage, String languageName, int idSubject,
            String subjectName, int idIndustry, String industryName, int idAuthor,
            String author, String docCreationDate, String docPublicationDate, int version,
            String criteria, String docPublicationDateFrom, String docPublicationDateTo) {
        this.check = check;
        this.idMarket = idMarket;
        this.marketName = marketName;
        this.idDocType = idDocType;
        this.docTypeName = docTypeName;
        this.idLanguage = idLanguage;
        this.languageName = languageName;
        this.idSubject = idSubject;
        this.subjectName = subjectName;
        this.idIndustry = idIndustry;
        this.industryName = industryName;
        this.idAuthor = idAuthor;
        this.author = author;
        this.docCreationDate = docCreationDate;
        this.docPublicationDateTo = docPublicationDateTo;
        this.docPublicationDateFrom = docPublicationDateFrom;
        this.criteria = criteria;
        this.version = version;
    }

    /**
     * @return the documentId
     */
    public int getDocumentId() {
        return documentId;
    }

    /**
     * @param documentId the documentId to set
     */
    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    /**
     * @return the documentName
     */
    public String getDocumentName() {
        return documentName;
    }

    /**
     * @param documentName the documentName to set
     */
    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    /**
     * @return the idMarket
     */
    public int getIdMarket() {
        return idMarket;
    }

    /**
     * @param idMarket the idMarket to set
     */
    public void setIdMarket(int idMarket) {
        this.idMarket = idMarket;
    }

    /**
     * @return the idDocType
     */
    public int getIdDocType() {
        return idDocType;
    }

    public int getIdstatus_publish() {
        return idstatus_publish;
    }

    public void setIdstatus_publish(int idstatus_publish) {
        this.idstatus_publish = idstatus_publish;
    }

    /**
     * @param idDocType the idDocType to set
     */
    public void setIdDocType(int idDocType) {
        this.idDocType = idDocType;
    }

    /**
     * @return the idLanguage
     */
    public int getIdLanguage() {
        return idLanguage;
    }

    /**
     * @param idLanguage the idLanguage to set
     */
    public void setIdLanguage(int idLanguage) {
        this.idLanguage = idLanguage;
    }

    /**
     * @return the idSubject
     */
    public int getIdSubject() {
        return idSubject;
    }

    /**
     * @param idSubject the idSubject to set
     */
    public void setIdSubject(int idSubject) {
        this.idSubject = idSubject;
    }

    /**
     * @return the marketName
     */
    public String getMarketName() {
        return marketName;
    }

    /**
     * @param marketName the marketName to set
     */
    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    /**
     * @return the subjectName
     */
    public String getSubjectName() {
        return subjectName;
    }

    /**
     * @param subjectName the subjectName to set
     */
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    /**
     * @return the idIndustry
     */
    public int getIdIndustry() {
        return idIndustry;
    }

    /**
     * @param idIndustry the idIndustry to set
     */
    public void setIdIndustry(int idIndustry) {
        this.idIndustry = idIndustry;
    }

    /**
     * @return the industryName
     */
    public String getIndustryName() {
        return industryName;
    }

    /**
     * @param industryName the industryName to set
     */
    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    /**
     * @return the languageName
     */
    public String getLanguageName() {
        return languageName;
    }

    /**
     * @param languageName the languageName to set
     */
    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    /**
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return the docCreationDate
     */
    public String getDocCreationDate() {
        return docCreationDate;
    }

    /**
     * @param docCreationDate the docCreationDate to set
     */
    public void setDocCreationDate(String docCreationDate) {
        this.docCreationDate = docCreationDate;
    }

    /**
     * @return the idAuthor
     */
    public int getIdAuthor() {
        return idAuthor;
    }

    /**
     * @param idAuthor the idAuthor to set
     */
    public void setIdAuthor(int idAuthor) {
        this.idAuthor = idAuthor;
    }

    /**
     * @return the docTypeName
     */
    public String getDocTypeName() {
        return docTypeName;
    }

    /**
     * @param docTypeName the docTypeName to set
     */
    public void setDocTypeName(String docTypeName) {
        this.docTypeName = docTypeName;
    }

    /**
     * @return the version
     */
    public int getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * @return the check
     */
    public boolean isCheck() {
        return check;
    }

    /**
     * @param check the check to set
     */
    public void setCheck(boolean check) {
        this.check = check;
    }

    /**
     * @return the criteria
     */
    public String getCriteria() {
        return criteria;
    }

    /**
     * @param criteria the criteria to set
     */
    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    /**
     * @return the docPublicationDateFrom
     */
    public String getDocPublicationDateFrom() {
        return docPublicationDateFrom;
    }

    /**
     * @return the docPublicationDateTo
     */
    public String getDocPublicationDateTo() {
        return docPublicationDateTo;
    }

    /**
     * @return the docPublicationDate
     */
    public String getDocPublicationDate() {
        return docPublicationDate;
    }

    /**
     * @param docPublicationDate the docPublicationDate to set
     */
    public void setDocPublicationDate(String docPublicationDate) {
        this.docPublicationDate = docPublicationDate;
    }

    /**
     * @param docPublicationDateFrom the docPublicationDateFrom to set
     */
    public void setDocPublicationDateFrom(String docPublicationDateFrom) {
        this.docPublicationDateFrom = docPublicationDateFrom;
    }

    /**
     * @param docPublicationDateTo the docPublicationDateTo to set
     */
    public void setDocPublicationDateTo(String docPublicationDateTo) {
        this.docPublicationDateTo = docPublicationDateTo;
    }

    /**
     * @return the idStatus
     */
    public int getIdStatus() {
        return idStatus;
    }

    /**
     * @param idStatus the idStatus to set
     */
    public void setIdStatus(int idStatus) {
        this.idStatus = idStatus;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the idStatus_publish
     */
    public int getIdStatus_publish() {
        return idStatus_publish;
    }

    /**
     * @param idStatus_publish the idStatus_publish to set
     */
    public void setIdStatus_publish(int idStatus_publish) {
        this.idStatus_publish = idStatus_publish;
    }

    /**
     * @return the document_status
     */
    public String getDocument_status() {
        return document_status == null ? "No Publicado" : document_status;
    }

    /**
     * @param document_status the document_status to set
     */
    public void setDocument_status(String document_status) {
        this.document_status = document_status;
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

    public String getCollaborative_Type() {
        return collaborative_type;
    }

    public void setCollaborative_Type(String collaborativeType) {
        this.collaborative_type = collaborativeType;
    }

    public boolean isScheduled() {
        return scheduled;
    }

    public void setScheduled(boolean scheduled) {
        this.scheduled = scheduled;
    }

    public String getDate_publish() {
        return date_publish;
    }

    public void setDate_publish(String date_publish) {
        this.date_publish = date_publish;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle_regex() {
        return title_regex;
    }

    public void setTitle_regex(String title_regex) {
        this.title_regex = title_regex;
    }

    public String getNomenclature() {
        return nomenclature;
    }

    public void setNomenclature(String nomenclature) {
        this.nomenclature = nomenclature;
    }

    /**
     * @return the searchByContent
     */
    public boolean isSearchByContent() {
        return searchByContent;
    }

    /**
     * @param searchByContent the searchByContent to set
     */
    public void setSearchByContent(boolean searchByContent) {
        this.searchByContent = searchByContent;
    }

    public int getIdPublish() {
        return idPublish;
    }

    public void setIdPublish(int idPublish) {
        this.idPublish = idPublish;
    }

    public int getEnableForDocumentTypeProfile() {
        return enableForDocumentTypeProfile;
    }

    public void setEnableForDocumentTypeProfile(int enableForDocumentTypeProfile) {
        this.enableForDocumentTypeProfile = enableForDocumentTypeProfile;
    }

    public String getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isHistory() {
        return history;
    }

    public void setHistory(boolean history) {
        this.history = history;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCode_hist() {
        return code_hist;
    }

    public void setCode_hist(String code_hist) {
        this.code_hist = code_hist;
        if(code_hist == null || code_hist.trim().isEmpty()){
            setHistory(false);
        }else{
            try {
                String[] val = code_hist.split("-");
                if("HIST".equals( val[0] ))
                    setHistory(true);
                else
                    setHistory(false);
            } catch (Exception e) {
                 setHistory(false);
            }
        }
    }
    
}
