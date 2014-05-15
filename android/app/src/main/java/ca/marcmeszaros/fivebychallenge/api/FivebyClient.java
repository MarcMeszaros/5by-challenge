package ca.marcmeszaros.fivebychallenge.api;

import ca.marcmeszaros.fivebychallenge.BuildConfig;
import ca.marcmeszaros.fivebychallenge.api.v1.Client;

public class FivebyClient extends Client {

    private static FivebyClient instance = null;
    private static Object instanceMutex = new Object();

    /**
     * Build a new instance of the SnapClient
     *
     * @param baseUrl the base API url
     */
    public FivebyClient(String baseUrl) {
        super(baseUrl);
    }

    /**
     * Get an instance of {@link ca.marcmeszaros.fivebychallenge.api.FivebyClient}. It will also take care
     * of point to the dev API in debug builds and the production API for release builds.
     *
     * @return An instance of {@link ca.marcmeszaros.fivebychallenge.api.FivebyClient}.
     */
    public static FivebyClient getInstance() {
        if (instance == null) {
            synchronized (instanceMutex) {
                if (BuildConfig.DEBUG) {
                    instance = new FivebyClient(Client.BASE_URL_DEV);
                } else {
                    instance = new FivebyClient(Client.BASE_URL);
                }
            }
        }
        return instance;

    }
}
