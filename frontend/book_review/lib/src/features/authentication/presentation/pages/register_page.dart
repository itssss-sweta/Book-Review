import 'package:book_review/src/core/styles/app_colors.dart';
import 'package:book_review/src/features/authentication/presentation/widgets/custom_labeled_text_field.dart';
import 'package:book_review/src/shared/presentation/widgets/base_primary_button.dart';
import 'package:flutter/material.dart';

class RegisterPage extends StatelessWidget {
  const RegisterPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(),
      body: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 20),
        child: SingleChildScrollView(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.start,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                'Create an Account',
                style: Theme.of(context).textTheme.headlineLarge,
              ),
              const SizedBox(height: 20),
              CustomLabeledTextField(
                label: 'Username',
                hintText: 'Enter your username',
                controller: TextEditingController(),
                keyboardType: TextInputType.name,
              ),
              const SizedBox(height: 20),
              CustomLabeledTextField(
                label: 'Email',
                hintText: 'Enter your email',
                controller: TextEditingController(),
                keyboardType: TextInputType.emailAddress,
              ),
              const SizedBox(height: 20),
              CustomLabeledTextField(
                label: 'New Password',
                hintText: 'Enter your password',
                controller: TextEditingController(),
                isObscure: true,
                suffixIcon: IconButton(
                    onPressed: () {},
                    icon: const Icon(
                      Icons.visibility,
                      color: AppColors.accentColor,
                    )),
              ),
              const SizedBox(height: 20),
              CustomLabeledTextField(
                label: 'Confirm Password',
                hintText: 'Confirm your password',
                controller: TextEditingController(),
                isObscure: true,
                suffixIcon: IconButton(
                    onPressed: () {},
                    icon: const Icon(
                      Icons.visibility,
                      color: AppColors.accentColor,
                    )),
              ),
              const SizedBox(height: 50),
              BasePrimaryButton(
                label: 'Register',
                onPressed: () {},
                buttonWidth: MediaQuery.sizeOf(context).width,
              ),
            ],
          ),
        ),
      ),
    );
  }
}
