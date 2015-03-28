package tv.organicinterac.FitTrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

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

    public long addCompleteWorkout(long workoutId, long duration, String timeCompleted) {
        ContentValues values = new ContentValues();
        values.put(WorkoutComplete.COLUMN_WORKOUT, workoutId);
        values.put(WorkoutComplete.COLUMN_DURATION, duration);
        values.put(WorkoutComplete.COLUMN_DATETIME, timeCompleted);

        return mDbHelper.getWritableDatabase().insert(
                WorkoutComplete.TABLE_NAME,
                null,
                values
        );
    }

    public long addCompleteExercise(long completeWorkoutId, long exerciseId) {
        ContentValues values = new ContentValues();
        values.put(ExerciseComplete.COLUMN_COMPLETE_WORKOUT, completeWorkoutId);
        values.put(ExerciseComplete.COLUMN_EXERCISE, exerciseId);

        return mDbHelper.getWritableDatabase().insert(
                ExerciseComplete.TABLE_NAME,
                null,
                values
        );
    }

    public List<String[]> getCompleteWorkouts() {
        Cursor completeWorkouts = mDbHelper.getReadableDatabase().query(
                WorkoutComplete.TABLE_NAME,
                new String[]{
                        WorkoutComplete._ID,
                        WorkoutComplete.COLUMN_WORKOUT,
                        WorkoutComplete.COLUMN_DATETIME,
                        WorkoutComplete.COLUMN_DURATION
                },
                "*",
                null,
                null,
                null,
                null

        );
        List<String[]> workouts = new ArrayList<String[]>();
        completeWorkouts.moveToFirst();
        while (!completeWorkouts.isAfterLast()) {
            String[] workout = new String[]{
                    Long.toString(completeWorkouts.getLong(
                            completeWorkouts.getColumnIndexOrThrow(WorkoutComplete._ID))),
                    Long.toString(completeWorkouts.getLong(
                            completeWorkouts.getColumnIndexOrThrow(
                                    WorkoutComplete.COLUMN_WORKOUT))),
                    completeWorkouts.getString(
                            completeWorkouts.getColumnIndexOrThrow(
                                    WorkoutComplete.COLUMN_DATETIME)),
                    Long.toString(completeWorkouts.getLong(
                            completeWorkouts.getColumnIndexOrThrow(
                                    WorkoutComplete.COLUMN_DATETIME)))
            };
            workouts.add(workout);
            completeWorkouts.moveToNext();
        }
        return workouts;
    }

    public List<String[]> getCompleteExercisesByCompleteWorkoutId(long completeWorkoutId) {
        Cursor completeExercises = mDbHelper.getReadableDatabase().rawQuery(
                "SELECT ?.?, ?.?, ?.? FROM ? JOIN ? ON ?.? = ?.? WHERE ? = ?",
                new String[]{
                        ExerciseEntry.TABLE_NAME, /* table.col for name*/
                        ExerciseEntry.COLUMN_EXERCISE_NAME,
                        ExerciseEntry.TABLE_NAME, /* table.col for sets*/
                        ExerciseEntry.COLUMN_SETS,
                        ExerciseEntry.TABLE_NAME, /* table.col for reps*/
                        ExerciseEntry.COLUMN_REPS,
                        ExerciseComplete.TABLE_NAME,
                        ExerciseEntry.TABLE_NAME,
                        ExerciseComplete.TABLE_NAME,
                        ExerciseComplete.COLUMN_EXERCISE,
                        ExerciseEntry.TABLE_NAME,
                        ExerciseEntry._ID,
                        ExerciseComplete.COLUMN_COMPLETE_WORKOUT,
                        Long.toString(completeWorkoutId)
                }
        );
        List<String[]> exercises = new ArrayList<>();
        completeExercises.moveToFirst();
        while (! completeExercises.isAfterLast()) {
            String[] exercise = new String[]{
                completeExercises.getString(0),
                Long.toString(completeExercises.getLong(1)),
                Long.toString(completeExercises.getLong(2))
            };
            exercises.add(exercise);
            completeExercises.moveToNext();
        }
        return exercises;
    }

    public List<String[]> getExercisesByWorkout(long workoutId) {
        Cursor exercisesCursor = mDbHelper.getReadableDatabase().query(
                ExerciseEntry.TABLE_NAME,
                new String[]{
                        ExerciseEntry.COLUMN_EXERCISE_NAME,
                        ExerciseEntry.COLUMN_SETS,
                        ExerciseEntry.COLUMN_REPS,
                        ExerciseEntry.COLUMN_WORKOUT

                },
                ExerciseEntry.COLUMN_VISIBLE + " = ? AND " + ExerciseEntry.COLUMN_WORKOUT + " = ?",
                new String[]{"1", Long.toString(workoutId)},
                null,
                null,
                null

        );
        List<String[]> exercises = new ArrayList<>();
        exercisesCursor.moveToFirst();
        while (! exercisesCursor.isAfterLast()) {
            String[] exercise = new String[]{
                    exercisesCursor.getString(0),
                    Long.toString(exercisesCursor.getLong(1)),
                    Long.toString(exercisesCursor.getLong(2)),
                    Long.toString(exercisesCursor.getLong(3)),
            };
            exercises.add(exercise);
            exercisesCursor.moveToNext();
        }
        return exercises;
    }

    public List<String[]> getWorkouts() {
        Cursor workoutsCursor = mDbHelper.getReadableDatabase().query(
                WorkoutEntry.TABLE_NAME,
                new String[]{
                        WorkoutEntry._ID,
                        WorkoutEntry.COLUMN_WORKOUT_NAME
                },
                WorkoutEntry.COLUMN_VISIBLE + " = ?",
                new String[]{"1"},
                null,
                null,
                null

        );
        List<String[]> workouts = new ArrayList<>();
        workoutsCursor.moveToFirst();
        while (! workoutsCursor.isAfterLast()) {
            String[] workout = new String[]{
                    Long.toString(workoutsCursor.getLong(0)),
                    workoutsCursor.getString(1)
            };
            workouts.add(workout);
            workoutsCursor.moveToNext();
        }
        return workouts;
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
