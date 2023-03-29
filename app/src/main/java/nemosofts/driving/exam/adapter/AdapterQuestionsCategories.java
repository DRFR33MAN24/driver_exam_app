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
import nemosofts.driving.exam.item.ItemQuestionsCat;
import nemosofts.driving.exam.item.ItemVideo;
import nemosofts.driving.exam.utils.ApplicationUtil;

public class AdapterQuestionsCategories extends RecyclerView.Adapter {

    private final List<ItemQuestionsCat> arrayList;
    private final RecyclerItemClickListener listener;

    public AdapterQuestionsCategories(List<ItemQuestionsCat> arrayList, RecyclerItemClickListener listener) {
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_q_cat,parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        final ItemQuestionsCat item = arrayList.get(position);

        ((ViewHolder) holder).q_cat_name.setText(item.getName());


        ((ViewHolder) holder).rl_item_q_cat.setOnClickListener(v -> listener.onClickListener(item, position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView q_cat_name;

        private final RelativeLayout rl_item_q_cat;

        public ViewHolder(View itemView) {
            super(itemView);
            q_cat_name = itemView.findViewById(R.id.q_cat_name);

            rl_item_q_cat = itemView.findViewById(R.id.rl_item_q_cat);
        }
    }

    public interface RecyclerItemClickListener{
        void onClickListener(ItemQuestionsCat itemData, int position);
    }

}
