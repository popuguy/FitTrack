package tv.organicinterac.FitTrack;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Paul on 3/7/2015.
 */
public final class WorkoutContract {

    public static final String CONTENT_AUTHORITY = "tv.organicinterac.FitTrack";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_EXERCISE = "exercise";
    public static final String PATH_WORKOUT = "workout";
    public static final String PATH_COMPLETE_EXERCISE = "exercise_complete";
    public static final String PATH_COMPLETE_WORKOUT = "workout_complete";

//    public static final String DATE_FORMAT = "yyyyMMdd";
//
//    public static String getDbDateString(Date date){
//        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
//        return sdf.format(date);
//    }
//
//    public static Date getDateFromDb(String dateText) {
//        SimpleDateFormat dbDateFormat = new SimpleDateFormat(DATE_FORMAT);
//        try {
//            return dbDateFormat.parse(dateText);
//        } catch ( ParseException e ) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    public static abstract class ExerciseEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_EXERCISE).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_EXERCISE;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_EXERCISE;

        public static final String TABLE_NAME = "exercises";
        public static final String COLUMN_EXERCISE_NAME = "name";
        public static final String COLUMN_SETS = "sets";
        public static final String COLUMN_REPS = "reps";
        public static final String COLUMN_WORKOUT = "workout";

        public static int getWorkoutFromUri(Uri uri) {
            return Integer.parseInt(uri.getPathSegments().get(1));
        }


        public static Uri buildExerciseWithWorkout(int id) {
            return CONTENT_URI.buildUpon().appendQueryParameter(COLUMN_WORKOUT, Integer.toString(id)).build();
        }

        public static Uri buildExerciseUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
    public static abstract class WorkoutEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_WORKOUT).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_WORKOUT;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_WORKOUT;

        public static final String TABLE_NAME = "workout";
        public static final String COLUMN_WORKOUT_NAME = "name";

        public static Uri buildWorkoutUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
    public static abstract class ExerciseComplete implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COMPLETE_EXERCISE).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_COMPLETE_EXERCISE;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_COMPLETE_EXERCISE;

        public static final String TABLE_NAME = "exercisecomplete";
        public static final String COLUMN_EXERCISE = "exercise";
        public static final String COLUMN_COMPLETE_WORKOUT = "completeworkoutid";

        public static String getWorkoutCompleteFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static Uri buildExerciseCompleteUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildExerciseCompleteWithWorkoutCompleteId(int id){
            return CONTENT_URI.buildUpon().appendQueryParameter(COLUMN_COMPLETE_WORKOUT, Integer.toString(id)).build();
        }

    }
    public static abstract class WorkoutComplete implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COMPLETE_WORKOUT).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_COMPLETE_WORKOUT;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_COMPLETE_WORKOUT;

        public static final String TABLE_NAME = "workoutcomplete";
        public static final String COLUMN_WORKOUT = "workoutid";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_DATETIME = "datetime";

        public static Uri buildWorkoutCompleteUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
