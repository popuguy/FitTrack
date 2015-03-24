package tv.organicinterac.FitTrack;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by Paul on 3/20/2015.
 */
public class WorkoutProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private WorkoutDbHelper mOpenDbHelper;

    private static final int EXERCISE = 100;
    private static final int EXERCISE_WITH_WORKOUT = 101;
    private static final int WORKOUT = 200;
    private static final int EXERCISE_COMPLETE = 300;
    private static final int EXERCISE_COMPLETE_WITH_WORKOUT_COMPLETE = 301;
    private static final int WORKOUT_COMPLETE = 400;

    private static final SQLiteQueryBuilder sExerciseByWorkoutQueryBuilder;

    static{
        sExerciseByWorkoutQueryBuilder = new SQLiteQueryBuilder();
        sExerciseByWorkoutQueryBuilder.setTables(
                WorkoutContract.ExerciseEntry.TABLE_NAME + " INNER JOIN " +
                        WorkoutContract.WorkoutEntry.TABLE_NAME +
                        " ON " + WorkoutContract.ExerciseEntry.TABLE_NAME + "." +
                        WorkoutContract.ExerciseEntry.COLUMN_WORKOUT +
                        " = " + WorkoutContract.WorkoutEntry.TABLE_NAME + "." +
                        WorkoutContract.WorkoutEntry._ID);
    }

    private static final String sWorkoutSettingSelection =
            WorkoutContract.ExerciseEntry.TABLE_NAME +
                    "." + WorkoutContract.ExerciseEntry.COLUMN_WORKOUT + " = ? ";
    private static final String sWorkoutCompleteSettingSelection =
            WorkoutContract.ExerciseComplete.TABLE_NAME +
                    "." + WorkoutContract.ExerciseComplete.COLUMN_COMPLETE_WORKOUT + " = ? ";

    private Cursor getExerciseByWorkout(Uri uri, String[] projection, String sortOrder) {
        long workout = WorkoutContract.ExerciseEntry.getWorkoutFromUri(uri);
        String selection = sWorkoutSettingSelection;
        String[] selectionArgs = new String[]{Long.toString(workout)};

        return sExerciseByWorkoutQueryBuilder.query(mOpenDbHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
                );
    }
    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = WorkoutContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, WorkoutContract.PATH_EXERCISE, EXERCISE);
        matcher.addURI(authority, WorkoutContract.PATH_WORKOUT, WORKOUT);
        matcher.addURI(authority, WorkoutContract.PATH_COMPLETE_EXERCISE + "/#",
                EXERCISE_COMPLETE_WITH_WORKOUT_COMPLETE);
        matcher.addURI(authority, WorkoutContract.PATH_COMPLETE_EXERCISE, EXERCISE_COMPLETE);
        matcher.addURI(authority, WorkoutContract.PATH_COMPLETE_WORKOUT, WORKOUT_COMPLETE);
        matcher.addURI(authority, WorkoutContract.PATH_EXERCISE + "/#", EXERCISE_WITH_WORKOUT);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        mOpenDbHelper = new WorkoutDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        switch(sUriMatcher.match(uri)) {
            case EXERCISE:
                retCursor = mOpenDbHelper.getReadableDatabase().query(
                        WorkoutContract.ExerciseEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case WORKOUT:
                retCursor = mOpenDbHelper.getReadableDatabase().query(
                        WorkoutContract.WorkoutEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case EXERCISE_WITH_WORKOUT:
                retCursor = getExerciseByWorkout(uri, projection, sortOrder);
                break;
            case EXERCISE_COMPLETE:
                retCursor = mOpenDbHelper.getReadableDatabase().query(
                        WorkoutContract.ExerciseComplete.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case EXERCISE_COMPLETE_WITH_WORKOUT_COMPLETE:
                retCursor = mOpenDbHelper.getReadableDatabase().query(
                        WorkoutContract.ExerciseComplete.TABLE_NAME,
                        projection,
                        sWorkoutCompleteSettingSelection,
                        new String[]{WorkoutContract.ExerciseComplete.getWorkoutCompleteFromUri(uri)},
                        null,
                        null,
                        sortOrder
                );
//                retCursor = getExerciseCompleteByWorkoutComplete(uri, projection, sortOrder);
                break;
            case WORKOUT_COMPLETE:
                retCursor = mOpenDbHelper.getReadableDatabase().query(
                        WorkoutContract.WorkoutComplete.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }



    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case EXERCISE:
                return WorkoutContract.ExerciseEntry.CONTENT_TYPE;
            case WORKOUT:
                return WorkoutContract.WorkoutEntry.CONTENT_TYPE;
            case EXERCISE_COMPLETE:
                return WorkoutContract.ExerciseComplete.CONTENT_TYPE;
            case EXERCISE_COMPLETE_WITH_WORKOUT_COMPLETE:
                return WorkoutContract.ExerciseComplete.CONTENT_TYPE;
            case WORKOUT_COMPLETE:
                return WorkoutContract.WorkoutComplete.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri = null;
        long _id;

        switch(match) {
            case EXERCISE:
                _id = db.insert(WorkoutContract.ExerciseEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = WorkoutContract.ExerciseEntry.buildExerciseUri(_id);
                else
                    throw new SQLException("Failed to insert row into " + uri);

                break;
            case WORKOUT:
                _id = db.insert(WorkoutContract.WorkoutEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = WorkoutContract.WorkoutEntry.buildWorkoutUri(_id);
                else
                    throw new SQLException("Failed to insert row into " + uri);

                break;
            case EXERCISE_COMPLETE:
                _id = db.insert(WorkoutContract.ExerciseComplete.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = WorkoutContract.ExerciseComplete.buildExerciseCompleteUri(_id);
                else
                    throw new SQLException("Failed to insert row into " + uri);

                break;
            case WORKOUT_COMPLETE:
                _id = db.insert(WorkoutContract.WorkoutComplete.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = WorkoutContract.WorkoutComplete.buildWorkoutCompleteUri(_id);
                else
                    throw new SQLException("Failed to insert row into " + uri);

                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case EXERCISE:
                rowsDeleted = db.delete(WorkoutContract.ExerciseEntry.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case WORKOUT:
                rowsDeleted = db.delete(WorkoutContract.WorkoutEntry.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case EXERCISE_COMPLETE:
                rowsDeleted = db.delete(WorkoutContract.ExerciseComplete.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case WORKOUT_COMPLETE:
                rowsDeleted = db.delete(WorkoutContract.WorkoutComplete.TABLE_NAME, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch(match) {
            case EXERCISE:
                rowsUpdated = db.update(WorkoutContract.ExerciseEntry.TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            case WORKOUT:
                rowsUpdated = db.update(WorkoutContract.WorkoutEntry.TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            case EXERCISE_COMPLETE:
                rowsUpdated = db.update(WorkoutContract.ExerciseComplete.TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            case WORKOUT_COMPLETE:
                rowsUpdated = db.update(WorkoutContract.WorkoutComplete.TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
