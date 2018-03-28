package edu.ntut.user.myhw3;

import android.widget.RadioGroup;

/**
 * Created by user on 2018/3/20.
 */

public class MarriageSuggestion {

    public String getSuggestion(String strSex, int iAgeRange, int numFamily) {

        String strSug = "建議：";

        if (strSex.equals("male")) {
            switch (iAgeRange) {
                case 1:
                    if (numFamily < 4)
                        strSug += "還不急";
                    else if (numFamily >= 4 && numFamily <= 10)
                        strSug += "開始找對象";
                    else
                        strSug += "趕快結婚";
                    break;
                case 2:
                    strSug += "開始找對象";
                    break;
                case 3:
                    strSug += "趕快結婚";
                    break;
            }
        } else {
        }

        return strSug;
    }
}
