package com.adinfi.formateador.util;
import com.adinfi.formateador.bos.seguridad.*;
import com.adinfi.ws.Access_Impl;
import com.adinfi.ws.ArrayOfUsuario;
import com.adinfi.ws.IAccess_Stub;
import com.adinfi.ws.searchcm.Result;
import com.adinfi.ws.vectormedia.IVectorMedia_Stub;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;
import org.datacontract.schemas._2004._07.VCB_Analisis_Data_Emisoras.EmisoraRecomendacionLog;
import org.tempuri.IEmisorasProxy;

/**
 *
 * @author USUARIO
 */
public class UtileriasWS {

    public static List<Usuario> getAllUsers() {
        List<Usuario> list = new ArrayList<>();
        try {
            IAccess_Stub stub = (IAccess_Stub) new Access_Impl().getBasicHttpBinding_IAccess();
            UtileriasWS.setEndpoint(stub);
            ArrayOfUsuario array = stub.buscarUsuarios(GlobalDefines.WS_INSTANCE, 0, null);

            if (array != null && array.getUsuario() != null) {
                for (com.adinfi.ws.Usuario o : array.getUsuario()) {
                    //com.adinfi.formateador.bos.seguridad.Perfil p = new com.adinfi.formateador.bos.seguridad.Perfil(o.getPerfilId(), o.getNombre(), o.getDescripcion(), o.getFechaAlta(), o.getIsActiv(), o.getIsAdministrable(), o.getIsVisible());
                    Usuario u = new com.adinfi.formateador.bos.seguridad.Usuario(
                            false,
                            o.getUsuarioId(),
                            o.getUsuarioNT(),
                            o.getNombre(),
                            o.getCorreo(),
                            o.getExtension(),
                            o.getFechaAlta(),
                            o.getUltimoAcceso(),
                            o.getIsAutor(),
                            o.getPerfilId(),
                            o.getPerfil(),
                            o.getMiVectorId(),
                            o.getIsDirectorio()
                    );
                    if(o.getIsDirectorio())
                        u.setOrdenDir(o.getOrdenDirectorio() != null ? o.getOrdenDirectorio().intValue() : 0 );
                    else
                        u.setOrdenDir(0);
                    
                    list.add(u);
                }
            }
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null, "El servicio de busqueda de usuarios no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
            Utilerias.logger(UtileriasWS.class).info(e);
        }
        return list;
    }

    public static List<Usuario> getAuthor() {
        List<Usuario> list = new ArrayList<>();
        try {
            IAccess_Stub stub = (IAccess_Stub) new Access_Impl().getBasicHttpBinding_IAccess();
            UtileriasWS.setEndpoint(stub);
            ArrayOfUsuario array = stub.buscarUsuarios(GlobalDefines.WS_INSTANCE, 0, null);

            if (array != null && array.getUsuario() != null) {
                for (com.adinfi.ws.Usuario o : array.getUsuario()) {
                    //com.adinfi.formateador.bos.seguridad.Perfil p = new com.adinfi.formateador.bos.seguridad.Perfil(o.getPerfilId(), o.getNombre(), o.getDescripcion(), o.getFechaAlta(), o.getIsActiv(), o.getIsAdministrable(), o.getIsVisible());
                    if (o.getIsAutor() == true) {
                        Usuario u = new Usuario(
                                false,
                                o.getUsuarioId(),
                                o.getUsuarioNT(),
                                o.getNombre(),
                                o.getCorreo(),
                                o.getExtension(),
                                o.getFechaAlta(),
                                o.getUltimoAcceso(),
                                o.getIsAutor(),
                                o.getPerfilId(),
                                o.getPerfil(),
                                o.getMiVectorId(),
                                o.getIsDirectorio()
                        );
                        u.setOrdenDir(o.getOrdenDirectorio());
                        list.add(u);
                    }
                }
            }
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null, "El servicio de busqueda de usuarios no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
            Utilerias.logger(UtileriasWS.class).info(e);
        }
        return list;
    }
    
    public static List<Usuario> getDirectorio() {
        List<Usuario> list = new ArrayList<>();
        try {
            IAccess_Stub stub = (IAccess_Stub) new Access_Impl().getBasicHttpBinding_IAccess();
            UtileriasWS.setEndpoint(stub);
            ArrayOfUsuario array = stub.buscarUsuarios(GlobalDefines.WS_INSTANCE, 0, null);

            if (array != null && array.getUsuario() != null) {
                for (com.adinfi.ws.Usuario o : array.getUsuario()) {
                    if (o.getIsDirectorio() == true) {
                        Usuario u = new Usuario(
                                false,
                                o.getUsuarioId(),
                                o.getUsuarioNT(),
                                o.getNombre(),
                                o.getCorreo(),
                                o.getExtension(),
                                o.getFechaAlta(),
                                o.getUltimoAcceso(),
                                o.getIsAutor(),
                                o.getPerfilId(),
                                o.getPerfil(),
                                o.getMiVectorId(),
                                o.getIsDirectorio()
                        );
                        u.setOrdenDir(o.getOrdenDirectorio());
                        list.add(u);
                    }
                }
            }
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null, "El servicio de busqueda de usuarios no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
            Utilerias.logger(UtileriasWS.class).info(e);
        }
        return list;
    }

