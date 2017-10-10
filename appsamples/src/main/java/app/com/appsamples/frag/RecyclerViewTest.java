package app.com.appsamples.frag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app.com.appsamples.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Reena on 8/26/2017.
 */

public class RecyclerViewTest extends Fragment {

    @BindView(R.id.list)
    RecyclerView list;
    Unbinder unbinder;

    ArrayList<String> data = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_recyclerview, container, false);
        unbinder = ButterKnife.bind(this, view);

        data.add("Windows");
        data.add("Mac Os");
        data.add("Linux");
        data.add("Solar");

        MyAdapter myAdapter = new MyAdapter();
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(myAdapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    class MyAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_recyclerview, parent, false);
            return new CustomHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            CustomHolder customHolder = (CustomHolder) holder;
            customHolder.text1.setText(data.get(position));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "Click", Toast.LENGTH_SHORT).show();
                }
            });
        }


        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    class CustomHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text1)
        TextView text1;

        public CustomHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
