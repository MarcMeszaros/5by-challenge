package ca.marcmeszaros.fivebychallenge.api;

import retrofit.RequestInterceptor;

public class BaseInterceptor implements RequestInterceptor {

    @Override
    public void intercept(RequestFacade request) {
        request.addHeader("User-Agent", "5by-challenge-java/0.0.1"); // it's nice to let the server know...
        request.addHeader("Accept", "application/json"); // the API only returns JSON, but let's be explicit
    }

}
