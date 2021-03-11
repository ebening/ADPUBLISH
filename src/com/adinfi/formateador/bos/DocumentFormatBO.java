package com.adinfi.formateador.bos;

import java.sql.Clob;
import java.util.List;

public class DocumentFormatBO {
   
    private boolean check;
    private int idDocument_send = 0;
    private int document_id = 0;
    private int idDocument_type = 0;      
    private String document_type_name;
    private int idMarket = 0;    
    private String market_name;    
    private String date_publish;    
    private String subject_name;
    private int idIndustry = 0;
    private String industry_name;
    private int idLanguage = 0;
    private String language_name;
    private int idStatus_publish = 0;
    private String publish_status_name;    
    private String author_name;    
    private int isEmisora;
    private String urlPdfForm;
    private String urlHtmlForm;
    private String urlHtmlVector;
    private String urlPdfVector;
    private List UrlTypes;
    private String doc_Url;
    private String urlType;
    private int contentmanagerEstatus;
    private String pid;
    private String pidZip;
    
    private String documentTitle;
    private String mailContent;
    private Clob documentContent;
    private String authorMail;
    private String flagHistory;
    private String pdfUrl;
    private boolean publishAttach;
    
    private String reportYear;
    private String reportTrim;
    private String arregloAutores;
    private String arregloDistribucion;

    /**
     * @return the idDocument_send
     */
    public int getIdDocument_send() {
        return idDocument_send;
    }

    /**
     * @param idDocument_send the idDocument_send to set
     */
    public void setIdDocument_send(int idDocument_send) {
        this.idDocument_send = idDocument_send;
    }

    /**
     * @return the document_id
     */
    public int getDocument_id() {
        return document_id;
    }

    /**
     * @param document_id the document_id to set
     */
    public void setDocument_id(int document_id) {
        this.document_id = document_id;
    }

    /**
     * @return the idDocument_type
     */
    public int getIdDocument_type() {
        return idDocument_type;
    }

    /**
     * @param idDocument_type the idDocument_type to set
     */
    public void setIdDocument_type(int idDocument_type) {
        this.idDocument_type = idDocument_type;
    }

    /**
     * @return the document_type_name
     */
    public String getDocument_type_name() {
        return document_type_name;
    }

