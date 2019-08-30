package com.datalife.datalife_company.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.datalife.datalife_company.activity.SimplebackActivity;
import com.datalife.datalife_company.bean.SimpleBackPage;
import com.datalife.datalife_company.fragment.FamilyFragment;

/**
 * Created by LG on 2019/7/16.
 */

public class UIHelper{

        /**
         * 不带有参的跳转
         */
        public static void showSimpleBackForResult(Activity context,
                                                   int requestCode, SimpleBackPage page) {
            Intent intent = new Intent(context, SimplebackActivity.class);
            intent.putExtra(SimplebackActivity.BUNDLE_KEY_PAGE, page.getValue());
            context.startActivityForResult(intent, requestCode);
}


        /**
         * 不带有参的跳转
         */
        public static void showSimpleBackForResult2(Fragment context,
                                                    int requestCode, SimpleBackPage page) {
            Intent intent = new Intent(context.getActivity(), SimplebackActivity.class);
            intent.putExtra(SimplebackActivity.BUNDLE_KEY_PAGE, page.getValue());
            context.startActivityForResult(intent, requestCode);
        }

        /**
         * 不带有参的跳转
         */
        public static void showSimpleBackForResult1(FamilyFragment context,
                                                    int requestCode, SimpleBackPage page) {
            Intent intent = new Intent(context.getActivity(), SimplebackActivity.class);
            intent.putExtra(SimplebackActivity.BUNDLE_KEY_PAGE, page.getValue());
            context.startActivityForResult(intent, requestCode);
        }

        /**
         *带有参的跳转
         */
        public static void showSimpleBackBundleForResult(Activity context,
                                                         int requestCode, SimpleBackPage page, Bundle bundle) {
            Intent intent = new Intent(context, SimplebackActivity.class);
            intent.putExtra(SimplebackActivity.BUNDLE_KEY_PAGE, page.getValue());
            intent.putExtra(SimplebackActivity.BUNDLE_KEY_ARGS,bundle);
            context.startActivityForResult(intent, requestCode);
        }

        /**
         * Fragment带有参的跳转
         */
        public static void showSimpleBackBundleForResult(Fragment context,
                                                         int requestCode, SimpleBackPage page, Bundle bundle) {
            Intent intent = new Intent(context.getActivity(), SimplebackActivity.class);
            intent.putExtra(SimplebackActivity.BUNDLE_KEY_PAGE, page.getValue());
            intent.putExtra(SimplebackActivity.BUNDLE_KEY_ARGS,bundle);
            context.startActivityForResult(intent, requestCode);
        }

        public static void launcherForResult(Activity activity, Class<?> targetActivity, int requestCode) {
            Intent intent = new Intent();
            intent.setClass(activity, targetActivity);
            activity.startActivityForResult(intent, requestCode);
        }

        public static void launcherForResult(Fragment activity, Class<?> targetActivity, int requestCode) {
            Intent intent = new Intent();
            intent.setClass(activity.getContext(), targetActivity);
            activity.startActivityForResult(intent, requestCode);
        }

        public static void launcherForResultBundle(Activity activity, Class<?> targetActivity, int requestCode,Bundle bundle) {
            Intent intent = new Intent();
            intent.setClass(activity, targetActivity);
            intent.putExtra(IDatalifeConstant.BUNDLEMEMBER,bundle);
            activity.startActivityForResult(intent, requestCode);
        }


        public static void launcher(Context context, Class<?> targetActivity) {
            Intent intent = new Intent();
            intent.setClass(context, targetActivity);
            context.startActivity(intent);
        }

        public static void launcherBundle(Activity activity, Class<?> targetActivity,Bundle bundle) {
            Intent intent = new Intent();
            intent.setClass(activity, targetActivity);
            intent.putExtra(IDatalifeConstant.BUNDLEMEMBER,bundle);
            activity.startActivity(intent);
        }
        public static void launcherBundle(Fragment activity, Class<?> targetActivity,Bundle bundle) {
            Intent intent = new Intent();
            intent.setClass(activity.getActivity(), targetActivity);
            intent.putExtra(IDatalifeConstant.BUNDLEMEMBER,bundle);
            activity.startActivity(intent);
        }

        public static void launcher(Activity activity, Class<?> targetActivity) {
            Intent intent = new Intent();
            intent.setClass(activity, targetActivity);
            activity.startActivity(intent);
        }

}

