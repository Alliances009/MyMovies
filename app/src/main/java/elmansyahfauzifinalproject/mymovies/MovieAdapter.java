package elmansyahfauzifinalproject.mymovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by ALLIANCES on 7/30/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.Adapter> {



    private List<Result> listData;
    private ItemClickListener listener;

    public MovieAdapter(List<Result> listData) {
        this.listData = listData;
    }

    public MovieAdapter(List<Result> listData, ItemClickListener listener) {
        this.listData = listData;
        this.listener = listener;
    }

    public interface ItemClickListener{
        public void onItemClick(int position);
    }

    @Override
    public Adapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie,parent,false);
        Adapter holder = new Adapter(view);
        return holder;

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

    public class Adapter extends RecyclerView.ViewHolder {

        TextView tvTitle;
        ImageView ivPoster;

        public Adapter(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            ivPoster = (ImageView) itemView.findViewById(R.id.iv_poster);
        }
    }
}
