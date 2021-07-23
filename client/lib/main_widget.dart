import 'package:client/cables/cables_widget.dart';
import 'package:client/devices/devices_widget.dart';
import 'package:flutter/material.dart';

/// This is the stateful widget that the main application instantiates.
class MainWidget extends StatefulWidget {
  const MainWidget({Key? key}) : super(key: key);

  @override
  State<MainWidget> createState() => _MainWidgetState();
}

/// This is the private State class that goes with MyStatefulWidget.
class _MainWidgetState extends State<MainWidget> {
  int _selectedIndex = 0;

  var list = [
    DevicesWidget(),
    CablesWidget(),
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Row(
        children: <Widget>[
          NavigationRail(
            selectedIndex: _selectedIndex,
            onDestinationSelected: (int index) {
              setState(() {
                _selectedIndex = index;
              });
            },
            labelType: NavigationRailLabelType.selected,
            destinations: const <NavigationRailDestination>[
              NavigationRailDestination(
                icon: Icon(Icons.devices_rounded),
                selectedIcon: Icon(Icons.devices),
                label: Text('Devices'),
              ),
              NavigationRailDestination(
                icon: Icon(Icons.cable_rounded),
                selectedIcon: Icon(Icons.cable),
                label: Text('Cables'),
              )
            ],
          ),
          const VerticalDivider(thickness: 1, width: 1),
          // This is the main content.
          Expanded(
            child: list[_selectedIndex],

          )
        ],
      ),
    );
  }
}
