import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:full_screen_notification/full_screen_notification.dart';
import 'package:full_screen_notification/models/notification_detail.dart';
import 'package:full_screen_notification_example/firebase_options.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp(
    options: DefaultFirebaseOptions.currentPlatform,
  );
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  static const MethodChannel _channel = MethodChannel('com.example.my_channel');

  final _fullScreenNotificationPlugin = FullScreenNotification();
 
  @override
  void initState() {
    super.initState();
    initFirebase();
    _channel.setMethodCallHandler(_handleNativeCall);
  }

  Future<void> _handleNativeCall(MethodCall call) async {
    if (call.method == "acceptButtonClick") {
      final spinnerValue = call.arguments as String?;
      print("VRS Accept button clicked with spinner value: $spinnerValue");
    } else if (call.method == "rejectButtonClick") {
      final spinnerValue = call.arguments as String?;
      print("VRS Reject button clicked with spinner value: $spinnerValue");
    } else if (call.method == "ignoreButtonClick") {
      final spinnerValue = call.arguments as String?;
      print("VRS Ignore button clicked with spinner value: $spinnerValue");
    } else if (call.method == "acceptAndClockOutClick") {
      final spinnerValue = call.arguments as String?;
      print("VRS Accept and Clock Out clicked with spinner value: $spinnerValue");
    } else if (call.method == "timeoutCallback") {
      print("VRS Activity timed out");
    }
  }

  Future<void> initFirebase() async {
    final notificationSettings = await FirebaseMessaging.instance.requestPermission(provisional: true);
    FirebaseMessaging.onBackgroundMessage(_firebaseMessagingBackgroundHandler);
    FirebaseMessaging.onMessage.listen((RemoteMessage message) async {
      print(
          'Message title: ${message.notification?.title}, body: ${message.notification?.body}, data: ${message.data}');
      // showNotificationScreen();
    });
    FirebaseMessaging.instance.getToken().then((token) {
      print('Device Token FCM: $token');
    });
  }

  showNotificationScreen() async {
    try {
      NotificationDetail detail = NotificationDetail(
          userName: 'Sunny Kadivar',
          image: 'https://picsum.photos/200',
          userId: '123',
          visitorName: "Varshil Soni",
          comments: "",
          from: "ABC Solution",
          purpose: "",
          email: "varshil.s@yudiz.com",
          logo: "https://picsum.photos/300");
      String? data = await _fullScreenNotificationPlugin
          .showFullScreenNotification(detail: detail);
      print('Data :: $data');
    } catch (e) {
      print('Error: Show Notification Screen $e');
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: TextButton(
            onPressed: () {
              showNotificationScreen();
            },
            child: Text('Show Notification Screen'),
          ),
        ),
      ),
    );
  }
}

@pragma('vm:entry-point')
Future<void> _firebaseMessagingBackgroundHandler(RemoteMessage message) async {
  print("Handling a background message data: ${message.data}");
  await Firebase.initializeApp(options: DefaultFirebaseOptions.currentPlatform);
  try {
    final fullScreenNotificationPlugin = FullScreenNotification();
    NotificationDetail detail = NotificationDetail(
        userName: 'Sunny Kadivar',
        image: 'https://picsum.photos/200',
        userId: '123',
        visitorName: "Varshil Soni",
        comments: "",
        from: "ABC Solution",
        purpose: "",
        email: "varshil.s@yudiz.com",
        logo: "https://picsum.photos/3000");
        String? data = await fullScreenNotificationPlugin
        .showFullScreenNotification(detail: detail);
    print('Data :: $data');
  } catch (e) {
    print('Error: Show Notification Screen $e');
  }
}
