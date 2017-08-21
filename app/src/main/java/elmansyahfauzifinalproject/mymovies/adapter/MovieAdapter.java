package elmansyahfauzifinalproject.mymovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import elmansyahfauzifinalproject.mymovies.R;
import elmansyahfauzifinalproject.mymovies.utils.UrlComposer;
import elmansyahfauzifinalproject.mymovies.model.Result;

import static android.content.ContentValues.TAG;


/**
 * Created by ALLIANCES on 7/30/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.Adapter> {



    private List<Result> listData;
    private ItemClickListener listener;
    private boolean isFavorite = false;

    public MovieAdapter(List<Result> listData) {
        this.listData = listData;
    }

    public MovieAdapter(List<Result> listData, ItemClickListener listener) {
        this.listData = listData;
        this.listener = listener;
    }

    public MovieAdapter(List<Result> listData, ItemClickListener listener,boolean isFavorite) {
        this.listData = listData;
        this.listener = listener;
        this.isFavorite = isFavorite;
    }


    public interface ItemClickListener{
        public void onItemClick(int position);
    }

    public Result getData(int pos){
        return listData.get(pos);
    }

    @Override
    public Adapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie,parent,false);
        Adapter holder = new Adapter(view);
        return holder;

    }

    public void append(List<Result> dataToAppend) {
        if (!isFavorite){
            int firstPosition = listData.size();
            Log.d(TAG, "append: "+listData.size());
            listData.addAll(dataToAppend);
            notifyItemRangeChanged(firstPosition, dataToAppend.size()-1);
            //notifyDataSetChanged();
        }

    }



    @Override
    public void onBindViewHolder(Adapter holder, int position) {
        holder.tvTitle.setText(listData.get(position).getTitle());
        Picasso.with(holder.ivPoster.getContext())
                .load(UrlComposer.getPosterUrl(listData.get(position).getPosterPath()))
                .into(holder.ivPoster);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class Adapter extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTitle;
        ImageView ivPoster;

        public Adapter(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            ivPoster = (ImageView) itemView.findViewById(R.id.iv_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();
            listener.onItemClick(position);
        }
    }
}
