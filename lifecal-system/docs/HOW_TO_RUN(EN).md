# LifeCAL - Application Run Guide

## Method 1: Using Maven (Recommended) ✅

```bash
cd c:\LifeCal\lifecal-system
mvn javafx:run
```

**Or simply double-click the LifeCAL.bat file in the project folder**

## Method 2: Using BAT File Directly

The `LifeCAL.bat` file will automatically execute the command: `mvn javafx:run`

## ⚠️ Why doesn’t the JAR file work?

JavaFX applications require a specific module path configuration and cannot be executed directly from a fat JAR.
Therefore, you must use one of the following approaches:
- Maven JavaFX Plugin (`mvn javafx:run`) — Recommended
- jpackage with WiX Toolset — Advanced setup (more complex)

## 🔐 Admin Account

-Username: `admin`
-Password: `admin123`

## ✨ Features
✅ User Authentication (Login/Register)
✅ Dashboard with BMI & BMR calculations
✅ Food, Exercise, and Weight Tracking
✅ Reports and Analytics
✅ Light/Dark Theme Support
✅ Admin System – User Management