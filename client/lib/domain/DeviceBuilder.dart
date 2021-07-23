import 'package:client/domain/Device.dart';

class DeviceBuilder {
  static Device fromJson(Map<dynamic,dynamic> json) {
    Device response = new Device();
    response.id = json['id'];
    response.name = json['name'];
    return response;
  }
}
