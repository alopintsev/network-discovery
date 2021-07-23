import 'package:client/ProgressIndicator.dart';
import 'package:client/domain/Device.dart';
import 'package:client/services/DeviceModel.dart';
import 'package:flutter/material.dart';
import 'package:logger/logger.dart';

class DevicesWidget extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => _DeviceWidgetState();
}

class _DeviceWidgetState extends State<DevicesWidget> {
  var logger = Logger();

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
      initialData: [],
      future: _getDevices(),
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
              final List<Device> deviceList = snapshot.data as List<Device>;
              return ListView.builder(
                  itemCount: deviceList.length,
                  itemBuilder: (context, index) {
                    Device device = deviceList[index];
                    return ListTile(
                        onTap: () {
                          Navigator.pushNamed(context, '/device', arguments: device);
                        },
                        title: Text(device.name),
                        subtitle: Text(device.id.toString()));
                  });
          }

        logger.e("DevicesWidget::build()  unknown error");
        return SnackBar(content: Text("DevicesWidget::build()  unknown error"));
      },
    );
  }

  Future _getDevices() async {
    DeviceModel deviceModel = new DeviceModel();
    List<Device> deviceList = await deviceModel.getDevices();
    return deviceList;
  }
}
