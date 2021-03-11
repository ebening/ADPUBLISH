package com.adinfi.formateador.view;

import com.adinfi.formateador.bos.AutoresVideoBO;
import com.adinfi.formateador.bos.CategoriaVideoBO;
import com.adinfi.formateador.bos.ContentTypeBO;
import com.adinfi.formateador.bos.LanguageBO;
import com.adinfi.formateador.bos.ObjectInfoBO;
import com.adinfi.formateador.bos.TipoAnalisisMarketBO;
import com.adinfi.formateador.dao.LanguageDAO;
import com.adinfi.formateador.dao.StatementConstant;
import com.adinfi.formateador.util.ApplicationProperties;
import com.adinfi.formateador.util.GlobalDefines;
import com.adinfi.formateador.util.Utilerias;
import com.adinfi.formateador.util.UtileriasWS;
import com.adinfi.formateador.view.administration.tablemodel.SearchVideoModel;
import com.adinfi.formateador.view.administration.tablemodel.VideoModel;
import com.adinfi.ws.vectormedia.ArrayOftdsAutores;
import com.adinfi.ws.vectormedia.ArrayOftdsCategoriaVideo;
import com.adinfi.ws.vectormedia.ArrayOftdsTipoAnalisis;
import com.adinfi.ws.vectormedia.ArrayOftdsTipoMultimedia;
import com.adinfi.ws.vectormedia.IVectorMedia_Stub;
import com.adinfi.ws.vectormedia.TdsAutores;
import com.adinfi.ws.vectormedia.TdsTipoAnalisis;
import com.adinfi.ws.vectormedia.TdsVectorMediaBusqueda;
import com.adinfi.ws.vectormedia.TdsVectorMediaConfig;
import com.adinfi.ws.vectormedia.VectorMediaBusqueda;
import com.adinfi.ws.vectormedia.VectorMedia_Impl;
import com.inet.jortho.FileUserDictionary;
import com.inet.jortho.PopupListener;
import com.inet.jortho.SpellChecker;
import com.inet.jortho.SpellCheckerOptions;
import com.jktoolkit.table.JKTable;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingWorker;

/**
 *
 * @author USUARIO
 */
public class ScrObjVideo extends javax.swing.JDialog {

    protected ObjectInfoBO objInfo;
    protected Boolean ok;
    private String htmlVideo = "";
    private String html5Video = "";
    private JFXPanel panelVideoChild;
    private TdsVectorMediaConfig[] vectorConfig;
    private String thumb;
    private String tituloVideo;
    private String subtituloVideo;
    private String fuenteVideo;
    WebView view;
    boolean proxy = Boolean.parseBoolean(Utilerias.getProperty(ApplicationProperties.PROXY_CONFIG));
    String idKaltura = null;
    String idVideoRow = null;
    String id_youtube = null;
    String id_video;
    String idvideodb;
    String tipoVideo;
    public JKTable<VideoModel> table;
    public int numPag = 1;
    public int numEPP = 5;
    public TdsVectorMediaBusqueda[] lstETot = null;
    private boolean isSaved = true;
    private int MAX_ROWS = 5;
    private int NUM_PAGS = 0;
    private int CURRENT_PAGE = 1;

    private String youtubeEmbed = "<iframe width=\"{WIDTH}\" height=\"{HEIGHT}\"\n"
            + "src=\"{@embed}?rel=0\">\n"
            + "</iframe>";

