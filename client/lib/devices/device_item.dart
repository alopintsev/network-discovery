import 'package:flutter/material.dart';

class DevicesItem extends StatelessWidget {
  late String itemName;

  DevicesItem(String s) {
    this.itemName = s;
  }

  @override
  Widget build(BuildContext context) {

    return ListTile(
      title: Text(itemName),
      subtitle: Text(itemName+"subtitle"),
    );
  }
}
