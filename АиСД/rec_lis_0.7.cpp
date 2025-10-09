
#include <iostream>
#include <vector>
#include <algorithm>

#include <fstream>
using namespace std;

int main()
{
    fstream in("input.txt");
    ofstream out("output.txt");
    if (!in.is_open() || !out.is_open()) {
        return -1;
    }
    int size1;
    in >> size1;
    int n;
    int* mas = new int[size1];
    for (int i = 0; i < size1; i++)
    {
        in >> n;
        mas[i] = n;
    }
    vector<int> vec = {};
    vec.push_back(mas[0]);
    for (int i = 1; i < size1; i++)
    {
        if (mas[i]>vec[vec.size()-1])
        {
            vec.push_back(mas[i]);
        }
        else
        {
            auto it1 = std::lower_bound(vec.begin(), vec.end(), mas[i]);
            *it1 = mas[i];
        }
    }
    out << vec.size();
    in.close();
    out.close();
}

