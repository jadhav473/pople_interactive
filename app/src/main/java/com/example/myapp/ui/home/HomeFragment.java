package com.example.myapp.ui.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;
import com.example.myapp.Volley.PreferenceProvider;
import com.example.myapp.Volley.UrlHelper;
import com.example.myapp.Volley.VolleyCallbacks;
import com.example.myapp.Volley.VolleySender;
import com.example.myapp.adapters.ProductAdapter;
import com.example.myapp.model.ProfileData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class HomeFragment extends Fragment {

    RecyclerView recycler_home;
    TextView message_text;
    private ArrayList<ProfileData> profileDataArrayList;
    Button btn_accept, btn_decline;
    ProductAdapter productAdapter;
    Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recycler_home = root.findViewById(R.id.recycler_home);
        btn_accept = root.findViewById(R.id.btn_accept);
        btn_decline = root.findViewById(R.id.btn_decline);
        message_text = root.findViewById(R.id.message_text);

        context = getContext();
        profileDataArrayList = new ArrayList<>();
        requestProfileAPI();

        PreferenceProvider.save("list_position", "0");

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_accept.setVisibility(View.GONE);
                btn_decline.setVisibility(View.GONE);
                message_text.setVisibility(View.VISIBLE);
                message_text.setText("Member Accepted");

                checkListSize();

            }
        });

        btn_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position  = Integer.parseInt(PreferenceProvider.get("list_position")) ;

                btn_accept.setVisibility(View.GONE);
                btn_decline.setVisibility(View.GONE);
                message_text.setVisibility(View.VISIBLE);
                message_text.setText("Member Declined");

                checkListSize();

            }
        });

        return root;
    }

    private void checkListSize() {

        int position  = Integer.parseInt(PreferenceProvider.get("list_position"));

        if (profileDataArrayList.size() == position) {
            btn_accept.setVisibility(View.GONE);
            btn_decline.setVisibility(View.GONE);
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);

            dialog.setTitle("Message")
                    .setMessage("No More Profiles. Do you want to refresh?")

                    .setNegativeButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            dialoginterface.dismiss();
                            HomeFragment homeFragment = new HomeFragment();
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.nav_host_fragment, homeFragment);
                            fragmentTransaction.commit();
                        }
                    });
            dialog.show();
            return;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recycler_home.scrollToPosition(position + 1 );
                PreferenceProvider.save("list_position", position + 1);
                btn_accept.setVisibility(View.VISIBLE);
                btn_decline.setVisibility(View.VISIBLE);
                message_text.setVisibility(View.GONE);
                       /* new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {


                            }
                        }, 100);*/
            }
        }, 1500);
    }

    private void requestProfileAPI() {
        VolleySender volleySender = new VolleySender(context, UrlHelper.getBaseUrl(), new VolleyCallbacks() {
            @Override
            public boolean successCallback(String response) {
                Log.e("Get Profile", response);
                try {
                    final JSONObject getProfilesJSON = new JSONObject(response);
                    if (getProfilesJSON.has("results")) {
                        //String results = getProfilesJSON.getJSONArray("results").toString();

                        JSONArray jsonArray = getProfilesJSON.getJSONArray("results");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            try {
                                JSONObject dataJSONObject = jsonArray.getJSONObject(i);
                                if (dataJSONObject.length() > 0) {
                                    ProfileData profileData = new ProfileData();
                                    Iterator<String> iterator = dataJSONObject.keys();
                                    while (iterator.hasNext()) {
                                        String key = iterator.next();
                                        System.out.println(key);

                                        JSONObject dob = dataJSONObject.getJSONObject("dob");
                                        if (dob.length() > 0) {
                                            profileData.setAge(dob.getInt("age"));
                                        }

                                        profileData.setGender(dataJSONObject.getString("gender"));

                                        JSONObject name = dataJSONObject.getJSONObject("name");
                                        if (name.length() > 0) {

                                            profileData.setFirst(name.getString("first"));
                                            profileData.setLast(name.getString("last"));

                                        }

                                        JSONObject location = dataJSONObject.getJSONObject("location");
                                        if (location.length() > 0) {

                                            profileData.setCity(location.getString("city"));
                                            profileData.setPostcode(location.getInt("postcode"));

                                        }

                                        JSONObject media = dataJSONObject.getJSONObject("picture");
                                        if (media.length() > 0) {

                                            profileData.setLarge(media.getString("large"));

                                        }

                                    }
                                    profileDataArrayList.add(profileData);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    } else if (getProfilesJSON.has("results")) {
                        String msg = getProfilesJSON.getString("msg");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                recycler_home.setHasFixedSize(true);
                recycler_home.setClickable(false);
                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false){
                    @Override
                    public boolean canScrollHorizontally() {
                        return false;
                    }
                };

                recycler_home.setLayoutManager(layoutManager);
                recycler_home.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
                recycler_home.setItemAnimator(new DefaultItemAnimator());

                productAdapter = new ProductAdapter(context, profileDataArrayList, recycler_home);
                recycler_home.setAdapter(productAdapter);
                return false;
            }

            @Override
            public void failCallback(String response, boolean isNoInternet) {

                Toast.makeText(context, "No internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
        volleySender.sendAjax();
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        int as= Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
        return as;
    }
}