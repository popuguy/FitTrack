package tv.organicinterac.FitTrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import tv.organicinterac.FitTrack.WorkoutContract.ExerciseEntry;
import tv.organicinterac.FitTrack.WorkoutContract.WorkoutEntry;
import tv.organicinterac.FitTrack.WorkoutContract.ExerciseComplete;
import tv.organicinterac.FitTrack.WorkoutContract.WorkoutComplete;

/**
 * Created by Paul on 3/22/2015.
 */
public class DatabaseInteraction  {
    WorkoutDbHelper mDbHelper;

    public DatabaseInteraction(Context context) {
        mDbHelper = new WorkoutDbHelper(context);
    }

    public long addWorkout(String workoutName) {
        ContentValues values = new ContentValues();
        values.put(WorkoutEntry.COLUMN_WORKOUT_NAME, workoutName);

        return mDbHelper.getWritableDatabase().insert(
                WorkoutEntry.TABLE_NAME,
                null,
                values
        );
    }

    public long addExercise(String exerciseName, String sets, String reps, long workoutId) {
        ContentValues values = new ContentValues();
        values.put(ExerciseEntry.COLUMN_EXERCISE_NAME, exerciseName);
        values.put(ExerciseEntry.COLUMN_SETS, sets);
        values.put(ExerciseEntry.COLUMN_REPS, reps);
        values.put(ExerciseEntry.COLUMN_WORKOUT, workoutId);

        return mDbHelper.getWritableDatabase().insert(
                ExerciseEntry.TABLE_NAME,
                null,
                values
        );
    }

    public int deleteCompleteWorkout(long id) { /* fine */
        int workoutRowsDeleted = mDbHelper.getWritableDatabase().delete(
                WorkoutComplete.TABLE_NAME,
                WorkoutComplete._ID + " = ?",
                new String[]{Long.toString(id)}
        );
        int exerciseRowsDeleted = mDbHelper.getWritableDatabase().delete(
                ExerciseComplete.TABLE_NAME,
                ExerciseComplete.COLUMN_COMPLETE_WORKOUT + " = ?",
                new String[]{Long.toString(id)}
        );
        return workoutRowsDeleted + exerciseRowsDeleted;
    }

    public int deleteWorkout(long id) {
        Cursor completeWorkouts = mDbHelper.getReadableDatabase().query(
                WorkoutComplete.TABLE_NAME,
                new String[]{WorkoutComplete.COLUMN_WORKOUT},
                WorkoutComplete.COLUMN_WORKOUT + " = ?",
                new String[]{Long.toString(id)},
                null,
                null,
                null

        );
        int rowsInWorkoutComplete = completeWorkouts.getCount();
        if (rowsInWorkoutComplete < 1) {
            int workoutRowsDeleted = mDbHelper.getWritableDatabase().delete(
                    WorkoutEntry.TABLE_NAME,
                    WorkoutEntry._ID + " = ?",
                    new String[]{Long.toString(id)}
            );
            int exerciseRowsDeleted = mDbHelper.getWritableDatabase().delete(
                    ExerciseEntry.TABLE_NAME,
                    ExerciseEntry.COLUMN_WORKOUT + " = ?",
                    new String[]{Long.toString(id)}
            );
            return workoutRowsDeleted + exerciseRowsDeleted;
        }
        ContentValues values = new ContentValues();
        values.put(WorkoutEntry.COLUMN_VISIBLE, "0");
        int workoutRowsModified = mDbHelper.getWritableDatabase().update(
                WorkoutEntry.TABLE_NAME,
                values,
                WorkoutEntry._ID + " = ?",
                new String[]{Long.toString(id)}
        );
        int exerciseRowsModified = deleteExercisesByWorkoutId(id);
        return workoutRowsModified + exerciseRowsModified;
    }

    public int deleteExercisesByWorkoutId(long id) {
        Cursor exercisesToChange = mDbHelper.getReadableDatabase().query(
                ExerciseEntry.TABLE_NAME,
                new String[]{ExerciseEntry._ID},
                ExerciseEntry.COLUMN_WORKOUT + " = ?",
                new String[]{Long.toString(id)},
                null,
                null,
                null

        );
        exercisesToChange.moveToFirst();
        int totalRows = 0;
        while (! exercisesToChange.isAfterLast()) {
            long curExerciseId = exercisesToChange.getLong(0);
            totalRows += deleteExercise(curExerciseId);

            exercisesToChange.moveToNext();
        }
        return totalRows;
    }

    public int deleteExercise(long id) {
        Cursor completeExercisesWithSameId = mDbHelper.getReadableDatabase().query(
                ExerciseComplete.TABLE_NAME,
                new String[]{ExerciseComplete._ID},
                ExerciseComplete.COLUMN_EXERCISE + " = ?",
                new String[]{Long.toString(id)},
                null,
                null,
                null
        );
        int numExercises = completeExercisesWithSameId.getCount();
        if (numExercises < 1) {
            int exerciseRowsDeleted = mDbHelper.getWritableDatabase().delete(
                    ExerciseEntry.TABLE_NAME,
                    ExerciseEntry.COLUMN_WORKOUT + " = ?",
                    new String[]{Long.toString(id)}
            );
            return exerciseRowsDeleted;
        }

        ContentValues values = new ContentValues();
        values.put(ExerciseEntry.COLUMN_VISIBLE, "0");
        int exerciseRowsModified = mDbHelper.getWritableDatabase().update(
                ExerciseEntry.TABLE_NAME,
                values,
                ExerciseEntry._ID + " = ?",
                new String[]{Long.toString(id)}
        );

        return exerciseRowsModified;
    }
}
