package ca.marcmeszaros.fivebychallenge.api.v1.objects;

import com.google.common.base.Objects;

/**
 * Represents the JSON structure of the response. Not all the fields are mapped,
 * only the ones we care about for this project.
 */
public class Video {

    public Media media;

    public String toString() {
        return Objects.toStringHelper(this)
                .add("media", this.media)
                .toString();
    }

    public class Media {

        public Oembed oembed;

        public String toString() {
            return Objects.toStringHelper(this)
                    .add("oembed", this.oembed)
                    .toString();
        }

        public class Oembed {
            public String title;
            public String description;
            public String thumbnail_url;
            public String url;

            public String toString() {
                return Objects.toStringHelper(this)
                        .add("title", this.title)
                        .add("description", this.description)
                        .add("thumbnail_url", this.thumbnail_url)
                        .add("url", this.url)
                        .toString();
            }
        }
    }

}
