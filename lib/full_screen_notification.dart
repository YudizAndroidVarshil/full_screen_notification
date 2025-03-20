
import 'package:full_screen_notification/models/notification_detail.dart';

import 'full_screen_notification_platform_interface.dart';

class FullScreenNotification {
  Future<String?> getPlatformVersion() {
    return FullScreenNotificationPlatform.instance.getPlatformVersion();
  }

  Future<String?> showFullScreenNotification({required NotificationDetail detail}) {
    return FullScreenNotificationPlatform.instance.showFullScreenNotification(detail: detail);
  }
}
