﻿#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <windows.h>
#include "employee.h"

using namespace std;

void printBinaryFile(const string& filename)
{
    ifstream file(filename, ios::binary);
    if (!file)
    {
        cout << "Cannot open file: " << filename << endl;
        return;
    }

    cout << "Contents of binary file " << filename << ":" << endl;
    cout << "Number    Name    Hours" << endl;

    employee emp;
    while (file.read(reinterpret_cast<char*>(&emp), sizeof(employee)))
    {
        cout << emp.num << "        "
            << emp.name << "        "
            << emp.hours << endl;
    }

    file.close();
}

void printReportFile(const string& filename)
{
    ifstream file(filename);
    if (!file)
    {
        cout << "Cannot open file: " << filename << endl;
        return;
    }

    cout << "Contents of report file " << filename << ":" << endl;

    string line;
    while (getline(file, line))
    {
        cout << line << endl;
    }

    file.close();
}

int main()
{
    SetConsoleCP(1251);
    SetConsoleOutputCP(1251);

    string binFilename, reportFilename;
    int recordCount;
    double hourlyRate;

    cout << "Enter binary filename: ";
    cin >> binFilename;
    cout << "Enter number of records: ";
    cin >> recordCount;

    wstring creatorCmd = L"Creator.exe " + wstring(binFilename.begin(), binFilename.end()) + L" " + to_wstring(recordCount);

    STARTUPINFO siCreator;
    PROCESS_INFORMATION piCreator;
    ZeroMemory(&siCreator, sizeof(siCreator));
    siCreator.cb = sizeof(siCreator);
    ZeroMemory(&piCreator, sizeof(piCreator));

    cout << "Starting Creator process..." << endl;

    if (!CreateProcess(
        NULL,
        &creatorCmd[0],
        NULL,
        NULL,
        FALSE,
        CREATE_NEW_CONSOLE,
        NULL,
        NULL,
        &siCreator,
        &piCreator))
    {
        cout << "CreateProcess failed for Creator (" << GetLastError() << ")" << endl;
        return 1;
    }

    cout << "Waiting for Creator to complete..." << endl;
    WaitForSingleObject(piCreator.hProcess, INFINITE);
    CloseHandle(piCreator.hProcess);
    CloseHandle(piCreator.hThread);

    printBinaryFile(binFilename);

    cout << "Enter report filename: ";
    cin >> reportFilename;
    cout << "Enter hourly rate: ";
    cin >> hourlyRate;

    wstring reporterCmd = L"Reporter.exe " +
        wstring(binFilename.begin(), binFilename.end()) + L" " +
        wstring(reportFilename.begin(), reportFilename.end()) + L" " +
        to_wstring(hourlyRate);

    STARTUPINFO siReporter;
    PROCESS_INFORMATION piReporter;
    ZeroMemory(&siReporter, sizeof(siReporter));
    siReporter.cb = sizeof(siReporter);
    ZeroMemory(&piReporter, sizeof(piReporter));

    cout << "Starting Reporter process..." << endl;

    if (!CreateProcess(
        NULL,
        &reporterCmd[0],
        NULL,
        NULL,
        FALSE,
        CREATE_NEW_CONSOLE,
        NULL,
        NULL,
        &siReporter,
        &piReporter))
    {
        cout << "CreateProcess failed for Reporter (" << GetLastError() << ")" << endl;
        return 1;
    }

    cout << "Waiting for Reporter to complete..." << endl;
    WaitForSingleObject(piReporter.hProcess, INFINITE);
    CloseHandle(piReporter.hProcess);
    CloseHandle(piReporter.hThread);

    printReportFile(reportFilename);

    cout << "Program completed successfully." << endl;
    system("pause");
    return 0;
}