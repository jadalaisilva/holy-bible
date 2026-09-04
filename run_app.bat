@echo off
setlocal
set "JAVA_HOME=C:\Program Files\Android\openjdk\jdk-21.0.8"
set "ANDROID_HOME=C:\Program Files (x86)\Android\android-sdk"
set "PATH=%JAVA_HOME%\bin;%ANDROID_HOME%\platform-tools;%PATH%"

cd /d "%~dp0"

echo [1/3] Compilando e instalando en dispositivo Android...
call gradlew.bat installDebug
if %ERRORLEVEL% NEQ 0 (
    echo Error compilando/instalando APK.
    exit /b %ERRORLEVEL%
)

echo [2/3] Iniciando aplicacion en dispositivo...
adb shell am start -n com.jadalai.reinavalera1960/.MainActivity

echo [3/3] Aplicacion ejecutandose.
endlocal
