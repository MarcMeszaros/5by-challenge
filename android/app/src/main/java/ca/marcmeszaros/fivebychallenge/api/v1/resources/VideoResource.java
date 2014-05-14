package ca.marcmeszaros.fivebychallenge.api.v1.resources;

import ca.marcmeszaros.fivebychallenge.api.v1.objects.*;
import retrofit.http.*;

public interface VideoResource {

    public static final String RESOURCE_NAME = "videos";

    @GET("/"+RESOURCE_NAME+"/")
    Pager<Video> getVideos();

    @GET("/"+RESOURCE_NAME+"/{id}/")
    Video getVideo(
            @Path("id") String id
    );

    @DELETE("/"+RESOURCE_NAME+"/{id}/")
    void deleteVideo(
            @Path("id") String id
    );

}
