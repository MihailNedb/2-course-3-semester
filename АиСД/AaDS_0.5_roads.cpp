#include <iostream>
#include <fstream>
using namespace std;

long long find(long long* m, long long city)
{
    long long root = city;
    // Находим корень
    while (m[root] != root)
    {
        root = m[root];
    }

    // Сжатие пути
    long long temp = city;
    while (m[temp] != temp)
    {
        long long next = m[temp];
        m[temp] = root;
        temp = next;
    }

    return root;
}

bool union1(long long* m, long long city1, long long city2, long long kolgorodov)
{
    long long c1 = find(m, city1);
    long long c2 = find(m, city2);

    if (c1 == c2)
    {
        return false;
    }

    m[c2] = c1;
    return true;
}

int main()
{
    ifstream in("input.txt");
    ofstream out("output.txt");
    long long kolgorodov = 0;
    in >> kolgorodov;
    long long koldorog;
    in >> koldorog;

    long long* m = new long long[kolgorodov];
    for (long long i = 0; i < kolgorodov; i++)
    {
        m[i] = i;
    }

    long long city1, city2;
    bool check = false;
    long long comp = kolgorodov;

    for (long long i = 0; i < koldorog; i++)
    {
        in >> city1;
        in >> city2;
        city1--;
        city2--;

        check = union1(m, city1, city2, kolgorodov);
        if (check == true)
        {
            comp--;
        }
        out << comp;
        out << '\n';
    }

    delete[] m;
    in.close();
    out.close();
    return 0;
}