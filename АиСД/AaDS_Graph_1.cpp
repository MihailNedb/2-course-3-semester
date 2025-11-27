

#include <iostream>
#include <fstream>

using namespace std;

int main()
{
    fstream in("input.txt");
    ofstream out("output.txt");
    int n;
    in >> n;
    int m1;
    in >> m1;

    int** m = new int* [n];
    for (int i = 0; i < n; i++) {
        m[i] = new int[n];
    }
    for (int i = 0; i < n; i++)
    {
        for (int j = 0; j < n; j++)
        {
            m[i][j] = 0;
        }
    }
    int x = 0;
    int y = 0;
    for (int i = 0; i < m1; i++)
    {
        in >> x;
        in >> y;
        m[x-1][y-1] = 1;
        m[y-1][x-1] = 1;
    }
    for (int i = 0; i < n; i++)
    {
        for (int j = 0; j < n; j++)
        {
            out << m[i][j] << " ";
        }
        out << '\n';
    }


    in.close();
    out.close();

}

