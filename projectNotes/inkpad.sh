echo "instrumenting uid 10084"
adb shell su -c setprop rw.eagleeye.nt.uids "10084"
adb shell su -c setprop rw.eagleeye.fr.uids "10084"
pid=$(adb shell ps | grep inkpad | tr -s ' ' | cut -d ' ' -f 2)
adb shell su -c kill $pid
adb logcat -s EagleEye:I
