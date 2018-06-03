package com.hixel.hixel.dashboard;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hixel.hixel.R;
import com.hixel.hixel.company.CompanyActivity;

import java.util.Calendar;
import java.util.Locale;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    private final DashboardContract.Presenter presenter;
    private Context context;

    DashboardAdapter(Context context, DashboardContract.Presenter presenter) {
        this.presenter = presenter;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        String companyName = presenter.getCompanies()
                .get(position)
                .getIdentifiers()
                .getName()
                .split("\\,| ")[0]
                .toLowerCase();

        companyName =
                companyName.substring(0, 1).toUpperCase() + companyName.substring(1);

        holder.companyName.setText(companyName);

        holder.companyTicker.setText(presenter.getCompanies()
                                              .get(position)
                                              .getIdentifiers()
                                              .getTicker());

        int last_year = Calendar.getInstance().get(Calendar.YEAR) - 1;


        // TODO: Replace with an 'indicator arrow'
        holder.companyIndicator.setText(String.format(Locale.ENGLISH, "%.1f%%",
                                    presenter.getCompanies()
                                                   .get(position)
                                                   .getRatio("Return on Equity", last_year) * 100));

        // TODO: Replace as part of PTH-140
        //holder.companyHealth.setTextColor(presenter.setHealthColor(position));

        holder.parentLayout.setOnClickListener((View view) -> {
            Intent intent = new Intent(context, CompanyActivity.class);
            intent.putExtra("company",
                    presenter.getCompanies().get(holder.getAdapterPosition()));

            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return presenter.getCompanies().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout parentLayout;
        TextView companyName;
        TextView companyTicker;
        TextView companyIndicator;

        ViewHolder(View itemView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.company_name);
            companyTicker = itemView.findViewById(R.id.company_ticker);
            companyIndicator = itemView.findViewById(R.id.indicator_value);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
