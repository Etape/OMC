package com.davinci.etone.omc;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.system.Os;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aspose.cells.Workbook;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opencsv.CSVReader;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.davinci.etone.omc.R.color.colorAccent;
import static javax.xml.datatype.DatatypeConstants.DURATION;

public class Activity_dashboard extends AppCompatActivity {
    ImageView home,tchat,occupancy,inscriptions,rh,but_homeMil,but_tchatMil,but_addInsMil,back,menu;
    User user;
    View first,last;
    FirebaseAuth auth= FirebaseAuth.getInstance();
    TextView dashboard_stat1,dashboard_stat2,dashboard_stat3,dashboard_stat4,dashboard_stat5,
            dashboard_stat6,dashboard_rh_stat1,dashboard_rh_stat2,dashboard_rh_stat3,
            dashboard_rh_stat4,dashboard_occ_stat1,dashboard_occ_stat2,dashboard_occ_stat3,
            dashboard_occ_stat4,dashboard_ins_stat1,dashboard_ins_stat2,dashboard_ins_stat3,
            dashboard_ins_stat4;
    FirebaseUser Ui=auth.getCurrentUser();
    TextView see_history,no_result_rh,no_result_occ,no_result_ins;
    LinearLayout filter,dashboard_rh_contain,dashboard_ins_contain,dashboard_occupancy_contain,bottomMenuMil;
    RelativeLayout dashboard,dashboard_rh,dashboard_occupancy,dashboard_tchat,
    dashboard_inscription,bottomMenu;
    ImageView add_registration,add_tchat,add_bv,close_selection,add_pre,add_ins;
    ImageView but_filter_rh,but_filter_bv,but_filter_ins;
    LinearLayout select_registration,results_container_rh,results_container_occupancy,
            results_container_ins,layout_period,human_filter,new_disc_box;
    ImageView back_rh,back_occupancy,back_ins;
    ProgressBar search_rh_progressBar,search_occupancy_progressBar,search_ins_progressBar,
    progressBarLoading,progressBar_create_disc;
    AutoCompleteTextView research_bar_rh,research_bar_ins,research_bar_occupancy,disc_destinataire;
    AutoCompleteTextView search_region,search_dep,search_commune,search_pays,search_origine,
            search_sexe;
    EditText search_startDate,search_endDate,search_age,search_endAge,disc_title;
    Button search_valider,but_create_disc;
    RadioGroup filter_select_where;
    RadioButton radio_militant,radio_Dec,radio_Der,radio_Dep,radio_BV,radio_pre,radio_ins,radio_dep,
            radio_commune, radio_reg, radio_pays,radio_private,radio_sms,radio_forum;
    RecyclerView recylerViewBv, recylerViewIns, recylerViewPre, recylerViewMil, recylerViewCom,
            recylerViewDep, recylerViewReg,private_recyclerview,forum_recyclerview,
            sms_recyclerview;

    ViewHolderDiscussions viewHolderForum,viewHolderPrivate,viewHolderSms;
    ViewHolderMilitants viewHolderMil;
    ViewHolderBv viewHolderBv;
    ViewHolderInscriptions viewHolderIns;
    ViewHolderPreinscriptions viewHolderPre;
    ViewHolderCommunes viewHolderCom;
    ViewHolderDepartements viewHolderDep;
    ViewHolderRegions viewHolderReg;
    ImageView but_search_rh,but_search_ins,but_search_occupancy;
    private FirebaseDatabase Db=FirebaseDatabase.getInstance();
    View forum1,forum2,forum3,sms1,sms2,hide_filter;
    ArrayList<String> communes=new ArrayList<>();
    ArrayList<String> regions=new ArrayList<>();
    ArrayList<String> sexe=new ArrayList<>();
    ArrayList<String[]> rdc=new ArrayList<>();
    ArrayList<String> departements=new ArrayList<>();
    ArrayList<String> postes=new ArrayList<>();
    ArrayList<String> bv=new ArrayList<>();
    ArrayList<String> Oui_Non=new ArrayList<>();
    ArrayList<String> pays=new ArrayList<>();
    ArrayList<String> millNames=new ArrayList<>();
    ArrayList<String> preNames=new ArrayList<>();
    ArrayList<String> insNames=new ArrayList<>();

