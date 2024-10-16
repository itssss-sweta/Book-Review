import 'package:flutter/material.dart';

/// BottomWaveClipper is a custom clipper that creates a wave-like pattern
/// at the bottom of a widget.
class BottomWaveClipper extends CustomClipper<Path> {
  @override
  Path getClip(Size size) {
    var path = Path();
    path.lineTo(0.0, size.height - 20);

    // Create the first wave
    path.quadraticBezierTo(
        size.width / 4, size.height, size.width / 2, size.height - 20);

    // Create the second wave
    path.quadraticBezierTo(
        size.width * 3 / 4, size.height - 40, size.width, size.height - 20);

    path.lineTo(size.width, 0.0);
    path.close();
    return path;
  }

  @override
  bool shouldReclip(covariant CustomClipper<Path> oldClipper) {
    return false;
  }
}