    /**
     * Creates new form ScrObjVideo
     *
     * @param parent
     * @param modal
     */
    public ScrObjVideo(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initPanelVideo();
        fillCboIdiomas();

        try {

            searchVideosBar.setVisible(false);

            gridVideo.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent evnt) {
                    if (evnt.getClickCount() == 1) {

                    selectAction();
                        
                    }
                }
            });
        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTituloVideo() {
        return tituloVideo;
    }

    public void setTituloVideo(String tituloVideo) {
        this.tituloVideo = tituloVideo;
    }

    public String getSubtituloVideo() {
        return subtituloVideo;
    }

    public void setSubtituloVideo(String subtituloVideo) {
        this.subtituloVideo = subtituloVideo;
    }

    public String getFuenteVideo() {
        return fuenteVideo;
    }

    public void setFuenteVideo(String fuenteVideo) {
        this.fuenteVideo = fuenteVideo;
    }

    public void initScrObject(ObjectInfoBO objInfo) {
        this.objInfo = objInfo;
    }

    public void setObjInfo(ObjectInfoBO objInfo) {
        this.objInfo = objInfo;
    }

    public Boolean isAccept() {
        return ok;
    }

    /**
     * @return the htmlVideo
     */
    public String getHtmlVideo() {
        return htmlVideo;
    }

    /**
     * @param htmlVideo the htmlVideo to set
     */
    public void setHtmlVideo(String htmlVideo) {
        this.htmlVideo = htmlVideo;
    }

    public String getId_video() {
        return id_video;
    }

    public void setId_video(String id_video) {
        this.id_video = id_video;
    }

    public String getIdvideodb() {
        return idvideodb;
    }

    public void setIdvideodb(String idvideodb) {
        this.idvideodb = idvideodb;
    }

    public String getTipoVideo() {
        return tipoVideo;
    }

    public void setTipoVideo(String tipoVideo) {
        this.tipoVideo = tipoVideo;
    }

    public void enableKalturaFields(boolean b) {
        cboContentType.setEnabled(b);
        cboCategoryVideo.setEnabled(b);
        cboMarketVideo.setEnabled(b);
        cboAutorVideo.setEnabled(b);
        inputSearchVideo.setEnabled(b);
        searchVideo.setEnabled(b);
        gridVideo.setEnabled(b);
        btnDisplay.setEnabled(b);
    }

    public void enableURLFields(boolean b) {
        urlVideo.setEnabled(b);
        btnDisplay.setEnabled(b);
    }

    private void initPanelVideo() {
        videoDisplay.removeAll();
        videoDisplay.setLayout(new BorderLayout());
        panelVideoChild = new JFXPanel();
        videoDisplay.add(panelVideoChild, BorderLayout.CENTER);
        videoDisplay.setPreferredSize(new Dimension(277, 308));
        videoDisplay.setMaximumSize(new Dimension(277, 308));
    }

    private void createScene(String urlVideo) {

        htmlVideo = youtubeEmbed;
        htmlVideo = htmlVideo.replace("{@embed}", urlVideo);
        //htmlVideo = htmlVideo.replace("{WIDTH}", "400");
        //htmlVideo = htmlVideo.replace("{HEIGHT}", "400");

        if (proxy) {
            System.setProperty("http.proxyHost", Utilerias.getProperty(ApplicationProperties.PROXY_CONFIG_HOST));
            System.setProperty("http.proxyPort", Utilerias.getProperty(ApplicationProperties.PROXY_CONFIG_PORT));
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //System.out.println(urlVideo);
                view = new WebView();
                view.getEngine().load(urlVideo);
                //view.getEngine().loadContent(htmlVideo);
                panelVideoChild.setScene(new Scene(view));
            }
        });
    }

    private void loadKalturaVideo(String kalturaID, boolean muted) {

        String embed = getConfig("KalturaScript");
        String embed5 = getConfig("KalturaScriptHTML5");

        final String embedConfig = embed.replace("{KALTURA_ID}", kalturaID).replace("{WIDTH}", "100%").replace("flashvars[autoPlay]=true", "flashvars[autoPlay]=false").replace("mozAllowFullScreen", "wmode=\"opaque\"");
        final String embedConfig5 = embed5.replace("{KALTURA_ID}", kalturaID).replace("{WIDTH}", "100%");

        htmlVideo = embedConfig;
        html5Video = embedConfig5;

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                view = new WebView();
                //stopVideo();
                view.getEngine().loadContent(embedConfig);
                panelVideoChild.setScene(new Scene(view));
            }
        });

    }

    public void contentType() {
        try {

            IVectorMedia_Stub stub2 = (IVectorMedia_Stub) new VectorMedia_Impl().getBasicHttpBinding_IVectorMedia();
            UtileriasWS.setEndpoint(stub2);
            /*Tipo*/
            ArrayOftdsTipoMultimedia array = stub2.getVectorMediaCatalogos().getDsVectorMedia().getTdsTipoMultimedia();
            if (array != null && array.getTdsTipoMultimedia() != null) {

                cboContentType.removeAllItems();

                for (com.adinfi.ws.vectormedia.TdsTipoMultimedia t : array.getTdsTipoMultimedia()) {
                    com.adinfi.formateador.bos.ContentTypeBO bo = new com.adinfi.formateador.bos.ContentTypeBO();
                    bo.setDescripcion(t.getDescripcion());
                    bo.setTipoMultimediaId(t.getTipoMultimediaId());
                    cboContentType.addItem(bo);
                }
            }

            /*Categoria*/
            ArrayOftdsCategoriaVideo cat = stub2.getVectorMediaCatalogos().getDsVectorMedia().getTdsCategoriaVideo();

            if (cat != null && cat.getTdsCategoriaVideo() != null) {
                cboCategoryVideo.removeAllItems();

                for (com.adinfi.ws.vectormedia.TdsCategoriaVideo c : cat.getTdsCategoriaVideo()) {
                    com.adinfi.formateador.bos.CategoriaVideoBO bo2 = new com.adinfi.formateador.bos.CategoriaVideoBO();
                    bo2.setCategoriaVideo(c.getCategoriaVideo());
                    bo2.setDescripcion(c.getDescripcion());
                    cboCategoryVideo.addItem(bo2);
                }

            }

            /* Mercado */
            ArrayOftdsTipoAnalisis mkt = stub2.getVectorMediaCatalogos().getDsVectorMedia().getTdsTipoAnalisis();
            if (mkt != null && mkt.getTdsTipoAnalisis() != null) {

                cboMarketVideo.removeAllItems();

                for (TdsTipoAnalisis m : mkt.getTdsTipoAnalisis()) {
                    com.adinfi.formateador.bos.TipoAnalisisMarketBO bo3 = new com.adinfi.formateador.bos.TipoAnalisisMarketBO();
                    bo3.setCategoriaVideo(m.getIdCategoria());
                    bo3.setDescripcion(m.getDescripcion());
                    cboMarketVideo.addItem(bo3);
                }

            }

            /* Autores */
            ArrayOftdsAutores autores = stub2.getVectorMediaCatalogos().getDsVectorMedia().getTdsAutores();
            if (autores != null && autores.getTdsAutores() != null) {

                cboAutorVideo.removeAllItems();
//
                for (TdsAutores a : autores.getTdsAutores()) {
                    com.adinfi.formateador.bos.AutoresVideoBO bo4 = new com.adinfi.formateador.bos.AutoresVideoBO();
                    bo4.setIdAutor(a.getIdAutor());
                    bo4.setNombre(a.getNombre());
                    cboAutorVideo.addItem(bo4);
                }

            }

        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(this, "El servicio de envío de tweet no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
            Utilerias.logger(getClass()).info(ex);
        }

    }

    public void setVideoInfo() {

        if (this.objInfo != null) {

            isSaved = false;
            titleVideo.setText(objInfo.getTitulo());
            subtitleVideo.setText(objInfo.getSubTitulo());
            sourceVideo.setText(objInfo.getComentarios());

            if (objInfo.getTipoVideo().equals("vectormedia")) {

                SearchVideosWorker worker = new SearchVideosWorker(this.objInfo.getIdVideoDb(), true);
                searchVideosBar.setVisible(true);
                worker.execute();

            } else if (objInfo.getTipoVideo().equals("youtube")) {

                String url = "https://www.youtube.com/watch?v=" + objInfo.getIdVideo();
                urlVideo.setText(url);
                playYoutube(url);
                selectURLVideo.setSelected(true);
            }
        }
    }

    private void stopVideo() {

        if (idKaltura != null || id_youtube != null) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (Platform.isFxApplicationThread()) {
                        Platform.setImplicitExit(false);
                        if (view.getEngine() != null) {
                            view.getEngine().load(null);
                        }
                    }
                }
            });
        }
    }

    private void searchVideos(boolean inicial, int pag, int maxrows) {

        SearchVideoModel mod = new SearchVideoModel();

        CURRENT_PAGE = pag;
        txtPage.setText(String.valueOf(CURRENT_PAGE));

        /* Se obtiene el tipo de contenido */
        Object ct = cboContentType.getSelectedItem();
        String type = "Todos";
        if (ct != null) {
            ContentTypeBO ctbo = (ContentTypeBO) ct;
            type = ctbo.getTipoMultimediaId();
        }

        /* Obtener el id de la categoria */
        Object ccat = cboCategoryVideo.getSelectedItem();
        int cat = 0;
        if (ccat != null) {
            CategoriaVideoBO ccatbo = (CategoriaVideoBO) ccat;
            cat = ccatbo.getCategoriaVideo();
        }

        /* Obtener el id del mercado */
        Object cmkt = cboMarketVideo.getSelectedItem();
        int marketid = 0;
        if (cmkt != null) {
            TipoAnalisisMarketBO cmktbo = (TipoAnalisisMarketBO) cmkt;
            marketid = cmktbo.getCategoriaVideo();
        }

        /**/
        Object caut = cboAutorVideo.getSelectedItem();
        int autor = 0;
        if (caut != null) {
            AutoresVideoBO cautbo = (AutoresVideoBO) caut;
            autor = cautbo.getIdAutor();
        }

        String contenido = "";
        if (inputSearchVideo.getText() != null) {
            contenido = inputSearchVideo.getText();
        }

        /*Tomar del datePicker */
        String fechaInicial = "";
        String fechaFinal = "";

        /*tomar los valores de la tabla allowed*/
        int page = pag;
        int maxRows = maxrows;

        SearchVideosWorker worker = new SearchVideosWorker(cat, marketid, autor, contenido, fechaInicial, fechaFinal, page, maxRows, type, inicial, false);
        searchVideosBar.setVisible(true);
        worker.execute();

    }

    public TdsVectorMediaBusqueda[] getElementosIniciales() {
        TdsVectorMediaBusqueda[] retVal = new TdsVectorMediaBusqueda[numEPP];
        numPag = 1;
        try {
            if (lstETot == null || lstETot.length < 0) {
                return retVal;
            }

            for (int i = 0; i < numEPP; i++) {
                if (i >= lstETot.length) {
                    break;
                }

                retVal[i] = lstETot[i];
            }

        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        }

        txtPage.setText(String.valueOf(numPag));
        return retVal;
    }

    public TdsVectorMediaBusqueda[] getElementosSiguientes() {
        TdsVectorMediaBusqueda[] retVal = new TdsVectorMediaBusqueda[numEPP];

        numPag++;

        int valFin = numPag * numEPP;
        int valIni = valFin - numEPP;

        if (lstETot != null && valIni >= lstETot.length) {
            numPag--;
            if (numPag <= 0) {
                numPag = 1;
            }
        }

        try {
            if (lstETot == null || lstETot.length < 0) {
                return retVal;
            }

            valFin = numPag * numEPP;
            valIni = valFin - numEPP;
            int y = 0;

            for (int i = valIni; i < valFin; i++) {
                if (i >= lstETot.length) {
                    break;
                }

                retVal[y] = lstETot[i];
                y++;
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        }
        txtPage.setText(String.valueOf(numPag));
        return retVal;
    }

    public TdsVectorMediaBusqueda[] getElementosAnteriores() {
        TdsVectorMediaBusqueda[] retVal = new TdsVectorMediaBusqueda[numEPP];

        numPag--;
        if (numPag <= 0) {
            numPag = 1;
        }

        try {
            if (lstETot == null || lstETot.length < 0) {
                return retVal;
            }

            int valFin = numPag * numEPP;
            int valIni = valFin - numEPP;
            int y = 0;

            for (int i = valIni; i < valFin; i++) {
                if (i >= lstETot.length) {
                    break;
                }
                retVal[y] = lstETot[i];
                y++;
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        }
        txtPage.setText(String.valueOf(numPag));
        return retVal;
    }

    public TdsVectorMediaBusqueda[] getElementosFinales() {
        TdsVectorMediaBusqueda[] retVal = new TdsVectorMediaBusqueda[numEPP];

        if (lstETot == null || lstETot.length < 0) {
        } else {
            numPag = (int) lstETot.length / numEPP;
            int residuo = lstETot.length % numEPP;
            if (residuo > 0) {
                numPag++;
            }

            if (numPag <= 0) {
                numPag = 1;
            }
        }

        try {

            int valFin = numPag * numEPP;
            int valIni = valFin - numEPP;
            int y = 0;

            for (int i = valIni; i < valFin; i++) {
                if (i >= lstETot.length) {
                    break;
                }

                retVal[y] = lstETot[i];
                y++;
            }
        } catch (Exception e) {
            Utilerias.logger(getClass()).error(e);
        }
        txtPage.setText(String.valueOf(numPag));
        return retVal;
    }

    private void loadVideo(String idKaltura) {
        String embed = getConfig("KalturaScript");
        embed = embed.replace("{KALTURA_ID}", idKaltura);
        loadKalturaVideo(idKaltura, false);
    }

    private String getConfig(String key) {
        for (int i = 0; i < vectorConfig.length; i++) {
            TdsVectorMediaConfig tdsVectorMediaConfig = vectorConfig[i];
            if (tdsVectorMediaConfig.getKeyConfig().equals(key)) {
                return tdsVectorMediaConfig.getValor();
            }
        }
        return "";
    }

    private void fillCboIdiomas() {
        LanguageDAO lanDao = new LanguageDAO();
        List<LanguageBO> listLan = lanDao.get(null);
        LanguageBO lanBO = new LanguageBO();
        /*Agregando valores por default al combo*/
        lanBO.setName("Sin Corrección");
        lanBO.setIdLanguage(-1);
        spellCheckVideo.addItem(lanBO);
        for (LanguageBO lanBO2 : listLan) {
            if ("es".equals(lanBO2.getNomenclature().toLowerCase().trim()) || "en".equals(lanBO2.getNomenclature().toLowerCase().trim())) {
                spellCheckVideo.addItem(lanBO2);
            }
        }
        spellCheckVideo.setSelectedIndex(1);
        activarIdioma();
    }

    private void activarIdioma() {
        int i = spellCheckVideo.getSelectedIndex();
        String language;
        switch (i) {
            case 1:
                language = "es";
                break;
            case 2:
                language = "en";
                break;
            default:
                try {
                    SpellChecker.register(titleVideo);
                    SpellChecker.register(subtitleVideo);
                    SpellChecker.register(sourceVideo);
                } catch (Exception ex) {
                    Utilerias.logger(getClass()).info(ex);
                }
                language = null;
                break;
        }

        if (language == null) {
            return;
        }

        try {
            // Create user dictionary in the current working directory of your application
            SpellChecker.setUserDictionaryProvider(new FileUserDictionary());
            // Load the configuration from the file dictionaries.cnf and 
            // use the current locale or the first language as default
            // You can download the dictionary files from 
            // http://sourceforge.net/projects/jortho/files/Dictionaries/                        
            URL is = new URL(Utilerias.rutaDiccionario());
            SpellChecker.registerDictionaries(is, language);
            // enable the spell checking on the text component with all features

            SpellChecker.register(titleVideo);
            SpellChecker.register(subtitleVideo);
            SpellChecker.register(sourceVideo);

            SpellCheckerOptions sco = new SpellCheckerOptions();
            sco.setCaseSensitive(true);
            sco.setSuggestionsLimitMenu(10);
            sco.setLanguageDisableVisible(false);
            sco.setIgnoreWordsWithNumbers(true);
            JPopupMenu popup = SpellChecker.createCheckerPopup(sco);

            titleVideo.addMouseListener(new PopupListener(popup));
            subtitleVideo.addMouseListener(new PopupListener(popup));
            sourceVideo.addMouseListener(new PopupListener(popup));

        } catch (NullPointerException | MalformedURLException ex) {
            Utilerias.logger(getClass()).info(ex);
        }
    }

    private void playYoutube(String url) {

        ReproducirVideo video = new ReproducirVideo(url);
        String urlparsed = video.parseVideo(url);

        id_youtube = urlparsed.replace("http://www.youtube.com/embed/", "");
        String thumbYoutube = "http://img.youtube.com/vi/" + id_youtube.trim() + "/0.jpg";
        setThumb(thumbYoutube);
        createScene(urlparsed);
    }

    private void setVideoModel(TdsVectorMediaBusqueda[] data, String imagenDefault) {
        SearchVideoModel videoModel = new SearchVideoModel(data, imagenDefault);
        gridVideo.setModel(videoModel);
        SearchTableCellRenderer stcr = new SearchTableCellRenderer();
        gridVideo.setDefaultRenderer(Object.class, stcr);
        fitColumnsVideos(gridVideo);
    }

    private void fitColumnsVideos(JTable table) {
        try {
            /* Ajustar columnas de la tabla */
            Utilerias.adjustJTableRowSizes(table);
            for (int i = 0; i < table.getColumnCount(); i++) {
                Utilerias.adjustColumnSizes(table, i, 4);
            }

            table.removeColumn(table.getColumnModel().getColumn(StatementConstant.SC_1.get()));
            table.removeColumn(table.getColumnModel().getColumn(table.getColumnModel().getColumnCount() - StatementConstant.SC_1.get()));
            table.removeColumn(table.getColumnModel().getColumn(table.getColumnModel().getColumnCount() - StatementConstant.SC_2.get()));
            //table.removeColumn(table.getColumnModel().getColumn(table.getColumnModel().getColumnCount() - StatementConstant.SC_1.get()));

        } catch (Exception e) {
            Utilerias.logger(getClass()).info(e);
        }
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonsPanel = new javax.swing.JPanel();
        acceptVideo = new javax.swing.JButton();
        cancelVideo = new javax.swing.JButton();
        splitPane = new javax.swing.JSplitPane();
        centerVideo = new javax.swing.JPanel();
        innerCenter = new javax.swing.JPanel();
        cboAutorVideo = new javax.swing.JComboBox();
        cboMarketVideo = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        inputSearchVideo = new javax.swing.JTextField();
        selectKalturaVideo = new javax.swing.JRadioButton();
        cboCategoryVideo = new javax.swing.JComboBox();
        cboContentType = new javax.swing.JComboBox();
        searchVideo = new javax.swing.JButton();
        panelVideo = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        gridVideo = new javax.swing.JTable();
        elemIniciales = new javax.swing.JButton();
        elemAnterior = new javax.swing.JButton();
        elemSiguiente = new javax.swing.JButton();
        elemFinales = new javax.swing.JButton();
        txtPage = new javax.swing.JTextField();
        searchVideosBar = new javax.swing.JProgressBar();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        innerSouth = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        spellCheckVideo = new javax.swing.JComboBox();
        selectURLVideo = new javax.swing.JRadioButton();
        urlVideo = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        subtitleVideo = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        sourceVideo = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        titleVideo = new javax.swing.JTextField();
        btnDisplay = new javax.swing.JButton();
        rightVideo = new javax.swing.JPanel();
        videoDisplay = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Insertar Video");
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        buttonsPanel.setName("buttonsPanel"); // NOI18N
        buttonsPanel.setPreferredSize(new java.awt.Dimension(574, 48));

        acceptVideo.setText("Aceptar");
        acceptVideo.setName("acceptVideo"); // NOI18N
        acceptVideo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptVideoActionPerformed(evt);
            }
        });
        buttonsPanel.add(acceptVideo);

        cancelVideo.setText("Cancelar");
        cancelVideo.setName("cancelVideo"); // NOI18N
        cancelVideo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelVideoActionPerformed(evt);
            }
        });
        buttonsPanel.add(cancelVideo);

        getContentPane().add(buttonsPanel, java.awt.BorderLayout.SOUTH);

        splitPane.setDividerLocation(750);
        splitPane.setName("splitPane"); // NOI18N
        splitPane.setPreferredSize(new java.awt.Dimension(1226, 620));

        centerVideo.setName("centerVideo"); // NOI18N
        centerVideo.setLayout(new java.awt.BorderLayout());

        innerCenter.setName("innerCenter"); // NOI18N

        cboAutorVideo.setName("cboAutorVideo"); // NOI18N

        cboMarketVideo.setName("cboMarketVideo"); // NOI18N

        jLabel2.setText("Búsqueda");
        jLabel2.setName("jLabel2"); // NOI18N

        inputSearchVideo.setName("inputSearchVideo"); // NOI18N
        inputSearchVideo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputSearchVideoActionPerformed(evt);
            }
        });

        buttonGroup1.add(selectKalturaVideo);
        selectKalturaVideo.setSelected(true);
        selectKalturaVideo.setText("Vector Media");
        selectKalturaVideo.setName("selectKalturaVideo"); // NOI18N
        selectKalturaVideo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                selectKalturaVideoItemStateChanged(evt);
            }
        });
        selectKalturaVideo.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                selectKalturaVideoStateChanged(evt);
            }
        });

        cboCategoryVideo.setName("cboCategoryVideo"); // NOI18N

        cboContentType.setName("cboContentType"); // NOI18N
        cboContentType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboContentTypeActionPerformed(evt);
            }
        });

        searchVideo.setText("Buscar");
        searchVideo.setName("searchVideo"); // NOI18N
        searchVideo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchVideoActionPerformed(evt);
            }
        });

        panelVideo.setName("panelVideo"); // NOI18N
        panelVideo.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        gridVideo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        gridVideo.setName("gridVideo"); // NOI18N
        gridVideo.setRowHeight(25);
        jScrollPane1.setViewportView(gridVideo);

        panelVideo.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        elemIniciales.setText("|<");
        elemIniciales.setEnabled(false);
        elemIniciales.setName("elemIniciales"); // NOI18N
        elemIniciales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elemInicialesActionPerformed(evt);
            }
        });

        elemAnterior.setText("<");
        elemAnterior.setEnabled(false);
        elemAnterior.setName("elemAnterior"); // NOI18N
        elemAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elemAnteriorActionPerformed(evt);
            }
        });

        elemSiguiente.setText(">");
        elemSiguiente.setEnabled(false);
        elemSiguiente.setName("elemSiguiente"); // NOI18N
        elemSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elemSiguienteActionPerformed(evt);
            }
        });

        elemFinales.setText(">|");
        elemFinales.setEnabled(false);
        elemFinales.setName("elemFinales"); // NOI18N
        elemFinales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elemFinalesActionPerformed(evt);
            }
        });

        txtPage.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPage.setText("1");
        txtPage.setEnabled(false);
        txtPage.setName("txtPage"); // NOI18N

        searchVideosBar.setIndeterminate(true);
        searchVideosBar.setName("searchVideosBar"); // NOI18N

        jLabel10.setText("Tipo");
        jLabel10.setName("jLabel10"); // NOI18N

        jLabel11.setText("Categoría");
        jLabel11.setName("jLabel11"); // NOI18N

        jLabel12.setText("Mercado");
        jLabel12.setName("jLabel12"); // NOI18N

        jLabel13.setText("Autor");
        jLabel13.setName("jLabel13"); // NOI18N

        javax.swing.GroupLayout innerCenterLayout = new javax.swing.GroupLayout(innerCenter);
        innerCenter.setLayout(innerCenterLayout);
        innerCenterLayout.setHorizontalGroup(
            innerCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(innerCenterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(innerCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelVideo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(innerCenterLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(inputSearchVideo, javax.swing.GroupLayout.DEFAULT_SIZE, 607, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchVideo))
                    .addGroup(innerCenterLayout.createSequentialGroup()
                        .addGroup(innerCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(innerCenterLayout.createSequentialGroup()
                                .addComponent(selectKalturaVideo)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, innerCenterLayout.createSequentialGroup()
                                .addGroup(innerCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboContentType, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(innerCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(innerCenterLayout.createSequentialGroup()
                                        .addComponent(cboCategoryVideo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                    .addGroup(innerCenterLayout.createSequentialGroup()
                                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(162, 162, 162)))
                                .addGroup(innerCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(innerCenterLayout.createSequentialGroup()
                                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(cboMarketVideo, 0, 214, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(innerCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboAutorVideo, 0, 157, Short.MAX_VALUE)
                            .addGroup(innerCenterLayout.createSequentialGroup()
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(innerCenterLayout.createSequentialGroup()
                        .addComponent(elemIniciales)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(elemAnterior)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPage, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(elemSiguiente)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(elemFinales)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(searchVideosBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        innerCenterLayout.setVerticalGroup(
            innerCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(innerCenterLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(selectKalturaVideo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(innerCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(innerCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboContentType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboCategoryVideo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboMarketVideo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboAutorVideo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(innerCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputSearchVideo, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(innerCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(searchVideo)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelVideo, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addGroup(innerCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(searchVideosBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(innerCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(elemIniciales)
                        .addComponent(elemAnterior)
                        .addComponent(elemSiguiente)
                        .addComponent(elemFinales)
                        .addComponent(txtPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        centerVideo.add(innerCenter, java.awt.BorderLayout.CENTER);

        innerSouth.setName("innerSouth"); // NOI18N

        jLabel5.setText("Liga");
        jLabel5.setName("jLabel5"); // NOI18N

        spellCheckVideo.setName("spellCheckVideo"); // NOI18N
        spellCheckVideo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                spellCheckVideoItemStateChanged(evt);
            }
        });

        buttonGroup1.add(selectURLVideo);
        selectURLVideo.setText("Liga de video Externo");
        selectURLVideo.setName("selectURLVideo"); // NOI18N
        selectURLVideo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                selectURLVideoItemStateChanged(evt);
            }
        });

        urlVideo.setEnabled(false);
        urlVideo.setName("urlVideo"); // NOI18N
        urlVideo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                urlVideoActionPerformed(evt);
            }
        });

        jLabel9.setText("Corrector Ortográfico");
        jLabel9.setName("jLabel9"); // NOI18N

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setName("jPanel2"); // NOI18N

        subtitleVideo.setName("subtitleVideo"); // NOI18N
        subtitleVideo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subtitleVideoActionPerformed(evt);
            }
        });

        jLabel8.setText("Fuente");
        jLabel8.setName("jLabel8"); // NOI18N

        jLabel7.setText("Subtitulo");
        jLabel7.setName("jLabel7"); // NOI18N

        sourceVideo.setName("sourceVideo"); // NOI18N
        sourceVideo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sourceVideoActionPerformed(evt);
            }
        });

        jLabel6.setText("Tiítulo");
        jLabel6.setName("jLabel6"); // NOI18N

        titleVideo.setName("titleVideo"); // NOI18N
        titleVideo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                titleVideoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(24, 24, 24)
                        .addComponent(titleVideo))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(subtitleVideo)
                            .addComponent(sourceVideo))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(titleVideo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(subtitleVideo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(sourceVideo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        btnDisplay.setText("Visualizar");
        btnDisplay.setEnabled(false);
        btnDisplay.setName("btnDisplay"); // NOI18N
        btnDisplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisplayActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout innerSouthLayout = new javax.swing.GroupLayout(innerSouth);
        innerSouth.setLayout(innerSouthLayout);
        innerSouthLayout.setHorizontalGroup(
            innerSouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(innerSouthLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(innerSouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(innerSouthLayout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(spellCheckVideo, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 486, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(innerSouthLayout.createSequentialGroup()
                        .addGroup(innerSouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(innerSouthLayout.createSequentialGroup()
                                .addComponent(selectURLVideo)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(innerSouthLayout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(32, 32, 32)
                                .addComponent(urlVideo)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDisplay)))
                .addContainerGap())
        );
        innerSouthLayout.setVerticalGroup(
            innerSouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(innerSouthLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(innerSouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(innerSouthLayout.createSequentialGroup()
                        .addComponent(selectURLVideo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(innerSouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(urlVideo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnDisplay, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(innerSouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(spellCheckVideo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        centerVideo.add(innerSouth, java.awt.BorderLayout.SOUTH);

        splitPane.setLeftComponent(centerVideo);

        rightVideo.setName("rightVideo"); // NOI18N

        videoDisplay.setBorder(new javax.swing.border.MatteBorder(null));
        videoDisplay.setName("videoDisplay"); // NOI18N
        videoDisplay.setLayout(new java.awt.BorderLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/video_.png"))); // NOI18N
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel1.setName("jLabel1"); // NOI18N
        videoDisplay.add(jLabel1, java.awt.BorderLayout.CENTER);

        jPanel1.setName("jPanel1"); // NOI18N

        jTextField2.setName("jTextField2"); // NOI18N
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jTextField4.setName("jTextField4"); // NOI18N
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jLabel3.setText("Fecha:");
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel4.setText("a:");
        jLabel4.setName("jLabel4"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout rightVideoLayout = new javax.swing.GroupLayout(rightVideo);
        rightVideo.setLayout(rightVideoLayout);
        rightVideoLayout.setHorizontalGroup(
            rightVideoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightVideoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rightVideoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(rightVideoLayout.createSequentialGroup()
                        .addComponent(videoDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        rightVideoLayout.setVerticalGroup(
            rightVideoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightVideoLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(videoDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)
                .addContainerGap())
        );

        splitPane.setRightComponent(rightVideo);

        getContentPane().add(splitPane, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void inputSearchVideoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputSearchVideoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inputSearchVideoActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void urlVideoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_urlVideoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_urlVideoActionPerformed

    private void titleVideoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_titleVideoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_titleVideoActionPerformed

    private void subtitleVideoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subtitleVideoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_subtitleVideoActionPerformed

    private void sourceVideoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sourceVideoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sourceVideoActionPerformed

    private void acceptVideoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptVideoActionPerformed

        if (selectKalturaVideo.isSelected()) {
            setId_video(idKaltura);
            setIdvideodb(idVideoRow);
            setTipoVideo("vectormedia");
            setHtmlVideo(html5Video);

        } else {
            setId_video(id_youtube);
            setTipoVideo("youtube");
            playYoutube(urlVideo.getText());
            setHtmlVideo(htmlVideo);
        }

        boolean ban = true;
        StringBuffer warnings = new StringBuffer();
        setTituloVideo(titleVideo.getText());
        setSubtituloVideo(subtitleVideo.getText());
        setFuenteVideo(sourceVideo.getText());
        setThumb(thumb);

        if (titleVideo.getText().isEmpty()) {
            ban = false;
            warnings.append("\nTitulo requerido");
        } else if (titleVideo.getText().length() > 100) {
            ban = false;
            warnings.append("\nTitulo menor a 100 caracteres");
        }

        if (subtitleVideo.getText().isEmpty() == true) {
            if (subtitleVideo.getText().length() > 100) {
                ban = false;
                warnings.append("\nSubtitulo menor a 100 caracteres");
            }
        }

        if (sourceVideo.getText().isEmpty()) {
            ban = false;
            warnings.append("\nFuente requerido");
        } else if (sourceVideo.getText().length() > 300) {
            ban = false;
            warnings.append("\nFuente menor a 300 caracteres");
        }

        // seleccionado kaltura videos
        if (selectKalturaVideo.isSelected()) {
            if (idKaltura == null) {
                ban = false;
                warnings.append("\nDebe Seleccionar un video para continuar.");
            }
        } else {
            // seleccionado videos externos (youtube)
            if (id_youtube == null) {
                ban = false;
                warnings.append("\nDebe Seleccionar un video para continuar.");
            }

        }

        if (ban) {
            stopVideo();
            ok = true;
            if (proxy) {
                System.setProperty("http.proxySet", "false");
                System.setProperty("http.proxyHost", "");
                System.setProperty("http.proxyPort", "");
            }
            dispose();
        } else {
            Utilerias.showMessage(null, "Campos Requeridos:" + warnings, JOptionPane.WARNING_MESSAGE);
        }
        
        

    }//GEN-LAST:event_acceptVideoActionPerformed

    private void selectAction(){
        SearchVideoModel model = (SearchVideoModel) gridVideo.getModel();

                        if (model == null || model.getData() == null) {
                            return;
                        }
                        int row = gridVideo.getSelectedRow();
                        TdsVectorMediaBusqueda obj = model.getData()[row];
                        idKaltura = obj.getKalturaId();
                        idVideoRow = obj.getRowiddoc();
                        System.out.println(idVideoRow);
                        //thumb = obj.getThumbnail();
                        thumb = obj.getThumbnail();
                        setThumb(thumb);
                        loadVideo(idKaltura);
    }
    
    private void cancelVideoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelVideoActionPerformed
        stopVideo();
        ok = false;
        if (proxy) {
            System.setProperty("http.proxySet", "false");
            System.setProperty("http.proxyHost", "");
            System.setProperty("http.proxyPort", "");
        }
        dispose();
    }//GEN-LAST:event_cancelVideoActionPerformed

    private void selectKalturaVideoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_selectKalturaVideoStateChanged
    }//GEN-LAST:event_selectKalturaVideoStateChanged

    private void selectKalturaVideoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_selectKalturaVideoItemStateChanged
        stopVideo();
        if (selectKalturaVideo.isSelected()) {
            enableKalturaFields(true);
        } else {
            enableKalturaFields(false);
        }
    }//GEN-LAST:event_selectKalturaVideoItemStateChanged

    private void selectURLVideoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_selectURLVideoItemStateChanged
        if (selectURLVideo.isSelected()) {
            enableURLFields(true);

        } else {
            enableURLFields(false);
        }
    }//GEN-LAST:event_selectURLVideoItemStateChanged

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        if (objInfo.getTipoVideo() != null && objInfo.getTipoVideo().equals("youtube")) {
            selectURLVideo.setSelected(true);
        }
        //contentType();
        if (proxy) {
            System.setProperty("http.proxySet", "false");
            System.setProperty("http.proxyHost", "");
            System.setProperty("http.proxyPort", "");
        }
        loadCombosWorker combosWorker = new loadCombosWorker();
        searchVideosBar.setIndeterminate(true);
        searchVideosBar.setVisible(true);
        combosWorker.execute();
    }//GEN-LAST:event_formComponentShown

    private void btnDisplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisplayActionPerformed

        playYoutube(urlVideo.getText());

    }//GEN-LAST:event_btnDisplayActionPerformed

    private void searchVideoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchVideoActionPerformed
        searchVideos(false, 1, MAX_ROWS);
        elemAnterior.setEnabled(true);
        elemSiguiente.setEnabled(true);
        elemFinales.setEnabled(true);
        elemIniciales.setEnabled(true);
    }//GEN-LAST:event_searchVideoActionPerformed

    private void cboContentTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboContentTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboContentTypeActionPerformed

    private void spellCheckVideoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_spellCheckVideoItemStateChanged
        activarIdioma();
    }//GEN-LAST:event_spellCheckVideoItemStateChanged

    private void elemSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elemSiguienteActionPerformed

        if (CURRENT_PAGE < NUM_PAGS) {
            int cur = CURRENT_PAGE + 1;
            CURRENT_PAGE = cur;
            searchVideos(false, CURRENT_PAGE, MAX_ROWS);
        }

    }//GEN-LAST:event_elemSiguienteActionPerformed

    private void elemAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elemAnteriorActionPerformed

        if (CURRENT_PAGE > 1) {
            int cur = CURRENT_PAGE - 1;
            CURRENT_PAGE = cur;
            searchVideos(false, CURRENT_PAGE, MAX_ROWS);
        }

    }//GEN-LAST:event_elemAnteriorActionPerformed

    private void elemInicialesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elemInicialesActionPerformed

        searchVideos(false, 1, MAX_ROWS);
        CURRENT_PAGE = 1;

    }//GEN-LAST:event_elemInicialesActionPerformed

    private void elemFinalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elemFinalesActionPerformed

        searchVideos(false, NUM_PAGS, MAX_ROWS);
        CURRENT_PAGE = NUM_PAGS;


    }//GEN-LAST:event_elemFinalesActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acceptVideo;
    private javax.swing.JButton btnDisplay;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JButton cancelVideo;
    private javax.swing.JComboBox cboAutorVideo;
    private javax.swing.JComboBox cboCategoryVideo;
    private javax.swing.JComboBox cboContentType;
    private javax.swing.JComboBox cboMarketVideo;
    private javax.swing.JPanel centerVideo;
    private javax.swing.JButton elemAnterior;
    private javax.swing.JButton elemFinales;
    private javax.swing.JButton elemIniciales;
    private javax.swing.JButton elemSiguiente;
    private javax.swing.JTable gridVideo;
    private javax.swing.JPanel innerCenter;
    private javax.swing.JPanel innerSouth;
    private javax.swing.JTextField inputSearchVideo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JPanel panelVideo;
    private javax.swing.JPanel rightVideo;
    private javax.swing.JButton searchVideo;
    private javax.swing.JProgressBar searchVideosBar;
    private javax.swing.JRadioButton selectKalturaVideo;
    private javax.swing.JRadioButton selectURLVideo;
    private javax.swing.JTextField sourceVideo;
    private javax.swing.JComboBox spellCheckVideo;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JTextField subtitleVideo;
    private javax.swing.JTextField titleVideo;
    private javax.swing.JTextField txtPage;
    private javax.swing.JTextField urlVideo;
    private javax.swing.JPanel videoDisplay;
    // End of variables declaration//GEN-END:variables

    class SearchVideosWorker extends SwingWorker<TdsVectorMediaBusqueda[], TdsVectorMediaBusqueda[]> {

        int cat;
        int marketid;
        int autor;
        String type;
        String contenido;
        String fechaInicial;
        String fechaFinal;
        int page;
        int maxRows;
        boolean checkInicial;
        boolean isActive;
        TdsVectorMediaBusqueda[] arr;
        String idKaltura;

        SearchVideosWorker(int cat, int marketid, int autor, String contenido, String fechaInicial, String fechaFinal, int page, int maxRows, String type, Boolean checkInicial, Boolean isActive) {
            this.cat = cat;
            this.marketid = marketid;
            this.autor = autor;
            this.contenido = contenido;
            this.fechaInicial = fechaInicial;
            this.fechaFinal = fechaFinal;
            this.page = page;
            this.maxRows = maxRows;
            this.type = type;
            this.checkInicial = checkInicial;
            this.isActive = isActive;

        }

        SearchVideosWorker(Boolean isActive, TdsVectorMediaBusqueda[] arr) {
            this.arr = arr;
            this.isActive = true;
        }

        SearchVideosWorker(String idKaltura, boolean checkInicial) {
            this.idKaltura = idKaltura.trim();
            this.checkInicial = checkInicial;

            this.cat = -1;
            this.marketid = -1;
            this.autor = -1;
            this.contenido = "";
            this.fechaInicial = "";
            this.fechaFinal = "";
            this.page = 0;
            this.maxRows = 1;
            this.type = "";
            this.isActive = false;
        }

        @Override
        protected TdsVectorMediaBusqueda[] doInBackground() {

            IVectorMedia_Stub stub2 = (IVectorMedia_Stub) new VectorMedia_Impl().getBasicHttpBinding_IVectorMedia();
            UtileriasWS.setEndpoint(stub2);
            VectorMediaBusqueda bus = null;
            TdsVectorMediaBusqueda[] array = null;

            if (isActive) {

                array = arr;

            } else {
                
                try {
                    bus = stub2.buscar(
                            false,
                            idKaltura,
                            type,
                            cat,
                            marketid,
                            autor,
                            contenido,
                            fechaInicial,
                            fechaFinal,
                            page,
                            maxRows,
                            "",
                            "");
                } catch (RemoteException ex) {
                    JOptionPane.showMessageDialog(null, "El servicio de busqueda no esta disponible.", "Inane error", JOptionPane.ERROR_MESSAGE);
                    Utilerias.logger(getClass()).info(ex);
                }
                  
                if (bus.getDsVectorMediaBusqueda() != null
                        && bus.getDsVectorMediaBusqueda().getTdsVectorMediaBusqueda() != null
                        && bus.getDsVectorMediaBusqueda().getTdsVectorMediaBusqueda().getTdsVectorMediaBusqueda() != null) {

                    array = bus.getDsVectorMediaBusqueda().getTdsVectorMediaBusqueda().getTdsVectorMediaBusqueda();
                    vectorConfig = bus.getDsVectorMediaBusqueda().getTdsVectorMediaConfig().getTdsVectorMediaConfig();

                    /*Test num paginas*/
                    NUM_PAGS = Integer.valueOf(getConfig("TotalPaginas"));
                }
            }
            return array;
        }

        @Override
        protected void done() {
            TdsVectorMediaBusqueda[] array;
            try {
                array = get();

                if (array != null) {

                    if (this.checkInicial) {

                        setVideoModel(array, getConfig("DefaultThumbnail"));
                        txtPage.setText(String.valueOf(CURRENT_PAGE));
                        gridVideo.setRowSelectionInterval(0, 0);
                        selectAction();
                       
                    } else {
                        if (!isActive) {
                            lstETot = array;
                            array = getElementosIniciales();
                        }
                        setVideoModel(array, getConfig("DefaultThumbnail"));
                        txtPage.setText(String.valueOf(CURRENT_PAGE));
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Servicio no disponible");
                }

            } catch (InterruptedException | ExecutionException ex) {
                Utilerias.logger(getClass()).error(ex);
            } finally {
                searchVideosBar.setVisible(false);
            }
        }

    }

    class loadCombosWorker extends SwingWorker<IVectorMedia_Stub, IVectorMedia_Stub> {

        @Override
        protected IVectorMedia_Stub doInBackground() throws Exception {
            IVectorMedia_Stub stub = null;

            try {

                IVectorMedia_Stub stub2 = (IVectorMedia_Stub) new VectorMedia_Impl().getBasicHttpBinding_IVectorMedia();
                if (stub2 != null) {
                    UtileriasWS.setEndpoint(stub2);
                    stub = stub2;
                }

            } catch (Exception e) {

                Utilerias.logger(getClass()).info(e);

            }

            IVectorMedia_Stub stub2 = (IVectorMedia_Stub) new VectorMedia_Impl().getBasicHttpBinding_IVectorMedia();
            UtileriasWS.setEndpoint(stub2);

            return stub;
        }

        @Override
        protected void done() {
            try {
                IVectorMedia_Stub stub2 = get();

                /*Tipo*/
                ArrayOftdsTipoMultimedia array = stub2.getVectorMediaCatalogos().getDsVectorMedia().getTdsTipoMultimedia();
                if (array != null && array.getTdsTipoMultimedia() != null) {

                    cboContentType.removeAllItems();

                    for (com.adinfi.ws.vectormedia.TdsTipoMultimedia t : array.getTdsTipoMultimedia()) {
                        com.adinfi.formateador.bos.ContentTypeBO bo = new com.adinfi.formateador.bos.ContentTypeBO();
                        bo.setDescripcion(t.getDescripcion());
                        bo.setTipoMultimediaId(t.getTipoMultimediaId());
                        cboContentType.addItem(bo);
                    }
                }

                /*Categoria*/
                ArrayOftdsCategoriaVideo cat = stub2.getVectorMediaCatalogos().getDsVectorMedia().getTdsCategoriaVideo();

                if (cat != null && cat.getTdsCategoriaVideo() != null) {
                    cboCategoryVideo.removeAllItems();

                    for (com.adinfi.ws.vectormedia.TdsCategoriaVideo c : cat.getTdsCategoriaVideo()) {
                        com.adinfi.formateador.bos.CategoriaVideoBO bo2 = new com.adinfi.formateador.bos.CategoriaVideoBO();
                        bo2.setCategoriaVideo(c.getCategoriaVideo());
                        bo2.setDescripcion(c.getDescripcion());
                        cboCategoryVideo.addItem(bo2);
                    }

                }

                /* Mercado */
                ArrayOftdsTipoAnalisis mkt = stub2.getVectorMediaCatalogos().getDsVectorMedia().getTdsTipoAnalisis();
                if (mkt != null && mkt.getTdsTipoAnalisis() != null) {

                    cboMarketVideo.removeAllItems();

                    for (TdsTipoAnalisis m : mkt.getTdsTipoAnalisis()) {
                        com.adinfi.formateador.bos.TipoAnalisisMarketBO bo3 = new com.adinfi.formateador.bos.TipoAnalisisMarketBO();
                        bo3.setCategoriaVideo(m.getIdCategoria());
                        bo3.setDescripcion(m.getDescripcion());
                        cboMarketVideo.addItem(bo3);
                    }

                }

                /* Autores */
                ArrayOftdsAutores autores = stub2.getVectorMediaCatalogos().getDsVectorMedia().getTdsAutores();
                if (autores != null && autores.getTdsAutores() != null) {

                    cboAutorVideo.removeAllItems();
//
                    for (TdsAutores a : autores.getTdsAutores()) {
                        com.adinfi.formateador.bos.AutoresVideoBO bo4 = new com.adinfi.formateador.bos.AutoresVideoBO();
                        bo4.setIdAutor(a.getIdAutor());
                        bo4.setNombre(a.getNombre());
                        cboAutorVideo.addItem(bo4);
                    }

                }

            } catch (InterruptedException | RemoteException | ExecutionException e) {
                Utilerias.logger(getClass()).info(e);

            } finally {
                searchVideosBar.setVisible(false);
                if (isSaved) {
                    searchVideos(false, 0, MAX_ROWS);
                    CURRENT_PAGE = 1;
                    elemAnterior.setEnabled(true);
                    elemSiguiente.setEnabled(true);
                    elemFinales.setEnabled(true);
                    elemIniciales.setEnabled(true);
                }
            }

        }

    }

}
