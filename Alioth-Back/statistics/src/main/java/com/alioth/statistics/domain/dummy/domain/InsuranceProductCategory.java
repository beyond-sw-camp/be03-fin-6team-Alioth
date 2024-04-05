package com.alioth.statistics.domain.dummy.domain;

public enum InsuranceProductCategory {
    HEALTH("건강보험"),
    WHOLELIFE("종신보험"),
    CAR("자동차보험"),
    LIFE("생명보험"),
    TRAVEL("여행자보험");

    private String insurance;

    private InsuranceProductCategory(String insurance) {
        this.insurance = insurance;
    }

    public String getInsurance() {
        return insurance;
    }

}
