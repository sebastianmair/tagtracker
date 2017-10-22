package mair.zsi.at.tagtracker.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by sebastian on 21.10.17.
 */

@DatabaseTable(tableName = "track")
public class Track {
    @DatabaseField (generatedId = true)
    private int id;
    @DatabaseField
    private double lon;
    @DatabaseField
    private double lat;
    @DatabaseField
    private Date date;

    public Track() {
        this.lon = 0.;
        this.lat = 0.;
        this.date = new Date();
    }

    public Track(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
        this.date = new Date();
    }

    public Track(double lon, double lat, Date date) {
        this.lon = lon;
        this.lat = lat;
        this.date = date;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
