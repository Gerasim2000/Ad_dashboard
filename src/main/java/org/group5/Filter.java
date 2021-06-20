package org.group5;

import java.util.Date;

public class Filter {

    public void setStart(Date start) {
        this.start = start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    private Date start;
    private Date end;

    private String gender;
    private boolean hasGender;

    private String age;
    private boolean hasAge;

    private String income;
    private boolean hasIncome;

    private String context;
    private boolean hasContext;

    public static final String GENDER_MALE = "Male";
    public static final String GENDER_FEMALE = "Female";

    public static final String AGE_LESSTHAN25 = "<25";
    public static final String AGE_25TO34 = "25-34";
    public static final String AGE_35TO44 = "35-44";
    public static final String AGE_45TO54 = "45-54";
    public static final String AGE_GREATERTHAN54 = ">54";

    public static final String INCOME_LOW = "Low";
    public static final String INCOME_MED = "Medium";
    public static final String INCOME_HIGH = "High";

    public static final String CONTEXT_NEWS = "News";
    public static final String CONTEXT_SHOPPING = "Shopping";
    public static final String CONTEXT_SOCIAL = "Social";
    public static final String CONTEXT_MEDIA = "Media";
    public static final String CONTEXT_BLOG = "Blog";
    public static final String CONTEXT_HOBBIES = "Hobbies";
    public static final String CONTEXT_TRAVEL = "Travel";

    public Filter(Date start, Date end) {
        this.start = start;
        this.end = end;
        hasGender = false;
        hasAge = false;
        hasIncome = false;
        hasContext = false;
    }

    public void setGender(String gender) {
        this.gender = gender;
        hasGender = true;
    }

    public boolean hasGender() {
        return hasGender;
    }

    public void setAge(String age) {
        this.age = age;
        hasAge = true;
    }

    public boolean hasAge() {
        return hasAge;
    }

    public void setIncome(String income) {
        this.income = income;
        hasIncome = true;
    }

    public boolean hasIncome() {
        return hasIncome;
    }

    public void setContext(String context) {
        this.context = context;
        hasContext = true;
    }

    public boolean hasContext() {
        return hasContext;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public String getGender() {
        return gender;
    }

    public String getAge() {
        return age;
    }

    public String getIncome() {
        return income;
    }

    public String getContext() {
        return context;
    }


}
