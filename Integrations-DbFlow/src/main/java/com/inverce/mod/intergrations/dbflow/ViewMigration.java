package com.inverce.mod.intergrations.dbflow;

import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.migration.BaseMigration;
import com.raizlabs.android.dbflow.structure.ModelViewAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

public class ViewMigration extends BaseMigration {
    private Class<?> modelViewClass;

    public ViewMigration(Class<?> modelViewClass) {
        this.modelViewClass = modelViewClass;
    }

    @Override
    public void migrate(@NonNull DatabaseWrapper database) {
        ModelViewAdapter adapter = FlowManager.getModelViewAdapter(modelViewClass);
        QueryBuilder dropQueryBuilder = new QueryBuilder()
                .append("DROP VIEW IF EXISTS")
                .appendSpaceSeparated(adapter.getViewName());
        try {
            database.execSQL(dropQueryBuilder.getQuery());
        } catch (SQLiteException e) {
            FlowLog.logError(e);
        }

        QueryBuilder createQueryBuilder = new QueryBuilder()
                .append("CREATE VIEW IF NOT EXISTS")
                .appendSpaceSeparated(adapter.getViewName())
                .append("AS ")
                .append(adapter.getCreationQuery());
        try {
            database.execSQL(createQueryBuilder.getQuery());
        } catch (SQLiteException e) {
            FlowLog.logError(e);
        }
    }
}

//class ViewMigration2 extends ViewMigration {
//    public ViewMigration2() {
//        super(PersonView.class);
//    }
//}