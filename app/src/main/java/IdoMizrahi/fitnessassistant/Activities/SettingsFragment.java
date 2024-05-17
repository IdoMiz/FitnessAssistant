package IdoMizrahi.fitnessassistant.Activities;


import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Outline;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide; // Import Glide library
import com.bumptech.glide.request.target.SimpleTarget; // Import SimpleTarget for Glide
import com.bumptech.glide.request.transition.Transition; // Import Transition for Glide


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.model.User;

import com.example.viewmodel.GenericViewModelFactory;
import com.example.viewmodel.ImageViewModel;
import com.example.viewmodel.MealViewModel;
import com.example.viewmodel.UserViewModel;


import java.io.IOException;

import IdoMizrahi.fitnessassistant.R;

public class SettingsFragment extends Fragment {

    private ImageView profilePicture;
    private TextView userName, progress;
    private Button logOutBtn;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private TextView daysInAppTV;
    private TextView editProfile;
    private ImageViewModel imageViewModel;
    private UserViewModel userViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);
    }

    public void initializeViews(View view){
        profilePicture = view.findViewById(R.id.userImage);
        userName = view.findViewById(R.id.userNameTV);
        progress = view.findViewById(R.id.progressTV);
        logOutBtn = view.findViewById(R.id.logOutBtn);
        editProfile = view.findViewById(R.id.editProfileTV);
        daysInAppTV = view.findViewById(R.id.daysInAppTV);

        User user = BaseActivity.getLoggedInUser();
        if(user != null) {
            userName.setText(user.getName());
            daysInAppTV.setText(String.valueOf(user.getDaysInApp()));
        }
        GenericViewModelFactory<ImageViewModel> factory1 = new GenericViewModelFactory<>(getActivity().getApplication(), ImageViewModel::new);
        imageViewModel = new ViewModelProvider(this, factory1).get(ImageViewModel.class);

        GenericViewModelFactory<UserViewModel> factory2 = new GenericViewModelFactory<>(getActivity().getApplication(), UserViewModel::new);
        userViewModel = new ViewModelProvider(this, factory2).get(UserViewModel.class);
        if(user.getProfilePictureId() != null && !user.getProfilePictureId().isEmpty()){
            imageViewModel.getImage(user.getProfilePictureId());
            imageViewModel.getImageMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Uri>() {
                @Override
                public void onChanged(Uri uri) {
                    Glide.with(requireContext())
                            .asBitmap()
                            .load(uri)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    profilePicture.setImageBitmap(resource);
                                }
                            });
                }
            });
        }

        setImageCircle();

        setListeners();
    }

    private void setListeners() {
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearUserFromSharedPreferences(requireContext());
            }
        });
        progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProgressActivity.class);
                startActivity(intent);
            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profilePicture.setClickable(false);
                showOptionsDialog();
            }
        });
    }

    private void showOptionsDialog() {
        final String[] options = {"Take a Picture", "Choose a Picture", "Cancel"};
        profilePicture.setClickable(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose an Option")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                takePicture();
                                break;
                            case 1:
                                openGallery();
                                break;
                            case 2:
                                Toast.makeText(getContext(), "Canceled!", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void takePicture() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    profilePicture.setImageBitmap(imageBitmap);
                    if(BaseActivity.getLoggedInUser().getProfilePictureId() != null && !BaseActivity.getLoggedInUser().getProfilePictureId().isEmpty()){
                        imageViewModel.update(BaseActivity.getLoggedInUser().getProfilePictureId(), imageBitmap);
                    }
                    else{
                        imageViewModel.add(imageBitmap, BaseActivity.getLoggedInUser());
                    }
                    User user = BaseActivity.getLoggedInUser();
                    user.setProfilePictureId(user.getIdFs());
                    userViewModel.update(user);
                }
            } else if (requestCode == PICK_IMAGE_REQUEST && data != null) {
                Uri selectedImageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                    profilePicture.setImageBitmap(bitmap);
                    if(BaseActivity.getLoggedInUser().getProfilePictureId() != null && !BaseActivity.getLoggedInUser().getProfilePictureId().isEmpty()){
                        imageViewModel.update(BaseActivity.getLoggedInUser().getProfilePictureId(), bitmap);
                    }
                    else{
                        imageViewModel.add(bitmap, BaseActivity.getLoggedInUser());
                    }
                    User user = BaseActivity.getLoggedInUser();
                    user.setProfilePictureId(user.getIdFs());
                    userViewModel.update(user);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    public void clearUserFromSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("savedUserId");
        editor.apply();
        Toast.makeText(context, "Logged Out!", Toast.LENGTH_SHORT).show();
        BaseActivity.setLoggedInUser(null);
        if (getActivity() != null) {
            getActivity().finish();
        }

    }
    private void setImageCircle() {
        profilePicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
        profilePicture.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                int size = Math.min(view.getWidth(), view.getHeight());
                float radius = size / 2f;
                outline.setRoundRect(0, 0, size, size, radius);
            }
        });
        profilePicture.setClipToOutline(true);
    }
}