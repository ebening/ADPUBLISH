

package com.adinfi.formateador.bos;

import java.sql.Blob;

/**
 *
 * @author Josue
 */
public class HeaderfooterBO {
    
    private int id;
    private Blob header;
    private Blob footer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Blob getHeader() {
        return header;
    }

    public void setHeader(Blob header) {
        this.header = header;
    }

    public Blob getFooter() {
        return footer;
    }

    public void setFooter(Blob footer) {
        this.footer = footer;
    }

}
