import 'package:book_review/src/core/styles/app_colors.dart';
import 'package:flutter/material.dart';

/// A reusable primary button widget that can be customized with a label,
/// background color, border color, and text color.
/// It uses a TextButton with custom styles for flexibility in various UI parts.
class BasePrimaryButton extends StatelessWidget {
  /// The label text displayed on the button.
  final String label;

  /// The background color of the button. Defaults to `AppColors.secondaryColor` if not provided.
  final Color? buttonColor;

  /// The border color of the button. If not provided, a default with a 20% opacity of `AppColors.primaryTextColor` is used.
  final Color? borderColor;

  /// The color of the button's text. Defaults to `AppColors.white`.
  final Color? textColor;

  /// The function that gets called when the button is pressed.
  final VoidCallback? onPressed;

  /// Height of button.
  final double? buttonHeight;

  /// Width of button.
  final double? buttonWidth;

  /// Constructor for the BasePrimaryButton widget.
  ///
  /// * [label] is required and defines the text on the button.
  /// * [buttonColor] sets the background color of the button, with a default value.
  /// * [borderColor] sets the border color, with a fallback to a partially transparent primary text color.
  /// * [textColor] sets the color of the text, defaulting to white.
  /// * [onPressed] is the function to handle button presses, with a default empty function.
  const BasePrimaryButton({
    super.key,
    required this.label,
    this.buttonColor = AppColors.secondaryColor,
    this.borderColor,
    this.textColor = AppColors.white,
    required this.onPressed,
    this.buttonHeight,
    this.buttonWidth,
  });

  @override
  Widget build(BuildContext context) {
    return TextButton(
      onPressed: onPressed,
      style: ButtonStyle(
        alignment: Alignment.center,
        shape: WidgetStatePropertyAll(
          RoundedRectangleBorder(
              side: BorderSide(
                color:
                    borderColor ?? AppColors.primaryTextColor.withOpacity(0.2),
              ),
              borderRadius: const BorderRadius.all(Radius.circular(28))),
        ),
        fixedSize: WidgetStatePropertyAll(Size(
            buttonWidth ?? MediaQuery.sizeOf(context).width / 1.2,
            buttonHeight ?? 50)),
        backgroundColor: WidgetStatePropertyAll(buttonColor),
      ),
      child: Text(
        label,
        style:
            Theme.of(context).textTheme.labelLarge?.copyWith(color: textColor),
      ),
    );
  }
}
