import 'package:client/ProgressIndicator.dart';
import 'package:client/domain/Connection.dart';
import 'package:client/services/DeviceModel.dart';
import 'package:flutter/material.dart';
import 'package:logger/logger.dart';

class ConnectionsWidget extends StatefulWidget {
  final int deviceId;

  ConnectionsWidget(this.deviceId);

  @override
  State<StatefulWidget> createState() => _ConnectionsWidgetState(deviceId);
}

class _ConnectionsWidgetState extends State<ConnectionsWidget> {
  var logger = Logger();
  final int deviceId;

  _ConnectionsWidgetState(this.deviceId);

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
      initialData: [],
      future: _getConnections(),
      builder: (context, snapshot) {
        if (snapshot.data != null)
          switch (snapshot.connectionState) {
            case ConnectionState.none:
              break;
            case ConnectionState.waiting:
              return ProgressIndicatorWidget();
            case ConnectionState.active:
              break;
            case ConnectionState.done:
              logger.d('_ConnectionsWidgetState::build $deviceId ');
              final List<Connection> connections = snapshot.data as List<Connection>;
              return ListView.builder(
                  itemCount: connections.length,
                  itemBuilder: (context, index) {
                    Connection connection = connections[index];
                    return ListTile(
                        // onTap: () {
                        //   Navigator.pushNamed(context, '/device', arguments: device);
                        // },
                        title:Row(
                          children: <Widget>[
                            Text(connection.id.toString()),
                            Text(" - "),
                            Text(connection.localIfName),
                            Text(" - "),
                            Text(connection.remoteDevName),
                            Text(":"),
                            Text(connection.remoteIfName)
                          ],
                        ),
                        subtitle: Text(connection.name));
                  });
          }

        logger.e("_ConnectionsWidgetState::build()  unknown error");
        return SnackBar(
            content: Text("_ConnectionsWidgetState::build()  unknown error"));
      },
    );
  }

  Future _getConnections() async {
    DeviceModel deviceModel = new DeviceModel();
    var response = await deviceModel.getConnections(deviceId);
    return response;
  }
}
