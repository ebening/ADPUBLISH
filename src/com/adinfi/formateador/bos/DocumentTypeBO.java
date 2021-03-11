/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.bos;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USUARIO
 */
public class DocumentTypeBO {

    boolean check;
    int iddocument_type;
    String iddocument_type_vector;

    String name;

    String marketNameSearch;
    int marketIDSearch;

    String name_market;
    int idMarket;
    String nomenclature;
    String email;
    int idOutgoingEmail;

    String idMiVector;
    String publishP_NAME;
    int idPublishProfile;

    String name_disclosure;
    int idDisclosure;
    boolean sendMedia;
    boolean publish;
    boolean collaborative;
    boolean sendEmail;
    boolean subject;
    boolean title;
    boolean subTitle;
    boolean html;
    boolean pdf;
    private boolean status;

    /*titulo regex */
    private String title_regex;
    private String title_regex_decoded;

    private ReturnSelectedTemplates selectedTemplates;
    private List<Integer> templates = new ArrayList<>();
    private List<String> contracts = new ArrayList<>();
    private List<Integer> profiles = new ArrayList<>();
    private List<Integer> profilesPublish = new ArrayList<>();

    public List<Integer> getProfilesPublish() {
        return profilesPublish;
    }

    public void setProfilesPublish(List<Integer> profilesPublish) {
        this.profilesPublish = profilesPublish;
    }

    public List<Integer> getTemplates() {
        return templates;
    }

    public void setTemplates(List<Integer> templates) {
        this.templates = templates;
    }

    public List<String> getContracts() {
        return contracts;
    }

    public void setContracts(List<String> contracts) {
        this.contracts = contracts;
    }

    public List<Integer> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<Integer> profiles) {
        this.profiles = profiles;
    }

    @Override
    public String toString() {
        return name;
    }

    public DocumentTypeBO() {
    }

    public DocumentTypeBO(int document_id) {
        this.iddocument_type = document_id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMarketNameSearch() {
        return marketNameSearch;
    }

    public void setMarketNameSearch(String marketNameSearch) {
        this.marketNameSearch = marketNameSearch;
    }

    public int getMarketIDSearch() {
        return marketIDSearch;
    }

    public void setMarketIDSearch(int marketIDSearch) {
        this.marketIDSearch = marketIDSearch;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getIddocument_type() {
        return iddocument_type;
    }

    public void setIddocument_type(int iddocument_type) {
        this.iddocument_type = iddocument_type;
    }

    public String getIddocument_type_vector() {
        return iddocument_type_vector;
    }

    public void setIddocument_type_vector(String iddocument_type_vector) {
        this.iddocument_type_vector = iddocument_type_vector;
    }
    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_market() {
        return name_market;
    }

    public void setName_market(String name_market) {
        this.name_market = name_market;
    }

    public int getIdMarket() {
        return idMarket;
    }

    public void setIdMarket(int idMarket) {
        this.idMarket = idMarket;
    }

    public String getNomenclature() {
        return nomenclature;
    }

    public void setNomenclature(String nomenclature) {
        this.nomenclature = nomenclature;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIdOutgoingEmail() {
        return idOutgoingEmail;
    }

    public void setIdOutgoingEmail(int idOutgoingEmail) {
        this.idOutgoingEmail = idOutgoingEmail;
    }

    public String getIdMiVector() {
        return idMiVector;
    }

    public void setIdMiVector(String idMiVector) {
        this.idMiVector = idMiVector;
    }

    public String getPublishP_NAME() {
        return publishP_NAME;
    }

    public void setPublishP_NAME(String publishP_NAME) {
        this.publishP_NAME = publishP_NAME;
    }

    public int getIdPublishProfile() {
        return idPublishProfile;
    }

    public void setIdPublishProfile(int idPublishProfile) {
        this.idPublishProfile = idPublishProfile;
    }

    public String getName_disclosure() {
        return name_disclosure;
    }

    public void setName_disclosure(String name_disclosure) {
        this.name_disclosure = name_disclosure;
    }

    public int getIdDisclosure() {
        return idDisclosure;
    }

    public void setIdDisclosure(int idDisclosure) {
        this.idDisclosure = idDisclosure;
    }

    public boolean isSendMedia() {
        return sendMedia;
    }

    public void setSendMedia(boolean send) {
        this.sendMedia = send;
    }

    public boolean isPublish() {
        return publish;
    }

    public void setPublish(boolean publish) {
        this.publish = publish;
    }

    public boolean isCollaborative() {
        return collaborative;
    }

    public void setCollaborative(boolean collaborative) {
        this.collaborative = collaborative;
    }

    public boolean isSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(boolean sendEmail) {
        this.sendEmail = sendEmail;
    }

    public boolean isSubject() {
        return subject;
    }

    public void setSubject(boolean subject) {
        this.subject = subject;
    }

    public boolean isTitle() {
        return title;
    }

    public void setTitle(boolean title) {
        this.title = title;
    }

    public boolean isSubTitle() {
        return subTitle;
    }

    public void setSubTitle(boolean subTitle) {
        this.subTitle = subTitle;
    }

    public boolean isHtml() {
        return html;
    }

    public void setHtml(boolean html) {
        this.html = html;
    }

    public boolean isPdf() {
        return pdf;
    }

    public void setPdf(boolean pdf) {
        this.pdf = pdf;
    }

    /**
     * @return the selectedTemplates
     */
    public ReturnSelectedTemplates getSelectedTemplates() {
        return selectedTemplates;
    }

    /**
     * @param selectedTemplates the selectedTemplates to set
     */
    public void setSelectedTemplates(ReturnSelectedTemplates selectedTemplates) {
        this.selectedTemplates = selectedTemplates;
    }

    public String getTitle_regex() {
        return title_regex;
    }

    public void setTitle_regex(String title_regex) {
        this.title_regex = title_regex;
    }

    public String getTitle_regex_decoded() {
        return title_regex_decoded;
    }

    public void setTitle_regex_decoded(String title_regex_decoded) {
        this.title_regex_decoded = title_regex_decoded;
    }

}
