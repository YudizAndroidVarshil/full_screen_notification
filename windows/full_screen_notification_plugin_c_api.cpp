#include "include/full_screen_notification/full_screen_notification_plugin_c_api.h"

#include <flutter/plugin_registrar_windows.h>

#include "full_screen_notification_plugin.h"

void FullScreenNotificationPluginCApiRegisterWithRegistrar(
    FlutterDesktopPluginRegistrarRef registrar) {
  full_screen_notification::FullScreenNotificationPlugin::RegisterWithRegistrar(
      flutter::PluginRegistrarManager::GetInstance()
          ->GetRegistrar<flutter::PluginRegistrarWindows>(registrar));
}
