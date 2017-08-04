LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := cocos2dcpp_shared

LOCAL_MODULE_FILENAME := libcocos2dcpp

LOCAL_SRC_FILES := hellocpp/main.cpp \
                  ../../Classes/AboutLayer.cpp \
../../Classes/AchieveCocosBuilder/AchieveCocosBuilder.cpp \
../../Classes/AchieveCocosBuilder/CustomTableViewCell.cpp \
../../Classes/AppDelegate.cpp \
../../Classes/AStar/Astar.cpp \
../../Classes/AStar/Astaritem.cpp \
../../Classes/Box.cpp \
../../Classes/Bullet.cpp \
../../Classes/CCShake/CCShake.cpp \
../../Classes/DrawDebug/GLES-Render.cpp \
../../Classes/EditBoxLayer/EditBoxLayer.cpp \
../../Classes/enemyBullet.cpp \
../../Classes/enemyTank.cpp \
../../Classes/GameScene.cpp \
../../Classes/GB2ShapeCache/GB2ShapeCache-x.cpp \
../../Classes/GCLayer.cpp \
../../Classes/HelpLayer.cpp \
../../Classes/landMine.cpp \
../../Classes/MenuCocosBuilder/MapSelectCocosBuilderLayer.cpp \
../../Classes/MenuCocosBuilder/MenuCocosBuilderLayer.cpp \
../../Classes/MenuLayer.cpp \
../../Classes/option/option.cpp \
../../Classes/PauseLayer.cpp \
../../Classes/Props.cpp \
../../Classes/ShopCocosBuilder/BoxCocosBuilderLayer.cpp \
../../Classes/ShopCocosBuilder/cashShopCocosBuilderLayer.cpp \
../../Classes/ShopCocosBuilder/magazine_frameCocosBuilderLayer.cpp \
../../Classes/ShopCocosBuilder/ShopCocosBuilderLayer.cpp \
../../Classes/SimpleTimer/SimpleTimer.cpp \
../../Classes/SkillButton/ImmediateButton.cpp \
../../Classes/SkillButton/SkillButton.cpp \
../../Classes/tanker.cpp \
../../Classes/teach/shopTeach_1.cpp \
../../Classes/teach/shopTeach_2.cpp \
../../Classes/Tool.cpp \
../../Classes/sqlite3/sqlite3.c \
../../Classes/UserDataConsistencyCCSprite.cpp \
../../Classes/UserRecord/UserRecord.cpp \
../../Classes/xml/XmlParser.cpp \


LOCAL_C_INCLUDES := $(LOCAL_PATH)/../../Classes

LOCAL_WHOLE_STATIC_LIBRARIES += cocos2dx_static
LOCAL_WHOLE_STATIC_LIBRARIES += cocosdenshion_static
LOCAL_WHOLE_STATIC_LIBRARIES += cocos_extension_static
LOCAL_WHOLE_STATIC_LIBRARIES += cocos_ssl_static
LOCAL_WHOLE_STATIC_LIBRARIES += cocos_crypto_static

include $(BUILD_SHARED_LIBRARY)
$(call import-add-path,D:/cocos2d/cocos2d-x-2.2.3)\
$(call import-add-path,D:/cocos2d/cocos2d-x-2.2.3/cocos2dx/platform/third_party/android/prebuilt)

$(call import-module,cocos2dx)
$(call import-module,cocos2dx/platform/third_party/android/prebuilt/libcurl)
#$(call import-module,cocos2dx/platform/third_party/android/prebuilt/libcrypto)
#$(call import-module,cocos2dx/platform/third_party/android/prebuilt/libssl)
$(call import-module,CocosDenshion/android)
$(call import-module,extensions)



