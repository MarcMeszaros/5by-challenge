package ca.marcmeszaros.fivebychallenge.api.v1;

import ca.marcmeszaros.fivebychallenge.api.BaseClient;
import retrofit.RestAdapter;
import retrofit.client.*;

public class Client extends BaseClient {

    public static final String BASE_URL = "http://localhost:3000/api/";
    public static final String BASE_URL_DEV = "http://192.168.1.155:3000/api/";

    public Client() {
        super(BASE_URL);
    }

    public Client(String baseUrl) {
        super(baseUrl);
    }

    public RestAdapter getRestAdapter() {
        return createRestAdapterBuilder().build();
    }

    @Override
    protected Request sign(Request request) {
        // if we had any kind or request signing (ie. HMAC sign the request)
        // this is where the value would be computed and appended to the request headers
        // in this case we don't sign/modify the outgoing request
        return request;
    }

}
