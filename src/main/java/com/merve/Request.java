package com.merve;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Request implements Serializable {
    @SerializedName("place")
    String place;

    @SerializedName("list")
    City[] list;

    @SerializedName("type")
    RequestType type;
}

enum RequestType {
    REPORT, LIST
}

