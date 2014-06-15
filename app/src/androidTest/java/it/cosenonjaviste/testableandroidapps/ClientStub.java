package it.cosenonjaviste.testableandroidapps;

import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import retrofit.client.Client;
import retrofit.client.Header;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedString;

public class ClientStub implements Client {

    private Resources resources;
    private Map<String, int[]> urlMapping;

    public ClientStub(Resources resources, Map<String, int[]> urlMapping) {
        this.resources = resources;
        this.urlMapping = urlMapping;
    }

    @Override public Response execute(Request request) throws IOException {
        String url = request.getUrl();
        int resourceId = getResourceId(url);
        StringBuilder b = new StringBuilder();
        InputStream resourceAsStream = resources.openRawResource(resourceId);
        BufferedReader reader = new BufferedReader(new InputStreamReader(resourceAsStream));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                b.append(line);
            }
        } finally {
            try {
                reader.close();
            } catch (IOException ignored) {
            }
        }
        return new Response(url, 200, "reason", new ArrayList<Header>(), new TypedString(b.toString()));
    }

    private int getResourceId(String url) {
        for (Map.Entry<String, int[]> entry : urlMapping.entrySet()) {
            String regExpr = entry.getKey();
            if (url.equals(regExpr) || url.matches(regExpr)) {
                int[] value = entry.getValue();
                return value[0];
            }
        }
        return 0;
    }
}
