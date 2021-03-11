package com.adinfi.formateador.bos;

/**
 *
 * @author Josue Sanchez
 */
public class AllowedBO {

    private int idAllowed;
    private String key;
    private String value;
    private String desc;
    private boolean status;

    public AllowedBO() {
    }

    public AllowedBO(int idAllowed, String key, String value, String desc, boolean status) {
        this.idAllowed = idAllowed;
        this.key = key;
        this.value = value;
        this.desc = desc;
        this.status = status;
    }

    /**
     * @return the idAllowed
     */
    public int getIdAllowed() {
        return idAllowed;
    }

    /**
     * @param idAllowed the idAllowed to set
     */
    public void setIdAllowed(int idAllowed) {
        this.idAllowed = idAllowed;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc the desc to set
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * @return the status
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(boolean status) {
        this.status = status;
    }
    
    
}
