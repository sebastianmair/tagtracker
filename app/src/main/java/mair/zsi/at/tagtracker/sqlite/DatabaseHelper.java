package mair.zsi.at.tagtracker.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import mair.zsi.at.tagtracker.entities.Tag;
import mair.zsi.at.tagtracker.entities.TagTrack;
import mair.zsi.at.tagtracker.entities.Track;

/**
 * Created by sebastian on 21.10.17.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // database version
    private static final int DATABASE_VERSION = 1;

    // database name
    private static final String DATABASE_NAME = "tagtracker_database";

    private Dao<Tag, Integer> tagDao;
    private Dao<Track, Integer> trackDao;
    private Dao<TagTrack, Integer> tagTrackDao;

    // tag tables
    private static final String TAG_TABLE_NAME = "tag";
    private static final String TRACK_TABLE_NAME = "track";
    private static final String TAG_TRACK_TABLE_NAME = "tag_track";

    // table columns
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";

    private static final String TRACK_ID = "id";
    private static final String TRACK_LON = "lon";
    private static final String TRACK_LAT = "lat";
    private static final String TRACK_DATE = "date";

    private static final String TAGTRACK_TAG_ID = "tag_id";
    private static final String TAGTRACK_TRACK_ID = "track_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {

        try {
            TableUtils.createTable(connectionSource, Tag.class);
            TableUtils.createTable(connectionSource, Track.class);
            TableUtils.createTable(connectionSource, TagTrack.class);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
            // TODO: do smth.
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {

        try {
            TableUtils.dropTable(connectionSource, TagTrack.class, true);
            TableUtils.dropTable(connectionSource, Tag.class, true);
            TableUtils.dropTable(connectionSource, Track.class, true);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
            // TODO: do smth.
        }
    }

    public Dao<Tag, Integer> getTagDao() throws SQLException {
        if (tagDao == null) {
            tagDao = getDao(Tag.class);
        }
        return tagDao;
    }

    public Dao<Track, Integer> getTrackDao() throws SQLException {
        if (trackDao == null) {
            trackDao = getDao(Track.class);
        }
        return trackDao;
    }

    public Dao<TagTrack, Integer> getTagTrackDao() throws SQLException {
        if (tagTrackDao == null) {
            tagTrackDao = getDao(TagTrack.class);
        }
        return tagTrackDao;
    }
}
