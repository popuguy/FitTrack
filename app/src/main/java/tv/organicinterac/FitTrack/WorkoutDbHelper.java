package tv.organicinterac.FitTrack;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import tv.organicinterac.FitTrack.WorkoutContract.ExerciseEntry;
import tv.organicinterac.FitTrack.WorkoutContract.WorkoutEntry;
import tv.organicinterac.FitTrack.WorkoutContract.WorkoutComplete;
import tv.organicinterac.FitTrack.WorkoutContract.ExerciseComplete;

/**
 * Created by Paul on 3/8/2015.
 */
public class WorkoutDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Workout.db";

    public WorkoutDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_WORKOUT_TABLE =
                "CREATE TABLE " + WorkoutEntry.TABLE_NAME + " (" +
                        WorkoutEntry._ID + " INTEGER PRIMARY KEY, " +
                        WorkoutEntry.COLUMN_WORKOUT_NAME + " TEXT NOT NULL);";
        final String SQL_CREATE_EXERCISE_TABLE =
                "CREATE TABLE " + ExerciseEntry.TABLE_NAME + " (" +
                        ExerciseEntry._ID + " INTEGER PRIMARY KEY, " +
                        ExerciseEntry.COLUMN_EXERCISE_NAME + " TEXT NOT NULL, " +
                        ExerciseEntry.COLUMN_SETS + " TEXT NOT NULL, " +
                        ExerciseEntry.COLUMN_REPS + " TEXT NOT NULL, " +
                        "FOREIGN KEY (" + ExerciseEntry.COLUMN_WORKOUT + ") REFERENCES " +
                        WorkoutEntry.TABLE_NAME + " (" + WorkoutEntry._ID + "));";
        final String SQL_CREATE_WORKOUT_COMPLETE_TABLE =
                "CREATE TABLE " + WorkoutComplete.TABLE_NAME + " (" +
                        WorkoutComplete._ID + " INTEGER PRIMARY KEY, " +
                        WorkoutComplete.COLUMN_DATETIME + " TEXT NOT NULL, " +
                        WorkoutComplete.COLUMN_DURATION + " INTEGER NOT NULL, " +
                        "FOREIGN KEY (" + WorkoutComplete.COLUMN_WORKOUT + ") REFERENCES " +
                        WorkoutEntry.TABLE_NAME + " (" + WorkoutEntry._ID + "));";
        final String SQL_CREATE_EXERCISE_COMPLETE_TABLE =
                "CREATE TABLE " + ExerciseComplete.TABLE_NAME + " (" +
                        ExerciseComplete._ID + " INTEGER PRIMARY KEY, " +
                        "FOREIGN KEY (" + ExerciseComplete.COLUMN_EXERCISE + ") REFERENCES " +
                        ExerciseEntry.TABLE_NAME + " (" + ExerciseEntry._ID + "), " +
                        "FOREIGN KEY (" + ExerciseComplete.COLUMN_COMPLETE_WORKOUT + ") " +
                        "REFERENCES " + WorkoutComplete.TABLE_NAME + " (" + WorkoutComplete._ID +
                        "));";

        db.execSQL(SQL_CREATE_WORKOUT_TABLE);
        db.execSQL(SQL_CREATE_EXERCISE_TABLE);
        db.execSQL(SQL_CREATE_WORKOUT_COMPLETE_TABLE);
        db.execSQL(SQL_CREATE_EXERCISE_COMPLETE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ExerciseEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + WorkoutEntry.TABLE_NAME);
        onCreate(db);
    }
}
