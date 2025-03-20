import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:full_screen_notification/models/notification_detail.dart';

import 'full_screen_notification_platform_interface.dart';

/// An implementation of [FullScreenNotificationPlatform] that uses method channels.
class MethodChannelFullScreenNotification extends FullScreenNotificationPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('full_screen_notification');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<String?> showFullScreenNotification({required NotificationDetail detail}) async {
    final version = await methodChannel.invokeMethod<String>('showFullScreenNotification',detail.toMap());
    return version;
  }
}
