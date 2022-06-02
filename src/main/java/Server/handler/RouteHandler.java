package Server.handler;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPOutputStream;

import Server.constant.ServerConstant;
import Server.enums.HttpMethod;
import Server.utils.ServerUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import org.json.JSONObject;

import Logger.AppLogger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public abstract class RouteHandler implements HttpHandler {
    private final AppLogger logger = AppLogger.getInstance();

    private final String pathToRoot;
    private final boolean gzippable;
    private final boolean casheable;
    private final Map<String, Resource> resources = new HashMap<>();
    private Handler404 handler404;
    
    public RouteHandler() throws IOException {
        this.pathToRoot = ServerConstant.getPathToRoot();

        this.gzippable = isGzippable();

        this.casheable = isCasheable();

        File[] files =  new File(pathToRoot).listFiles();

        if (files == null) {
            throw new IOException("No files found in " + pathToRoot);
        }

        for (File f : files) {
            processFile("", f, gzippable);
        }

        handler404 = new Handler404(gzippable, casheable);
    }

    /**
     * Uses the HttpExchange to get the parameters from the request.
     * @param he - The http exchange to get the parameters from.
     * @return - A map of the parameters.
     * @throws IOException
     */
    public JSONObject getParamaters(HttpExchange he) {
        try {
            InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");

            BufferedReader br = new BufferedReader(isr);
            String query = br.readLine();
            
            return parseQuery(query);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return new JSONObject("data: {}");
    }

    public AppLogger getLogger() {
        return logger;
    }
    
    private static JSONObject parseQuery(String query) throws UnsupportedEncodingException {
        if (query != null) {
            return new JSONObject(" { data : " + query + " }");
        }else {
            return new JSONObject("{ data: {} }");
        }
    }

    public void sendJSONResponse(HttpExchange he, JSONObject json) throws IOException {
        he.getResponseHeaders().set(ServerConstant.CONTENT_TYPE, ServerConstant.APPLICATION_JSON);
        he.sendResponseHeaders(200, json.toString().length());

        OutputStream os = he.getResponseBody();
        os.write(json.toString().getBytes());
        os.close();
    }

    public void sendStringResponse(HttpExchange he, JSONObject json) throws IOException {
        he.getResponseHeaders().set(ServerConstant.CONTENT_TYPE, ServerConstant.TEXT_PLAIN);
        he.sendResponseHeaders(200, json.toString().length());

        OutputStream os = he.getResponseBody();
        os.write(json.toString().getBytes());
        os.close();
    }

    public void sendStringResponse(HttpExchange he, String response) throws IOException {
        he.getResponseHeaders().set(ServerConstant.CONTENT_TYPE, ServerConstant.TEXT_PLAIN);
        he.sendResponseHeaders(200, response.length());

        OutputStream os = he.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    @Override
    public void handle(HttpExchange he) throws IOException {
        if (HttpMethod.HEAD.getName().equals(he.getRequestMethod())) {
            HeadHandler(he);
        }else if (HttpMethod.GET.getName().equals(he.getRequestMethod())) {
            GetHandler(he);
        }else if (HttpMethod.POST.getName().equals(he.getRequestMethod())) {
            PostHandler(he);
        }else if (HttpMethod.PUT.getName().equals(he.getRequestMethod())) {
            PutHandler(he);
        }else if (HttpMethod.DELETE.getName().equals(he.getRequestMethod())) {
            DeleteHandler(he);
        }
    }

    public void PostHandler(HttpExchange he) throws IOException {
        handler404.server404(he, ServerConstant.Error404File, resources);
    }

    public void GetHandler(HttpExchange he) throws IOException {
        handler404.server404(he, ServerConstant.Error404File, resources);
    }

    public void PutHandler(HttpExchange he) throws IOException {
        handler404.server404(he, ServerConstant.Error404File, resources);
    }

    public void DeleteHandler(HttpExchange he) throws IOException {
        handler404.server404(he, ServerConstant.Error404File, resources);
    }

    public abstract boolean isGzippable();

    public abstract boolean isCasheable();

    public abstract String getRoute();

    public void HeadHandler(HttpExchange he) throws IOException {
        Set<Map.Entry<String, List<String>>> entries = he.getRequestHeaders().entrySet();

        String response = "";

        for (Map.Entry<String, List<String>> entry : entries) {
            response += entry.toString() + "\n";
        }

        he.getResponseHeaders().set(ServerConstant.CONTENT_TYPE, ServerConstant.TEXT_PLAIN);
        he.sendResponseHeaders(200, response.length());
        he.getResponseBody().write(response.getBytes());
        he.getResponseBody().close();
    }

    public void processFile(String path, File file, boolean gzippable) throws IOException {
        if (!file.isDirectory()) {
            resources.put(path + file.getName(), new Resource(readResource(new FileInputStream(file), gzippable)));
        }

        if (file.isDirectory()) {
            for (File sub : file.listFiles()) {
                processFile(path + file.getName() + ServerConstant.FORWARD_SINGLE_SLASH, sub, gzippable);
            }
        }
    }

    public static byte[] readResource(final InputStream in, final boolean gzip) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        OutputStream gout = gzip ? new GZIPOutputStream(bout) : new DataOutputStream(bout);

        byte[] bs = new byte[4096];
        int r;

        while ( (r = in.read(bs) ) >= 0 ) 
            gout.write(bs, 0, r);

        gout.flush();

        gout.close();

        in.close();

        return bout.toByteArray();
    }

    public void serverResource(HttpExchange he, String requestPath) throws IOException {
        //requestPath = requestPath.substring(1);

        requestPath = requestPath.replaceAll(ServerConstant.FORWARD_DOUBLE_SLASH, ServerConstant.FORWARD_SINGLE_SLASH);

        if (requestPath.length() == 0) {
            requestPath = ServerConstant.homeFileName;
        }

        serveFile(he, pathToRoot + requestPath);
    }

    public void serveFile(HttpExchange he, String resourcePath) throws IOException {
        File file = new File(resourcePath);

        if (file.exists()) {
            InputStream in = new FileInputStream(resourcePath);

            Resource re = null;

            if (casheable) {
                if (resources.get(resourcePath) == null) {
                    re = new Resource(readResource(in, gzippable));
                } else {
                    re =  resources.get(resourcePath);
                }
            } else {
                re = new Resource(readResource(in, gzippable));
            }

            if (gzippable) {
                he.getResponseHeaders().set(ServerConstant.CONTENT_ENCODING, ServerConstant.ENCODING_GZIP);
            }

            String mimeType = ServerUtil.getFileMime(resourcePath);

            RouteHandler.writeOutput(he, re.content.length, re.content, mimeType);
        } else {
            // Send a 404 response if possible... if not do default 404 message
            handler404.server404(he, ServerConstant.Error404File, resources);
        }
    }

    public static void writeOutput(HttpExchange he, int length, byte[] content, String mimeType) throws IOException {
        he.getResponseHeaders().set(ServerConstant.CONTENT_TYPE, mimeType);
        he.sendResponseHeaders(200, length);
        he.getResponseBody().write(content);
        he.getResponseBody().close();
    }
}
