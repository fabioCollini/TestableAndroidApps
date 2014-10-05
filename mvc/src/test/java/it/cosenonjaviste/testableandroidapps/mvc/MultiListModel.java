package it.cosenonjaviste.testableandroidapps.mvc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fabiocollini on 05/10/14.
 */
public class MultiListModel {
    public List<Integer> list1 = new ArrayList<>();
    public List<Integer> list2 = new ArrayList<>();

    public MultiListModel clona() {
        MultiListModel multiListModel = new MultiListModel();
        multiListModel.list1 = new ArrayList<>(list1);
        multiListModel.list2 = new ArrayList<>(list2);
        return multiListModel;
    }

    @Override public String toString() {
        return "KillModel{" +
                "list1=" + list1 +
                ", list2=" + list2 +
                '}';
    }

    public String getKey() {
        return "key";
    }
}
