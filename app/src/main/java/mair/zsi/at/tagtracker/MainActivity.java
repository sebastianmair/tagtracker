package mair.zsi.at.tagtracker;

/* okay how this should work,...
 * TODO:
 * on create: load a list of most likely taggers, depending on the current geo-location of the user (for now, just load all)
 *
 * on text entering: search and filter the tags
 *     - on hitting the lower left button, add the text as a new tag, if it does not match with a given entry
 *     - if it matches an entry, just proceed as if it was added the regular way.
 * on list-item-select: expand the list item, and offer to capture the current geolocation, or take a photo
 *     - store geolocation to sqlite db.
 *     - empty the search bar
 *     - reorder the list, make the most recent one the top-most.
 */



import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mair.zsi.at.tagtracker.entities.Tag;
import mair.zsi.at.tagtracker.entities.TagTrack;
import mair.zsi.at.tagtracker.entities.Track;
import mair.zsi.at.tagtracker.sqlite.DatabaseHelper;
import mair.zsi.at.tagtracker.utils.GPSTracker;
import mair.zsi.at.tagtracker.utils.TagArrayAdapter;

public class MainActivity extends OrmLiteBaseActivity<DatabaseHelper> //AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private GPSTracker gpsTracker;
    private List<Tag> tags;
    private TagArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        gpsTracker = new GPSTracker(getApplicationContext());

        // ok load all tags from the database
        try {
            tags = getHelper().getTagDao().queryForAll();
        } catch (SQLException e) {
            throw new RuntimeException("Could not lookup tags in the database", e);
        }

        // load them into the listview
        final ListView listview = (ListView) findViewById(R.id.recentTagList);
        adapter = new TagArrayAdapter(this, tags);
        listview.setAdapter(adapter);

        // on click, change order of list items.
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final Tag item = (Tag) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {

                                addTrack(item);
                                tags.remove(item);
                                tags.add(0, item);
                                adapter.clear();
                                adapter.addAll(tags);
                                adapter.notifyDataSetChanged();
                                view.setAlpha(1);
                            }
                        });
            }

        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = (EditText) findViewById(R.id.tagName);
                String newTagName = editText.getText().toString();
                Tag newTag = new Tag(newTagName);

                try {
                    getHelper().getTagDao().create(newTag);
                    tags.add(0, newTag);
                    addTrack(newTag);
                } catch (SQLException e) {
                    throw new RuntimeException("Could add tag to the database", e);
                }

                adapter.clear();
                adapter.addAll(tags);
                adapter.notifyDataSetChanged();

                editText.setText("");

                Snackbar.make(view, "Added " + newTagName, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                view.setAlpha(1);
            }
        });

        EditText ed = (EditText) findViewById(R.id.tagName);
        //ed.addTextChangedListener(new MyTextWatcher(ed));
        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    QueryBuilder<Tag, Integer> qb = getHelper().getTagDao().queryBuilder();
                    qb.where().like("name", "%" + s.toString() + "%");
                    PreparedQuery<Tag> pq = qb.prepare();
                    tags = getHelper().getTagDao().query(pq);
                } catch (SQLException e) {
                    throw new RuntimeException("Could not lookup Tag in the database", e);
                }

                adapter.clear();
                adapter.addAll(tags);

                adapter.notifyDataSetChanged();
                //Toast.makeText(getActivity(), "search for: "+s.toString()+" found: "+tags.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void addTrack(Tag tag) {
        try {
            Location loc = gpsTracker.getLocation();
            // create track
            Track track = new Track(loc.getLongitude(), loc.getLatitude(),new Date());

            if (tag == null) {
                getHelper().getTrackDao().create(track);
                TagTrack tagtrack = new TagTrack(tag, track);
                getHelper().getTagTrackDao().create(tagtrack);
            } else {
                getHelper().getTrackDao().create(track);
            }

            Toast.makeText(getApplicationContext(), "added a new track, lat: "+track.getLat()+", lon: "+track.getLon()+", date: "+track.getDate().toString(), Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Activity getActivity() {
        return this;
    }

/*    public void addTagOnClick(View v) {
        EditText editText = (EditText)findViewById(R.id.tagName);
        String newTagName = editText.getText().toString();

        Snackbar.make(v, "Added "+newTagName, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();

        list.add(0, newTagName);
        adapter.notifyDataSetChanged();
    }
*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
