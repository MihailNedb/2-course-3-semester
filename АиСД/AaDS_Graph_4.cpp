#include <iostream>
#include <fstream>
#include <vector>
using namespace std;
int main() {
    ifstream in("input.txt");
    ofstream out("output.txt");
    long n; 
    in >> n;
    int** m = new int*[n];
    for (int i = 0; i < n; i++)
    {
      m[i] = new int[n];
    }
    vector<long> m1(n, 0);
    for (int i = 0; i < n; i++)
    {
        for (int j = 0; j < n; j++)
        {
            in >> m[i][j];
            if (m[i][j] ==1)
            {
                m1[j] = i + 1;
            }
        }
    }
    
    for (int i = 0; i < n; i++)
    {
        out << m1[i] << " ";
    }
    out.close();
    in.close();
}