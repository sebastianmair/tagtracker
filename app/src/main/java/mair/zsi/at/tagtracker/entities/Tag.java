package mair.zsi.at.tagtracker.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by sebastian on 21.10.17.
 */

@DatabaseTable (tableName = "tag")
public class Tag {

    @DatabaseField (generatedId = true)
    private int id;

    @DatabaseField
    private String name;

    public Tag() {
        this.name = "";
    }

    public Tag(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
