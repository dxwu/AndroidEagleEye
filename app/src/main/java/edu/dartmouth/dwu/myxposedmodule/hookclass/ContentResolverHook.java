package edu.dartmouth.dwu.myxposedmodule.hookclass;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import de.robv.android.xposed.XposedHelpers;
import edu.dartmouth.dwu.myxposedmodule.MethodParser;
import edu.dartmouth.dwu.myxposedmodule.Util;

import android.annotation.SuppressLint;
import android.os.Binder;
import android.util.Log;
import android.net.Uri;
import android.content.ContentValues;

public class ContentResolverHook extends MethodHook {
	private Methods mMethod = null;
	private static final String mClassName = "android.content.ContentResolver";
	
	private ContentResolverHook(Methods method) {
		super(mClassName, method.name());
		mMethod = method;
	}
	

	// @formatter:off

	// public final Cursor query(final Uri uri, String[] projection,String selection, String[] selectionArgs, String sortOrder,CancellationSignal cancellationSignal)
	// public final Cursor query(Uri uri, String[] projection,String selection, String[] selectionArgs, String sortOrder)
	// public final Uri insert(Uri url, ContentValues values)
	// public final int update(Uri uri, ContentValues values, String where, String[] selectionArgs)
	// public final int delete(Uri url, String where, String[] selectionArgs)

	// frameworks/base/core/java/android/content/ContentResolver.java

	// @formatter:on
	private enum Methods {
		query, insert, update, delete
	};
	

	@SuppressLint("InlinedApi")
	public static List<MethodHook> getMethodHookList() {
		List<MethodHook> methodHookList = new ArrayList<MethodHook>();
		for(Methods method : Methods.values())
			methodHookList.add(new ContentResolverHook(method));
		
		return methodHookList;
	}

    public void before(MethodHookParam param) throws Throwable {
        if (mMethod == Methods.update) {
            String argNames = "uri|values|where|selectionArgs";
            String[] argNamesArray = argNames.split("\\|");
            String formattedArgs = MethodParser.parseMethodArgs(param, argNamesArray);
            //Log.i(Util.LOG_TAG, "*param: " + formattedArgs + "\n");

            if (formattedArgs.contains("values") && formattedArgs.contains("note")) {
                // param.args[1] = values arg
                // param.args[1].getClass() = android.content.ContentValues
                //Log.i(Util.LOG_TAG, "***param: " + param.args[1] +  "\n");
                //Log.i(Util.LOG_TAG, "type: " + param.args[1].getClass() + "\n");

                ContentValues cvs = (ContentValues)param.args[1];
                Log.i(Util.LOG_TAG, "value: " + cvs.getAsString("note") + "\n");
                cvs.put("note", "muhahaha");
                Log.i(Util.LOG_TAG, "new values: " + cvs);
            }
        }
    }
	
	public void after(MethodHookParam param) throws Throwable {
		int uid = Binder.getCallingUid();
		String argNames = null;
		
		if(mMethod == Methods.query){
			if(param.args.length == 6)
				argNames = "uri|projection|selection|selectionArgs|sortOrder|cancellationSignal";
			else if(param.args.length == 5)
				argNames = "uri|projection|selection|selectionArgs|sortOrder";
		}else if(mMethod == Methods.insert){
			argNames = "url|values";
		}else if(mMethod == Methods.update){
			argNames = "uri|values|where|selectionArgs";
		}else if(mMethod == Methods.delete){
			argNames = "url|where|selectionArgs";
		}

		log(uid, param, argNames);
	}
}
