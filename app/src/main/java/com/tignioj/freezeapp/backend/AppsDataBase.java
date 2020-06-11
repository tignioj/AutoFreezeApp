package com.tignioj.freezeapp.backend;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.tignioj.freezeapp.backend.dao.AppsCategoryDao;
import com.tignioj.freezeapp.backend.dao.FreezeAppDao;
import com.tignioj.freezeapp.backend.dao.FreezeTaskerDao;
import com.tignioj.freezeapp.backend.entitys.AppsCategory;
import com.tignioj.freezeapp.backend.entitys.FreezeApp;
import com.tignioj.freezeapp.backend.entitys.FreezeTasker;

import java.text.SimpleDateFormat;
import java.util.Date;


@Database(entities = {AppsCategory.class, FreezeApp.class, FreezeTasker.class}, version = 3, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppsDataBase extends RoomDatabase {

    private static AppsDataBase INSTANCE;

    public static synchronized AppsDataBase getDataBase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppsDataBase.class,
                    "apps_database"
            )
                    /*允许主线程执行SQL*/
                    .allowMainThreadQueries()
                    /*清空当前数据式的迁移*/
//                    .fallbackToDestructiveMigration()
                    /*自定义迁移策略*/
                    .addMigrations(MIGRATION_2_3)
//                    .addMigrations(MIGRATION_3_4)
                    /*级联删除tasks*/
//                    .addMigrations(MIGRATION_4_5)
                    /*修改列名，添加新字段*/
//                    .addMigrations(MIGRATION_5_6)
                    /*添加Task创建时间*/
//                    .addMigrations(MIGRATION_6_7)
                    .build();
        }
        return INSTANCE;
    }

    public abstract AppsCategoryDao getAppsCategoryDao();
    public abstract FreezeAppDao getFreezeAppDao();
    public abstract FreezeTaskerDao getFreezeTaskerDao();


    /**
     * freezeTimer添加字段description
     */
    private static final Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            Date date = new Date();
            String s =  "\'" + date.toString() + "\'";
            database.execSQL("ALTER TABLE  freeze_tasker ADD COLUMN description VARCHAR DEFAULT "+ s);
        }
    };


    /**
     * freezeTimer添加字段description
     */
    private static final Migration MIGRATION_2_3 = new Migration(2,3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE  freeze_tasker ADD COLUMN enable INTEGER  NOT NULL DEFAULT 0");
        }
    };


    //============================参考=========================


    /**
     * 版本3->4 添加myTask的创建时间
     */
    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
//            supportSQLiteDatabase.execSQL("ALTER TABLE myTask  ADD COLUMN create_time INTEGER NOT NULL DEFAULT 1");
            Date date = new Date();
            long time = date.getTime();
            supportSQLiteDatabase.execSQL("ALTER TABLE myTask  ADD COLUMN create_time INTEGER DEFAULT " + time);
        }
    };


    /**
     * 删除字段迁移策略
     * 修改外键为级联删除，需要修改表，复制表
     * https://www.techonthenet.com/sqlite/foreign_keys/foreign_delete.php
     */
    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {

            //删除旧约束
            supportSQLiteDatabase.execSQL("DROP INDEX `index_MyTask_timeline_id`");

            //一，创建新表
            String sql = "CREATE TABLE `myTaskTemp` " +
                    "(`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`timeline_id` INTEGER NOT NULL, " +
                    "`content` TEXT, " +
                    "`has_finished` INTEGER NOT NULL, " +
                    "create_time INTEGER DEFAULT " + new Date().getTime() +
                    ", FOREIGN KEY(`timeline_id`) REFERENCES `timeline`(`id`) ON UPDATE CASCADE ON DELETE CASCADE)";

            supportSQLiteDatabase.execSQL(sql);

            //添加index
            supportSQLiteDatabase.execSQL("CREATE INDEX `index_MyTask_timeline_id` ON `myTaskTemp` (`timeline_id`)");

            //二、将旧表数据导入新表
            supportSQLiteDatabase.execSQL("INSERT INTO myTaskTemp " +
                    /*从临时表中插入*/
                    " SELECT * FROM myTask");

            //三，删除旧表
            supportSQLiteDatabase.execSQL("DROP TABLE myTask");

            //四，新表改名为旧表
            supportSQLiteDatabase.execSQL("ALTER TABLE myTaskTemp RENAME TO myTask");
        }
    };

    /**
     * 修改createTime字段为remindMeTime
     * 添加repeat字段
     */
    static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {

            //删除旧约束
            supportSQLiteDatabase.execSQL("DROP INDEX `index_MyTask_timeline_id`");

            //一，创建新表
            String sql = "CREATE TABLE `myTaskTemp` " +
                    "(`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`timeline_id` INTEGER NOT NULL, " +
                    "`content` TEXT, " +
                    "`has_finished` INTEGER DEFAULT 0 NOT NULL, " +
                    /*添加repeat字段，在java中是boolean, SQLite中以INTEGER形式存在，0表示false，1表示true*/
                    "`repeat` INTEGER DEFAULT 0 NOT NULL, " +
                    /*修改原来的create_time为remind_me_date*/
                    "remind_me_date INTEGER DEFAULT " + new Date().getTime() +
                    ", FOREIGN KEY(`timeline_id`) REFERENCES `timeline`(`id`) ON UPDATE CASCADE ON DELETE CASCADE)";

            supportSQLiteDatabase.execSQL(sql);

            //添加index
            supportSQLiteDatabase.execSQL("CREATE INDEX `index_MyTask_timeline_id` ON `myTaskTemp` (`timeline_id`)");

            //二、将旧表数据导入新表
            supportSQLiteDatabase.execSQL("INSERT INTO myTaskTemp(id, timeline_id, content, has_finished, remind_me_date) " +
                    /*从临时表中插入*/
                    " SELECT id, timeline_id, content, has_finished, create_time  " +
                    "  FROM myTask");

            //三，删除旧表
            supportSQLiteDatabase.execSQL("DROP TABLE myTask");

            //四，新表改名为旧表
            supportSQLiteDatabase.execSQL("ALTER TABLE myTaskTemp RENAME TO myTask");
        }
    };

    /**
     * 版本3->4 添加myTask的创建时间
     */
    static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
            Date date = new Date();
            long time = date.getTime();
            supportSQLiteDatabase.execSQL("ALTER TABLE myTask  ADD COLUMN create_time INTEGER DEFAULT " + time);
        }
    };



}
