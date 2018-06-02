package com.hixel.hixel.company;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.hixel.hixel.R;
import com.hixel.hixel.models.Company;

import java.util.ArrayList;


public class CompanyActivity extends AppCompatActivity implements CompanyContract.View {

    private CompanyContract.Presenter presenter;
    private String TAG = "COMPANY_VIEW";
    private ArrayList<String> ratios1 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_company);
        // doMeta();
        presenter = new CompanyPresenter(this);
        if(getIntent().hasExtra("company")) {
            presenter.setCompany((Company) getIntent().getSerializableExtra("company"));
        }
        else
        {
            presenter.setCompany((Company) getIntent().getSerializableExtra("ticker"));

        }


        /*
        Intent intentFromSearch=getIntent();
        String ticker=intentFromSearch.getStringExtra("ticker");
        presenter.setTickerFromSearchSuggestion(ticker);
        */
        presenter.start();



        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(presenter.getCompanyName());

        // from search suggestion

    }


    public void updateRatios(ArrayList<String> ratios1) {
        TextView liquidity = findViewById(R.id.liquidity_text);
        TextView leverage = findViewById(R.id.leverage_text);
        TextView health = findViewById(R.id.health_text);
        liquidity.setText(ratios1.get(0));
        leverage.setText(ratios1.get(1).substring(0,10));
        health.setText(ratios1.get(2).substring(0,10));

        TextView liquidityScore = findViewById(R.id.liquidity_score);
        TextView leverageScore = findViewById(R.id.leverage_score);
        TextView healthScore = findViewById(R.id.health_score);
        liquidityScore.setText(getValue(ratios1.get(0), 2017));
        leverageScore.setText(getValue(ratios1.get(1), 2017));
        healthScore.setText(getValue(ratios1.get(2), 2017));
        // For the line chart
        BarChart barChart=findViewById(R.id.lineChart);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(100);
        barChart.setPinchZoom(true);
        barChart.setDrawGridBackground(true);

        // values for the Bar chart
        ArrayList<BarEntry> barEntries=new ArrayList<>();
        barEntries.add(new BarEntry(1,120000f));
        barEntries.add(new BarEntry(2,144000f));
        barEntries.add(new BarEntry(3,130000f));
        barEntries.add(new BarEntry(4,36000f));

        BarDataSet barDataSet=new BarDataSet(barEntries,"Data set");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data=new BarData(barDataSet);
        data.setBarWidth(0.9f);

        barChart.setData(data);

        // create X axis
        String[] financial={"","","","",""};
        XAxis xAxis=barChart.getXAxis();
        xAxis.setValueFormatter(new MyXaxisValueFormatter(financial));

        // set up the list view
        ListView listView =findViewById(R.id.listView);
        // call the method to setup the values
        ArrayList<String> ratiosList=new ArrayList<>();
        ratiosList.add(getValue(ratios1.get(0),2017));
        ratiosList.add(getValue(ratios1.get(1),2017));
        ratiosList.add(getValue(ratios1.get(2),2017));
        ratiosList.add(getValue(ratios1.get(3),2017));
        ratiosList.add(getValue(ratios1.get(4),2017));


        CompanyAdapter adapter=new CompanyAdapter(this,R.layout.adpater_view_layout,ratiosList);
        listView.setAdapter(adapter);


    }

    public void setPresenter(@NonNull CompanyContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public String getValue(String name,int year)
    {
       String value= presenter.getRatio(name,year);
       if(value.length()>4)
       {
           return value.substring(0,5);
       }
       else
       {
           return value;
       }
    }
public class  MyXaxisValueFormatter implements IAxisValueFormatter
{
    String[] mValues;
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mValues[(int)value];
    }

    public MyXaxisValueFormatter(String[] values) {
        this.mValues=values;
    }
}


}
