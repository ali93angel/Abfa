package com.app.leon.abfa.Models.InterCommunation;

import java.util.ArrayList;

/**
 * Created by Leon on 12/26/2017.
 */

public class HighLowWrapper {
    ArrayList<HighLowAlgorithmViewModel> highLowAlgorithmViewModels = new ArrayList<HighLowAlgorithmViewModel>();
    ArrayList<HighLowZoneProiorityViewModel> highLowZoneProiorityViewModels = new ArrayList<HighLowZoneProiorityViewModel>();

    public HighLowWrapper(ArrayList<HighLowAlgorithmViewModel> highLowAlgorithmViewModels, ArrayList<HighLowZoneProiorityViewModel> highLowZoneProiorityViewModels) {
        this.highLowAlgorithmViewModels = highLowAlgorithmViewModels;
        this.highLowZoneProiorityViewModels = highLowZoneProiorityViewModels;
    }

    public ArrayList<HighLowAlgorithmViewModel> getHighLowAlgorithmViewModels() {
        return highLowAlgorithmViewModels;
    }

    public void setHighLowAlgorithmViewModels(ArrayList<HighLowAlgorithmViewModel> highLowAlgorithmViewModels) {
        this.highLowAlgorithmViewModels = highLowAlgorithmViewModels;
    }

    public ArrayList<HighLowZoneProiorityViewModel> getHighLowZoneProiorityViewModels() {
        return highLowZoneProiorityViewModels;
    }

    public void setHighLowZoneProiorityViewModels(ArrayList<HighLowZoneProiorityViewModel> highLowZoneProiorityViewModels) {
        this.highLowZoneProiorityViewModels = highLowZoneProiorityViewModels;
    }
}
