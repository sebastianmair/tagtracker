package mair.zsi.at.tagtracker.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by sebastian on 21.10.17.
 */

@DatabaseTable(tableName = "tag_track")
public class TagTrack {

    @DatabaseField (foreign = true)
    private Track track;
    @DatabaseField (foreign = true)
    private Tag tag;

    public TagTrack() {
        this.tag = new Tag();
        this.track = new Track();
    }

    public TagTrack(double lon, double lat, Date date, Tag tag) {
        this.tag = tag;
        this.track = new Track(lon, lat, date);
    }

    public TagTrack(Tag tag, Track track) {
        this.tag = tag;
        this.track = track;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }
}
