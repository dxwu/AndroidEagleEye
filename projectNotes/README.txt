Installing and Working with Xposed
David Wu

Most instructions are from https://github.com/rovo89/XposedBridge/wiki/Development-tutorial

make sure the phone is rooted

install xposed installer
	http://repo.xposed.info/module/de.robv.android.xposed.installer
	adb install /path_to_apk
	open app -> framework -> Install/Update

install eagleeye apk (adb install EagleEye.apk)
enable apk in xposed installer 
reboot

get uid of app you want to trace:
	get name of package (adb shell; ps)
	adb shell dumpsys package com.android.chrome | grep userId=

set the prop for eagleeye to the uid:
	adb shell su -c setprop rw.eagleeye.nt.uids "10033"
	adb shell su -c setprop rw.eagleeye.fr.uids "10033"

restart the app:
	adb shell
	ps | grep chrome
	kill <pid>

See log info:
	adb logcat -s EagleEye:I


Developing xposed modules

	https://github.com/rovo89/XposedBridge/wiki/Development-tutorial
	** with the jar: 
		don't put in libs/ dir, put in root dir
		right click -> add as library
		android studio -> build -> edit libraries and dependencies -> app -> dependencies (top bar) -> jar -> scopt -> change to provided
	** with the assets folder
		gradle expects it to be in app/src/main/assets


--------------------------------------------------------------
--------------------------------------------------------------


Installing and Working with Xposed (ART) for Nexus 7 (2013)

Google Nexus 7 2013 Wifi (flo) - Armv7-a 32 bit

Root:
    http://forum.xda-developers.com/showthread.php?t=2382051

    download CF-root, extract, chmod +x root-mac.sh
        http://download.chainfire.eu/347/CF-Root/CF-Auto-Root/CF-Auto-Root-flo-razor-nexus7.zip?retrieve_file=1
    download adb, fastboot
    enable usb debugging

    adb reboot bootloader
    fastboot oem unlock
    confirm yes
    press power button to select start

    wait for device to reboot
    enable usb debugging again
    go into bootloader: adb reboot bootloader
    ./root-mac.sh

To flash custom recovery:
	fastboot oem unlock
	enable usb debugging
	adb reboot bootloader
	fastboot flash recovery recoveryfile.img
		for nexus 7, make sure it's ending in "flo"
		**** Because these nexus 7's were shipped with android 5.0 lollipop, and twrp original image was made for kit kat (4.4),
			the regular recovery images won't work. Get the multirom TWRP for flo and flash that instead
			http://forum.xda-developers.com/nexus-7-2013/nexus-7-2013-qa/mount-recovery-t3064562
	once it shows that it's flashed in term, use power and volume buttons in bootloader page to go to "recovery"

Installed Xposed for ART:
	http://forum.xda-developers.com/showthread.php?t=3034811
	If you want to install a zip, go into advanced, sideload with adb
	I unchecked "inject multirom after installation"
	in term, adb sideload xposed.zip
	then after you reboot, adb install xposed_installer.apk (make sure you uninstall any prev versions first)
