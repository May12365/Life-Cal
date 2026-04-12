# 🔧 How to Run LIFE CAL System - JavaFX Runtime Error Fix

## 🎯 Problem
You're seeing: **"Error: JavaFX runtime components are missing, and are required to run this application"**

This happens because JavaFX needs to be properly configured when running the application.

---

## ✅ Solutions (Choose One)

### **Solution 1: Install Maven and Use It to Run (Recommended)**

Maven handles all JavaFX configuration automatically.

#### Step 1: Install Maven

1. **Download Maven**:
   - Go to: https://maven.apache.org/download.cgi
   - Download the `.zip` file (e.g., `apache-maven-3.9.x-bin.zip`)

2. **Extract Maven**:
   - Extract to `C:\Program Files\Apache\maven` (or any location)

3. **Add Maven to PATH**:
   - Press `Windows Key` + Search "Environment Variables"
   - Click "Environment Variables"
   - Under "System Variables", find `Path`, click "Edit"
   - Click "New" and add: `C:\Program Files\Apache\maven\bin`
   - Click "OK" on all windows

4. **Verify Installation**:
   - Open a **NEW** PowerShell window
   - Run: `mvn -version`
   - You should see Maven version info

#### Step 2: Run the Application

Open PowerShell in the project directory and run:

```powershell
cd "c:\PJ\lifecal-system"
mvn javafx:run
```

---

### **Solution 2: Use IntelliJ IDEA (Easiest for Development)**

If you have IntelliJ IDEA installed:

1. **Open IntelliJ IDEA**
2. **Import Project**:
   - File → Open → Select `c:\PJ\lifecal-system`
   - Wait for Maven dependencies to download
3. **Run the Application**:
   - Navigate to `src/main/java/com/lifecal/LifeCalApp.java`
   - Right-click on the file
   - Select "Run 'LifeCalApp.main()'"

IntelliJ will automatically configure JavaFX modules.

---

### **Solution 3: Use VS Code with Java Extensions**

If you have VS Code:

1. **Install Extensions**:
   - Java Extension Pack (by Microsoft)
   - Maven for Java

2. **Open Project**:
   - File → Open Folder → Select `c:\PJ\lifecal-system`

3. **Run**:
   - Open `LifeCalApp.java`
   - Click the "Run" button above the `main` method

---

### **Solution 4: Manual Java Command (Advanced)**

This requires downloading JavaFX separately and configuring modules manually.

#### Step 1: Download JavaFX SDK

1. Download JavaFX SDK from: https://gluonhq.com/products/javafx/
2. Choose version **17.0.2** for Windows
3. Extract to a location (e.g., `C:\javafx-sdk-17.0.2`)

#### Step 2: Build the Project First

You'll need Maven installed for this. If you don't have Maven, use Solution 1 or 2.

```powershell
mvn clean package
```

#### Step 3: Run with Module Path

```powershell
java --module-path "C:\javafx-sdk-17.0.2\lib" --add-modules javafx.controls,javafx.fxml -jar target\lifecal-system-1.0-jar-with-dependencies.jar
```

Replace `C:\javafx-sdk-17.0.2\lib` with your actual JavaFX lib path.

---

## 🎯 Recommended Path

**For beginners**: Use **Solution 2** (IntelliJ IDEA) - it's the easiest

**For command-line users**: Use **Solution 1** (Install Maven) - it's the most straightforward

---

## 🐛 Troubleshooting

### "mvn is not recognized"
- Maven is not installed or not in PATH
- Follow Solution 1 carefully
- Make sure to open a **NEW** PowerShell window after adding to PATH

### "No compiler is provided in this environment"
- Make sure JDK (not JRE) is installed
- Verify: `java -version` shows "OpenJDK" or "Java SE Development Kit"

### Build fails with errors
- Clean the project: `mvn clean`
- Try again: `mvn javafx:run`

---

## 📝 Quick Reference Commands

```powershell
# Navigate to project
cd "c:\PJ\lifecal-system"

# Clean and compile
mvn clean compile

# Run the application (RECOMMENDED)
mvn javafx:run

# Build JAR files
mvn clean package

# Check Java version
java -version

# Check Maven version
mvn -version
```

---

## ✨ What Happens When You Run Successfully?

1. The application window will open
2. You'll see the **Login Screen**
3. Click "Create Account" to register a new user
4. After registration, you can log in and use all features!

---

**Need more help?** Check the main [README.md](README.md) for full documentation.
