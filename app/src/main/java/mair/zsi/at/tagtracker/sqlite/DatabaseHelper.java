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

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {

        try {
            TableUtils.createTable(connectionSource, Tag.class);
            TableUtils.clearTable(connectionSource, Track.class);
            TableUtils.createTable(connectionSource, TagTrack.class);
        } catch (SQLException e) {
            // TODO: do smth.
        }

        /*// Drop older table if existed
        String CREATE_TAG_TABLE = "CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s VARCHAR(255));"
                .format(TAG_TABLE_NAME, TAG_ID, TAG_NAME);
        db.execSQL(CREATE_TAG_TABLE);

        String CREATE_TRACK_TABLE = "CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s DOUBLE, %s DOUBLE, %s DATETIME);"
                .format(TRACK_TABLE_NAME, TRACK_ID, TRACK_LON, TRACK_LAT, TRACK_DATE);
        db.execSQL(CREATE_TRACK_TABLE);

        String CREATE_TAG_TRACK_TABLE = ("CREATE TABLE %s (%s INTEGER NOT NULL, %s INTEGER NOT NULL, " +
                "FOREIGN KEY(%s) REFERENCES %s(%s), FOREIGN KEY(%s) REFERENCES %s(%s), " +
                " PRIMARY KEY (%s, %s))")
                .format(TAG_TRACK_TABLE_NAME, TAGTRACK_TAG_ID, TAGTRACK_TRACK_ID,
                        TAGTRACK_TAG_ID, TAG_TABLE_NAME, TAG_ID,
                        TAGTRACK_TRACK_ID, TRACK_TABLE_NAME, TRACK_ID,
                        TAGTRACK_TAG_ID, TAGTRACK_TRACK_ID);
        db.execSQL(CREATE_TAG_TRACK_TABLE);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {

        try {
            TableUtils.dropTable(connectionSource, TagTrack.class, true);
            TableUtils.dropTable(connectionSource, Tag.class, true);
            TableUtils.dropTable(connectionSource, Track.class, true);
        } catch (SQLException e) {
            // TODO: do smth.
        }
        /*
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TAG_TRACK_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TRACK_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TAG_TABLE_NAME);

        // Create tables again
        onCreate(db);
        */
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
