package app.com.appfirebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Reena on 8/3/2017.
 */

public class FragRegister extends Fragment {

    private static final String TAG = FragRegister.class.getSimpleName();
    private static final int REQUEST_CODE_PICKER = 101;
    private ArrayList<Image> images = new ArrayList<>();

    @BindView(R.id.ivProfile)
    CircularImageView ivProfile;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.edtPerson)
    EditText edtPerson;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.edtPhone)
    EditText edtPhone;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.btnDate)
    Button btnDate;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    Unbinder unbinder;

    FirebaseDatabase firebaseDatabase;
    ProgressDialog progressDialog;
    MyApplication myApplication;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register, container, false);
        unbinder = ButterKnife.bind(this, view);

        ivProfile.setImageResource(R.drawable.shape_person_bg);

        myApplication = (MyApplication) getActivity().getApplication();
        firebaseDatabase = myApplication.getFirebaseDatabase();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btnDate, R.id.btnSubmit, R.id.ivProfile})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivProfile:

                Intent intent = new Intent(myApplication, ImagePickerActivity.class);
                intent.putExtra(ImagePickerActivity.INTENT_EXTRA_FOLDER_MODE, true);
                intent.putExtra(ImagePickerActivity.INTENT_EXTRA_MODE, ImagePickerActivity.MODE_SINGLE);
                intent.putExtra(ImagePickerActivity.INTENT_EXTRA_SHOW_CAMERA, true);
                intent.putExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES, images);
                intent.putExtra(ImagePickerActivity.INTENT_EXTRA_FOLDER_TITLE, "Album");
                intent.putExtra(ImagePickerActivity.INTENT_EXTRA_IMAGE_TITLE, "Tap to select images");
                intent.putExtra(ImagePickerActivity.INTENT_EXTRA_IMAGE_DIRECTORY, "Camera");
                startActivityForResult(intent, REQUEST_CODE_PICKER);

                //Toast.makeText(myApplication, "Heeee", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnDate:
                break;
            case R.id.btnSubmit:

                if (images.size() > 0) {
                    Image image = images.get(0);

                    Log.e(TAG, "image.getName: " + image.getName());


                    try {

                        final File file = new File(image.getPath());
                        String filename = file.getName();

                        StorageReference storageReference = myApplication.getFirebaseStorage().getReference().child("user_profiles");
                        StorageReference imageRef = storageReference.child(
                                filename);
                        UploadTask uploadTask = imageRef.putStream(new FileInputStream(
                                file
                        ));

                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Uri uri = taskSnapshot.getDownloadUrl();
                                saveData(uri.toString());
                            }
                        }).addOnProgressListener(getActivity(), new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                float per = (taskSnapshot.getBytesTransferred() * 100) /
                                        file.length();
                                // 50 * 100 / 200
                                /*Log.e(TAG, "onProgress: (" + taskSnapshot.getBytesTransferred()
                                        + " *100) / " + file.length());*/
                                Log.e(TAG, "onProgress: (" + per);
                            }
                        });

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                } else {
                    saveData("");
                }

                break;
        }
    }

    void saveData(String profile) {

        DatabaseReference databaseReference = firebaseDatabase.getReference("users");
        String userId = databaseReference.push().getKey();

        //create new user
        User user = new User(edtPerson.getText().toString(), profile,
                edtPhone.getText().toString(), System.currentTimeMillis());

        // progressDialog = ProgressDialog.show(getActivity(), "", "Please Wait", true);

        //pushing user to 'users' node using userId
        databaseReference.child(userId).setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: " + requestCode);

        if (requestCode == REQUEST_CODE_PICKER && resultCode == RESULT_OK && data != null) {
            images = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);

            Image image = images.get(0);
            File file = new File(image.getPath());
            String filename = file.getName();

            Log.e(TAG, "image.getName: " + filename);

            if (images.size() > 0) {
                Glide.with(getActivity())
                        .load(images.get(0).getPath())
                        .centerCrop()
                        .into(ivProfile);
            }
        }
    }

}
