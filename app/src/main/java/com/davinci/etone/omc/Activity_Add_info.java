package com.davinci.etone.omc;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Activity_Add_info extends AppCompatActivity {
    TextView pageTitle,error_text_action;
    static TextView error_txt;
    ImageView back,ok;
    User user=new User();
    Button setImage;
    EditText infotitle,url;
    View item_info;
    Uri imageUri;
    com.github.siyamed.shapeimageview.RoundedImageView image_apercu;
    String path;
    com.github.siyamed.shapeimageview.RoundedImageView image;
    ProgressBar info_progressBar,action_progressBar,progressBar_history;
    RecyclerView history_recyclerview;
    ScrollView add_info_container,add_action_container,add_history_container;
    FirebaseAuth auth= FirebaseAuth.getInstance();
    FirebaseStorage storageRef=FirebaseStorage.getInstance();
    private FirebaseDatabase Db=FirebaseDatabase.getInstance();
    ArrayList<Info> infos;
    ViewHolderHistoryInfos viewHolderHistoryInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__add_info);
        final String    pageType=getIntent().getStringExtra("type");
        ok=findViewById(R.id.ok);
        back=findViewById(R.id.back);
        item_info=findViewById(R.id.info_apercu);
        TextView text_apercu=item_info.findViewById(R.id.text_info);
        image_apercu=item_info.findViewById(R.id.image_info);
        infotitle=findViewById(R.id.title);
        url=findViewById(R.id.url);
        info_progressBar=findViewById(R.id.info_progressBar);
        action_progressBar=findViewById(R.id.action_progressBar);
        add_history_container=findViewById(R.id.add_history_container);
        history_recyclerview=findViewById(R.id.history_recyclerview);
        progressBar_history=findViewById(R.id.progressBar_history);
        setImage=findViewById(R.id.image);
        error_txt=findViewById(R.id.error_txt);
        add_info_container=findViewById(R.id.add_info_container);
        add_action_container=findViewById(R.id.add_action_container);
        pageTitle=findViewById(R.id.page_title);

        infos=new ArrayList<>();
        history_recyclerview.setHasFixedSize(true);
        history_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        viewHolderHistoryInfos =new ViewHolderHistoryInfos(this,infos);
        history_recyclerview.setAdapter(viewHolderHistoryInfos);

        if(pageType.equals("info")){
            add_info_container.setVisibility(View.VISIBLE);
            add_action_container.setVisibility(View.GONE);
            add_history_container.setVisibility(View.GONE);
            pageTitle.setText("Ajouter une Info");
        }
        else if(pageType.equals("action")){

            add_info_container.setVisibility(View.GONE);
            add_history_container.setVisibility(View.GONE);
            add_action_container.setVisibility(View.VISIBLE);
            pageTitle.setText("Ajouter une Action");
        }
        else if(pageType.equals("history")){

            add_info_container.setVisibility(View.GONE);
            add_history_container.setVisibility(View.VISIBLE);
            add_action_container.setVisibility(View.GONE);
            pageTitle.setText("Toutes les infos");
            ok.setVisibility(View.GONE);
            DatabaseReference refHistory=Db.getReference().child("Info");
            Calendar  calendar=Calendar.getInstance();
            long currentTimestamp=calendar.getTimeInMillis()/1000;
            calendar.add(Calendar.DAY_OF_YEAR, -7);
            long OneWeekAgoTimestamp=calendar.getTimeInMillis()/1000;
            progressBar_history.setVisibility(View.VISIBLE);
            refHistory.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    infos.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Info test = postSnapshot.getValue(Info.class);
                        if(test.getTime()<currentTimestamp && test.getTime()>OneWeekAgoTimestamp ){
                            infos.add(test);
                        }
                    }
                    Log.i("History","History size:"+infos.size());
                    viewHolderHistoryInfos.notifyDataSetChanged();
                    progressBar_history.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        ok.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                if(add_info_container.getVisibility()==View.VISIBLE){
                    Info info=new Info();
                    info_progressBar.setVisibility(View.VISIBLE);
                    if(!url.getText().toString().startsWith("http://") || !url.getText().toString
                            ().startsWith("https://") || url.getText().toString().isEmpty() ){
                        info.setPath(url.getText().toString());
                    }
                    else {
                        error_txt.setText("Veuillez ajouter un lien url a votre info");
                        error_txt.setVisibility(View.VISIBLE);
                        info_progressBar.setVisibility(View.GONE);
                    }
                    if(!infotitle.getText().toString().isEmpty() && !infotitle.getText().toString
                            ().equals("titre")){
                        info.setTitle(infotitle.getText().toString());
                    }
                    else {
                        error_txt.setText("Veuillez ajouter un titre a votre info");
                        error_txt.setVisibility(View.VISIBLE);
                        info_progressBar.setVisibility(View.GONE);
                    }

                    if(path!=null){
                        Bitmap bitmap = ((BitmapDrawable) image_apercu.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        final java.text.DateFormat formatterTime=new SimpleDateFormat("dd_MM_yyyy_hh_mm");
                        Calendar  calendar=Calendar.getInstance();
                        String filename=formatterTime.format(calendar.getTime())+".jpg";
                        StorageReference imagesRef = FirebaseStorage.getInstance().getReference()
                                .child
                                ("images/"+filename);
                        UploadTask uploadTask = imagesRef.putBytes(data);
                        // Register observers to listen for when the download is done or if it fails
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imagesRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {

                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        info.setImage(task.getResult().toString());

                                        info.setTime(calendar.getTimeInMillis()/1000);
                                        Log.i("Images","Firestorage image Path : "+imagesRef
                                                .getDownloadUrl().toString());
                                        final DatabaseReference refLit=Db.getReference("Info");
                                        final String Key=refLit.push().getKey();
                                        refLit.child(Key).setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {

                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(Activity_Add_info.this, "Information ajoutee " +
                                                        "avec succes",Toast.LENGTH_SHORT).show();
                                                info_progressBar.setVisibility(View.GONE);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Activity_Add_info.this, "Erreur lors de " +
                                                                "la creation de l'Info",
                                                        Toast.LENGTH_SHORT).show();
                                                info_progressBar.setVisibility(View.GONE);
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                    else {
                        error_txt.setText("Veuillez ajouter une image d'affiche a votre info");
                        error_txt.setVisibility(View.VISIBLE);
                        info_progressBar.setVisibility(View.GONE);
                    }
                }

                else if(add_action_container.getVisibility()==View.VISIBLE){

                }

            }
        });
        infotitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                text_apercu.setText(charSequence);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                text_apercu.setText(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        setImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture(image_apercu);
            }
        });
    }

    public void choosePicture( com.github.siyamed.shapeimageview.RoundedImageView image1){
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK  && data!=null && data.getData()!=null){
            imageUri=data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 300,
                        170, true);
                path=getPathFromUri(this,imageUri);
                Log.i("Images","image Path : "+path);
                image_apercu.setImageBitmap(newBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String getDate(long time) {
        java.util.Calendar cal = java.util.Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("dd-MM-yyyy HH:mm", cal).toString();
        return date;
    }

    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        }finally {
            if (cursor != null)
                cursor.close();
            else
            {error_txt.setText("Image non-valide, Essayez une autre!");
                error_txt.setVisibility(View.VISIBLE);}
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

}
