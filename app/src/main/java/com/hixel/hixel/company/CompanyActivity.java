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
import com.hixel.hixel.models.FinancialData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


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
    // TODO: The updateRatios()method will be cleaned up and refactored soon.Ignore this method for the time being.


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
        ArrayList<BigDecimal> values=new ArrayList<>();
        List<FinancialData>dataEntries=presenter.getCompany().getFinancialDataEntries();
        FinancialData dataFinanccial=dataEntries.get(0);
        HashMap<String, BigDecimal> xbrlElements=dataFinanccial.getXbrlElements();

        BigDecimal[] value=new BigDecimal[10];
        int i=0;
        for(String key:xbrlElements.keySet())
        {
            value[i]=xbrlElements.get(key);
            i++;
        }
        BigDecimal n1=value[4];
        String valueString=n1.toString();
        String intValue=valueString.substring(0,6);
        int n=Integer.parseInt(intValue);
        int[] nValues=new int[4];
        nValues[0]=n; // Assets
        n1=value[5];
        valueString=n1.toString();
        intValue=valueString.substring(0,6);
        n=Integer.parseInt(intValue);
        nValues[1]=n; //Liabilities

        n1=value[7];
        valueString=n1.toString();
        intValue=valueString.substring(0,6);
        n=Integer.parseInt(intValue);
        nValues[2]=n; // Equity

        n1=value[8];
        valueString=n1.toString();
        intValue=valueString.substring(0,6);
        n=Integer.parseInt(intValue);
        nValues[3]=n; //Net Income Loss

        barEntries.add(new BarEntry(1,nValues[0]));
        barEntries.add(new BarEntry(2,nValues[1]));
        barEntries.add(new BarEntry(3,nValues[2]));
        barEntries.add(new BarEntry(4,nValues[3]));

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
public boolean checkNull(String value)
{
    if(value.equals("null"))
    {
        return true;
    }
    return false;
}


}
