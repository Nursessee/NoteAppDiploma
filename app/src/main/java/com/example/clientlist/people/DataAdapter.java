package com.example.clientlist.people;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clientlist.R;
import com.example.clientlist.database.Client;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolderData> {
    private List<Client> clientListArray;
    private AdapterOnItemClicked adapterOnItemClicked;
    private Context context;
    private SharedPreferences def_pref;



    private int[] colorArray = {R.drawable.circle_green , R.drawable.circle_red, R.drawable.circle_blue};

    public DataAdapter(List<Client> clientListArray, AdapterOnItemClicked adapterOnItemClicked, Context context) {
        this.clientListArray = clientListArray;
        this.adapterOnItemClicked = adapterOnItemClicked;
        this.context = context;
        def_pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolderData onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolderData(view, adapterOnItemClicked);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolderData holder, int position) {
        holder.setData(clientListArray.get(position));
    }

    @Override
    public int getItemCount() {
        return clientListArray.size();
    }

    public class ViewHolderData extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvName;
        TextView tvTel;
        ImageView imSpecial;
        private AdapterOnItemClicked adapterOnItemClicked;

        ImageView imImportance;
        TextView tvSecName;
        public ViewHolderData(@NonNull @NotNull View itemView, AdapterOnItemClicked adapterOnItemClicked) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            imImportance = itemView.findViewById(R.id.imImportance);
            imSpecial = itemView.findViewById(R.id.imSpecial);
            tvSecName = itemView.findViewById(R.id.tvSecName);
            tvTel = itemView.findViewById(R.id.tvTel);
            this.adapterOnItemClicked = adapterOnItemClicked;
            itemView.setOnClickListener(this);

        }

        public void setData(Client client) {
                tvName.setTextColor(Color.parseColor(def_pref.getString(context.getResources()
                        .getString(R.string.text_name_color_key), "#000000")));
                tvSecName.setTextColor(Color.parseColor(def_pref.getString(context.getResources()
                        .getString(R.string.text_sec_name_color_key), "#000000")));
                tvName.setText(client.getName());
                tvSecName.setText(client.getSec_name());
                tvTel.setText(client.getNumber());
                imImportance.setImageResource(colorArray[client.getImportance()]);
                if(client.getSpecial() ==1)
                {
                    imSpecial.setVisibility(View.VISIBLE);
                }
                else
                {
                    imSpecial.setVisibility(View.GONE);
                }

        }

        @Override
        public void onClick(View v) {
            adapterOnItemClicked.onAdapterItemClicked(getAdapterPosition());

        }
    }

    public interface AdapterOnItemClicked{
        void onAdapterItemClicked(int position);
    }
    public void updateAdapter(List <Client> clientList)
    {
            clientListArray.clear();
            clientListArray.addAll(clientList);
            notifyDataSetChanged();
    }

}
