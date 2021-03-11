package com.adinfi.formateador.bos.seguridad;

import com.adinfi.formateador.bos.seguridad.*;
/**
 *
 * @author Guillermo Trejo
 */
public class Usuario {
    
    private boolean check;
    private int usuarioId;		//Id de Usuario
    private String usuarioNT;		//Usuario de red
    private String nombre;		//Nombre del usuario
    private String correo;		//Email
    private String extension;		//Extensión
    private String fechaAlta;		//Fecha de Alta
    private String ultimoAcceso;	//Fecha de Ãšltimo Acceso
    private boolean isAutor;		//Bandera que indica si es autor o no
    private int perfilId;		//Identificador del perfil
    private String perfil;		//Nombre del perfil
    private int MiVectorId;		//Id de usuario para MiVector
    private boolean directorio;         //Es directorio
    private int ordenDir;

    public Usuario() {
    }

    public Usuario(boolean check, int usuarioId, String usuarioNT, String nombre, String correo, String extension, String fechaAlta, String ultimoAcceso, boolean isAutor, int perfilId, String perfil, int MiVectorId, boolean directorio) {
        this.check = check;
        this.usuarioId = usuarioId;
        this.usuarioNT = usuarioNT;
        this.nombre = nombre;
        this.correo = correo;
        this.extension = extension;
        this.fechaAlta = fechaAlta;
        this.ultimoAcceso = ultimoAcceso;
        this.isAutor = isAutor;
        this.perfilId = perfilId;
        this.perfil = perfil;
        this.MiVectorId = MiVectorId;
        this.directorio = directorio;
    }



    /**
     * @return the usuarioId
     */
    public int getUsuarioId() {
        return usuarioId;
    }

    /**
     * @param usuarioId the usuarioId to set
     */
    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    /**
     * @return the usuarioNT
     */
    public String getUsuarioNT() {
        return usuarioNT;
    }

    /**
     * @param usuarioNT the usuarioNT to set
     */
    public void setUsuarioNT(String usuarioNT) {
        this.usuarioNT = usuarioNT;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the correo
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * @param correo the correo to set
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * @return the extension
     */
    public String getExtension() {
        return extension;
    }

    /**
     * @param extension the extension to set
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * @return the fechaAlta
     */
    public String getFechaAlta() {
        return fechaAlta;
    }

    /**
     * @param fechaAlta the fechaAlta to set
     */
    public void setFechaAlta(String fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    /**
     * @return the ultimoAcceso
     */
    public String getUltimoAcceso() {
        return ultimoAcceso;
    }

    /**
     * @param ultimoAcceso the ultimoAcceso to set
     */
    public void setUltimoAcceso(String ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    /**
     * @return the isAutor
     */
    public boolean isIsAutor() {
        return isAutor;
    }

    /**
     * @param isAutor the isAutor to set
     */
    public void setIsAutor(boolean isAutor) {
        this.isAutor = isAutor;
    }

    /**
     * @return the perfilId
     */
    public int getPerfilId() {
        return perfilId;
    }

    /**
     * @param perfilId the perfilId to set
     */
    public void setPerfilId(int perfilId) {
        this.perfilId = perfilId;
    }

    /**
     * @return the perfil
     */
    public String getPerfil() {
        return perfil;
    }

    /**
     * @param perfil the perfil to set
     */
    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    /**
     * @return the MiVectorId
     */
    public int getMiVectorId() {
        return MiVectorId;
    }

    /**
     * @param MiVectorId the MiVectorId to set
     */
    public void setMiVectorId(int MiVectorId) {
        this.MiVectorId = MiVectorId;
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

    public boolean isDirectorio() {
        return directorio;
    }

    public void setDirectorio(boolean directorio) {
        this.directorio = directorio;
    }

    public int getOrdenDir() {
        return ordenDir;
    }

    public void setOrdenDir(int ordenDir) {
        this.ordenDir = ordenDir;
    }
    
    @Override
    public String toString(){
    return nombre;
    }

}
