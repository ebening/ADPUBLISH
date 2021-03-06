
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author vectoran
 */
public class TestProxy {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        System.setProperty("java.net.useSystemProxies", "true");
        System.out.println("detecting proxies");
        List l = null;
        try {
            l = ProxySelector.getDefault().select(new URI("http://vecmtycgw10.vector.local:8080/"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (l != null) {
            for (Iterator iter = l.iterator(); iter.hasNext();) {
                java.net.Proxy proxy = (java.net.Proxy) iter.next();
                System.out.println("proxy hostname : " + proxy.type());

                InetSocketAddress addr = (InetSocketAddress) proxy.address();

                if (addr == null) {
                    System.out.println("No Proxy");
                } else {
                    System.out.println("proxy hostname : " + addr.getHostName());
                    System.setProperty("http.proxyHost", addr.getHostName());
                    System.out.println("proxy port : " + addr.getPort());
                    System.setProperty("http.proxyPort", Integer.toString(addr.getPort()));
                }
            }
        }

    }

}
