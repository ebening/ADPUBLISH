package com.adinfi.formateador.bos;

/**
 *
 * @author USUARIO
 */
public class usuarioADBO{
    
   private String correo;
   private String extension;
   private String nombre;
   private String usuarioNT;
   private String dummy;

    public usuarioADBO() {
    }

    public usuarioADBO(String correo, String extension, String nombre, String usuarioNT) {
        this.correo = correo;
        this.extension = extension;
        this.nombre = nombre;
        this.usuarioNT = usuarioNT;
    }
   
   

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuarioNT() {
        return usuarioNT;
    }

    public void setUsuarioNT(String usuarioNT) {
        this.usuarioNT = usuarioNT;
    }
   
   @Override
   public String toString(){
    return nombre;
   }

    public String getDummy() {
        return dummy;
    }

    public void setDummy(String dummy) {
        this.dummy = dummy;
    }
      
    
}
