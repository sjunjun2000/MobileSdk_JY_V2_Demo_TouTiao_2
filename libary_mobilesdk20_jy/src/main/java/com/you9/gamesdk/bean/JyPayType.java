package com.you9.gamesdk.bean;

import java.io.Serializable;

public class JyPayType implements Serializable {
    private String pay_type;
    private String name;

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
