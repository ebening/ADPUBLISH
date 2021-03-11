package com.adinfi.formateador.view;

/**
 *
 * @author Cristian Zamancona
 */
public class ReproducirVideo {

    private final String DOMINIO_YOUTUBE_1 = "youtube.com";
    private final String DOMINIO_YOUTUBE_2 = "youtu.be";
    private final String DOMINIO_VIMEO = "vimeo.com/";
    private final String ID_YOUTUBE_1 = "http://www.youtube.com/watch?v=";
    private final String ID_YOUTUBE_2 = "http://youtu.be/";
    private final String ID_VIMEO = "http://vimeo.com/";
    private String EMBEB_URL_YOUTUBE = "http://www.youtube.com/embed/";
    private String EMBEB_URL_VIMEO = "http://player.vimeo.com/video/";
    private String url;

  

    public ReproducirVideo(String url) {
        this.url = url;
    }

    private String generateYoutubeEmbebUrlVideo1(String url) {
        url = url.replace("https://", "http://");
        return EMBEB_URL_YOUTUBE + url.substring(ID_YOUTUBE_1.length());
    }

    private String generateYoutubeEmbebUrlVideo2(String url) {
        return EMBEB_URL_YOUTUBE + url.substring(ID_YOUTUBE_2.length());
    }

    private String generateVimeoEmbebUrlVideo(String url) {
        return EMBEB_URL_VIMEO + url.substring(ID_VIMEO.length());
    }

    public String parseVideo(String urlVideo) {

        if (url.contains(DOMINIO_YOUTUBE_1)) {

            return generateYoutubeEmbebUrlVideo1(urlVideo);

        } else if (url.contains(DOMINIO_YOUTUBE_2)) {

            return generateYoutubeEmbebUrlVideo2(urlVideo);

        } else if (url.contains(DOMINIO_VIMEO)) {

            return generateVimeoEmbebUrlVideo(urlVideo);
        } else {
            return "not found";
        }

    }

}