    ArrayList<Bv> ResultsBv= new ArrayList<>();
    ArrayList<User> ResultsMil= new ArrayList<>();
    ArrayList<Inscription> ResultsIns= new ArrayList<>();
    ArrayList<Preinscription> ResultsPre= new ArrayList<>();
    ArrayList<Commune> ResultsCom= new ArrayList<>();
    ArrayList<Departement> ResultsDep= new ArrayList<>();
    ArrayList<Region> ResultsReg= new ArrayList<>();
    ArrayList<Bv> allBv=new ArrayList<>();
    ArrayList<User> allUsers=new ArrayList<>();
    ArrayList<User> allMil=new ArrayList<>();
    ArrayList<Preinscription> allPre=new ArrayList<>();
    ArrayList<Inscription> allIns=new ArrayList<>();
    ArrayList<Discussion> forums=new ArrayList<>();
    ArrayList<Discussion> privateDiscs=new ArrayList<>();
    ArrayList<Discussion> smsDiscs=new ArrayList<>();
    DisplayMetrics displayMetrics;
    int nbrePremois=0,nbrePrejour=0,nbreInsmois=0,nbreInsjour=0;
    Postes poste;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);
        sexe.add("Homme");
        sexe.add("Femme");
        Oui_Non.add("OUI");
        Oui_Non.add("NON");

        Intent serviceIntent = new Intent(Activity_dashboard.this, Service_messaging.class);
        startService(serviceIntent);

        first=findViewById(R.id.first);
        last=findViewById(R.id.last);

        radio_private=findViewById(R.id.radio_private);
        radio_sms=findViewById(R.id.radio_sms);
        radio_forum=findViewById(R.id.radio_forum);
        but_create_disc=findViewById(R.id.but_create_disc);
        disc_title=findViewById(R.id.disc_title);
        disc_destinataire=findViewById(R.id.disc_destinataire);
        progressBar_create_disc=findViewById(R.id.progressBar_create_disc);
        new_disc_box=findViewById(R.id.new_disc_box);

        recylerViewMil=findViewById(R.id.recyclerviewMil);
        recylerViewBv=findViewById(R.id.recyclerviewBv);
        recylerViewIns=findViewById(R.id.recyclerviewIns);
        recylerViewPre=findViewById(R.id.recyclerviewPre);
        recylerViewCom=findViewById(R.id.recyclerviewCom);
        recylerViewReg=findViewById(R.id.recyclerviewReg);
        recylerViewDep=findViewById(R.id.recyclerviewDep);
        private_recyclerview=findViewById(R.id.private_recyclerview);
        forum_recyclerview=findViewById(R.id.forum_recyclerview);
        sms_recyclerview=findViewById(R.id.sms_recyclerview);
        recylerViewMil.setHasFixedSize(true);
        recylerViewBv.setHasFixedSize(true);
        recylerViewIns.setHasFixedSize(true);
        recylerViewPre.setHasFixedSize(true);
        recylerViewCom.setHasFixedSize(true);
        recylerViewReg.setHasFixedSize(true);
        recylerViewDep.setHasFixedSize(true);
        private_recyclerview.setHasFixedSize(true);
        forum_recyclerview.setHasFixedSize(true);
        sms_recyclerview.setHasFixedSize(true);

        private_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        forum_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        sms_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recylerViewMil.setLayoutManager(new LinearLayoutManager(this));
        recylerViewBv.setLayoutManager(new LinearLayoutManager(this));
        recylerViewIns.setLayoutManager(new LinearLayoutManager(this));
        recylerViewPre.setLayoutManager(new LinearLayoutManager(this));
        recylerViewCom.setLayoutManager(new LinearLayoutManager(this));
        recylerViewReg.setLayoutManager(new LinearLayoutManager(this));
        recylerViewDep.setLayoutManager(new LinearLayoutManager(this));
        viewHolderMil =new ViewHolderMilitants(this,ResultsMil);
        viewHolderBv =new ViewHolderBv(this,ResultsBv);
        viewHolderIns =new ViewHolderInscriptions(this,ResultsIns);
        viewHolderPre =new ViewHolderPreinscriptions(this,ResultsPre);
        viewHolderCom =new ViewHolderCommunes(this,ResultsCom);
        viewHolderReg =new ViewHolderRegions(this,ResultsReg);
        viewHolderDep =new ViewHolderDepartements(this,ResultsDep);
        recylerViewMil.setAdapter(viewHolderMil);
        recylerViewBv.setAdapter(viewHolderBv);
        recylerViewIns.setAdapter(viewHolderIns);
        recylerViewPre.setAdapter(viewHolderPre);
        recylerViewReg.setAdapter(viewHolderReg);
        recylerViewCom.setAdapter(viewHolderCom);
        recylerViewDep.setAdapter(viewHolderDep);

        // stats

        dashboard_stat1=findViewById(R.id.dashboard_stat1); // commune la plus active du mois
        dashboard_stat2=findViewById(R.id.dashboard_stat2); // couverture des Bv
        dashboard_stat3=findViewById(R.id.dashboard_stat3); // preins du jour
        dashboard_stat4=findViewById(R.id.dashboard_stat4); // Ins du jour
        dashboard_stat5=findViewById(R.id.dashboard_stat5); // 18,19
        dashboard_stat6=findViewById(R.id.dashboard_stat6); // diaspora
        dashboard_rh_stat1=findViewById(R.id.dashboard_rh_stat1); //commune la plus active du mois
        dashboard_rh_stat2=findViewById(R.id.dashboard_rh_stat2); // militant le plus actif du mois
        dashboard_occ_stat1=findViewById(R.id.dashboard_occupancy_stat1);//commune la plus active du mois
        dashboard_occ_stat2=findViewById(R.id.dashboard_occupancy_stat2); // couverture des Bv
        dashboard_ins_stat1=findViewById(R.id.dashboard_ins_stat1); // preins du mois
        dashboard_ins_stat2=findViewById(R.id.dashboard_ins_stat2); // preins du jour
        dashboard_rh_stat3=findViewById(R.id.dashboard_rh_stat3); // militant le plus actif du jour
        dashboard_rh_stat4=findViewById(R.id.dashboard_rh_stat4); // militants actif du jour
        dashboard_occ_stat3=findViewById(R.id.dashboard_occupancy_stat3); //Communes actives du jour
        dashboard_occ_stat4=findViewById(R.id.dashboard_occupancy_stat4); // Communes non actives du mois
        dashboard_ins_stat3=findViewById(R.id.dashboard_ins_stat3);// 18-19
        dashboard_ins_stat4=findViewById(R.id.dashboard_ins_stat4); // diaspora


        bottomMenu=findViewById(R.id.bottomMenu);
        progressBarLoading=findViewById(R.id.progressBarLoading);
        filter_select_where=findViewById(R.id.filter_select_where);
        radio_militant=findViewById(R.id.radio_militant);
        radio_BV=findViewById(R.id.radio_BV);
        radio_pre=findViewById(R.id.radio_pre);
        radio_Dec=findViewById(R.id.radio_Dec);
        radio_Der=findViewById(R.id.radio_Der);
        radio_Dep=findViewById(R.id.radio_Dep);
        radio_ins=findViewById(R.id.radio_insc);
        radio_reg=findViewById(R.id.radio_reg);
        no_result_rh=findViewById(R.id.no_results_rh);
        no_result_occ=findViewById(R.id.no_results_occupancy);
        no_result_ins=findViewById(R.id.no_results_ins);
        radio_pays=findViewById(R.id.radio_pays);
        layout_period=findViewById(R.id.layout_period);
        human_filter=findViewById(R.id.human_filter);
        radio_commune=findViewById(R.id.radio_commune);
        radio_dep=findViewById(R.id.radio_dep);
        search_valider=findViewById(R.id.search_valider);
        search_startDate=findViewById(R.id.search_startDate);
        search_endDate=findViewById(R.id.search_endDate);
        search_age=findViewById(R.id.search_age);
        search_endAge=findViewById(R.id.search_endAge);
        search_region=findViewById(R.id.search_region);
        search_dep=findViewById(R.id.search_dep);
        search_commune=findViewById(R.id.search_com);
        search_pays=findViewById(R.id.search_pays);
        search_origine=findViewById(R.id.search_origine);
        search_sexe=findViewById(R.id.search_sexe);

        research_bar_rh=findViewById(R.id.research_bar_rh);
        research_bar_ins=findViewById(R.id.research_bar_ins);
        research_bar_occupancy=findViewById(R.id.research_bar_occupancy);
        but_search_rh=findViewById(R.id.but_search_rh);
        but_search_ins=findViewById(R.id.but_search_ins);
        see_history=findViewById(R.id.see_history);
        but_search_occupancy=findViewById(R.id.but_search_occupancy);
        dashboard_rh_contain=findViewById(R.id.dashboard_rh_contain);
        dashboard_ins_contain=findViewById(R.id.dashboard_ins_contain);
        dashboard_occupancy_contain=findViewById(R.id.dashboard_occupancy_contain);


        but_addInsMil=findViewById(R.id.but_addInsMil);
        but_homeMil=findViewById(R.id.but_homeMil);
        but_tchatMil=findViewById(R.id.but_tchatMil);
        bottomMenuMil=findViewById(R.id.bottomMenuMil);

        home=findViewById(R.id.but_home);
        tchat=findViewById(R.id.but_tchat);
        occupancy=findViewById(R.id.but_occupancy);
        inscriptions=findViewById(R.id.but_addIns);
        rh=findViewById(R.id.but_group);
        menu=findViewById(R.id.menu);
        add_bv=findViewById(R.id.add_bv);
        add_pre=findViewById(R.id.add_pre);
        add_ins=findViewById(R.id.add_ins);
        add_tchat=findViewById(R.id.add_tchat);
        add_registration=findViewById(R.id.add_registration);
        select_registration=findViewById(R.id.select_registration);
        close_selection=findViewById(R.id.close_selection);
        dashboard=findViewById(R.id.dashboard);
        dashboard_rh=findViewById(R.id.dashboard_rh);
        dashboard_occupancy=findViewById(R.id.dashboard_occupancy);
        dashboard_tchat=findViewById(R.id.dashboard_tchat);
        dashboard_inscription=findViewById(R.id.dashboard_ins);
        but_filter_ins=findViewById(R.id.but_filter_ins);
        but_filter_rh=findViewById(R.id.but_filter_rh);
        but_filter_bv=findViewById(R.id.but_filter_occupancy);
        hide_filter=findViewById(R.id.hide_filter);
        filter=findViewById(R.id.filter_menu);

        back_ins=findViewById(R.id.back_ins);
        back_rh=findViewById(R.id.back_rh);
        back_occupancy=findViewById(R.id.back_occupancy);
        results_container_ins=findViewById(R.id.results_container_ins);
        results_container_rh=findViewById(R.id.results_container_rh);
        results_container_occupancy=findViewById(R.id.results_container_occupancy);
        search_ins_progressBar=findViewById(R.id.search_ins_progressBar);
        search_rh_progressBar=findViewById(R.id.search_rh_progressBar);
        search_occupancy_progressBar=findViewById(R.id.search_occupancy_progressBar);

        progressBarLoading.setVisibility(View.VISIBLE);

        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int height = displayMetrics.heightPixels-480;
        if(displayMetrics.heightPixels<=1300){
            height=displayMetrics.heightPixels-290;
        }
        if(displayMetrics.heightPixels>1300 & displayMetrics.heightPixels<1700){
            height=displayMetrics.heightPixels-230;
        }
        int width = displayMetrics.widthPixels;

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        params.addRule(RelativeLayout.BELOW,R.id.baner);

        dashboard.setLayoutParams(params);
        dashboard_rh.setLayoutParams(params);
        dashboard_occupancy.setLayoutParams(params);
        dashboard_tchat.setLayoutParams(params);
        dashboard_inscription.setLayoutParams(params);

        bottomMenu.setVisibility(View.GONE);
        dashboard.setVisibility(View.GONE);
        dashboard_rh.setVisibility(View.GONE);
        dashboard_occupancy.setVisibility(View.GONE);
        dashboard_tchat.setVisibility(View.GONE);
        dashboard_inscription.setVisibility(View.GONE);
        home.setBackgroundResource(R.color.colorAccent);
        home.setImageResource(R.drawable.ic_home_green_24dp);
        rh.setImageResource(R.drawable.ic_group_white_24dp);
        rh.setBackgroundResource(R.drawable.butt_raduis_up_left);
        tchat.setBackgroundResource(R.color.colorPrimaryDark);
        tchat.setImageResource(R.drawable.ic_textsms_white_24dp);
        occupancy.setBackgroundResource(R.color.colorPrimaryDark);
        occupancy.setImageResource(R.drawable.ic_occupancy_white_24dp);
        inscriptions.setBackgroundResource(R.color.colorPrimaryDark);
        inscriptions.setImageResource(R.drawable.ic_note_add_white_24dp);

        List<Info> infoList=new ArrayList<>();
        infoList.add(new Info("none","https://firebasestorage.googleapis.com/v0/b/omca2p-88fad" +
                ".appspot.com/o/images%2F19_09_2022%2Fimage_info1.png?alt=media&token=8af92e26-1e76-4d4b-980f-d62586c153b0"
                ,"Une Nouvelle victoire de l'OMC dans le WOURI","path",0));
        infoList.add(new Info("none","https://firebasestorage.googleapis.com/v0/b/omca2p-88fad.appspot.com/o/images%2F19_09_2022%2Fimage_info2.png?alt=media&token=5306a12f-41fd-4a7f-ac9c-ee30d47eb004"
                ,"Une Nouvelle victoire de l'OMC dans le WOURI","path",0));
        infoList.add(new Info("none","https://firebasestorage.googleapis.com/v0/b/omca2p-88fad.appspot.com/o/images%2F19_09_2022%2Fimage_info3.png?alt=media&token=a2c5151b-d015-4c85-bb9a-ab88ec418b7d","Une Nouvelle victoire de l'OMC dans le WOURI","path",0));
        infoList.add(new Info("none","https://firebasestorage.googleapis.com/v0/b/omca2p-88fad.appspot.com/o/images%2F19_09_2022%2Fimage_info4.png?alt=media&token=1fa6c9b6-0343-401d-9fe1-71bc9c1e3334"
                ,"Une Nouvelle victoire de l'OMC dans le WOURI","path",0));
        infoList.add(new Info("none","https://firebasestorage.googleapis.com/v0/b/omca2p-88fad.appspot.com/o/images%2F19_09_2022%2Fimage_info6.png?alt=media&token=49e475e9-6b75-483b-b133-ceee08571694","Une Nouvelle victoire de l'OMC dans le WOURI","path",0));

        SliderView sliderView = findViewById(R.id.imageSlider);
        sliderView.setScrollTimeInSec(infoList.size());
        sliderView.setSliderAdapter(new Slider_adapter(this,infoList));
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.startAutoCycle();

        bottomMenu.setVisibility(View.GONE);
        bottomMenuMil.setVisibility(View.GONE);
        dashboard.setVisibility(View.GONE);
        dashboard_rh.setVisibility(View.GONE);
        dashboard_occupancy.setVisibility(View.GONE);
        dashboard_tchat.setVisibility(View.GONE);
        dashboard_inscription.setVisibility(View.GONE);
        progressBarLoading.setVisibility(View.VISIBLE);

        String NOTIFICATION_CHANNEL_ID = "OMC_id_01";
        NotificationCompat.Builder b = new NotificationCompat.Builder(Activity_dashboard.this, NOTIFICATION_CHANNEL_ID);
        CharSequence name = "test";
        String description = "OMC_Notification";
        Intent intent=new Intent(Activity_dashboard.this,Activity_menu.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(Activity_dashboard.this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationChannel mChannel;
        b.setAutoCancel(true)
                .setSmallIcon(R.drawable.logo_omc2_50)
                .setContentTitle("Default notification")
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setContentText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(Activity_dashboard.this);
        notificationManager.notify(1, b.build());
        Log.i("Notification"," Notification Played");

        pays=LoadCountries();
        LoadCommunes();
        DatabaseReference refBv=Db.getReference().child("Bv");
        DatabaseReference refUs=Db.getReference().child("User");
        DatabaseReference refPre=Db.getReference().child("Preinscription");
        DatabaseReference refIns=Db.getReference().child("Inscription");
        refBv.keepSynced(true);
        refBv.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allBv.clear();
                Log.i("Firebase ","Firebase communes loading");
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Bv bv=postSnapshot.getValue(Bv.class);
                    allBv.add(bv);
                }


                final int[] couverture = {0};
                final int[] couvertureTotal = { allBv.size() };
                for (int i=0;i<allBv.size();i++){
                    Bv bv=allBv.get(i);
                    int count1=0,count2=0,count3=0,count4=0;
                    if(!bv.getBv_vol1_name().equals("none") & !bv.getBv_vol1_name().isEmpty() )
                        count1=1;
                    if(!bv.getBv_vol2_name().equals("none") & !bv.getBv_vol2_name().isEmpty() )
                        count2=1;
                    if(!bv.getBv_vol3_name().equals("none") & !bv.getBv_vol3_name().isEmpty() )
                        count3=1;
                    if(!bv.getBv_vol4_name().equals("none") & !bv.getBv_vol4_name().isEmpty() )
                        count4=1;
                    int count=count1+count2+count3+count4;
                    if(count>0){
                        couverture[0]++;
                    }
                }

                refUs.orderByKey().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        allUsers.clear();
                        allMil.clear();
                        millNames.clear();
                        Log.i("Firebase ","Firebase users loading");
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            User usr=postSnapshot.getValue(User.class);
                            allUsers.add(usr);
                            if(!usr.getType().equals("Citoyen") & !usr.getType().equals("citoyen") & !usr
                                    .getEmail().equals(Ui.getEmail())){
                                allMil.add(usr);
                                millNames.add(usr.getNom()+" "+usr.getPrenom());
                            }
                            if(usr.getEmail().equals(Ui.getEmail())){
                                user=usr;
                                poste= new Postes(user.getType());
                                Log.i("Firebase user type",user.getType());
                                Log.i("Firebase categorie",poste.getCategorie());

                                if (poste.getCategorie().equals("C") | poste.getCategorie().equals("D")){
                                    bottomMenuMil.setVisibility(View.VISIBLE);
                                    bottomMenu.setVisibility(View.GONE);
                                }
                                else{
                                    bottomMenu.setVisibility(View.VISIBLE);
                                    bottomMenuMil.setVisibility(View.GONE);
                                }

                                dashboard.setVisibility(View.VISIBLE);
                                dashboard_rh.setVisibility(View.GONE);
                                dashboard_occupancy.setVisibility(View.GONE);
                                dashboard_tchat.setVisibility(View.GONE);
                                dashboard_inscription.setVisibility(View.GONE);
                                progressBarLoading.setVisibility(View.GONE);

                                refPre.orderByKey().addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        allPre.clear();
                                        preNames.clear();
                                        ArrayList<Commune> coms = new ArrayList<>();
                                        ArrayList<User> mils = new ArrayList<>();
                                        ArrayList<Commune> comsM = new ArrayList<>();
                                        ArrayList<User> milsM = new ArrayList<>();
                                        int nbre1819 = 0;
                                        int nbreDiaspo = 0;
                                        Log.i("Firebase ", "Firebase Preinscriptions loading");
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                            Preinscription pre = postSnapshot.getValue(Preinscription.class);
                                            allPre.add(pre);
                                            preNames.add(pre.getNom() + " " + pre.getPrenom());
                                        }
                                        for (int i = 0; i < allPre.size(); i++) {
                                            if (!filterZonePre(poste, allPre.get(i), user)) {
                                                allPre.remove(i);
                                                preNames.remove(i);
                                            }
                                        }
                                        Log.i("Firebase", "Firebase Preinscriptions loaded");
                                        for (int i = 0; i < allPre.size(); i++) {
                                            if (calculateAge(Long.valueOf(allPre.get(i).getDate_naissance())) < 20)
                                                nbre1819++;
                                            if (!allPre.get(i).getPays().equals("Cameroun") & !allPre.get(i).getPays().equals("Cameroon"))
                                                nbreDiaspo++;
                                            long actual = System.currentTimeMillis() / 1000;
                                            if (actual - allPre.get(i).getCreation_date() < 24 * 3600) {
                                                nbrePrejour++;
                                                boolean test = true;
                                                if (!coms.isEmpty()) {
                                                    for (int j = 0; j < coms.size(); j++) {
                                                        if (allPre.get(i).getCommune().equals(coms.get(j).getCom_name())) {
                                                            coms.get(j).setActivite(coms.get(j).getActivite() + 1);
                                                            test = false;
                                                            break;
                                                        }
                                                    }
                                                }
                                                if (test == true)
                                                    coms.add(new Commune(allPre.get(i).getCommune()));
                                            }
                                            if (actual - allPre.get(i).getCreation_date() < 30 * 24 * 3600) {
                                                nbrePremois++;
                                                boolean test = true;
                                                if (!comsM.isEmpty()) {
                                                    for (int j = 0; j < comsM.size(); j++) {
                                                        if (allPre.get(i).getCommune().equals(comsM.get(j).getCom_name())) {
                                                            comsM.get(j).setActivite(comsM.get(j).getActivite() + 1);
                                                            test = false;
                                                            break;
                                                        }
                                                    }
                                                }
                                                if (test == true)
                                                    comsM.add(new Commune(allPre.get(i).getCommune()));
                                            }
                                            if (!allPre.get(i).getParrain().equals("none") & actual - allPre.get(i).getCreation_date() < 24 * 3600) {
                                                boolean test = true;
                                                if (!mils.isEmpty()) {
                                                    for (int j = 0; j < mils.size(); j++) {
                                                        if (allPre.get(i).getParrain().equals(mils.get(j).getId())) {
                                                            mils.get(j).setActivite(mils.get(j).getActivite() + 1);
                                                            test = false;
                                                            break;
                                                        }
                                                    }
                                                }
                                                if (test == true)
                                                    mils.add(new User(allPre.get(i).getParrain()));
                                            }
                                            if (!allPre.get(i).getParrain().equals("none") & actual - allPre.get(i).getCreation_date() < 30 * 24 * 3600) {
                                                boolean test = true;
                                                if (!milsM.isEmpty()) {
                                                    for (int j = 0; j < milsM.size(); j++) {
                                                        if (allPre.get(i).getParrain().equals(milsM.get(j).getId())) {
                                                            milsM.get(j).setActivite(milsM.get(j).getActivite() + 1);
                                                            test = false;
                                                            break;
                                                        }
                                                    }
                                                }
                                                if (test == true)
                                                    milsM.add(new User(allPre.get(i).getParrain()));
                                            }

                                        }
                                        refIns.orderByKey().addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                allIns.clear();
                                                insNames.clear();
                                                int nbreInsjour = 0;
                                                int nbreInsmois = 0;
                                                int nbreInsDiaspo = 0;
                                                Log.i("Firebase ", "Firebase Inscriptions loading");
                                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                    Inscription ins = postSnapshot.getValue(Inscription.class);
                                                    allIns.add(ins);
                                                    insNames.add(ins.getNom() + " " + ins.getPrenom());
                                                }
                                                for (int i = 0; i < allIns.size(); i++) {
                                                    if (!filterZoneIns(poste, allIns.get(i), user)) {
                                                        allIns.remove(i);
                                                        insNames.remove(i);
                                                    }
                                                }
                                                Log.i("Firebase ", "Firebase Inscriptions loaded");
                                                for (int i = 0; i < allIns.size(); i++) {
                                                    if (!allIns.get(i).getPays().equals("Cameroun") & !allIns.get(i).getPays()
                                                            .equals("Cameroon"))
                                                        nbreInsDiaspo++;
                                                    long actual = System.currentTimeMillis() / 1000;
                                                    if (actual - allIns.get(i).getCreation_date() < 24 * 3600) {
                                                        nbreInsjour++;
                                                        boolean test = true;
                                                        if (!coms.isEmpty()) {
                                                            for (int j = 0; j < coms.size(); j++) {
                                                                if (allIns.get(i).getCommune().equals(coms.get(j).getCom_name())) {
                                                                    coms.get(j).setActivite(coms.get(j).getActivite() + 1);
                                                                    test = false;
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                        if (test == true)
                                                            coms.add(new Commune(allIns.get(i).getCommune()));
                                                    }
                                                    if (actual - allIns.get(i).getCreation_date() < 30 * 24 * 3600) {
                                                        nbreInsmois++;
                                                        boolean test = true;
                                                        for (int j = 0; j < comsM.size(); j++) {
                                                            if (!comsM.isEmpty()) {
                                                                if (allIns.get(i).getCommune().equals(comsM.get(j).getCom_name())) {
                                                                    comsM.get(j).setActivite(comsM.get(j).getActivite() + 1);
                                                                    test = false;
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                        if (test == true)
                                                            comsM.add(new Commune(allPre.get(i).getCommune()));
                                                    }
                                                    if (!allIns.get(i).getParrain().equals("none") & actual - allIns.get(i).getCreation_date() < 24 * 3600) {
                                                        boolean test = true;
                                                        for (int j = 0; j < mils.size(); j++) {
                                                            if (!mils.isEmpty()) {
                                                                if (allIns.get(i).getParrain().equals(mils.get(j).getId())) {
                                                                    mils.get(j).setActivite(mils.get(j).getActivite() + 1);
                                                                    test = false;
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                        if (test == true)
                                                            mils.add(new User(allIns.get(i).getParrain()));
                                                    }

                                                    if (!allIns.get(i).getParrain().equals("none") & actual - allIns.get(i).getCreation_date() < 30 * 24 * 3600) {
                                                        boolean test = true;
                                                        for (int j = 0; j < milsM.size(); j++) {
                                                            if (!mils.isEmpty()) {
                                                                if (allIns.get(i).getParrain().equals(milsM.get(j).getId())) {
                                                                    milsM.get(j).setActivite(milsM.get(j).getActivite() + 1);
                                                                    test = false;
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                        if (test == true)
                                                            milsM.add(new User(allPre.get(i).getParrain()));
                                                    }
                                                }

                                                dashboard_ins_stat1.setText("" + nbrePremois);
                                                dashboard_ins_stat2.setText("" + nbrePrejour);
                                                dashboard_rh_stat3.setText("" + mostActiveU(mils).getNom());
                                                dashboard_rh_stat2.setText("" + mostActiveU(milsM).getNom());

                                                dashboard_occ_stat1.setText("" + mostActive(comsM).getCom_name());
                                                dashboard_stat1.setText("" + mostActive(comsM).getCom_name());
                                                dashboard_rh_stat1.setText("" + mostActive(comsM).getCom_name());


                                                dashboard_rh_stat4.setText("" + mils.size());
                                                dashboard_occ_stat3.setText("" + coms.size());
                                                dashboard_occ_stat4.setText("" + (communes.size() - coms.size()));
                                                dashboard_stat4.setText("" + nbreInsjour);
                                                if (nbreInsjour == 0) {
                                                    dashboard_stat4.setTextColor(ContextCompat.getColor(Activity_dashboard.this, R.color.red));
                                                }


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                        dashboard_stat6.setText("" + nbreDiaspo);
                                        dashboard_stat5.setText("" + nbre1819);
                                        dashboard_ins_stat1.setText("" + nbrePremois);
                                        dashboard_ins_stat2.setText("" + nbrePrejour);
                                        dashboard_stat3.setText("" + nbrePrejour);
                                        dashboard_ins_stat3.setText("" + nbre1819);
                                        dashboard_ins_stat4.setText("" + nbreDiaspo);

                                        if (nbre1819 == 0) {
                                            dashboard_stat5.setTextColor(ContextCompat.getColor(Activity_dashboard.this, R.color.red));
                                            dashboard_ins_stat3.setTextColor(ContextCompat.getColor(Activity_dashboard.this, R.color.red));
                                        }
                                        if (nbreDiaspo == 0) {
                                            dashboard_stat6.setTextColor(ContextCompat.getColor(Activity_dashboard.this, R.color.red));
                                            dashboard_ins_stat4.setTextColor(ContextCompat.getColor(Activity_dashboard.this, R.color.red));
                                        }
                                        if (nbrePremois == 0) {
                                            dashboard_ins_stat1.setTextColor(ContextCompat.getColor(Activity_dashboard.this, R.color.red));
                                        }
                                        if (nbrePrejour == 0) {
                                            dashboard_ins_stat2.setTextColor(ContextCompat.getColor(Activity_dashboard.this, R.color.red));
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });

                            }

                        }

                        for(int i=0;i<allUsers.size();i++){
                            if(!filterZoneUser(poste,allUsers.get(i),user)){
                                allUsers.remove(i);
                            }
                        }
                        for(int i=0;i<allMil.size();i++){
                            if(!filterZoneUser(poste,allMil.get(i),user)){
                                allMil.remove(i);
                            }
                        }

                        if (user==null){
                            auth.signOut();
                            finish();
                        }
                        else{
                            viewHolderForum=new ViewHolderDiscussions(Activity_dashboard.this,forums,user.getId());
                            viewHolderSms=new ViewHolderDiscussions(Activity_dashboard.this,smsDiscs,user.getId());
                            viewHolderPrivate=new ViewHolderDiscussions(Activity_dashboard.this,privateDiscs,user.getId());
                            forum_recyclerview.setAdapter(viewHolderForum);
                            sms_recyclerview.setAdapter(viewHolderSms);
                            private_recyclerview.setAdapter(viewHolderPrivate);
                            DatabaseReference refDisc=Db.getReference().child("Discussion");
                            refDisc.orderByKey().addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    forums.clear();
                                    smsDiscs.clear();
                                    privateDiscs.clear();
                                    for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                                        Discussion dis=postSnapshot.getValue(Discussion.class);
                                        if (dis.getType().equals("forum")){
                                            forums.add(dis);
                                        }
                                        else if(dis.getType().equals("sms") & dis.getInitiateur().equals
                                                (user.getId())){
                                            smsDiscs.add(dis);
                                        }
                                        else if(dis.getType().equals("tchat") &( dis.getInitiateur()
                                                .equals(user.getId()) | dis.getInterlocuteur()
                                                .equals(user.getId()) )){
                                            privateDiscs.add(dis);
                                        }
                                    }

                                    Log.i("fireBase ","Discussion count"+dataSnapshot.getChildrenCount());
                                    for(int i=0;i<forums.size();i++){
                                        if(poste.getZone().equals("commune") & forums.get(i).getId().length()<5 &  !forums.get(i).getType().equals("DEC")){
                                            forums.remove(i);
                                        }
                                        else if(poste.getZone().equals("department") & forums.get(i).getId().length()<5 & !forums.get(i).getType().equals("DED") & !forums.get(i).getType()
                                                .equals("DEC")){
                                            forums.remove(i);
                                        }
                                        else if(poste.getZone().equals("region") & forums.get(i).getId().length()<5 & !forums.get(i).getType().equals("DER") & !forums.get(i).getType().equals("DED") & !forums.get(i).getType()
                                                .equals("DEC")){
                                            forums.remove(i);
                                        }
                                        else if(poste.getZone().equals("pays") & forums.get(i).getId().length()<5 & !forums.get(i).getType().equals("DEPays") & !forums.get(i).getType().equals("DER") & !forums.get(i).getType().equals("DED") & !forums.get(i).getType()
                                                .equals("DEC")){
                                            forums.remove(i);
                                        }
                                        else if(poste.getZone().equals("continent") & forums.get(i).getId().length()<5 & !forums.get(i).getType().equals
                                                ("DECon")& !forums.get(i).getType().equals("DEPays") & !forums.get(i).getType().equals("DER") & !forums.get
                                                (i).getType().equals("DED") & !forums.get(i).getType().equals("DEC")){
                                            forums.remove(i);
                                        }
                                        else if(poste.getZone().equals("self") & forums.get(i).getId().length()<5){
                                            forums.remove(i);
                                        }
                                        Log.i("Firebase","Forums "+forums.size());
                                    }
                                    viewHolderPrivate.notifyDataSetChanged();
                                    viewHolderForum.notifyDataSetChanged();
                                    viewHolderSms.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        Log.i("Firebase ","Firebase users loaded");
                        Toast.makeText(Activity_dashboard.this, "Firebase Users loaded", Toast
                                .LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                dashboard_stat2.setText(""+couverture[0] +"\n/"+couvertureTotal[0]);
                dashboard_occ_stat2.setText(""+couverture[0] +"\n/"+couvertureTotal[0]);
                Log.i("Firebase ","Firebase communes loaded");
                Toast.makeText(Activity_dashboard.this, "Firebase communes loaded", Toast
                        .LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(poste!=null) {
        }
        ArrayAdapter<String> adapter_yes = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, Oui_Non);
        search_pays.setAdapter(adapter_yes);
        ArrayAdapter<String> adapter_pays = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, pays);
        search_pays.setAdapter(adapter_pays);
        ArrayAdapter<String> adapter_commune = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, communes);
        search_commune.setAdapter(adapter_commune);
        ArrayAdapter<String> adapter_region = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, regions);
        search_region.setAdapter(adapter_region);
        ArrayAdapter<String> adapter_dep = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, departements);
        search_dep.setAdapter(adapter_dep);
        ArrayAdapter<String> adapter_sexe = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, sexe);
        search_sexe.setAdapter(adapter_sexe);
        Filter filter1=new Filter();

        radio_private.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(radio_private.isChecked()){
                    Log.i("Firebage","tchat checked");
                    disc_destinataire.setHint("Destinataire");
                    ArrayAdapter<String> adapter_disc_destinator=new ArrayAdapter<String>
                            (Activity_dashboard.this,
                            android.R.layout.simple_dropdown_item_1line,millNames);
                    disc_destinataire.setAdapter(adapter_disc_destinator);
                }
            }
        });
        radio_sms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(radio_sms.isChecked()){
                    Log.i("Firebage","sms checked");
                    disc_destinataire.setHint("1 ere zone destinataire");
                    ArrayAdapter<String> adapter_disc_destinator=new ArrayAdapter<String>
                            (Activity_dashboard.this,
                                    android.R.layout.simple_dropdown_item_1line,communes);
                    disc_destinataire.setAdapter(adapter_disc_destinator);
                }
            }
        });
        radio_forum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(radio_forum.isChecked()){
                    Log.i("Firebage","forum checked");
                    disc_destinataire.setHint("1 er destinataire");
                    ArrayAdapter<String> adapter_disc_destinator=new ArrayAdapter<String>
                            (Activity_dashboard.this,
                                    android.R.layout.simple_dropdown_item_1line,millNames);
                    disc_destinataire.setAdapter(adapter_disc_destinator);
                }
            }
        });
        but_create_disc.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                progressBar_create_disc.setVisibility(View.VISIBLE);
                if(disc_title.getText().toString().trim().isEmpty())
                    disc_title.setHintTextColor(ContextCompat.getColor(Activity_dashboard.this, R
                            .color.red));
                else if(disc_destinataire.getText().toString().isEmpty() | (getID(allMil,
                        disc_destinataire.getText().toString()).equals("Null") & !verifyIn
                        (disc_destinataire.getText().toString(),
                        communes))){
                    disc_destinataire.setHintTextColor(ContextCompat.getColor(Activity_dashboard.this, R
                            .color.red));
                    disc_destinataire.setTextColor(ContextCompat.getColor(Activity_dashboard.this, R
                            .color.red));
                }
                else{
                    Discussion dsc= new Discussion();
                    dsc.setTitle(disc_title.getText().toString());
                    dsc.setInitiateur(user.getId());
                    if(radio_private.isChecked()){
                        dsc.setInterlocuteur(getID(allMil,disc_destinataire.getText().toString()));
                        dsc.setType("tchat");
                    }
                    if(radio_forum.isChecked()){
                        dsc.setInterlocuteur(getID(allMil,disc_destinataire.getText().toString()));
                        dsc.setType("forum");
                    }
                    if(radio_sms.isChecked()){
                        dsc.setInterlocuteur(disc_destinataire.getText().toString());
                        dsc.setType("sms");
                    }
                    DatabaseReference refdisc=Db.getReference().child("Discussion");
                    String key = refdisc.push().getKey();
                    dsc.setId(key);
                    refdisc.child(key).setValue(dsc).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent= new Intent(Activity_dashboard.this,Activity_tchat.class);
                            intent.putExtra("Id_disc",dsc.getId());
                            intent.putExtra("user_id",user.getId());
                            intent.putExtra("disc_title",dsc.getTitle());
                            intent.putExtra("type",dsc.getType());
                            progressBar_create_disc.setVisibility(View.GONE);
                            startActivity(intent);
                        }
                    });
                }
                progressBar_create_disc.setVisibility(View.GONE);
            }
        });
        search_valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radio_militant.isChecked()){
                    filter1.setType("Militant");
                }
                if (radio_Dec.isChecked()){
                    filter1.setType("DEC");
                }
                if (radio_Dep.isChecked()){
                    filter1.setType("DEP");
                }
                if (radio_Der.isChecked()){
                    filter1.setType("DER");
                }
                if (radio_BV.isChecked()){
                    filter1.setType("BV");
                }
                if (radio_commune.isChecked()){
                    filter1.setType("Commune");
                }
                if (radio_dep.isChecked()){
                    filter1.setType("Departement");
                }
                if (radio_reg.isChecked()){
                    filter1.setType("Region");
                }
                if (radio_pays.isChecked()){
                    filter1.setType("Pays");
                }
                if (radio_ins.isChecked()){
                    filter1.setType("Inscription");
                }
                if (radio_pre.isChecked()){
                    filter1.setType("Preinscription");
                }
                if(!search_region.getText().toString().isEmpty()){
                    filter1.setRegion(search_region.getText().toString());
                }else filter1.setRegion("none");
                if(!search_dep.getText().toString().isEmpty()){
                    filter1.setRegion(search_dep.getText().toString());
                }else filter1.setRegion("none");
                if(!search_pays.getText().toString().isEmpty()){
                    filter1.setRegion(search_pays.getText().toString());
                }else filter1.setRegion("none");
                if(!search_commune.getText().toString().isEmpty()){
                    filter1.setRegion(search_commune.getText().toString());
                }else filter1.setRegion("none");

                if(!search_startDate.getText().toString().isEmpty()){
                    filter1.setStartDate(getTimestamp(search_startDate.getText().toString()));
                }else filter1.setStartDate(0);
                if(!search_endDate.getText().toString().isEmpty()){
                    filter1.setStartDate(getTimestamp(search_endDate.getText().toString()));
                }else filter1.setEndDate(0);
                if(!search_age.getText().toString().isEmpty()){
                    filter1.setStartAge(Long.valueOf(search_age.getText().toString()));
                }else filter1.setStartDate(0);
                if(!search_endAge.getText().toString().isEmpty()){
                    filter1.setEndAge(Long.valueOf(search_endAge.getText().toString()));
                }else filter1.setEndAge(0);

                if(!search_sexe.getText().toString().isEmpty()){
                    filter1.setRegion(search_sexe.getText().toString());
                }else filter1.setSexe("none");
                if(!search_origine.getText().toString().isEmpty()){
                    filter1.setDepartement_org(search_origine.getText().toString());
                }else filter1.setDepartement_org("none");

                ValueAnimator anim = ValueAnimator.ofInt(1200,0);
                if(displayMetrics.heightPixels<=1300){
                    anim = ValueAnimator.ofInt(800,0);
                }
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int val = (Integer) valueAnimator.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams = filter.getLayoutParams();
                        layoutParams.height = val;
                        filter.setLayoutParams(layoutParams);
                    }
                });
                anim.setDuration(1000);
                anim.start();
            }
        });

        research_bar_rh.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                dashboard_rh_contain.setVisibility(View.GONE);
                results_container_rh.setVisibility(View.VISIBLE);
                back_rh.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        research_bar_rh.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    dashboard_rh_contain.setVisibility(View.GONE);
                    results_container_rh.setVisibility(View.VISIBLE);
                    back_rh.setVisibility(View.VISIBLE);
                }
            }
        });
        back_rh.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                research_bar_rh.setText("");
                research_bar_rh.clearFocus();
                dashboard_rh_contain.setVisibility(View.VISIBLE);
                results_container_rh.setVisibility(View.GONE);
                back_rh.setVisibility(View.GONE);
            }
        });
        but_filter_rh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValueAnimator anim = ValueAnimator.ofInt(0,1200);
                if(displayMetrics.heightPixels<=1300){
                    anim = ValueAnimator.ofInt(0,800);
                }
                if(displayMetrics.heightPixels>1300 & displayMetrics.heightPixels<1700){
                    anim = ValueAnimator.ofInt(0,800);
                }
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int val = (Integer) valueAnimator.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams = filter.getLayoutParams();
                        filter.setVisibility(View.VISIBLE);
                        layoutParams.height = val;
                        filter.setLayoutParams(layoutParams);
                    }
                });
                anim.setDuration(1000);
                anim.start();
            }
        });
        but_search_rh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filter1.getType().equals("none") & !filter1.getType().equals("Militant") &
                        !filter1.getType().equals("DEP") & !filter1.getType().equals("DEC") &
                        !filter1.getType().equals("DER"))
                    filter1.setType("Militant");
                Log.i("Filter", "Filter type = " + filter1.getType());
                results_container_rh.setVisibility(View.VISIBLE);
                search_rh_progressBar.setVisibility(View.VISIBLE);
                no_result_rh.setVisibility(View.GONE);
                ResultsMil.clear();
                for (int i = 0; i < allMil.size(); i++) {
                    User usr = allMil.get(i);
                    if(research_bar_rh.getText().toString().trim().equals("*")){
                        if (usr.getId().length()>4)
                        ResultsMil.add(usr);
                    }
                    else  if (ifPassFilter(usr, filter1, research_bar_rh.getText().toString())) {
                        ResultsMil.add(usr);
                    }
                }
                search_rh_progressBar.setVisibility(View.GONE);
                if (ResultsMil.size() == 0)
                    no_result_occ.setVisibility(View.VISIBLE);
                viewHolderMil.notifyDataSetChanged();
                recylerViewMil.setVisibility(View.VISIBLE);
            }

        });

        research_bar_occupancy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                dashboard_occupancy_contain.setVisibility(View.GONE);
                results_container_occupancy.setVisibility(View.VISIBLE);
                back_occupancy.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        research_bar_occupancy.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    dashboard_occupancy_contain.setVisibility(View.GONE);
                    results_container_occupancy.setVisibility(View.VISIBLE);
                    back_occupancy.setVisibility(View.VISIBLE);
                }
            }
        });
        back_occupancy.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                research_bar_occupancy.setText("");
                research_bar_occupancy.clearFocus();
                dashboard_occupancy_contain.setVisibility(View.VISIBLE);
                results_container_occupancy.setVisibility(View.GONE);
                back_occupancy.setVisibility(View.GONE);
            }
        });
        but_filter_bv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValueAnimator anim = ValueAnimator.ofInt(0,1200);
                if(displayMetrics.heightPixels<=1300){
                    anim = ValueAnimator.ofInt(0,800);
                }
                if(displayMetrics.heightPixels>1300 & displayMetrics.heightPixels<1700){
                    anim = ValueAnimator.ofInt(0,800);
                }
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int val = (Integer) valueAnimator.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams = filter.getLayoutParams();
                        filter.setVisibility(View.VISIBLE);
                        layoutParams.height = val;
                        filter.setLayoutParams(layoutParams);
                    }
                });
                anim.setDuration(1000);
                anim.start();
            }
        });
        but_search_occupancy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(filter1.getType().equals("none"))
                    filter1.setType("Bv");
                Log.i("Filter","Filter type = "+filter1.getType());
                results_container_occupancy.setVisibility(View.VISIBLE);
                search_occupancy_progressBar.setVisibility(View.VISIBLE);
                no_result_occ.setVisibility(View.GONE);
                if (filter1.getType().equals("Bv")){
                    recylerViewBv.setVisibility(View.GONE);
                    recylerViewCom.setVisibility(View.GONE);
                    recylerViewReg.setVisibility(View.GONE);
                    recylerViewDep.setVisibility(View.GONE);
                    ResultsBv.clear();
                    for (int i=0;i<allBv.size();i++){
                        Bv bvi=allBv.get(i);
                        if (ifPassFilter(bvi,filter1,research_bar_occupancy.getText().toString())){
                            ResultsBv.add(bvi);
                        }
                    }
                    search_occupancy_progressBar.setVisibility(View.GONE);
                    if (ResultsBv.size()==0)
                        no_result_occ.setVisibility(View.VISIBLE);
                    viewHolderBv.notifyDataSetChanged();
                    recylerViewBv.setVisibility(View.VISIBLE);
                }
                else if(filter1.getType().equals("Commune")){
                    recylerViewBv.setVisibility(View.GONE);
                    recylerViewCom.setVisibility(View.GONE);
                    recylerViewReg.setVisibility(View.GONE);
                    recylerViewDep.setVisibility(View.GONE);
                    int process=0;

                    ResultsCom.clear();
                    ArrayList<String> commune_listed=new ArrayList<>();
                    for (int i=0;i<allBv.size();i++){
                        Bv bvi=allBv.get(i);
                        if (ifPassFilter(bvi,filter1,research_bar_occupancy.getText().toString())){
                            Commune com=new Commune();
                            for(int j=0;j<allUsers.size();j++){
                                User usr1=allUsers.get(j);
                                if(usr1.getType().equals("DEC") &
                                        usr1.getCommune().equals(bvi.getBv_commune())){
                                    com.setCom_dec(usr1.getNom()+" "+
                                            usr1.getPrenom());
                                    break;
                                }
                            }
                            if(!verifyIn(bvi.getBv_commune(),commune_listed)){
                                com.setCom_name(bvi.getBv_commune());
                                com.setUpdatedBvlist(allBv);
                                ResultsCom.add(com);
                                viewHolderCom.notifyDataSetChanged();
                                recylerViewCom.setVisibility(View.VISIBLE);
                                commune_listed.add(com.getCom_name());
                                Log.i("Filter","Results passed "+commune_listed
                                        .size());
                            }
                        }
                    }
                    search_occupancy_progressBar.setVisibility(View.GONE);
                    if (ResultsCom.size()==0)
                        no_result_occ.setVisibility(View.VISIBLE);
                }
                else if(filter1.getType().equals("Departement")){
                    recylerViewBv.setVisibility(View.GONE);
                    recylerViewCom.setVisibility(View.GONE);
                    recylerViewReg.setVisibility(View.GONE);
                    recylerViewDep.setVisibility(View.GONE);
                    ResultsDep.clear();
                    ArrayList<String> commune_listed=new ArrayList<>();
                    for (int i=0;i<allBv.size();i++){
                        Bv bvi=allBv.get(i);
                        if (ifPassFilter(bvi,filter1,research_bar_occupancy.getText().toString())){
                            Departement dep=new Departement();
                            for(int j=0;j<allUsers.size();j++){
                                User usr1=allUsers.get(j);
                                if(usr1.getType().equals("DED") &
                                        usr1.getDepartement().equals(bvi.getBv_dep())){
                                    dep.setDep_dep(usr1.getNom()+" "+
                                            usr1.getPrenom());
                                    break;
                                }
                            }
                            if(!verifyIn(bvi.getBv_dep(),commune_listed)){
                                dep.setDep_name(bvi.getBv_dep());
                                dep.setUpdatedBvlist(allBv);
                                ResultsDep.add(dep);
                                viewHolderDep.notifyDataSetChanged();
                                recylerViewDep.setVisibility(View.VISIBLE);
                                commune_listed.add(dep.getDep_name());
                                Log.i("Filter","Results passed "+commune_listed.size());
                            }

                        }

                    }
                    search_occupancy_progressBar.setVisibility(View.GONE);
                    if (ResultsDep.size()==0)
                        no_result_occ.setVisibility(View.VISIBLE);
                }
                else if(filter1.getType().equals("Region")){
                    recylerViewBv.setVisibility(View.GONE);
                    recylerViewCom.setVisibility(View.GONE);
                    recylerViewReg.setVisibility(View.GONE);
                    recylerViewDep.setVisibility(View.GONE);
                    ResultsReg.clear();
                    ArrayList<String> commune_listed=new ArrayList<>();
                    for (int i=0;i<allBv.size();i++){
                        Bv bvi=allBv.get(i);
                        if (ifPassFilter(bvi,filter1,research_bar_occupancy.getText().toString())){
                            Region reg=new Region();
                            for(int j=0;j<allUsers.size();j++){
                                User usr1=allUsers.get(j);
                                if(usr1.getType().equals("DEC") &
                                        usr1.getRegion().equals(bvi.getBv_region())){
                                    reg.setReg_der(usr1.getNom()+" "+
                                            usr1.getPrenom());
                                    break;
                                }
                            }
                            if(!verifyIn(bvi.getBv_region(),commune_listed)){
                                reg.setReg_name(bvi.getBv_region());
                                reg.setUpdatedBvlist(allBv);
                                ResultsReg.add(reg);
                                viewHolderReg.notifyDataSetChanged();
                                recylerViewReg.setVisibility(View.VISIBLE);
                                commune_listed.add(reg.getReg_name());
                                Log.i("Filter","Results passed "+commune_listed
                                        .size());
                            }
                        }
                    }
                    search_occupancy_progressBar.setVisibility(View.GONE);
                    if (ResultsReg.size()==0)
                        no_result_occ.setVisibility(View.VISIBLE);
                }
            }
        });

        research_bar_ins.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                dashboard_ins_contain.setVisibility(View.GONE);
                results_container_ins.setVisibility(View.VISIBLE);
                back_ins.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        research_bar_ins.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    dashboard_ins_contain.setVisibility(View.GONE);
                    results_container_ins.setVisibility(View.VISIBLE);
                    back_ins.setVisibility(View.VISIBLE);
                }
            }
        });
        back_ins.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                research_bar_ins.setText("");
                research_bar_ins.clearFocus();
                dashboard_ins_contain.setVisibility(View.VISIBLE);
                results_container_ins.setVisibility(View.GONE);
                back_ins.setVisibility(View.GONE);
            }
        });
        but_filter_ins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValueAnimator anim = ValueAnimator.ofInt(0,1200);
                if(displayMetrics.heightPixels<=1300){
                    anim = ValueAnimator.ofInt(0,800);
                }
                if(displayMetrics.heightPixels>1300 & displayMetrics.heightPixels<1700){
                    anim = ValueAnimator.ofInt(0,800);
                }
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int val = (Integer) valueAnimator.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams = filter.getLayoutParams();
                        filter.setVisibility(View.VISIBLE);
                        layoutParams.height = val;
                        filter.setLayoutParams(layoutParams);
                    }
                });
                anim.setDuration(1000);
                anim.start();
            }
        });

        but_search_ins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(filter1.getType().equals("none"))
                    filter1.setType("Preinscription");
                Log.i("Filter","Filter type = "+filter1.getType());
                results_container_ins.setVisibility(View.VISIBLE);
                search_ins_progressBar.setVisibility(View.VISIBLE);
                no_result_ins.setVisibility(View.GONE);
                if (filter1.getType().equals("Preinscription")){
                    recylerViewIns.setVisibility(View.GONE);
                    recylerViewPre.setVisibility(View.VISIBLE);
                    ResultsPre.clear();
                    for (int i=0;i<allPre.size();i++){
                        Preinscription prei=allPre.get(i);
                        if(research_bar_ins.getText().toString().trim().equals("*")){
                            ResultsPre.add(prei);
                        }
                        else if (ifPassFilter(prei,filter1,research_bar_ins.getText().toString())){
                            ResultsPre.add(prei);
                        }
                    }
                    search_ins_progressBar.setVisibility(View.GONE);
                    if (ResultsPre.size()==0)
                        no_result_ins.setVisibility(View.VISIBLE);
                    viewHolderPre.notifyDataSetChanged();
                    recylerViewPre.setVisibility(View.VISIBLE);
                }
                else {
                    recylerViewIns.setVisibility(View.VISIBLE);
                    recylerViewPre.setVisibility(View.GONE);
                    ResultsIns.clear();
                    for (int i=0;i<allIns.size();i++){
                        Inscription insi=allIns.get(i);
                        if(research_bar_ins.getText().toString().trim().equals("*")){
                            ResultsIns.add(insi);
                        }
                        else if (ifPassFilter(insi,filter1,research_bar_ins.getText().toString())){
                            ResultsIns.add(insi);
                        }
                    }
                    search_ins_progressBar.setVisibility(View.GONE);
                    if (ResultsIns.size()==0)
                        no_result_ins.setVisibility(View.VISIBLE);
                    viewHolderIns.notifyDataSetChanged();
                    recylerViewIns.setVisibility(View.VISIBLE);
                }
            }
        });
        hide_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ValueAnimator anim = ValueAnimator.ofInt(1200,0);
                if(displayMetrics.heightPixels<=1300){
                    anim = ValueAnimator.ofInt(800,0);
                }
                if(displayMetrics.heightPixels>1300 & displayMetrics.heightPixels<1700){
                    anim = ValueAnimator.ofInt(800,0);
                }
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int val = (Integer) valueAnimator.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams = filter.getLayoutParams();
                        layoutParams.height = val;
                        filter.setLayoutParams(layoutParams);
                    }
                });
                anim.setDuration(1000);
                anim.start();
            }
        });
        close_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select_registration.setVisibility(View.GONE);
            }
        });
        add_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select_registration.setVisibility(View.VISIBLE);
            }
        });

        add_ins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Activity_dashboard.this,Activity_add_inscription.class);
                startActivity(intent);
            }
        });
        see_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Activity_dashboard.this,Activity_Add_info.class);
                intent.putExtra("type","history");
                startActivity(intent);
            }
        });
        add_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Activity_dashboard.this,Activity_add_preinscription
                        .class);
                startActivity(intent);
            }
        });
        add_bv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Activity_dashboard.this,Activity_bv.class);
                startActivity(intent);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter1.setType("none");
                dashboard.setVisibility(View.VISIBLE);
                dashboard_rh.setVisibility(View.GONE);
                dashboard_occupancy.setVisibility(View.GONE);
                dashboard_tchat.setVisibility(View.GONE);
                dashboard_inscription.setVisibility(View.GONE);
                home.setBackgroundResource(R.color.colorAccent);
                home.setImageResource(R.drawable.ic_home_green_24dp);
                rh.setImageResource(R.drawable.ic_group_white_24dp);
                rh.setBackgroundResource(R.drawable.butt_raduis_up_left);
                tchat.setBackgroundResource(R.color.colorPrimaryDark);
                tchat.setImageResource(R.drawable.ic_textsms_white_24dp);
                occupancy.setBackgroundResource(R.color.colorPrimaryDark);
                occupancy.setImageResource(R.drawable.ic_occupancy_white_24dp);
                inscriptions.setBackgroundResource(R.color.colorPrimaryDark);
                inscriptions.setImageResource(R.drawable.ic_note_add_white_24dp);
                //butt_raduis_up_left
            }
        });
        but_homeMil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter1.setType("none");
                dashboard.setVisibility(View.VISIBLE);
                dashboard_rh.setVisibility(View.GONE);
                dashboard_occupancy.setVisibility(View.GONE);
                dashboard_tchat.setVisibility(View.GONE);
                dashboard_inscription.setVisibility(View.GONE);
                first.setBackgroundResource(R.drawable.butt_radius_right);
                last.setBackgroundResource(R.color.colorPrimaryDark);
                but_homeMil.setBackgroundResource(R.color.colorAccent);
                but_homeMil.setImageResource(R.drawable.ic_home_green_24dp);
                but_tchatMil.setBackgroundResource(R.drawable.butt_raduis_up_left);
                but_tchatMil.setImageResource(R.drawable.ic_textsms_white_24dp);
                occupancy.setBackgroundResource(R.color.colorPrimaryDark);
                occupancy.setImageResource(R.drawable.ic_occupancy_white_24dp);
                but_addInsMil.setBackgroundResource(R.color.colorPrimaryDark);
                but_addInsMil.setImageResource(R.drawable.ic_note_add_white_24dp);
                //butt_raduis_up_left
            }
        });
        rh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter1.setType("none");
                dashboard.setVisibility(View.GONE);
                dashboard_rh.setVisibility(View.VISIBLE);
                dashboard_occupancy.setVisibility(View.GONE);
                dashboard_tchat.setVisibility(View.GONE);
                dashboard_inscription.setVisibility(View.GONE);
                home.setImageResource(R.drawable.ic_home_white_24dp);
                home.setBackgroundResource(R.drawable.butt_radius_right);
                rh.setBackgroundResource(R.color.colorAccent);
                rh.setImageResource(R.drawable.ic_group_green_24dp);
                occupancy.setBackgroundResource(R.drawable.butt_raduis_up_left);
                occupancy.setImageResource(R.drawable.ic_occupancy_white_24dp);
                tchat.setBackgroundResource(R.color.colorPrimaryDark);
                tchat.setImageResource(R.drawable.ic_textsms_white_24dp);
                inscriptions.setBackgroundResource(R.color.colorPrimaryDark);
                inscriptions.setImageResource(R.drawable.ic_note_add_white_24dp);

                filter_select_where.clearCheck();
                radio_militant.setVisibility(View.VISIBLE);
                radio_Dec.setVisibility(View.VISIBLE);
                radio_Dep.setVisibility(View.VISIBLE);
                radio_Der.setVisibility(View.VISIBLE);
                radio_ins.setVisibility(View.GONE);
                radio_pre.setVisibility(View.GONE);
                radio_BV.setVisibility(View.GONE);
                radio_commune.setVisibility(View.GONE);
                radio_reg.setVisibility(View.GONE);
                radio_pays.setVisibility(View.GONE);
                radio_dep.setVisibility(View.GONE);
                layout_period.setVisibility(View.GONE);
                human_filter.setVisibility(View.VISIBLE);
                search_origine.setVisibility(View.VISIBLE);
                //butt_raduis_up_left
            }
        });

        occupancy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter1.setType("none");
                dashboard.setVisibility(View.GONE);
                dashboard_rh.setVisibility(View.GONE);
                dashboard_occupancy.setVisibility(View.VISIBLE);
                dashboard_tchat.setVisibility(View.GONE);
                dashboard_inscription.setVisibility(View.GONE);
                home.setImageResource(R.drawable.ic_home_white_24dp);
                home.setBackgroundResource(R.color.colorPrimaryDark);
                rh.setBackgroundResource(R.drawable.butt_radius_right);
                rh.setImageResource(R.drawable.ic_group_white_24dp);
                occupancy.setBackgroundResource(R.color.colorAccent);
                occupancy.setImageResource(R.drawable.ic_occupancy_black_24dp);
                tchat.setBackgroundResource(R.drawable.butt_raduis_up_left);
                tchat.setImageResource(R.drawable.ic_textsms_white_24dp);
                inscriptions.setBackgroundResource(R.color.colorPrimaryDark);
                inscriptions.setImageResource(R.drawable.ic_note_add_white_24dp);
                filter_select_where.clearCheck();
                radio_ins.setVisibility(View.GONE);
                radio_militant.setVisibility(View.GONE);
                radio_pre.setVisibility(View.GONE);
                radio_Dec.setVisibility(View.GONE);
                radio_Dep.setVisibility(View.GONE);
                radio_Der.setVisibility(View.GONE);
                radio_BV.setVisibility(View.VISIBLE);
                radio_commune.setVisibility(View.VISIBLE);
                radio_reg.setVisibility(View.VISIBLE);
                radio_pays.setVisibility(View.VISIBLE);
                radio_dep.setVisibility(View.VISIBLE);
                layout_period.setVisibility(View.GONE);
                human_filter.setVisibility(View.GONE);
                search_origine.setVisibility(View.GONE);
                //butt_raduis_up_left
            }
        });
        tchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dashboard.setVisibility(View.GONE);
                dashboard_rh.setVisibility(View.GONE);
                dashboard_occupancy.setVisibility(View.GONE);
                dashboard_tchat.setVisibility(View.VISIBLE);
                dashboard_inscription.setVisibility(View.GONE);
                home.setImageResource(R.drawable.ic_home_white_24dp);
                home.setBackgroundResource(R.color.colorPrimaryDark);
                rh.setBackgroundResource(R.color.colorPrimaryDark);
                rh.setImageResource(R.drawable.ic_group_white_24dp);
                occupancy.setBackgroundResource(R.drawable.butt_radius_right);
                occupancy.setImageResource(R.drawable.ic_occupancy_white_24dp);
                tchat.setBackgroundResource(R.color.colorAccent);
                tchat.setImageResource(R.drawable.ic_textsms_black_24dp);
                inscriptions.setBackgroundResource(R.drawable.butt_raduis_up_left);
                inscriptions.setImageResource(R.drawable.ic_note_add_white_24dp);
                //butt_raduis_up_left
            }
        });
        but_tchatMil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dashboard.setVisibility(View.GONE);
                dashboard_rh.setVisibility(View.GONE);
                dashboard_occupancy.setVisibility(View.GONE);
                dashboard_tchat.setVisibility(View.VISIBLE);
                dashboard_inscription.setVisibility(View.GONE);
                first.setBackgroundResource(R.color.colorPrimaryDark);
                last.setBackgroundResource(R.color.colorPrimaryDark);
                but_homeMil.setImageResource(R.drawable.ic_home_white_24dp);
                but_homeMil.setBackgroundResource(R.drawable.butt_radius_right);
                but_tchatMil.setBackgroundResource(R.color.colorAccent);
                but_tchatMil.setImageResource(R.drawable.ic_textsms_black_24dp);
                but_addInsMil.setBackgroundResource(R.drawable.butt_raduis_up_left);
                but_addInsMil.setImageResource(R.drawable.ic_note_add_white_24dp);
                //butt_raduis_up_left
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Activity_dashboard.this,Activity_menu.class);
                startActivity(intent);
            }
        });
        inscriptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter1.setType("none");
                dashboard.setVisibility(View.GONE);
                dashboard_rh.setVisibility(View.GONE);
                dashboard_occupancy.setVisibility(View.GONE);
                dashboard_tchat.setVisibility(View.GONE);
                dashboard_inscription.setVisibility(View.VISIBLE);
                home.setImageResource(R.drawable.ic_home_white_24dp);
                home.setBackgroundResource(R.color.colorPrimaryDark);
                rh.setBackgroundResource(R.color.colorPrimaryDark);
                rh.setImageResource(R.drawable.ic_group_white_24dp);
                occupancy.setBackgroundResource(R.color.colorPrimaryDark);
                occupancy.setImageResource(R.drawable.ic_occupancy_white_24dp);
                tchat.setBackgroundResource(R.drawable.butt_radius_right);
                tchat.setImageResource(R.drawable.ic_textsms_white_24dp);
                inscriptions.setBackgroundResource(R.color.colorAccent);
                inscriptions.setImageResource(R.drawable.ic_note_add_black_24dp);
                filter_select_where.clearCheck();
                radio_ins.setVisibility(View.VISIBLE);
                radio_militant.setVisibility(View.GONE);
                radio_pre.setVisibility(View.VISIBLE);
                radio_Dec.setVisibility(View.GONE);
                radio_Dep.setVisibility(View.GONE);
                radio_Der.setVisibility(View.GONE);
                radio_BV.setVisibility(View.GONE);
                radio_commune.setVisibility(View.GONE);
                radio_reg.setVisibility(View.GONE);
                radio_pays.setVisibility(View.GONE);
                radio_dep.setVisibility(View.GONE);
                layout_period.setVisibility(View.VISIBLE);
                human_filter.setVisibility(View.VISIBLE);
                search_origine.setVisibility(View.VISIBLE);
                //butt_raduis_up_left
            }
        });
        but_addInsMil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter1.setType("none");
                dashboard.setVisibility(View.GONE);
                dashboard_rh.setVisibility(View.GONE);
                dashboard_occupancy.setVisibility(View.GONE);
                dashboard_tchat.setVisibility(View.GONE);
                dashboard_inscription.setVisibility(View.VISIBLE);
                first.setBackgroundResource(R.color.colorPrimaryDark);
                last.setBackgroundResource(R.drawable.butt_raduis_up_left);
                but_homeMil.setImageResource(R.drawable.ic_home_white_24dp);
                but_homeMil.setBackgroundResource(R.color.colorPrimaryDark);
                but_tchatMil.setBackgroundResource(R.drawable.butt_radius_right);
                but_tchatMil.setImageResource(R.drawable.ic_textsms_white_24dp);
                but_addInsMil.setBackgroundResource(R.color.colorAccent);
                but_addInsMil.setImageResource(R.drawable.ic_note_add_black_24dp);
                filter_select_where.clearCheck();
                radio_ins.setVisibility(View.VISIBLE);
                radio_militant.setVisibility(View.GONE);
                radio_pre.setVisibility(View.VISIBLE);
                radio_Dec.setVisibility(View.GONE);
                radio_Dep.setVisibility(View.GONE);
                radio_Der.setVisibility(View.GONE);
                radio_BV.setVisibility(View.GONE);
                radio_commune.setVisibility(View.GONE);
                radio_reg.setVisibility(View.GONE);
                radio_pays.setVisibility(View.GONE);
                radio_dep.setVisibility(View.GONE);
                layout_period.setVisibility(View.VISIBLE);
                human_filter.setVisibility(View.VISIBLE);
                search_origine.setVisibility(View.VISIBLE);
                //butt_raduis_up_left
            }
        });

        add_tchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new_disc_box.setVisibility(View.VISIBLE);
            }
        });
    }
    int count=0;
    @Override
    public void onBackPressed(){
        if(new_disc_box.getVisibility()==View.VISIBLE){
            new_disc_box.setVisibility(View.GONE);
        }
        else if(select_registration.getVisibility()==View.VISIBLE){
            select_registration.setVisibility(View.GONE);
        }
        else if(filter.getVisibility()==View.VISIBLE){
            ValueAnimator anim = ValueAnimator.ofInt(1200,0);
            if(displayMetrics.heightPixels<=1300){
                anim = ValueAnimator.ofInt(800,0);
            }
            if(displayMetrics.heightPixels>1300 & displayMetrics.heightPixels<1700){
                anim = ValueAnimator.ofInt(800,0);
            }
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = filter.getLayoutParams();
                    layoutParams.height = val;
                    filter.setLayoutParams(layoutParams);
                }
            });
            anim.setDuration(1000);
            anim.start();
        }
        else if(results_container_ins.getVisibility()==View.VISIBLE){
            dashboard_ins_contain.setVisibility(View.VISIBLE);
            results_container_ins.setVisibility(View.GONE);
        }
        else if(results_container_rh.getVisibility()==View.VISIBLE){
            dashboard_rh_contain.setVisibility(View.VISIBLE);
            results_container_rh.setVisibility(View.GONE);
        }
        else if(results_container_occupancy.getVisibility()==View.VISIBLE){
            dashboard_occupancy_contain.setVisibility(View.VISIBLE);
            results_container_occupancy.setVisibility(View.GONE);
        }
        else if(dashboard_occupancy.getVisibility()==View.VISIBLE | dashboard_rh.getVisibility()==View
                .VISIBLE | dashboard_tchat.getVisibility()==View.VISIBLE | dashboard_inscription.getVisibility()==View.VISIBLE){
            dashboard.setVisibility(View.VISIBLE);
            dashboard_occupancy.setVisibility(View.GONE);
            dashboard_rh.setVisibility(View.GONE);
            dashboard_tchat.setVisibility(View.GONE);
            dashboard_inscription.setVisibility(View.GONE);
            home.setBackgroundResource(R.color.colorAccent);
            home.setImageResource(R.drawable.ic_home_green_24dp);
            rh.setImageResource(R.drawable.ic_group_white_24dp);
            rh.setBackgroundResource(R.drawable.butt_raduis_up_left);
            tchat.setBackgroundResource(R.color.colorPrimaryDark);
            tchat.setImageResource(R.drawable.ic_textsms_white_24dp);
            occupancy.setBackgroundResource(R.color.colorPrimaryDark);
            occupancy.setImageResource(R.drawable.ic_occupancy_white_24dp);
            inscriptions.setBackgroundResource(R.color.colorPrimaryDark);
            inscriptions.setImageResource(R.drawable.ic_note_add_white_24dp);
        }
        else if(dashboard.getVisibility()==View.VISIBLE ){
            count++;
            if(count==1){
                Toast.makeText(Activity_dashboard.this,"Appuyer a nouveau pour quitter l'Application",Toast.LENGTH_LONG);
            }
            else if(count>1){
                finish();
            }
        }

    }
    private ArrayList<String> LoadCountries() {
        InputStreamReader is = null;
        ArrayList<String> countries=new ArrayList<>();
        try {
            is = new InputStreamReader(getAssets()
                    .open("sql_pays.csv"));

            BufferedReader reader = new BufferedReader(is);
            reader.readLine();
            String line;
            DatabaseReference refPays=Db.getReference().child("Pays");
            int i=0;
            while ((line = reader.readLine()) != null) {
                    //Toast.makeText(this, "file found", Toast.LENGTH_SHORT).show();
                    String[] cells = line.split(";");
                    countries.add(cells[3]);
            }
            Toast.makeText(this, "upload terminee", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "file not found", Toast.LENGTH_SHORT).show();
        }
        return countries;
    }
    private String getID(ArrayList<User> userlist,String key){
        for (int i=0;i<userlist.size();i++){
            String nom_prenom=userlist.get(i).getNom()+ " "+userlist.get(i).getPrenom();
            if (nom_prenom.equals(key))
                return userlist.get(i).getId();
        }
        return "Null";
    }
    private String getID(ArrayList<Preinscription> userlist,String key,boolean tst){
        for (int i=0;i<userlist.size();i++){
            String nom_prenom=userlist.get(i).getNom()+ " "+userlist.get(i).getPrenom();
            if (nom_prenom.equals(key))
                return userlist.get(i).getId();
        }
        return "Null";
    }
    private String getID(ArrayList<Inscription> userlist,String key,CharSequence test){
        for (int i=0;i<userlist.size();i++){
            String nom_prenom=userlist.get(i).getNom()+ " "+userlist.get(i).getPrenom();
            if (nom_prenom.equals(key))
                return userlist.get(i).getId();
        }
        return "Null";
    }
    private boolean ifPassFilter(User usr,Filter filt,String keyWord){
        if(!usr.getNom().toUpperCase().contains(keyWord.toUpperCase()) && !usr.getPrenom().toUpperCase().contains(keyWord.toUpperCase()))
            return false;
        if(!usr.getType().equals(filt.getType()))
            return false;
        if(!filt.getRegion().isEmpty() & !filt.getRegion().equals("none") & !usr.getRegion().equals
                (filt.getRegion()))
            return false;
        if(!filt.getCommune().isEmpty() & !filt.getCommune().equals("none") & !usr.getCommune().equals
                (filt.getCommune()))
            return false;
        if(!filt.getDepartement().isEmpty() & !filt.getDepartement().equals("none") & !usr.getDepartement().equals
                (filt.getDepartement()))
            return false;
        if(!filt.getDepartement_org().isEmpty() & !filt.getDepartement_org().equals("none") & !usr.getDepartement_org()
                .equals(filt.getDepartement_org()))
            return false;
        if(!filt.getPays().isEmpty() & !filt.getPays().equals("none") & !usr.getPays().equals(filt.getPays()))
            return false;
        if(!filt.getSexe().isEmpty() & !filt.getSexe().equals("none") & !usr.getSexe().equals(filt.getSexe()))
            return false;
        if(filt.getStartAge()!=0 & calculateAge(getTimestamp(usr.getDate_naissance()))>(filt.getStartAge()))
            return false;
        if(filt.getEndAge()!=0 & calculateAge(getTimestamp(usr.getDate_naissance()))<=(filt.getEndAge()))
            return false;
        if(!filt.getSexe().isEmpty() & !filt.getSexe().equals("none") & !usr.getSexe().equals(filt.getSexe()))
            return false;

        return true;
    }
    private boolean ifPassFilter(Preinscription usr,Filter filt,String keyWord){
        if(!usr.getNom().toUpperCase().contains(keyWord.toUpperCase()) && !usr.getPrenom().toUpperCase().contains(keyWord.toUpperCase()))
            return false;
        if(!filt.getRegion().isEmpty() & !filt.getRegion().equals("none") & !usr.getRegion().equals
                (filt.getRegion()))
            return false;
        if(!filt.getCommune().isEmpty() & !filt.getCommune().equals("none") & !usr.getCommune().equals
                (filt.getCommune()))
            return false;
        if(!filt.getDepartement().isEmpty() & !filt.getDepartement().equals("none") & !usr.getDepartement().equals
                (filt.getDepartement()))
            return false;
        if(!filt.getDepartement_org().isEmpty() & !filt.getDepartement_org().equals("none") & !usr.getDepartement_org()
                .equals(filt.getDepartement_org()))
            return false;
        if(!filt.getPays().isEmpty() & !filt.getPays().equals("none") & !usr.getPays().equals(filt.getPays()))
            return false;
        if(!filt.getSexe().isEmpty() & !filt.getSexe().equals("none") & !usr.getSexe().equals(filt.getSexe()))
            return false;
        if(filt.getStartAge()!=0 & calculateAge(getTimestamp(usr.getDate_naissance()))>=(filt
                .getStartAge()))
            return false;
        if(filt.getEndAge()!=0 & calculateAge(getTimestamp(usr.getDate_naissance()))<=(filt.getEndAge()))
            return false;
        if(!filt.getSexe().isEmpty() & !filt.getSexe().equals("none") & !usr.getSexe().equals(filt.getSexe()))
            return false;
        if(filt.getStartDate()!=0 & usr.getCreation_date()>=filt.getStartAge())
            return false;
        if(filt.getEndDate()!=0 & usr.getCreation_date()<=filt.getEndDate())
            return false;
        return true;
    }
    private boolean ifPassFilter(Inscription usr,Filter filt,String keyWord){
        if(!usr.getNom().toUpperCase().contains(keyWord.toUpperCase()) && !usr.getPrenom().toUpperCase().contains(keyWord.toUpperCase()))
            return false;
        if(!filt.getRegion().isEmpty() & !filt.getRegion().equals("none") & !usr.getRegion().equals
                (filt.getRegion()))
            return false;
        if(!filt.getCommune().isEmpty() & !filt.getCommune().equals("none") & !usr.getCommune().equals
                (filt.getCommune()))
            return false;
        if(!filt.getDepartement().isEmpty() & !filt.getDepartement().equals("none") & !usr.getDepartement().equals
                (filt.getDepartement()))
            return false;
        if(!filt.getDepartement_org().isEmpty() & !filt.getDepartement_org().equals("none") & !usr.getDepartement_org()
                .equals(filt.getDepartement_org()))
            return false;
        if(!filt.getPays().isEmpty() & !filt.getPays().equals("none") & !usr.getPays().equals(filt.getPays()))
            return false;
        if(!filt.getSexe().isEmpty() & !filt.getSexe().equals("none") & !usr.getSexe().equals(filt.getSexe()))
            return false;
        if(filt.getStartAge()!=0 & calculateAge(getTimestamp(usr.getDate_naissance()))>=(filt
                .getStartAge()))
            return false;
        if(filt.getEndAge()!=0 & calculateAge(getTimestamp(usr.getDate_naissance()))<=(filt.getEndAge()))
            return false;
        if(!filt.getSexe().isEmpty() & !filt.getSexe().equals("none") & !usr.getSexe().equals(filt.getSexe()))
            return false;
        if(filt.getStartDate()!=0 & usr.getCreation_date()>=filt.getStartAge())
            return false;
        if(filt.getEndDate()!=0 & usr.getCreation_date()<=filt.getEndDate())
            return false;
        return true;
    }
    private boolean ifPassFilter(Bv bv,Filter filt,String keyWord){
        if(filt.getType().equals("Bv")){
            if(!bv.getBv_name().toUpperCase().contains(keyWord.toUpperCase()) & !bv.getBv_vol1_name().toUpperCase().contains
                    (keyWord.toUpperCase()) & !bv.getBv_vol2_name().toUpperCase().contains(keyWord.toUpperCase())
                    & !bv.getBv_vol3_name().toUpperCase().contains(keyWord.toUpperCase())
                    & !bv.getBv_vol4_name().toUpperCase().contains(keyWord.toUpperCase()) )
                return false;
        }
        if(filt.getType().equals("Commune")){
            if(!bv.getBv_commune().toUpperCase().contains(keyWord.toUpperCase()) & !bv.getBv_vol1_name
                    ().toUpperCase().contains(keyWord.toUpperCase())
                    & !bv.getBv_vol2_name().toUpperCase().contains(keyWord.toUpperCase())
                    & !bv.getBv_vol3_name().toUpperCase().contains(keyWord.toUpperCase())
                    & !bv.getBv_vol4_name().toUpperCase().contains(keyWord.toUpperCase()) )
                return false;
        }
        if(filt.getType().equals("Departement")){
            if(!bv.getBv_dep().toUpperCase().contains(keyWord.toUpperCase()) & !bv.getBv_vol1_name
                    ().toUpperCase().contains(keyWord.toUpperCase())
                    & !bv.getBv_vol2_name().toUpperCase().contains(keyWord.toUpperCase())
                    & !bv.getBv_vol3_name().toUpperCase().contains(keyWord.toUpperCase())
                    & !bv.getBv_vol4_name().toUpperCase().contains(keyWord.toUpperCase()) )
                return false;
        }
        if(filt.getType().equals("Region")){
            if(!bv.getBv_region().toUpperCase().contains(keyWord.toUpperCase()) & !bv.getBv_vol1_name
                    ().toUpperCase().contains(keyWord.toUpperCase())
                    & !bv.getBv_vol2_name().toUpperCase().contains(keyWord.toUpperCase())
                    & !bv.getBv_vol3_name().toUpperCase().contains(keyWord.toUpperCase())
                    & !bv.getBv_vol4_name().toUpperCase().contains(keyWord.toUpperCase()) )
                return false;
        }
        if(!filt.getRegion().isEmpty() & !filt.getRegion().equals("none") & !bv.getBv_region().equals
                (filt.getRegion()))
            return false;
        if(!filt.getCommune().isEmpty() & !filt.getCommune().equals("none") & !bv.getBv_commune().equals
                (filt.getCommune()))
            return false;
        if(!filt.getDepartement().isEmpty() & !filt.getDepartement().equals("none") & !bv.getBv_dep()
                .equals(filt.getDepartement()))
            return false;

        return true;
    }
    private boolean filterZoneUser(Postes poste,User user,User userRef){
        String zone =poste.getZone();
        if(zone.equals("commune")){
            if(userRef.getCommune().equals(user.getCommune()))
                return true;
        }
        if(zone.equals("department")){
            if(userRef.getDepartement().equals(user.getDepartement()))
                return true;
        }
        if(zone.equals("region")){
            if(userRef.getRegion().equals(user.getRegion()))
                return true;
        }
        if(zone.equals("pays")){
            if(userRef.getPays().equals(user.getPays()))
                return true;
        }
        if(zone.equals("all")){
                return true;
        }
        return false;
    }
    private boolean filterZonePre(Postes poste,Preinscription pre,User userRef){
        String zone =poste.getZone();
        if(zone.equals("commune")){
            if(userRef.getCommune().equals(pre.getCommune()))
                return true;
        }
        if(zone.equals("department")){
            if(userRef.getDepartement().equals(pre.getDepartement()))
                return true;
        }
        if(zone.equals("region")){
            if(userRef.getRegion().equals(pre.getRegion()))
                return true;
        }
        if(zone.equals("pays")){
            if(userRef.getPays().equals(pre.getPays()))
                return true;
        }
        if(zone.equals("all")){
            return true;
        }
        if(zone.equals("self") | zone.equals("com")){
            if(userRef.getId().equals(pre.getParrain()))
                return true;
        }
        return false;
    }
    private boolean filterZoneIns(Postes poste,Inscription ins,User userRef){
        String zone =poste.getZone();
        if(zone.equals("commune")){
            if(userRef.getCommune().equals(ins.getCommune()))
                return true;
        }
        if(zone.equals("department")){
            if(userRef.getDepartement().equals(ins.getDepartement()))
                return true;
        }
        if(zone.equals("region")){
            if(userRef.getRegion().equals(ins.getRegion()))
                return true;
        }
        if(zone.equals("pays")){
            if(userRef.getPays().equals(ins.getPays()))
                return true;
        }
        if(zone.equals("all")){
            return true;
        }
        if(zone.equals("self") | zone.equals("com")){
            if(userRef.getId().equals(ins.getParrain()))
                return true;
        }
        return false;
    }
    private boolean filterZoneBv(Postes poste,Bv bv,User userRef){
        String zone =poste.getZone();
        if(zone.equals("commune")){
            if(userRef.getCommune().equals(bv.getBv_commune()))
                return true;
        }
        if(zone.equals("department")){
            if(userRef.getDepartement().equals(bv.getBv_dep()))
                return true;
        }
        if(zone.equals("region")){
            if(userRef.getRegion().equals(bv.getBv_region()))
                return true;
        }
        if(zone.equals("pays")){
            if(userRef.getPays().equals(bv.getBv_pays()))
                return true;
        }
        if(zone.equals("all")){
            return true;
        }
        if(zone.equals("self") | zone.equals("com") ){
            if(userRef.getId().equals(bv.getBv_commune()))
                return true;
        }
        return false;
    }
    private long getTimestamp(String str_date){
        long timestamp = 0;
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date date = null;
        try {
            date = (Date)formatter.parse(str_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }
    private int calculateAge(long timebirth){
        int age = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            age=Period.between(LocalDate.ofEpochDay(timebirth),LocalDate.now()).getYears();
            return age;
        }
        else {
            Calendar calact = Calendar.getInstance(Locale.ENGLISH);
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(timebirth * 1000);
            calact.getTime().getYear();
            age=calact.getTime().getYear()-cal.getTime().getYear();
            return age;
        }
    }
    private void LoadCommunes() {
        InputStreamReader is = null;
        DatabaseReference refBv=Db.getReference().child("Bv");
        ArrayList<String> registeredBv=new ArrayList<>();
        Toast.makeText(this, "loading communes", Toast.LENGTH_SHORT).show();
        try {
            is = new InputStreamReader(getAssets()
                    .open("rdc.csv"));
            BufferedReader reader = new BufferedReader(is);
            reader.readLine();
            String line;
            int i=0;
            regions.add("LITTORAL");
            regions.add("CENTRE");
            regions.add("ADAMAOUA");
            regions.add("NORD");
            regions.add("EXTREME-NORD");
            regions.add("OUEST");
            regions.add("EST");
            regions.add("SUD-OUEST");
            regions.add("NORD-OUEST");
            regions.add("SUD");
            while ((line = reader.readLine()) != null) {
                String[] cells = line.split(";");
                bv.add(cells[3]);
                rdc.add(cells);
                if(!verifyIn(cells[2],communes))
                    communes.add(cells[2]);
                if(!verifyIn(cells[1],departements))
                    departements.add(cells[1]);
            }
            Toast.makeText(this, "loading complete", Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(this, Service_AddBv.class);
            //startService(intent);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "file not found", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean verifyIn(String test, ArrayList<String> list){
        for (int i=0; i<list.size();i++){
            if (test.equals(list.get(i)))
                return true;
        }
        return false;
    }
    private Commune mostActive(ArrayList<Commune> list){
        if (list.isEmpty())
            return new Commune();
        Commune most=list.get(0);
        for(int i =1;i<list.size();i++){
            if (most.getActivite()<list.get(i).getActivite())
                most=list.get(i);
        }
        return most;
    }
    private User mostActiveU(ArrayList<User> list){
        if (list.isEmpty())
            return new User();
        User most=list.get(0);
        for(int i =1;i<list.size();i++){
            if (most.getActivite()<list.get(i).getActivite())
                most=list.get(i);
        }
        if (allMil.size()!=0){
            for (int k=0;k<allMil.size();k++){
                if (most.getId().equals(allMil.get(k).getId())){
                    return allMil.get(k);
                }
            }
        }
        return most;
    }
    private String getDate(long time) {
        java.util.Calendar cal = java.util.Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = android.text.format.DateFormat.format("dd/MM", cal).toString();
        String monthStr = "";
        Log.i("Firebase","monthStr :"+date.substring(3,5));
        switch(date.substring(3,5)) {
            case "01":
                monthStr = "Janvier";
                break;
            case "02":
                monthStr = "Fevrier";
                break;
            case "03":
                monthStr = "Mars";
                break;
            case "04":
                monthStr = "Avril";
                break;
            case "05":
                monthStr = "Mai";
                break;
            case "06":
                monthStr = "Juin";
                break;
            case "07":
                monthStr = "Juillet";
                break;
            case "08":
                monthStr = "Aout";
                break;
            case "09":
                monthStr = "Septembre";
                break;
            case "10":
                monthStr = "Octobre";
                break;
            case "11":
                monthStr = "Novembre";
                break;
            case "12":
                monthStr = "Decembre";
                break;
        }
        date=date.substring(0,2)+" "+monthStr;

        return date;
    }

}
