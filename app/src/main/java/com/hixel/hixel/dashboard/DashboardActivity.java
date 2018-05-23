package com.hixel.hixel.dashboard;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.hixel.hixel.R;
import com.hixel.hixel.comparison.ComparisonActivity;
import com.hixel.hixel.databinding.ActivityDashboardBinding;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements DashboardContract.View,
        OnItemSelectedListener {

    private DashboardContract.Presenter presenter;
    RecyclerView.Adapter dashboardAdapter;
    ActivityDashboardBinding binding;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);

        // Set up the toolbar
        setSupportActionBar(binding.toolbar.toolbar);
        binding.toolbar.toolbarTitle.setText(R.string.dashboard);

        // Set up the dropdown options
        Spinner spinner = binding.spinner;
        ArrayAdapter<CharSequence> dropdownAdapter = ArrayAdapter.createFromResource(
                this, R.array.company_dropdown, android.R.layout.simple_spinner_item);
        dropdownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dropdownAdapter);
        spinner.setOnItemSelectedListener(this);

        // Set up the list of companies
        mRecyclerView = binding.recyclerView;

        // Set up the bottom navigation bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) binding.bottomNav;
        setupBottomNavigationView(bottomNavigationView);

        // TODO: Have a setup for the main chart here

        // Init presenter
        presenter = new DashboardPresenter(this);
        presenter.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem search = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) search.getActionView();
        searchView.setQueryHint("enter company...");

        SearchView.SearchAutoComplete searchAutoComplete =
                searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(Color.WHITE);
        searchAutoComplete.setTextColor(Color.WHITE);

        ImageView searchClose = searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchClose.setImageResource(R.drawable.ic_clear);

        ArrayAdapter<String> newsAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
        searchAutoComplete.setAdapter(newsAdapter);

        searchAutoComplete.setOnItemClickListener((adapterView, view, itemIndex, id) -> {

            String queryString = (String) adapterView.getItemAtPosition(itemIndex);
            searchAutoComplete.setText(queryString.trim().substring(0,queryString.lastIndexOf(' ')));
            String queryTicker = queryString.substring(queryString.lastIndexOf(' '));
            int index = queryTicker.indexOf(":");

            presenter.setTickerFromSearchSuggestion(queryTicker.substring(index + 1).trim());

            newsAdapter.notifyDataSetChanged();

        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                presenter.loadSearchSuggestion(searchAutoComplete.getText().toString());

                ArrayAdapter<String> newsAdapter =
                        new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, presenter.getNames());
                searchAutoComplete.setAdapter(newsAdapter);

                newsAdapter.notifyDataSetChanged();

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    // TODO: Implement this properly
    @Override
    public void setPresenter(@NonNull DashboardContract.Presenter presenter) {
        //presenter = presenter;
    }

    // TODO: Implement this so the default is nothing selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        presenter.sortCompaniesBy(item);
        // dashboardAdapter.notifyDataSetChanged();
    }

    public void setupBottomNavigationView(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {
                case R.id.home_button:
                    // Already on this screen.
                    break;
                case R.id.compare_button:
                    Intent moveToCompare = new Intent(this, ComparisonActivity.class);
                    startActivity(moveToCompare);
                    break;
                case R.id.settings_button:
                    // This screen is yet to be implemented
                    break;
            }

            return true;
        });
    }

    // TODO: Break this up into a setup and a populate
    @Override
    public void setupChart() {
        RadarChart chart = findViewById(R.id.chart);
        List<RadarEntry> entries = new ArrayList<>();
        String[] ratios = { "Current Ratio", "ROE", "D2E", "Quick Ratio", "Cash Ratio" };

        XAxis xAxis = chart.getXAxis();
        xAxis.setXOffset(0f);
        xAxis.setYOffset(0f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setTextSize(8f);

        xAxis.setValueFormatter((value, axis) -> ratios[(int) value]);

        YAxis yAxis = chart.getYAxis();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(2.0f);
        yAxis.setTextSize(10f);
        yAxis.setLabelCount(5, false);
        yAxis.setDrawLabels(false);

        // Test data
        entries.add(new RadarEntry(1.8f));
        entries.add(new RadarEntry(1.2f));
        entries.add(new RadarEntry(2.0f));
        entries.add(new RadarEntry(1.5f));
        entries.add(new RadarEntry(0.7f));

        RadarDataSet dataSet = new RadarDataSet(entries, "");
        dataSet.setColor(Color.parseColor("#4BCA81"));
        dataSet.setFillColor(Color.parseColor("#4BCA81"));
        dataSet.setDrawFilled(true);

        RadarData data = new RadarData(dataSet);
        data.setDrawValues(false);

        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setWebColor(Color.WHITE);
        chart.setWebColorInner(Color.WHITE);
        chart.setWebLineWidth(2f);

        chart.animateY(1400);

        chart.setData(data);
        chart.invalidate();
    }

    @Override
    public void setupDashboardAdapter() {
        dashboardAdapter = new DashboardAdapter(this, presenter);
        mRecyclerView.setAdapter(this.dashboardAdapter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void updateRatios(ArrayList<String> ratios1) {

    }

    @Override
    public void portfolioChanged() {
        dashboardAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // TODO: Implement this without the need for a Company object
    /*
    public void goToCompanyView() {
        Intent intent = new Intent(this, CompanyActivity.class);
        intent.putExtra("ticker", presenter.getCompany());
        startActivity(intent);
    }*/

    @Override
    public void setLoadingIndicator(final boolean active) {

        final ProgressBar progressBar = binding.progressBar;

        if (active) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public void showLoadingError() {
        Snackbar.make(binding.getRoot(), "Error loading your portfolio", Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", view -> presenter.loadPortfolio())
                .show();
    }

}

