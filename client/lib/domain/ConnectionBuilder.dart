import 'package:client/domain/Connection.dart';

class ConnectionBuilder {
  static Connection fromJson(Map<dynamic,dynamic> json) {
    Connection response = new Connection();
    response.id = json['connectionId'];
    response.localIfName = json['localInterfaceName'];
    response.remoteIfName = json['remoteInterfaceName'];
    response.remoteDevName = json['remoteDeviceName'];
    response.name = json['connectionName'];
    return response;
  }
}
