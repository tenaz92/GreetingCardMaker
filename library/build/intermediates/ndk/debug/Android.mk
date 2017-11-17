LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := AndroidImageFilter
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_SRC_FILES := \
	/home/tenaz/AndroidStudioProjects/GreetingCard1/library/src/main/jni/GothamFilter.cpp \
	/home/tenaz/AndroidStudioProjects/GreetingCard1/library/src/main/jni/HDRFilter.cpp \
	/home/tenaz/AndroidStudioProjects/GreetingCard1/library/src/main/jni/AndroidImageFilter.cpp \
	/home/tenaz/AndroidStudioProjects/GreetingCard1/library/src/main/jni/GaussianBlurFilter.cpp \
	/home/tenaz/AndroidStudioProjects/GreetingCard1/library/src/main/jni/ColorTranslator.cpp \
	/home/tenaz/AndroidStudioProjects/GreetingCard1/library/src/main/jni/AverageSmoothFilter.cpp \
	/home/tenaz/AndroidStudioProjects/GreetingCard1/library/src/main/jni/OilFilter.cpp \
	/home/tenaz/AndroidStudioProjects/GreetingCard1/library/src/main/jni/MotionBlurFilter.cpp \
	/home/tenaz/AndroidStudioProjects/GreetingCard1/library/src/main/jni/LomoAddBlackRound.cpp \
	/home/tenaz/AndroidStudioProjects/GreetingCard1/library/src/main/jni/SharpenFilter.cpp \
	/home/tenaz/AndroidStudioProjects/GreetingCard1/library/src/main/jni/LightFilter.cpp \
	/home/tenaz/AndroidStudioProjects/GreetingCard1/library/src/main/jni/ReliefFilter.cpp \
	/home/tenaz/AndroidStudioProjects/GreetingCard1/library/src/main/jni/BrightContrastFilter.cpp \
	/home/tenaz/AndroidStudioProjects/GreetingCard1/library/src/main/jni/TvFilter.cpp \
	/home/tenaz/AndroidStudioProjects/GreetingCard1/library/src/main/jni/SketchFilter.cpp \
	/home/tenaz/AndroidStudioProjects/GreetingCard1/library/src/main/jni/Application.mk \
	/home/tenaz/AndroidStudioProjects/GreetingCard1/library/src/main/jni/SoftGlowFilter.cpp \
	/home/tenaz/AndroidStudioProjects/GreetingCard1/library/src/main/jni/cn_Ragnarok_NativeFilterFunc.cpp \
	/home/tenaz/AndroidStudioProjects/GreetingCard1/library/src/main/jni/PixelateFilter.cpp \
	/home/tenaz/AndroidStudioProjects/GreetingCard1/library/src/main/jni/HueSaturationFilter.cpp \
	/home/tenaz/AndroidStudioProjects/GreetingCard1/library/src/main/jni/GammaCorrectionFilter.cpp \
	/home/tenaz/AndroidStudioProjects/GreetingCard1/library/src/main/jni/Android.mk \
	/home/tenaz/AndroidStudioProjects/GreetingCard1/library/src/main/jni/NeonFilter.cpp \
	/home/tenaz/AndroidStudioProjects/GreetingCard1/library/src/main/jni/BlockFilter.cpp \

LOCAL_C_INCLUDES += /home/tenaz/AndroidStudioProjects/GreetingCard1/library/src/main/jni
LOCAL_C_INCLUDES += /home/tenaz/AndroidStudioProjects/GreetingCard1/library/src/debug/jni

include $(BUILD_SHARED_LIBRARY)
