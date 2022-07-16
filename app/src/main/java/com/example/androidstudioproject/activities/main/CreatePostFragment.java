package com.example.androidstudioproject.activities.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.androidstudioproject.R;
import com.example.androidstudioproject.entities.Post;
import com.example.androidstudioproject.repositories.post.PostsViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.material.snackbar.Snackbar;

public class CreatePostFragment extends Fragment {
    PostsViewModel postsViewModel;
    String email;
    String location;
    Bitmap image;
    Uri video;
    String url;
    int type;

    int PLACE_PICKER_REQUEST=1;

    Button btnUpload; //fragEditAccount_save_btn
    Button btnLocation;//addLocation;
    EditText edtContent;//fragNew_userName_et3
    ImageButton btnCamera;//frag_addP_cam_btn
    ImageButton btnGallery;//frag_addP_gallery_btn
    ImageView visableImg; //frag_addP_iv_p
    int status;
    public CreatePostFragment() {
        // Required empty public constructor
    }

    public static CreatePostFragment newInstance() {
        CreatePostFragment fragment = new CreatePostFragment();

        fragment.location="";
        fragment.url="";
        fragment.type=0;
        fragment.image=null;

        return fragment;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        postsViewModel = ((MainActivity)getActivity()).getPostViewModel();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)this.getActivity()).currentFragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_post, container, false);
    }

    public void setImage(Bitmap bm){
        image=bm;
        visableImg.setImageBitmap(image);
        type=1;
    }

    public void setVideo(Uri uri){
        video=uri;
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.isMemoryCacheable();
        Glide.with(getContext()).setDefaultRequestOptions(requestOptions)
                .load(video)
                .into(visableImg);
        //visableImg.setImageBitmap(thumbnail);
        type=2;
    }
    public void setLocation(String location_){
        this.location=location_;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtContent =view.findViewById(R.id.fragNew_userName_et3);
        btnLocation =  view.findViewById(R.id.addLocation);


        btnLocation.setOnClickListener(v->{

            PlacePicker.IntentBuilder builder=new PlacePicker.IntentBuilder();
            try {
                startActivityForResult(builder.build(getActivity()),PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        });
        btnCamera =view.findViewById(R.id.frag_addP_cam_btn);
        btnCamera.setOnClickListener(v->{
            ((MainActivity)this.getActivity()).openCamera();
            // sent to take a new pic/vid intent
            //then input to bitmap
            //change datatype to 1 or 2
            // add firebase cloud
            // and update: visableImg
        });
        btnGallery =view.findViewById(R.id.frag_addP_gallery_btn);
        btnGallery.setOnClickListener(v->{
            ((MainActivity)this.getActivity()).pickMediaFromGallery();
            // sent to get pic/vid from gallery intent
            //then input to bitmap
        });
        visableImg =view.findViewById(R.id.frag_addP_iv_p);
        visableImg.setOnClickListener(v->{
            visableImg.setImageResource(R.drawable.camera);
            image=null;
            url="";
        });
        btnUpload =view.findViewById(R.id.fragEditAccount_save_btn);
        btnUpload.setOnClickListener(v->{
            setNonAble();

            if(edtContent== null) //validate input
            {
                setAble();
                Snackbar.make(view, R.string.empty_input, Snackbar.LENGTH_LONG).show();
                return;
            }
            String content=edtContent.getText().toString();

            if(TextUtils.isEmpty(content)&&type==0)
            {
                setAble();
                Snackbar.make(view, R.string.error_no_data, Snackbar.LENGTH_LONG).show();
                return;
            }

            int len = content.length();
            if(len > 140){
                setAble();
                Snackbar.make(view, R.string.content_too_long, Snackbar.LENGTH_LONG).show();
                return;
            }

            email = ((MainActivity)getActivity()).getCurrEmail();

            if(location==null)
                location="";

            //add media to url
            url = "";
            Post p = new Post(email, content, type, url, location);


            if(type==1)//image
            {

                if(image!=null)
                    ((MainActivity) getActivity()).getStorageViewModel().addImageAndUploadPost(this, image, p);
                else
                {
                    setAble();
                    Snackbar.make(getView(), R.string.media_upload_failed, Snackbar.LENGTH_LONG).show();
                    return;
                }
            }
            else if(type==2) {
                if(video!=null)
                    ((MainActivity) getActivity()).getStorageViewModel()
                            .addVideoAndUploadPost(this, video, p);
                else
                {
                    setAble();
                    Snackbar.make(getView(), R.string.media_upload_failed, Snackbar.LENGTH_LONG).show();
                    return;
                }
            }

           // Post p = new Post(email,content,type,url);
           // postsViewModel.add(p);
            if(type==0) {
                postsViewModel.add(p);
                Snackbar.make(getView(), R.string.post_finish_upload, Snackbar.LENGTH_LONG).show();
            }

            //((MainActivity)getActivity()).onBackPressed();
        });
    }


    private void setNonAble(){
        btnUpload.setClickable(false);
        btnGallery.setClickable(false);
        btnCamera.setClickable(false);
        btnLocation.setClickable(false);
        edtContent.setEnabled(false);
        visableImg.setClickable(false);
    }

    private void setAble(){
        btnUpload.setClickable(true);
        btnGallery.setClickable(true);
        btnCamera.setClickable(true);
        btnLocation.setClickable(true);
        edtContent.setEnabled(true);
        visableImg.setClickable(true);

    }
}