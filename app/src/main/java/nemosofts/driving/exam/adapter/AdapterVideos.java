package nemosofts.driving.exam.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import nemosofts.driving.exam.R;
import nemosofts.driving.exam.item.ItemCat;
import nemosofts.driving.exam.item.ItemVideo;
import nemosofts.driving.exam.utils.ApplicationUtil;

public class AdapterVideos extends RecyclerView.Adapter {

    private final List<ItemVideo> arrayList;
    private final RecyclerItemClickListener listener;

    public AdapterVideos(List<ItemVideo> arrayList, RecyclerItemClickListener listener) {
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video,parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        final ItemVideo item = arrayList.get(position);

        ((ViewHolder) holder).vid_title.setText(item.getTitle());
        Picasso.get()
                .load(arrayList.get(position).getThumb())
                .placeholder(R.drawable.material_design_default)
                .into(((ViewHolder) holder).vid_image);

        ((ViewHolder) holder).rl_item_vid.setOnClickListener(v -> listener.onClickListener(item, position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView vid_title;
        private final ImageView vid_image;
        private final RelativeLayout rl_item_vid;

        public ViewHolder(View itemView) {
            super(itemView);
            vid_title = itemView.findViewById(R.id.vid_title);
            vid_image = itemView.findViewById(R.id.vid_image);
            rl_item_vid = itemView.findViewById(R.id.rl_item_vid);
        }
    }

    public interface RecyclerItemClickListener{
        void onClickListener(ItemVideo itemData, int position);
    }

}
