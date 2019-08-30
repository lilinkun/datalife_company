package com.datalife.datalife_company.util;

import android.content.Context;

/**
 * Created by LG on 2019/7/24.
 */
public class StandardUtils {

    public static Standard comparsionWithStandard(Context context, int week, double bmi) {
        if (bmi < 18.5) {
            bmi = 18.5;
        } else if (bmi < 25) {
            bmi = 25;
        } else if (bmi < 30) {
            bmi = 30;
        } else {
            bmi = 100;
        }
        return new Standard(49, 1,1);

    }

    public static class Standard {
        private double min;
        private double max;
        private double middle;

        public Standard() {

        }

        public Standard(double min, double max) {
            super();
            this.min = min;
            this.max = max;
        }

        public Standard(double min, double max, double middle) {
            super();
            this.min = min;
            this.max = max;
            this.middle = middle;
        }

        public double getMin() {
            return min;
        }

        public void setMin(double min) {
            this.min = min;
        }

        public double getMax() {
            return max;
        }

        public void setMax(double max) {
            this.max = max;
        }

        public double getMiddle() {
            return middle;
        }

        public void setMiddle(double middle) {
            this.middle = middle;
        }
    }

}

