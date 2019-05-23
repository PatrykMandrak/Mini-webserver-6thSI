import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Test {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8900), 0);
        server.createContext("/test1", new MyHandler());
        server.createContext("/test2", new MyHandler());
        server.setExecutor(null);
        server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            Routes routes = new Routes();

            for (Method m : Routes.class.getMethods()) {
                if (m.isAnnotationPresent(WebRoute.class)) {
                    WebRoute webRouteValue = m.getAnnotation(WebRoute.class);

                    if (t.getRequestURI().toString().equals(webRouteValue.value())) {
                        try {
                            String response = (String) m.invoke(routes);

                            t.sendResponseHeaders(200, response.length());
                            OutputStream os = t.getResponseBody();
                            os.write(response.getBytes());

                            os.close();
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

}