import 'package:client/devices/connections_widget.dart';
import 'package:client/domain/Device.dart';
import 'package:flutter/material.dart';

class DeviceDetailWidget extends StatefulWidget {
  const DeviceDetailWidget({Key? key}) : super(key: key);

  @override
  State<DeviceDetailWidget> createState() => _DeviceDetailWidgetState();
}

class _DeviceDetailWidgetState extends State<DeviceDetailWidget> {
  @override
  Widget build(BuildContext context) {
    final device = ModalRoute.of(context)!.settings.arguments as Device;

    return Scaffold(
      body: Row(
        children: <Widget>[
          Expanded(
            // child: Text(device.name)
            child: ConnectionsWidget(device.id),
          )
        ],
      ),
    );
  }
}
