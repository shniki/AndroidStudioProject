package com.example.androidstudioproject.activities.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.androidstudioproject.R;
import com.example.androidstudioproject.entities.Post;
import com.example.androidstudioproject.repositories.post.PostsViewModel;
import com.google.android.material.snackbar.Snackbar;

public class CreatePostFragment extends Fragment {

    PostsViewModel postsViewModel;
    String email;
    String location;
    Bitmap image;
    String url;
    int type;

    Button btnUpload; //fragEditAccount_save_btn
    Button btnLocation;//addLocation;
    EditText edtContent;//fragNew_userName_et3
    Button btnCamera;//frag_addP_cam_btn
    Button btnGallery;//frag_addP_gallery_btn
    ImageView visableImg; //frag_addP_iv_p

    public CreatePostFragment() {
        // Required empty public constructor
    }

    public static CreatePostFragment newInstance() {
        CreatePostFragment fragment = new CreatePostFragment();

        fragment.location=null;
        fragment.url=null;
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

    //TODO SET VIDEO
    public void setVideo(){
        //video=bm;
        visableImg.setImageBitmap(image);
        type=2;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtContent =view.findViewById(R.id.fragNew_userName_et3);
        btnLocation =  view.findViewById(R.id.addLocation);
        btnLocation.setOnClickListener(v->{
            //TODO sent to get location intent
            //then input to location
        });
        btnCamera =view.findViewById(R.id.frag_addP_cam_btn);
        btnCamera.setOnClickListener(v->{
            ((MainActivity)this.getActivity()).openCamera();
            // sent to take a new pic/vid intent
            //then input to bitmap
            //change datatype to 1 or 2
            // add firebase cloud
            //todo and update: visableImg
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
            url=null;
        });
        btnUpload =view.findViewById(R.id.fragEditAccount_save_btn);
        btnUpload.setOnClickListener(v->{
            if(edtContent== null) //validate input
            {
                Snackbar.make(view, R.string.empty_input, Snackbar.LENGTH_LONG).show();
                return;
            }
            String content=edtContent.getText().toString();

            if(TextUtils.isEmpty(content)&&type==0)
            {
                Snackbar.make(view, R.string.error_no_data, Snackbar.LENGTH_LONG).show();
                return;
            }

            if(content.length() > 140){
                Snackbar.make(view, R.string.content_too_long, Snackbar.LENGTH_LONG).show();
                return;
            }

         //   Post p = new Post(email,content,type,url,);
           // postsViewModel.add(p);
            //add media to url
            if(type==1)//image
                url =((MainActivity)getActivity()).getStorageModelFirebase().addImage(image);
            else if(type==2)
                url = null;//TODO ADD VIDEO

            if(url==null)//upload failed
            {
                Snackbar.make(view, R.string.media_upload_failed, Snackbar.LENGTH_LONG).show();
                return;
            }
           // Post p = new Post(email,content,type,url);
           // postsViewModel.add(p);

            ((MainActivity)getActivity()).replaceFragments(FeedFragment.class);
        });
    }

}