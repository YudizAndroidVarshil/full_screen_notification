import 'package:full_screen_notification/models/notification_detail.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'full_screen_notification_method_channel.dart';

abstract class FullScreenNotificationPlatform extends PlatformInterface {
  /// Constructs a FullScreenNotificationPlatform.
  FullScreenNotificationPlatform() : super(token: _token);

  static final Object _token = Object();

  static FullScreenNotificationPlatform _instance = MethodChannelFullScreenNotification();

  /// The default instance of [FullScreenNotificationPlatform] to use.
  ///
  /// Defaults to [MethodChannelFullScreenNotification].
  static FullScreenNotificationPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [FullScreenNotificationPlatform] when
  /// they register themselves.
  static set instance(FullScreenNotificationPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<String?> showFullScreenNotification({required NotificationDetail detail}) {
    throw UnimplementedError('Show full screen notification has not been implemented.');
  }
}