    /**
     * @param document_type_name the document_type_name to set
     */
    public void setDocument_type_name(String document_type_name) {
        this.document_type_name = document_type_name;
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
     * @return the market_name
     */
    public String getMarket_name() {
        return market_name;
    }

    /**
     * @param market_name the market_name to set
     */
    public void setMarket_name(String market_name) {
        this.market_name = market_name;
    }

    /**
     * @return the date_publish
     */
    public String getDate_publish() {
        return date_publish;
    }

    /**
     * @param date_publish the date_publish to set
     */
    public void setDate_publish(String date_publish) {
        this.date_publish = date_publish;
    }

    /**
     * @return the subject_name
     */
    public String getSubject_name() {
        return subject_name;
    }

    /**
     * @param subject_name the subject_name to set
     */
    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
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
     * @return the industry_name
     */
    public String getIndustry_name() {
        return industry_name;
    }

    /**
     * @param industry_name the industry_name to set
     */
    public void setIndustry_name(String industry_name) {
        this.industry_name = industry_name;
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
     * @return the language_name
     */
    public String getLanguage_name() {
        return language_name;
    }

    /**
     * @param language_name the language_name to set
     */
    public void setLanguage_name(String language_name) {
        this.language_name = language_name;
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
     * @return the publish_status_name
     */
    public String getPublish_status_name() {
        return publish_status_name;
    }

    /**
     * @param publish_status_name the publish_status_name to set
     */
    public void setPublish_status_name(String publish_status_name) {
        this.publish_status_name = publish_status_name;
    }

    /**
     * @return the author_name
     */
    public String getAuthor_name() {
        return author_name;
    }

    /**
     * @param author_name the author_name to set
     */
    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    /**
     * @return the contentmanagerEstatus
     */
    public int getContentmanagerEstatus() {
        return contentmanagerEstatus;
    }

    /**
     * @param contentmanagerEstatus the contentmanagerEstatus to set
     */
    public void setContentmanagerEstatus(int contentmanagerEstatus) {
        this.contentmanagerEstatus = contentmanagerEstatus;
    }

    /**
     * @return the urlHtmlVector
     */
    public String getUrlHtmlVector() {
        return urlHtmlVector;
    }

    /**
     * @param urlHtmlVector the urlHtmlVector to set
     */
    public void setUrlHtmlVector(String urlHtmlVector) {
        this.urlHtmlVector = urlHtmlVector;
    }

    /**
     * @return the urlPdfVector
     */
    public String getUrlPdfVector() {
        return urlPdfVector;
    }

    /**
     * @param urlPdfVector the urlPdfVector to set
     */
    public void setUrlPdfVector(String urlPdfVector) {
        this.urlPdfVector = urlPdfVector;
    }

    /**
     * @return the urlPdfForm
     */
    public String getUrlPdfForm() {
        return urlPdfForm;
    }

    /**
     * @param urlPdfForm the urlPdfForm to set
     */
    public void setUrlPdfForm(String urlPdfForm) {
        this.urlPdfForm = urlPdfForm;
    }

    /**
     * @return the urlHtmlForm
     */
    public String getUrlHtmlForm() {
        return urlHtmlForm;
    }

    /**
     * @param urlHtmlForm the urlHtmlForm to set
     */
    public void setUrlHtmlForm(String urlHtmlForm) {
        this.urlHtmlForm = urlHtmlForm;
    }

    /**
     * @return the doc_Url
     */
    public String getDoc_Url() {
        return doc_Url;
    }

    /**
     * @param doc_Url the doc_Url to set
     */
    public void setDoc_Url(String doc_Url) {
        this.doc_Url = doc_Url;
    }

    /**
     * @return the UrlTypes
     */
    public List getUrlTypes() {
        return UrlTypes;
    }

    /**
     * @param UrlTypes the UrlTypes to set
     */
    public void setUrlTypes(List UrlTypes) {
        this.UrlTypes = UrlTypes;
    }

    /**
     * @return the urlType
     */
    public String getUrlType() {
        return urlType;
    }

    /**
     * @param urlType the urlType to set
     */
    public void setUrlType(String urlType) {
        this.urlType = urlType;
    }

    /**
     * @return the isEmisora
     */
    public int getIsEmisora() {
        return isEmisora;
    }

    /**
     * @param isEmisora the isEmisora to set
     */
    public void setIsEmisora(int isEmisora) {
        this.isEmisora = isEmisora;
    }

    /**
     * @return the pid
     */
    public String getPid() {
        return pid;
    }

    /**
     * @param pid the pid to set
     */
    public void setPid(String pid) {
        this.pid = pid;
    }

    /**
     * @return the pidZip
     */
    public String getPidZip() {
        return pidZip;
    }

    /**
     * @param pidZip the pidZip to set
     */
    public void setPidZip(String pidZip) {
        this.pidZip = pidZip;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getMailContent() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }

    public Clob getDocumentContent() {
        return documentContent;
    }

    public void setDocumentContent(Clob documentContent) {
        this.documentContent = documentContent;
    }

    public String getAuthorMail() {
        return authorMail;
    }

    public void setAuthorMail(String authorMail) {
        this.authorMail = authorMail;
    }

    public String getFlagHistory() {
        return flagHistory;
    }

    public void setFlagHistory(String flagHistory) {
        this.flagHistory = flagHistory;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public boolean isPublishAttach() {
        return publishAttach;
    }

    public void setPublishAttach(boolean publishAttach) {
        this.publishAttach = publishAttach;
    }

    public String getReportYear() {
        return reportYear;
    }

    public void setReportYear(String reportYear) {
        this.reportYear = reportYear;
    }

    public String getReportTrim() {
        return reportTrim;
    }

    public void setReportTrim(String reportTrim) {
        this.reportTrim = reportTrim;
    }

    public String getArregloAutores() {
        return arregloAutores;
    }

    public void setArregloAutores(String arregloAutores) {
        this.arregloAutores = arregloAutores;
    }

    public String getArregloDistribucion() {
        return arregloDistribucion;
    }

    public void setArregloDistribucion(String arregloDistribucion) {
        this.arregloDistribucion = arregloDistribucion;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
    
    
}