package elmansyahfauzifinalproject.mymovies.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import elmansyahfauzifinalproject.mymovies.R;

/**
 * Created by ALLIANCES on 8/20/2017.
 */

public class GenerateView {
    public static View getReview(Context context, String author, String description){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_review, null);
        TextView tvAuthor = (TextView) view.findViewById(R.id.tv_author);
        tvAuthor.setText(author);
        TextView tvDescription = (TextView) view.findViewById(R.id.tv_description);
        tvDescription.setText(description);
        return view;
    }

    public static View getVideo(Context context,String name, String type){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_video, null);
        TextView tvName = (TextView) view.findViewById(R.id.tv_name);
        tvName.setText(name);
        TextView tvType = (TextView) view.findViewById(R.id.tv_type);
        tvType.setText(type);
        return view;
    }
}
