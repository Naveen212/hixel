package com.hixel.hixel.company;

import com.hixel.hixel.BasePresenter;
import com.hixel.hixel.BaseView;
import com.hixel.hixel.models.Company;

import java.util.ArrayList;

public interface CompanyContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        String getCompanyName();
        void setCompany(Company company);
        String getRatio(String name, int year);

        int getColorIndicator(String ratio, double value);
        void doMeta();
        ArrayList<String> getratios1();
        void setTickerFromSearchSuggestion(String tickerFromSearchSuggestion);
        Company getCompany();
    }
}
