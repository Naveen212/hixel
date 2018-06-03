package com.hixel.hixel.company;

import android.graphics.Color;
import android.util.Log;

import com.hixel.hixel.models.Company;
import com.hixel.hixel.network.Client;
import com.hixel.hixel.network.ServerInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyPresenter implements CompanyContract.Presenter {
    public Company getCompany() {
        return company;
    }

    private Company company;
    private final CompanyContract.View companyView;
    public static ArrayList<String>ratios1;



    private String tickerFromSearchSuggestion;

    CompanyPresenter(CompanyContract.View companyView) {
        this.companyView = companyView;
        companyView.setPresenter(this);

        ratios1=new ArrayList<>();
       // doMeta();
    }


    public void start() {
        doMeta();
        ratios1.add("");

    }

    public void loadFromServer() {

    }

    public String getRatio(String ratio, int year) {
        Double value = company.getRatio(ratio, year);

        return (value == null) ? "N/A" : Double.toString(value);
    }

    public int getColorIndicator(String ratio, double value) {
        //Default thresholds.
        double green = 1.5;
        double yellow = 1.0;

        //Specified thresholds for different ratios.
        switch (ratio) {
            case "Health":
                green = 0.7;
                yellow = 0.5;
                break;

            //TODO: Add other ratio thresholds.
        }

        return Color.parseColor((value > green) ? "#C23934":
                (value > yellow)? "#FFB75D":
                        "#4BCA81");
    }

    @Override
    public void doMeta() {
        ServerInterface client = Client.getRetrofit().create(ServerInterface.class);
        Call<ArrayList<String>> call = client.doMetaQuery();
        call.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                //searchSuggestion.setSearchEntries(response.body());
                //names = searchSuggestion.getNames();
                //if (names.size() != 0) {
                  //  Log.d("Search Suggestion=====", "" + names.get(0));
                //}
                ArrayList<String>stringArrayList=response.body();
                ratios1=stringArrayList;
                Log.d("ratios------------>",""+stringArrayList.size());
                companyView.updateRatios(ratios1);



            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                Log.d("Search Suggestion",
                        "Failed to load Search suggestions from the server: " + t.getMessage());
            }
        });
    }



    @Override
    public ArrayList<String> getratios1() {
        return ratios1;
    }



    public void setCompany(Company company) {
        this.company = company;
    }

    public String getCompanyName() {
        return company.getIdentifiers().getName();
    }

    public void setTickerFromSearchSuggestion(String tickerFromSearchSuggestion) {
        this.tickerFromSearchSuggestion = tickerFromSearchSuggestion;
        //loadDataForAParticularCompany(tickerFromSearchSuggestion);
    }
    public  void update()
    {

    }



}
