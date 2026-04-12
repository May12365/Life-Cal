# LifeCAL - วิธีรันโปรแกรม

## วิธีที่ 1: ใช้ Maven (แนะนำ) ✅

```bash
cd c:\LifeCal\lifecal-system
mvn javafx:run
```

**หรือ คลิกไฟล์ `LifeCAL.bat` ในโฟลเดอร์โปรเจค**

## วิธีที่ 2: ใช้ BAT File โดยตรง

ไฟล์ `LifeCAL.bat` จะรันคำสั่ง `mvn javafx:run` ให้อัตโนมัติ

## ⚠️ ทำไม JAR ไม่ทำงาน?

JavaFX ต้องรันผ่าน module path พิเศษ ไม่สามารถรันจาก fat JAR ได้โดยตรง
ดังนั้นต้องใช้:
- Maven JavaFX Plugin (`mvn javafx:run`) ✅
- หรือ jpackage พร้อม WiX Toolset (ซับซ้อน)

## บัญชี Admin

- Username: `admin`
- Password: `admin123`

## Features

✅ ระบบ Login/Register
✅ Dashboard พร้อม BMI/BMR
✅ บันทึกอาหาร ออกกำลังกาย น้ำหนัก
✅ รายงานและสถิติ
✅ ธีมกลางคืน/กลางวัน
✅ **ระบบ Admin** - จัดการผู้ใช้


