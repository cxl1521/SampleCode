package com.unit6_marriagesuggestion;

public class MarriageSuggestion {

    public String getSuggestion(String strSex, int iAge) {
        String strSug = "";
        if (strSex.equals("男"))
            if (iAge < 28)
                strSug += "還不急。";
            else if (iAge > 33)
                strSug += "趕快結婚！";
            else
                strSug += "開始找對象。";
        else
        if (iAge < 25)
            strSug += "還不急。";
        else if (iAge > 30)
            strSug += "趕快結婚！";
        else
            strSug += "開始找對象。";

        return strSug;
    }
}
