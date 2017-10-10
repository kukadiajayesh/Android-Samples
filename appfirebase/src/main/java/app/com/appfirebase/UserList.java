package app.com.appfirebase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Reena on 8/3/2017.
 */

public class UserList extends Fragment {

    private static final String TAG = UserList.class.getSimpleName();

    Unbinder unbinder;
    FirebaseDatabase firebaseDatabase;
    @BindView(R.id.list)
    RecyclerView list;

    ArrayList<User> userArrayList = new ArrayList<>();
    CustomAdapter customAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        MyApplication myApplication = (MyApplication) getActivity().getApplication();

        firebaseDatabase = myApplication.getFirebaseDatabase();

        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        customAdapter = new CustomAdapter();
        list.setAdapter(customAdapter);

        firebaseDatabase.getReference("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "onDataChange: " + dataSnapshot.getValue());
                updateList(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    void updateList(DataSnapshot dataSnapshot) {
        userArrayList.clear();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            User user = snapshot.getValue(User.class);
            userArrayList.add(user);
        }
        customAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    class CustomAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity())
                    .inflate(R.layout.list_item_user, parent, false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MyHolder myHolder = (MyHolder) holder;

            User user = userArrayList.get(position);
            myHolder.tvUsername.setText(user.name);
            myHolder.tvMobile.setText(user.mobile);

            if (!TextUtils.isEmpty(user.profile)) {
                Glide.with(getActivity())
                        .load(user.profile)
                        .centerCrop()
                        .into(myHolder.ivProfile);
            } else {
                myHolder.ivProfile.setImageResource(R.drawable.shape_person_bg);
            }
        }

        @Override
        public int getItemCount() {
            return userArrayList.size();
        }

        class MyHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.ivProfile)
            CircularImageView ivProfile;
            @BindView(R.id.tvUsername)
            TextView tvUsername;
            @BindView(R.id.tvMobile)
            TextView tvMobile;

            MyHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
