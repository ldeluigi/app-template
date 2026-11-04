---
name: run-app
description: Launch the app on a project-local Android emulator and drive it - build and install the debug APK, navigate with adb taps and swipes, take screenshots, check another language. Use to see a change working in the real app, or to compare a build before and after a change.
---

# Run the app on the emulator

The emulator, its SDK packages and its virtual device live in the git-ignored `.emulator/`, so nothing is installed
system-wide. The app's id is `app.id` in `gradle.properties`.

## Environment

Every command below assumes this, run from the repository root (Git Bash on Windows, any shell elsewhere):

```bash
source .emulator/env.sh                      # $ADB, $EMULATOR, and the project-local SDK, AVD and user homes
export MSYS_NO_PATHCONV=1                    # Git Bash only: else /sdcard/... becomes a Windows path
APP_ID="$(sed -n 's/^app.id=//p' gradle.properties)"
SCRATCH=...                                  # any folder outside the repository: APKs, screenshots, dumps
```

## Boot

Start the emulator in the background (it runs until killed), then wait for the boot to finish:

```bash
"$EMULATOR" -avd app -no-snapshot-save -no-boot-anim > .emulator/emulator.log 2>&1 &
"$ADB" wait-for-device
until [ "$("$ADB" shell getprop sys.boot_completed | tr -d '\r')" = 1 ]; do sleep 3; done
"$ADB" shell input keyevent KEYCODE_WAKEUP
"$ADB" shell wm dismiss-keyguard
```

## Build, install, launch

```bash
./gradlew :androidApp:assembleDebug -q
"$ADB" install -r androidApp/build/outputs/apk/debug/androidApp-debug.apk
"$ADB" shell monkey -p "$APP_ID" -c android.intent.category.LAUNCHER 1
```

## See and drive

```bash
"$ADB" exec-out screencap -p > "$SCRATCH/shot.png"          # then look at it
"$ADB" shell uiautomator dump /sdcard/ui.xml > /dev/null && "$ADB" pull /sdcard/ui.xml "$SCRATCH/ui.xml" > /dev/null
sed 's/></>\n</g' "$SCRATCH/ui.xml" | grep -E 'text="[^"]+"' \
  | sed -E 's/.*text="([^"]*)".*bounds="([^"]*)".*/\1 | \2/'   # each text and its bounds
"$ADB" shell input tap 540 1200                               # tap the centre of a node's bounds
"$ADB" shell input swipe 540 1800 540 600 400                 # scroll down
```

Read positions from the dump rather than guessing them from a screenshot.

## Another language

```bash
"$ADB" shell cmd locale set-app-locales "$APP_ID" --locales it    # the app restarts in Italian
"$ADB" shell "cmd locale set-app-locales $APP_ID --locales ''"    # back to the phone's language
```

## Shut down

```bash
"$ADB" emu kill
```

## If `.emulator/` is missing

A fresh clone doesn't have it. Make an SDK root `.emulator/sdk` with the `emulator` and `platform-tools` packages, a
system image (`system-images;android-36;google_apis_playstore;x86_64`) and an empty `platforms/` folder (the emulator
refuses an SDK root without one), and an AVD named `app` in `.emulator/avd`. Install the packages with the Android
SDK's `cmdline-tools` (`sdkmanager --sdk_root=.emulator/sdk`), create the AVD with `avdmanager`, and write
`.emulator/env.sh` exporting `ANDROID_SDK_ROOT`, `ANDROID_AVD_HOME`, `ANDROID_USER_HOME`, `ADB` and `EMULATOR`.
