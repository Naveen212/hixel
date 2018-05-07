package com.hixel.hixel.company;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.hixel.hixel.R;
import com.hixel.hixel.dashboard.DashboardContract;
import com.hixel.hixel.data.Company;
import com.hixel.hixel.databinding.ActivityDashboardBinding;

public class CompanyActivity extends AppCompatActivity implements CompanyContract.View {

    private CompanyContract.Presenter presenter;
    private ActivityDashboardBinding binding;
    private String TAG = "COMPANY_VIEW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);

        presenter = new CompanyPresenter(this);
        presenter.setCompany((Company) getIntent().getSerializableExtra("company"));
        presenter.start();
        setCompanyDetails();
    }

    public void setPresenter(@NonNull CompanyContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public void setCompanyDetails() {
        TextView liquidity = findViewById(R.id.liquidity_score);
        TextView leverage = findViewById(R.id.leverage_score);
        TextView health = findViewById(R.id.health_score);

        liquidity.setText(presenter.getLiquidityScore());
        leverage.setText(presenter.getLeverageScore());
        health.setText(presenter.getHealthScore());
    }
}
