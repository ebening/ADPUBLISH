package com.adinfi.formateador.bos;

import java.awt.image.BufferedImage;

/**
 *
 * @author Carlos Félix
 */
public class DisclosureBO {
    private int disclosureID=0;
    private int type=0;
    private int insertUser= 0;
    private int updateUser= 0;
    private String textTop="";
    private String regularExpression="";
    private String preview="";
    private String textBottom="";
    private ExcelBO excelIni;
    private ExcelBO excelFin;
    
    public static class ExcelBO{
        private int disclosureId;
	private int excelId;
	private int insertUser= 0;
	private int updateUser= 0;
	private int categoryId= 0;
	private int nameId= 0;
	private String titulo="";
	private String subTitulo="";
	private String descripcion="";
        private boolean delete;
        private BufferedImage image;

        public BufferedImage getImage() {
            return image;
        }

        public void setImage(BufferedImage image) {
            this.image = image;
        }

        public boolean isDelete() {
            return delete;
        }

        public void setDelete(boolean delete) {
            this.delete = delete;
        }

        public int getDisclosureId() {
            return disclosureId;
        }

        public void setDisclosureId(int disclosureId) {
            this.disclosureId = disclosureId;
        }

        public int getExcelId() {
            return excelId;
        }

        public void setExcelId(int excelId) {
            this.excelId = excelId;
        }

        public int getInsertUser() {
            return insertUser;
        }

        public void setInsertUser(int insertUser) {
            this.insertUser = insertUser;
        }

        public int getUpdateUser() {
            return updateUser;
        }

        public void setUpdateUser(int updateUser) {
            this.updateUser = updateUser;
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

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }
    }
    
    public int getDisclosureID() {
        return disclosureID;
    }

    public void setDisclosureID(int disclosureID) {
        this.disclosureID = disclosureID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getInsertUser() {
        return insertUser;
    }

    public void setInsertUser(int insertUser) {
        this.insertUser = insertUser;
    }

    public int getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(int updateUser) {
        this.updateUser = updateUser;
    }

    public String getTextTop() {
        return textTop;
    }

    public void setTextTop(String textTop) {
        this.textTop = textTop;
    }

    public String getRegularExpression() {
        return regularExpression;
    }

    public void setRegularExpression(String regularExpression) {
        this.regularExpression = regularExpression;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getTextBottom() {
        return textBottom;
    }

    public void setTextBottom(String textBottom) {
        this.textBottom = textBottom;
    }

    public ExcelBO getExcelIni() {
        return excelIni;
    }

    public void setExcelIni(ExcelBO excelIni) {
        this.excelIni = excelIni;
    }

    public ExcelBO getExcelFin() {
        return excelFin;
    }

    public void setExcelFin(ExcelBO excelFin) {
        this.excelFin = excelFin;
    }
}