    public static void setEndpoint(com.sun.xml.rpc.client.StubBase stub) {
        String url = "";
        if (stub instanceof IAccess_Stub) {
            url = Utilerias.getProperty(ApplicationProperties.URL_WS_VCBAUTH_ACCESS);
            stub._setProperty(javax.xml.rpc.Stub.ENDPOINT_ADDRESS_PROPERTY, url);
        } else if (stub instanceof IVectorMedia_Stub) {
            url = Utilerias.getProperty(ApplicationProperties.URL_WS_VECTORMEDIA);
            stub._setProperty(javax.xml.rpc.Stub.ENDPOINT_ADDRESS_PROPERTY, url);
        } else if (stub instanceof com.adinfi.ws.analisisws.publicador.IPublicador_Stub) {
            url = Utilerias.getProperty(ApplicationProperties.URL_WS_ANALISIS_PUBLICADOR);
            stub._setProperty(javax.xml.rpc.Stub.ENDPOINT_ADDRESS_PROPERTY, url);
        } else if (stub instanceof com.adinfi.ws.publicador.IPublicador_Stub) {
            url = Utilerias.getProperty(ApplicationProperties.URL_WS_PUBLICADOR_PUBLICADOR);
            stub._setProperty(javax.xml.rpc.Stub.ENDPOINT_ADDRESS_PROPERTY, url);
        } else if (stub instanceof com.adinfi.ws.data.IData_Stub) {
            url = Utilerias.getProperty(ApplicationProperties.URL_WS_ANALISIS_DATA);
            stub._setProperty(javax.xml.rpc.Stub.ENDPOINT_ADDRESS_PROPERTY, url);
        } else if (stub instanceof com.adinfi.ws.emisoras.IEmisoras_Stub){
            url = Utilerias.getProperty(ApplicationProperties.EMISORAS_WS);
            stub._setProperty(javax.xml.rpc.Stub.ENDPOINT_ADDRESS_PROPERTY, url);
        }
    }

    public static com.adinfi.ws.searchcm.DocumentServicePortType setEndpointWS(com.adinfi.ws.searchcm.DocumentService service) {
        com.adinfi.ws.searchcm.DocumentServicePortType port = service.getDocumentServiceHttpSoap12Endpoint();
//        String url = "";
//        BindingProvider bp = (BindingProvider) port;
//        url = Utilerias.getProperty(ApplicationProperties.SEARCHCM_WS);
//        bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }

    public static List<org.datacontract.schemas._2004._07.VCB_Analisis_Data.Emisora> getEmisoras() {
        List<org.datacontract.schemas._2004._07.VCB_Analisis_Data.Emisora> list = null;
        try {
            org.tempuri.IEmisorasProxy proxy = new IEmisorasProxy(Utilerias.getProperty(ApplicationProperties.EMISORAS_WS));
            org.datacontract.schemas._2004._07.VCB_Analisis_Data.Emisora[] emisoras = proxy.emisorasConRecomendacion("");
            if (emisoras != null) {
                list = Arrays.asList(emisoras);
            }
        } catch (RemoteException e) {
            Utilerias.logger(UtileriasWS.class).info(e);
        }
        return list;
    }

    public static List<EmisoraRecomendacionLog> getEmisorasFundamentales(String theme) {
        List<EmisoraRecomendacionLog> list = null;
        try {
            String xmlTheme = "<Emisoras><Emisora>" + theme.toUpperCase() + "</Emisora></Emisoras>";
            org.tempuri.IEmisorasProxy proxy = new IEmisorasProxy(Utilerias.getProperty(ApplicationProperties.EMISORAS_WS));
            EmisoraRecomendacionLog[] emisoras = proxy.logRecomendacionesFundamentales(xmlTheme);
            if (emisoras != null) {
                list = Arrays.asList(emisoras);
            }
        } catch (RemoteException e) {
           JOptionPane.showMessageDialog(null, "El servicio de log de recomendaciones fundamentales no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
            Utilerias.logger(UtileriasWS.class).info(e);
        }
        return list;
    }

    public static String[] getIDSMarketMiVector(String returnMessage) {
        String[] ids = new String[2];

        StringTokenizer stk = new StringTokenizer(returnMessage, ",");
        if (stk.hasMoreElements() == true) {
            String s = (String) stk.nextElement();
            ids[0] = s;
        }
        if (stk.hasMoreElements() == true) {
            String s = (String) stk.nextElement();
            ids[1] = s;
        }

        return ids;
    }

    public static Result documentSearch(java.lang.String idDocumento, java.lang.String idMercado, java.lang.String sFechaInicial, java.lang.String sFechaFinal, java.lang.String sTema, java.lang.String sSector, java.lang.Integer idSector, java.lang.String sIdioma, java.lang.String sEstado, java.lang.String sAutor, java.lang.String sPalabraClave, java.lang.String sColumnaOrden, java.lang.Integer sTipoOrden, java.lang.Integer iPag, java.lang.Integer iMaxCant) {
        com.adinfi.ws.searchcm.DocumentService service = new com.adinfi.ws.searchcm.DocumentService();
        com.adinfi.ws.searchcm.DocumentServicePortType port = setEndpointWS(service);
        return port.documentSearch(idDocumento, idMercado, sFechaInicial, sFechaFinal, sTema, sSector, idSector, sIdioma, sEstado, sAutor, sPalabraClave, sColumnaOrden, sTipoOrden, iPag, iMaxCant);
    }

}
