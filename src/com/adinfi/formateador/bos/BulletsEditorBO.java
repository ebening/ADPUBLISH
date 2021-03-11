/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.bos;

/**
 *
 * @author USUARIO
 */
public class BulletsEditorBO {

    private int idTextEditor;
    private boolean copy;
    private boolean paste;
    private boolean pasteNoFormat;
    private boolean cut;
    private boolean bold;
    private boolean underline;
    private boolean italic;
    private boolean undo;
    private boolean redo;
    private boolean leftalign;
    private boolean centeralign;
    private boolean rightalign;
    private boolean unorderlist;
    private boolean orderlist;
    private boolean unicodecharacter;
    private boolean mathsymbols;
    private boolean strikethrough;

    public BulletsEditorBO() {

    }

    public int getIdTextEditor() {
        return idTextEditor;
    }

    public void setIdTextEditor(int idTextEditor) {
        this.idTextEditor = idTextEditor;
    }

    public boolean isCopy() {
        return copy;
    }

    public void setCopy(boolean copy) {
        this.copy = copy;
    }

    public boolean isPaste() {
        return paste;
    }

    public void setPaste(boolean paste) {
        this.paste = paste;
    }

    public boolean isPasteNoFormat() {
        return pasteNoFormat;
    }

    public void setPasteNoFormat(boolean pasteNoFormat) {
        this.pasteNoFormat = pasteNoFormat;
    }

    public boolean isCut() {
        return cut;
    }

    public void setCut(boolean cut) {
        this.cut = cut;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isUnderline() {
        return underline;
    }

    public void setUnderline(boolean underline) {
        this.underline = underline;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public boolean isUndo() {
        return undo;
    }

    public void setUndo(boolean undo) {
        this.undo = undo;
    }

    public boolean isRedo() {
        return redo;
    }

    public void setRedo(boolean redo) {
        this.redo = redo;
    }

    /**
     * @return the leftalign
     */
    public boolean isLeftalign() {
        return leftalign;
    }

    /**
     * @param leftalign the leftalign to set
     */
    public void setLeftalign(boolean leftalign) {
        this.leftalign = leftalign;
    }

    /**
     * @return the centeralign
     */
    public boolean isCenteralign() {
        return centeralign;
    }

    /**
     * @param centeralign the centeralign to set
     */
    public void setCenteralign(boolean centeralign) {
        this.centeralign = centeralign;
    }

    /**
     * @return the rightalign
     */
    public boolean isRightalign() {
        return rightalign;
    }

    /**
     * @param rightalign the rightalign to set
     */
    public void setRightalign(boolean rightalign) {
        this.rightalign = rightalign;
    }

    /**
     * @return the unorderlist
     */
    public boolean isUnorderlist() {
        return unorderlist;
    }

    /**
     * @param unorderlist the unorderlist to set
     */
    public void setUnorderlist(boolean unorderlist) {
        this.unorderlist = unorderlist;
    }

    /**
     * @return the orderlist
     */
    public boolean isOrderlist() {
        return orderlist;
    }

    /**
     * @param orderlist the orderlist to set
     */
    public void setOrderlist(boolean orderlist) {
        this.orderlist = orderlist;
    }

    /**
     * @return the unicodecharacter
     */
    public boolean isUnicodecharacter() {
        return unicodecharacter;
    }

    /**
     * @param unicodecharacter the unicodecharacter to set
     */
    public void setUnicodecharacter(boolean unicodecharacter) {
        this.unicodecharacter = unicodecharacter;
    }

    /**
     * @return the mathsymbols
     */
    public boolean isMathsymbols() {
        return mathsymbols;
    }

    /**
     * @param mathsymbols the mathsymbols to set
     */
    public void setMathsymbols(boolean mathsymbols) {
        this.mathsymbols = mathsymbols;
    }

    /**
     * @return the strikethrough
     */
    public boolean isStrikethrough() {
        return strikethrough;
    }

    /**
     * @param strikethrough the strikethrough to set
     */
    public void setStrikethrough(boolean strikethrough) {
        this.strikethrough = strikethrough;
    }

}
