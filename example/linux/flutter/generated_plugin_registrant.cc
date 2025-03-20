//
//  Generated file. Do not edit.
//

// clang-format off

#include "generated_plugin_registrant.h"

#include <full_screen_notification/full_screen_notification_plugin.h>

void fl_register_plugins(FlPluginRegistry* registry) {
  g_autoptr(FlPluginRegistrar) full_screen_notification_registrar =
      fl_plugin_registry_get_registrar_for_plugin(registry, "FullScreenNotificationPlugin");
  full_screen_notification_plugin_register_with_registrar(full_screen_notification_registrar);
}
