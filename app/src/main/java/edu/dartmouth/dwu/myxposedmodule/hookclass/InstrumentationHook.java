package edu.dartmouth.dwu.myxposedmodule.hookclass;

import android.os.Binder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import edu.dartmouth.dwu.myxposedmodule.MethodParser;
import edu.dartmouth.dwu.myxposedmodule.Util;

public class InstrumentationHook extends MethodHook {
	private Methods mMethod = null;

	private static final String mClassName = "android.app.Instrumentation";

	private InstrumentationHook(Methods method) {
		super( mClassName, method.name());
		mMethod = method;
	}



	// @formatter:off

	// public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Activity target,Intent intent, int requestCode, Bundle options, UserHandle user)
	// public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Activity target,Intent intent, int requestCode, Bundle options)
	// frameworks/base/core/java/android/app/Instrumentation.java

	// @formatter:on

	private enum Methods {
		execStartActivity
	};

	public static List<MethodHook> getMethodHookList() {
		List<MethodHook> methodHookList = new ArrayList<MethodHook>();
		methodHookList.add(new InstrumentationHook(Methods.execStartActivity));

		return methodHookList;
	}

	public void before(MethodHookParam param) throws Throwable {
		if (mMethod == Methods.execStartActivity) {
			Log.i(Util.LOG_TAG, "\n\n***We got something here!\n");

			String[] argNamesArray = {"who", "contextThread", "token", "target", "intent", "requestCode", "options"};
			String formattedArgs = MethodParser.parseMethodArgs(param, argNamesArray);
			Log.i(Util.LOG_TAG, "FormattedArgs: " + formattedArgs + "\n");

			if (formattedArgs.contains("intent") && formattedArgs.contains("act=android.media.action.IMAGE_CAPTURE")) {
				Log.i(Util.LOG_TAG, param.args[0].toString() + " requested an image capture!!!\n");
				// block method call
				param.setResult(null);
			}
		}
	}
	
	@Override
	public void after(MethodHookParam param) throws Throwable {
		int uid = Binder.getCallingUid();
		String argNames = null;
		
		if(mMethod == Methods.execStartActivity){
			if(param.args.length == 8)
				argNames = "who|contextThread|token|target|intent|requestCode|options|user";
			else if(param.args.length == 7)
				argNames = "who|contextThread|token|target|intent|requestCode|options";
		}
		
		log(uid, param, argNames);
	}
}
