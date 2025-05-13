📊 Student Marksheet Management System
📌 Table of Contents
Project Overview

Features

Technologies Used

Database Schema

Installation

Usage

Screenshots

Contributing

License

🔍 Project Overview
A Java console-based application that allows the user to manage student records and academic marks using JDBC and a MySQL database. It includes features to add, update, view, and delete student data and calculate percentage and grade based on scores.

✅ Features
Add new student records

View all or individual student marksheets

Update student marks

Delete student details

Calculate total marks, percentage, and grade

JDBC-powered MySQL integration

💻 Technologies Used
Java (JDK 8 or higher)

JDBC API

MySQL

IntelliJ IDEA / Eclipse

MySQL Workbench (optional)

🗃 Database Schema
Table Name: students

Field	Type	Description
id	INT	Primary Key
name	VARCHAR	Student name
subject1	INT	Marks for Subject 1
subject2	INT	Marks for Subject 2
subject3	INT	Marks for Subject 3
total	INT	Total Marks
percentage	FLOAT	Percentage
grade	VARCHAR	Grade

⚙️ Installation
Clone the repository

Create the MySQL database and table

Update DB credentials in DBConnection.java

Compile and run MarksheetApp.java (or main class)

🚀 Usage
Run the application from the console

Follow menu prompts to manage student data

Data is stored in the MySQL database via JDBC

📸 Screenshots (optional)
Add relevant screenshots here if any

🤝 Contributing
Contributions are welcome! Please fork the repo and submit a pull request.

📄 License
This project is open source under the MIT License.
