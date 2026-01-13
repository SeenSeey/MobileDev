package com.example.hoops_mobile_7.network.model;

import com.google.gson.annotations.SerializedName;

public class Info {
    @SerializedName("count")
    private int count;
    @SerializedName("pages")
    private int pages;
    @SerializedName("next")
    private String next;
    @SerializedName("prev")
    private String prev;

    public int getCount() { return count; }
    public int getPages() { return pages; }
    public String getNext() { return next; }
    public String getPrev() { return prev; }
}