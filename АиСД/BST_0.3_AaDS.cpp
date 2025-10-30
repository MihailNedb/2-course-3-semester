#include <iostream>
#include <fstream>
#include <climits>

using namespace std;

int main()
{
    ifstream in("bst.in");
    ofstream out("bst.out");

    long long size1;
    in >> size1;
    long long* elements = new long long[size1];
    long long* kids = new long long[size1];
    char* leters = new char[size1];
    long long** bounds1 = new long long* [size1];

    for (long long i = 0; i < size1; i++) {
        bounds1[i] = new long long[2];
    }

    bounds1[0][0] = LLONG_MIN;
    bounds1[0][1] = LLONG_MAX;

    in >> elements[0];
    for (long long i = 1; i < size1; i++)
    {
        in >> elements[i];
        in >> kids[i];
        in >> leters[i];

        bounds1[i][0] = bounds1[kids[i] - 1][0];
        bounds1[i][1] = bounds1[kids[i] - 1][1];

        if (leters[i] == 'R')
        {
            bounds1[i][0] = elements[kids[i] - 1];
        }
        if (leters[i] == 'L')
        {
            bounds1[i][1] = elements[kids[i] - 1];
        }

        if (elements[i] >= bounds1[i][1] || elements[i] < bounds1[i][0])
        {
            out << "NO";
            return 0;
        }
    }
    out << "YES";

    in.close();
    out.close();

    return 0;
}