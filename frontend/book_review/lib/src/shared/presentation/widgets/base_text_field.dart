import 'package:book_review/src/core/styles/app_colors.dart';
import 'package:flutter/material.dart';

/// A customizable text field widget used across the app to provide consistent styling
/// and functionality, such as optional password visibility toggle and input type selection.
class BaseTextField extends StatelessWidget {
  /// Placeholder text shown inside the text field when it is empty.
  final String hintText;

  /// Indicates whether the text field should obscure text (typically used for passwords).
  final bool? isObscure;

  /// An optional icon displayed at the end of the text field.
  final IconButton? suffixIcon;

  /// Type of keyboard to be displayed, e.g., email, text, or number input.
  final TextInputType? textInputType;

  /// Controls the text being entered in the field, allowing the text to be accessed or manipulated.
  final TextEditingController controller;

  /// Constructor for the BaseTextField widget.
  ///
  /// * [hintText] is required and defines the placeholder text displayed when the field is empty.
  /// * [isObscure] indicates whether the text should be obscured, defaulting to false.
  /// * [suffixIcon] is an optional icon displayed at the end of the text field.
  /// * [textInputType] defines the type of keyboard layout to be shown, defaulting to standard text input.
  /// * [controller] is required and manages the text input by providing access to its current value.
  const BaseTextField({
    super.key,
    required this.hintText,
    this.isObscure = false,
    this.suffixIcon,
    this.textInputType = TextInputType.text,
    required this.controller,
  });

  @override
  Widget build(BuildContext context) {
    return TextField(
      decoration: InputDecoration(
          border: const UnderlineInputBorder(
            borderSide: BorderSide(color: AppColors.disabledButtonColor),
          ),
          focusedBorder: const UnderlineInputBorder(
            borderSide: BorderSide(color: AppColors.secondaryColor),
          ),
          errorBorder: const UnderlineInputBorder(
            borderSide: BorderSide(color: AppColors.errorColor),
          ),
          contentPadding: const EdgeInsets.only(top: 8, right: 20),
          hintText: hintText,
          hintStyle: Theme.of(context)
              .textTheme
              .bodyMedium
              ?.copyWith(color: AppColors.secondaryTextColor),
          suffixIcon: suffixIcon,
          suffixIconConstraints: BoxConstraints.tight(const Size(40, 30))),
      obscureText: isObscure ?? false,
      keyboardType: textInputType,
      controller: controller,
      style: Theme.of(context).textTheme.bodyMedium,
    );
  }
}
