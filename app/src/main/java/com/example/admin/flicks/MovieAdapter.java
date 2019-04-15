package com.example.admin.flicks;

import android.content.Context;
import android.graphics.Movie;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.admin.flicks.models.Config;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter  extends RecyclerView.Adapter<MovieAdapter.viewHolder>{
    // list of movies
    ArrayList<Movie> movies;
    // config needed for image urls
    Config config;
    // context for rendering
    Context context;

    // initialize with list

    public MovieAdapter(ArrayList<Movie> movies){
        this.movies = movies;
    }

    // creates and inflates a new view
    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // get the context and create the inflater
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from( context );
        // create the view using the item_movie layout
        View movieView = inflater.inflate( R.layout.item_movie,parent, false );
        //return a new viewHolder
        return new viewHolder( movieView );
    }
    // binds an inflated view to a new item
    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        // get the movie data at the specified position
        Movie movie = movies.get( position );
        // populate the view with the movie data
        holder.tvTitle.setText( movie.getTitle)));
        holder.tvOverview.setText( movie.(getItemId( position )));

        // build url for poster image
        String imageUrl = Config.getImageUrl(Config.getPosterSize(), movie.getPostePath());

        // load image using glide
        Glide.with( context )
                .load( imageUrl )
                .bitmapTransform( new RoundedCornersTransformation(context, 25,0  ) )
                .placeholder( R.drawable.flicks_movie_placeholder )
                .error(R.drawable.flicks_movie_placeholder)
                .into( holder.ivPosterImage );




    }
    // returns the total number of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    // create the viewHolder as a static inner class
    public static class viewHolder extends RecyclerView.ViewHolder{
      // track view objects
        ImageView ivPosterImage;
        TextView tvTitle;
        TextView tvOverview;
        public viewHolder(@NonNull View itemView) {
            super( itemView );
            // lookup view objects by id
            ivPosterImage = (ImageView) itemView.findViewById( R.id.ivPosterImage );
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview );
            tvTitle = (TextView) itemView.findViewById( R.id.tvTitle);
        }
    }
}
