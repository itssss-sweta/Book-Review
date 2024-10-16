import 'package:book_review/src/core/styles/app_colors.dart';
import 'package:book_review/src/shared/presentation/widgets/base_text_field.dart';
import 'package:flutter/material.dart';

class CustomLabeledTextField extends StatelessWidget {
  final String label;
  final bool? isRequired;
  final String hintText;
  final bool? isObscure;
  final IconButton? suffixIcon;
  final TextInputType? keyboardType;
  final TextEditingController controller;
  const CustomLabeledTextField({
    super.key,
    this.isRequired = true,
    required this.label,
    required this.hintText,
    this.isObscure,
    this.suffixIcon,
    this.keyboardType,
    required this.controller,
  });

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      mainAxisAlignment: MainAxisAlignment.start,
      children: [
        RichText(
          text: TextSpan(
              text: label,
              style: Theme.of(context).textTheme.labelLarge,
              children: [
                TextSpan(
                  text: (isRequired!) ? ' *' : '',
                  style: Theme.of(context)
                      .textTheme
                      .labelLarge
                      ?.copyWith(color: AppColors.errorColor),
                ),
              ]),
        ),
        BaseTextField(
          hintText: hintText,
          controller: TextEditingController(),
          isObscure: isObscure,
          suffixIcon: suffixIcon,
          textInputType: keyboardType,
        ),
      ],
    );
  }
}
