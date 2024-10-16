import 'package:book_review/src/core/styles/app_colors.dart';
import 'package:book_review/src/features/authentication/presentation/pages/welcome_page.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: AppColors.primaryColor),
        useMaterial3: true,
        scaffoldBackgroundColor: AppColors.backgroundColor,
        appBarTheme: const AppBarTheme(
          backgroundColor: AppColors.backgroundColor,
        ),
        textTheme: const TextTheme(
          headlineLarge: TextStyle(
            color: AppColors.primaryTextColor,
            fontWeight: FontWeight.w600,
            fontSize: 22,
          ),
          headlineMedium: TextStyle(
            color: AppColors.primaryTextColor,
            fontWeight: FontWeight.w600,
            fontSize: 20,
          ),
          headlineSmall: TextStyle(
            color: AppColors.primaryTextColor,
            fontWeight: FontWeight.w600,
            fontSize: 18,
          ),
          titleLarge: TextStyle(
            color: AppColors.primaryTextColor,
            fontWeight: FontWeight.w500,
            fontSize: 20,
          ),
          titleMedium: TextStyle(
            color: AppColors.primaryTextColor,
            fontWeight: FontWeight.w500,
            fontSize: 18,
          ),
          titleSmall: TextStyle(
            color: AppColors.primaryTextColor,
            fontWeight: FontWeight.w500,
            fontSize: 16,
          ),
          labelLarge: TextStyle(
            color: AppColors.primaryTextColor,
            fontWeight: FontWeight.w400,
            fontSize: 18,
          ),
          labelMedium: TextStyle(
            color: AppColors.primaryTextColor,
            fontWeight: FontWeight.w400,
            fontSize: 16,
          ),
          labelSmall: TextStyle(
            color: AppColors.primaryTextColor,
            fontWeight: FontWeight.w400,
            fontSize: 14,
          ),
          bodyLarge: TextStyle(
            color: AppColors.primaryTextColor,
            fontWeight: FontWeight.w400,
            fontSize: 16,
          ),
          bodyMedium: TextStyle(
            color: AppColors.primaryTextColor,
            fontWeight: FontWeight.w400,
            fontSize: 14,
          ),
          bodySmall: TextStyle(
            color: AppColors.primaryTextColor,
            fontWeight: FontWeight.w400,
            fontSize: 12,
          ),
        ),
      ),
      home: const WelcomePage(),
    );
  }
}
