#include <iostream>
#include <fstream>
#include <vector>

using namespace std;

int main()
{
    ifstream in("input.txt");
    ofstream out("output.txt");

    int n, m;
    in >> n >> m;

    int* mas = new int[n + 1];
    vector<vector<int>> vec(n + 1); 

    for (int i = 0; i <= n; i++)
    {
        mas[i] = 0;
    }

    for (int i = 0; i < m; i++)
    {
        int v1, v2;
        in >> v1 >> v2;

        vec[v1].push_back(v2);
        vec[v2].push_back(v1);

        mas[v1]++;
        mas[v2]++;
    }

    for (int i = 1; i <= n; i++)
    {
        out << mas[i] << " ";
        for (int a : vec[i])
        {
            out << a << " ";
        }
        out << '\n'; 
    }
}