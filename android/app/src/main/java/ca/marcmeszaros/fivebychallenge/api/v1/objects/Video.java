package ca.marcmeszaros.fivebychallenge.api.v1.objects;

import com.google.common.base.Objects;

import java.util.Date;

public class Video {

    public String author_name;
    public String caption;
    public Date created_at;
    public String title;

    public String toString() {
        return Objects.toStringHelper(this)
                .add("author_name", this.author_name)
                .add("caption", this.caption)
                .add("created_at", this.created_at)
                .toString();
    }

}
