 class NotificationDetail {
  final String userName;
  final String visitorName;
  final String image;
  final String userId;
  final String from;
  final String purpose;
  final String comments;
  final String email;
  final String logo;


  NotificationDetail({
    required this.userName,
    required this.visitorName,
    required this.image,
    required this.userId,
    required this.from,
    required this.purpose,
    required this.comments,
    required this.email,
    required this.logo,
  });

  Map<String, dynamic> toMap() {
    return {
      'userName': userName,
      'visitorName': visitorName,
      'image': image,
      'userId': userId,
      'from' : from,
      'purpose' : purpose,
      'comments' : comments,
      'email' : email,
      'logo' : logo,
    };
  }

  factory NotificationDetail.fromMap(Map<String, dynamic> map) {
    return NotificationDetail(
      userName: map['userName'] ?? '',
      visitorName: map['visitorName'] ?? '',
      image: map['image'] ?? '',
      userId: map['userId'] ?? '',
      from: map['from'] ?? '',
      purpose: map['purpose'] ?? '',
      comments: map['comments'] ?? '',
      email: map['email'] ?? '',
      logo: map['logo'] ?? '',
    );
  }
}