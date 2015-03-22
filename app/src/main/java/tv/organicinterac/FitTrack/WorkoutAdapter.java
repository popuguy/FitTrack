package tv.organicinterac.FitTrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import tv.organicinterac.FitTrack.WorkoutContract.ExerciseEntry;
import tv.organicinterac.FitTrack.WorkoutContract.WorkoutEntry;

/**
 * Created by Paul on 3/8/2015.
 */
public class WorkoutAdapter {

    SQLiteDatabase writeDb, readDb;
    WorkoutDbHelper mDbHelper;

    public WorkoutAdapter(Context context) {
        mDbHelper = new WorkoutDbHelper(context);
        writeDb = mDbHelper.getWritableDatabase();
        readDb = mDbHelper.getReadableDatabase();
    }

    public long addExercise(String name, String setsReps) {
        /**
         * Adds an exercise to the database
         *
         * @param name      descriptive exercise name for display
         * @param setsReps  sets and reps formatted as "SETS x REPS"
         *
         * @return          id of new row
         */

        ContentValues values = new ContentValues();
        values.put(ExerciseEntry.COLUMN_EXERCISE_NAME, name);
        values.put(ExerciseEntry.COLUMN_SETS_REPS, setsReps);
        long newRowId;
        newRowId = writeDb.insert(
                ExerciseEntry.TABLE_NAME,
                null,
                values);
        return newRowId;
    }

    public long addWorkout(String date, int duration, String completed) {
        /**
         * Adds time data about a workout to the database
         *
         * @param date      the date the workout was completed formatted as something
         * @param duration  the length in ms of the workout
         * @param completed the ids of exercises completed formatted as a comma separated list
         *
         * @return          id of new row
         */
        ContentValues values = new ContentValues();
        values.put(WorkoutEntry.COLUMN_DATE, date);
        values.put(WorkoutEntry.COLUMN_DURATION, duration);
        values.put(WorkoutEntry.COLUMN_EXERCISES_COMPLETED, completed);
        long newRowId;
        newRowId = writeDb.insert(
                WorkoutEntry.TABLE_NAME,
                null,
                values);
        return newRowId;
    }

    public List<String[]> getWorkouts() {
        /**
         * Returns all workouts that have been completed via the timer.
         *
         * @return  a list of workouts as string arrays formatted as ID, DATE, DURATION, EXERCISES
         */
        String[] projection = {
                WorkoutEntry._ID,
                WorkoutEntry.COLUMN_DATE,
                WorkoutEntry.COLUMN_DURATION,
                WorkoutEntry.COLUMN_EXERCISES_COMPLETED
        };

        String sortOrder =
                WorkoutEntry.COLUMN_DATE + " DESC";
        List<String[]> results = new ArrayList<>();
        Cursor cursor = readDb.query(
                WorkoutEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder);

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String id = Integer.toString(cursor.getInt(cursor.getColumnIndex(WorkoutEntry._ID)));
            String date = cursor.getString(cursor.getColumnIndex(WorkoutEntry.COLUMN_DATE));
            String duration = Integer.toString(cursor.getInt(
                    cursor.getColumnIndex(WorkoutEntry.COLUMN_DURATION)));
            String exercises = cursor.getString(cursor.getColumnIndex(
                    WorkoutEntry.COLUMN_EXERCISES_COMPLETED));
            results.add(new String[]{id, date, duration, exercises});
            cursor.moveToNext();
        }

        return results;
    }

    public List<String[]> getExercises() {
        /**
         * Returns the exercises that populate the check area for the workout
         *
         * @return  a list of the saved exercises in the order of the names formatted in string
         *          arrays as ID, NAME, SETS_REPS
         */
        String[] projection = {
                ExerciseEntry._ID,
                ExerciseEntry.COLUMN_EXERCISE_NAME,
                ExerciseEntry.COLUMN_SETS_REPS
        };

        String sortOrder =
                ExerciseEntry.COLUMN_EXERCISE_NAME + " DESC";
        List<String[]> results = new ArrayList<>();
        Cursor cursor = readDb.query(
                ExerciseEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder);

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String id = Integer.toString(cursor.getInt(cursor.getColumnIndex(ExerciseEntry._ID)));
            String name = cursor.getString(cursor.getColumnIndex(ExerciseEntry.COLUMN_EXERCISE_NAME));
            String setsReps = cursor.getString(cursor.getColumnIndex(ExerciseEntry.COLUMN_SETS_REPS));

            results.add(new String[]{id, name, setsReps});
            cursor.moveToNext();
        }

        return results;
    }
}
