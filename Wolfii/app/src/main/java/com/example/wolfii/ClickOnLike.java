package com.example.wolfii;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import static com.example.wolfii.MainActivity.database;
import static com.example.wolfii.MainActivity.mService;
import static com.example.wolfii.R.drawable.like;

public class ClickOnLike implements View.OnClickListener{
    private Musique musique;

    private Context context;

    private ImageView like;

    private Boolean isWhite = false;

    public void setMusique(Musique musique){this.musique = musique;}
    public void setLike(ImageView like) {this.like = like;}
    public void setContext(Context context) {this.context = context;}
    public void setIsWhite(Boolean isWhite) {this.isWhite = isWhite;}

    @Override
    public void onClick (View v) {
        Log.d("debug_like", database.mainDao ().getLikes ().toString ());

        DataMusique dataMusique = new DataMusique ();
        dataMusique.setNomMusique (musique.getName ());
        dataMusique.setPath (musique.getPath ());
        dataMusique.setAuthor (musique.getAuthor ());
        dataMusique.setDuration (musique.getDuration ());
        dataMusique.setDateTaken (musique.getDateTaken ());
        dataMusique.setGenre (musique.getGenre ());

        DataLikedMusic dataLikedMusic = new DataLikedMusic ();
        dataLikedMusic.setPath (musique.getPath ());

        if(!database.mainDao ().getLikes ().contains (musique.getPath ())) {
            database.mainDao ().insertMusic (dataMusique);
            database.mainDao ().insertLike (dataLikedMusic);
            if(isWhite) like.setImageBitmap (drawableEnBitmap (R.drawable.like_white));
            else like.setImageBitmap (drawableEnBitmap (R.drawable.like));
        }
        else {
            database.mainDao ().deleteLike (dataLikedMusic);

            if(isWhite) like.setImageBitmap (drawableEnBitmap (R.drawable.unlike_white));
            else like.setImageBitmap (drawableEnBitmap (R.drawable.unlike));
        }

        if (mService.getMusiquePlayerIsSet() && mService.getMusiquePlayerPath()==musique.getPath ())
            mService.notificationInitEtMaj();

    }
    public Bitmap drawableEnBitmap (int drawableRes) {
        @SuppressLint("UseCompatLoadingForDrawables") Drawable drawable = context.getResources().getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
