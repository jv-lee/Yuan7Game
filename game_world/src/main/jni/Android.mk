LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := hellocpp_shared

LOCAL_MODULE_FILENAME := libhellocpp

LOCAL_SRC_FILES := hellocpp/main.cpp \
                   hellocpp/PayJava.cpp \
				   ../../Classes/AppDelegate.cpp \
									../../Classes/CCShake.cpp \
									../../Classes/cocos2dUIEx.cpp \
									../../Classes/game/BackgroundLayer.cpp \
									../../Classes/game/CloudLayer.cpp \
									../../Classes/game/CountingLayer.cpp \
									../../Classes/game/ExitLayer.cpp \
									../../Classes/game/GameData.cpp \
									../../Classes/game/GameScene.cpp \
									../../Classes/game/HelpLayer.cpp \
									../../Classes/game/InGameLayer.cpp \
									../../Classes/game/KK.cpp \
									../../Classes/game/KKData.cpp \
									../../Classes/game/KKLayer.cpp \
									../../Classes/game/LevelData.cpp \
									../../Classes/game/MainMenuLayer.cpp \
									../../Classes/game/MainMenuScene.cpp \
									../../Classes/game/MapLayer.cpp \
									../../Classes/game/PanelLayer.cpp \
									../../Classes/game/PauseLayer.cpp \
									../../Classes/game/PhysicsLayer.cpp \
									../../Classes/game/ReadyLayer.cpp \
									../../Classes/game/ShopLayer.cpp \
									../../Classes/game/TargetLayer.cpp \
									../../Classes/game/TouchLayer.cpp \
									../../Classes/HelloWorldScene.cpp \
									../../Classes/Pay.cpp \
									../../Classes/PersonalAudioEngine.cpp \
									../../Classes/StaticData.cpp \
									../../Classes/tinyxml/tinystr.cpp \
									../../Classes/tinyxml/tinyxml.cpp \
									../../Classes/tinyxml/tinyxmlerror.cpp \
									../../Classes/tinyxml/tinyxmlparser.cpp 

LOCAL_C_INCLUDES := $(LOCAL_PATH)/../../Classes

LOCAL_WHOLE_STATIC_LIBRARIES := cocos2dx_static  cocosdenshion_static cocos_extension_static 

include $(BUILD_SHARED_LIBRARY)

$(call import-module,CocosDenshion/android) 
$(call import-module,cocos2dx) 
$(call import-module,extensions)








