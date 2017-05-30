package id.odt.museek.viewholder;


import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import id.odt.museek.R;

/**
 * Created by Admin on 9/12/2016.
 */
public class VHPlaylist extends RecyclerView.ViewHolder {
    public TextView title;
    public TextView desc;
    public ImageView imgCover;
    public LinearLayout llItem;

    public VHPlaylist(View itemView) {
        super(itemView);
        title = (TextView)itemView.findViewById(R.id.title);
        desc = (TextView)itemView.findViewById(R.id.description);
        imgCover = (ImageView) itemView.findViewById(R.id.image);
        llItem = (LinearLayout) itemView.findViewById(R.id.llItem);

        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Bundle extras = new Bundle();
                //extras.putInt("position",getAdapterPosition());
                //intent.putExtras(extras);
            }
        });
    }
}

