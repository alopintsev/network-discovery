import 'dart:convert';
import 'dart:core';
import 'dart:io';

import 'package:client/domain/Connection.dart';
import 'package:client/domain/ConnectionBuilder.dart';
import 'package:client/domain/Device.dart';
import 'package:client/domain/DeviceBuilder.dart';
import 'package:http/http.dart' as http;
import 'package:logger/logger.dart';

class DeviceModel {
  var logger = Logger();

  Future<List<Device>> getDevices() async {
    logger.d("DeviceModel::getDevices");
    var response;
    List<Device> result = new List.empty();
    try {
      response =
          await http.get(Uri.parse("http://127.0.0.1:8080/rest/v1/devices"));
      if (response.statusCode != 200)
        throw HttpException('${response.statusCode}');
      Iterable l = json.decode(response.body);
      result =
          List<Device>.from(l.map((model) => DeviceBuilder.fromJson(model)));
    } catch (e) {
      logger.e('got exception: $e');
    }
    logger.d('DeviceModel::getDevices response: $response');
    return result;
  }

  Future<List<Connection>> getConnections(int deviceId) async {
    logger.d("DeviceModel::getConnections");
    var response;
    List<Connection> result = new List.empty();

    try {
      response = await http.get(Uri.parse(
          'http://localhost:8080/rest/v1/device/$deviceId/interfaces'));
      if (response.statusCode != 200)
        throw HttpException('${response.statusCode}');
      Iterable l = json.decode(response.body);
      result = List<Connection>.from(
          l.map((model) => ConnectionBuilder.fromJson(model)));
    } catch (e) {
      logger.e('got exception: $e');
    }
    logger.d('DeviceModel::getConnections response: $response');
    return result;
  }
}
