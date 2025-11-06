#include <iostream>
#include <fstream>
#include <queue>
using namespace std;

int main()
{
    ifstream in("huffman.in");
    ofstream out("huffman.out");

    int size1 = 0;
    in >> size1;
    queue<long long> m;

    for (int i = 0; i < size1; i++)
    {
        long long a;
        in >> a;
        m.push(a);
    }

    long long mysum = 0;
    long long sum = 0;
    queue<long long> m2;
    while (m.size() > 0 || m2.size() > 0)
    {
        long long ch1, ch2;
        if (m2.size() == 0 && m.size() == 0)
        {
            break;
        }
        if (m2.size() == 0)
        {
            ch1 = m.front();
            m.pop();
        }
        else if (m.size() == 0)
        {
            ch1 = m2.front();
            m2.pop();
        }
        else if (m2.front() > m.front())
        {
            ch1 = m.front();
            m.pop();
        }
        else
        {
            ch1 = m2.front();
            m2.pop();
        }


        if (m2.size() == 0 && m.size() == 0)
        {
            break;
        }
        if (m2.size() == 0)
        {
            ch2 = m.front();
            m.pop();
        }
        else if (m.size() == 0)
        {
            ch2 = m2.front();
            m2.pop();
        }
        else if (m2.front() > m.front())
        {
            ch2 = m.front();
            m.pop();
        }
        else
        {
            ch2 = m2.front();
            m2.pop();
        }

        long long sum = ch1 + ch2;
        mysum = mysum + sum;
        m2.push(sum);
    }

    out << mysum;

    return 0;
}