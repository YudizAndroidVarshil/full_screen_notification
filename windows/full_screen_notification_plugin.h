#ifndef FLUTTER_PLUGIN_FULL_SCREEN_NOTIFICATION_PLUGIN_H_
#define FLUTTER_PLUGIN_FULL_SCREEN_NOTIFICATION_PLUGIN_H_

#include <flutter/method_channel.h>
#include <flutter/plugin_registrar_windows.h>

#include <memory>

namespace full_screen_notification {

class FullScreenNotificationPlugin : public flutter::Plugin {
 public:
  static void RegisterWithRegistrar(flutter::PluginRegistrarWindows *registrar);

  FullScreenNotificationPlugin();

  virtual ~FullScreenNotificationPlugin();

  // Disallow copy and assign.
  FullScreenNotificationPlugin(const FullScreenNotificationPlugin&) = delete;
  FullScreenNotificationPlugin& operator=(const FullScreenNotificationPlugin&) = delete;

  // Called when a method is called on this plugin's channel from Dart.
  void HandleMethodCall(
      const flutter::MethodCall<flutter::EncodableValue> &method_call,
      std::unique_ptr<flutter::MethodResult<flutter::EncodableValue>> result);
};

}  // namespace full_screen_notification

#endif  // FLUTTER_PLUGIN_FULL_SCREEN_NOTIFICATION_PLUGIN_H_
