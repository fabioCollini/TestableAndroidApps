package it.cosenonjaviste.testableandroidapps;

import android.content.res.Resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import retrofit.client.Header;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedString;

public class ClientErrorStub extends ClientStub {

    private boolean firstTime = true;

    public ClientErrorStub(Resources resources, Map<String, int[]> urlMapping) {
        super(resources, urlMapping);
    }

    @Override public Response execute(Request request) throws IOException {
        if (firstTime) {
            firstTime = false;
            return new Response(request.getUrl(), 400, "reason", new ArrayList<Header>(), new TypedString(""));
        }
        return super.execute(request);
    }
}
