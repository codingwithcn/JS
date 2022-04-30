package Server.handler;

import Server.constant.ServerConstant;
import Server.utils.ServerUtil;

import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

public class Handler404 {
    public final boolean gzippable, casheable;

    public Handler404(boolean gzippable, boolean casheable) {
        this.gzippable = gzippable;
        this.casheable = casheable;
    }

    public void server404(HttpExchange httpExchange, String resourcePath, Map<String, Resource> resources) throws IOException {
        File file = new File(resourcePath);

        if (file.exists()) {
            InputStream in = new FileInputStream(resourcePath);

            Resource re = null;

            if (casheable) {
                if (resources.get(resourcePath) == null) {
                    re = new Resource(RouteHandler.readResource(in, gzippable));
                } else {
                    re = resources.get(resourcePath);
                }
            } else {
                re = new Resource(RouteHandler.readResource(in, gzippable));
            }

            if (gzippable) {
                httpExchange.getResponseHeaders().set(ServerConstant.CONTENT_ENCODING,
                        ServerConstant.ENCODING_GZIP);
            }

            String mimeType = ServerUtil.getFileMime(resourcePath);

            RouteHandler.writeOutput(httpExchange, re.content.length, re.content, mimeType);
        } else {
            showError(httpExchange, 404, ServerConstant.Error404FileMessage);
        }
    }

    private void showError(HttpExchange httpExchange, int respCode, String errDesc) throws IOException {
        String message = "HTTP error " + respCode + ": " + errDesc;
        byte[] messageBytes = message.getBytes(ServerConstant.ENCODING_UTF8);

        httpExchange.getResponseHeaders().set(ServerConstant.CONTENT_TYPE, ServerConstant.TEXT_PLAIN);
        httpExchange.sendResponseHeaders(respCode, messageBytes.length);

        OutputStream os = httpExchange.getResponseBody();
        os.write(messageBytes);
        os.close();
    }
}
