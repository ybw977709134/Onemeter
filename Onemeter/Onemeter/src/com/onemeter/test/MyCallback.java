package com.onemeter.test;

import java.util.ArrayList;

import com.onemeter.entity.DData;

public interface MyCallback {
 void updatePage(int page);
 void updateListView(ArrayList<DData> list);
 void clearListView();
}
