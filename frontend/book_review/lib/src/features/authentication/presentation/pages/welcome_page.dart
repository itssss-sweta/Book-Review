import 'package:book_review/src/core/styles/app_colors.dart';
import 'package:book_review/src/features/authentication/presentation/pages/login_page.dart';
import 'package:book_review/src/features/authentication/presentation/pages/register_page.dart';
import 'package:book_review/src/features/authentication/presentation/widgets/bottom_clipper.dart';
import 'package:book_review/src/shared/presentation/widgets/base_primary_button.dart';
import 'package:flutter/material.dart';

/// WelcomePage is a stateless widget that represents the main welcome screen of the app.
class WelcomePage extends StatelessWidget {
  /// A stateless widget that represents the main welcome screen of the app.
  const WelcomePage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        children: [
          ClipPath(
            clipper: BottomWaveClipper(),
            child: Stack(
              children: [
                Image.asset(
                  'assets/images/welcome_logo.jpg',
                  height: MediaQuery.sizeOf(context).height / 2,
                  width: double.infinity,
                  fit: BoxFit.cover,
                ),
                Container(
                  height: MediaQuery.sizeOf(context).height / 2,
                  decoration: BoxDecoration(
                    gradient: LinearGradient(
                      colors: [
                        AppColors.backgroundColor.withOpacity(0.0),
                        AppColors.backgroundColor.withOpacity(0.0),
                        AppColors.backgroundColor.withOpacity(0.0),
                        AppColors.backgroundColor.withOpacity(0.0),
                        AppColors.backgroundColor.withOpacity(0.0),
                        AppColors.backgroundColor.withOpacity(0.0),
                        AppColors.backgroundColor.withOpacity(0.0),
                        AppColors.backgroundColor.withOpacity(0.1),
                        AppColors.backgroundColor.withOpacity(0.25),
                        AppColors.backgroundColor.withOpacity(0.5),
                        AppColors.backgroundColor.withOpacity(0.75),
                        AppColors.backgroundColor.withOpacity(1.0),
                      ],
                      begin: Alignment.topCenter,
                      end: Alignment.bottomCenter,
                    ),
                  ),
                ),
              ],
            ),
          ),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 20),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                const SizedBox(height: 30),
                RichText(
                  text: TextSpan(
                    text: 'Welcome to ',
                    style: Theme.of(context).textTheme.headlineLarge,
                    children: [
                      TextSpan(
                        text: 'ReviewBook 👋',
                        style: Theme.of(context)
                            .textTheme
                            .headlineLarge
                            ?.copyWith(color: AppColors.secondaryColor),
                      )
                    ],
                  ),
                ),
                const SizedBox(height: 10),
                Text(
                  'Discover, Review, Connect – For the Love of Books.',
                  style: Theme.of(context).textTheme.labelLarge,
                  textAlign: TextAlign.center,
                ),
                const SizedBox(height: 50),
                BasePrimaryButton(
                  label: 'Get Started',
                  onPressed: () {
                    Navigator.of(context).push(MaterialPageRoute(
                      builder: (context) => const RegisterPage(),
                    ));
                  },
                ),
                const SizedBox(height: 15),
                BasePrimaryButton(
                  onPressed: () {
                    Navigator.of(context).push(MaterialPageRoute(
                      builder: (context) => const LoginPage(),
                    ));
                  },
                  label: 'I Already Have an Account',
                  buttonColor: AppColors.cardColor,
                  textColor: AppColors.primaryTextColor,
                  borderColor: AppColors.secondaryTextColor.withOpacity(0.2),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
